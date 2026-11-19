package org.cobra.moreores.world.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.cobra.moreores.MoreOresModLoader;
import org.cobra.moreores.world.block.entity.ModBlockEntityType;
import org.cobra.moreores.world.block.entity.gem.machine.GemPurifierBlockEntity;
import org.cobra.moreores.world.item.util.impl.PurificationGemstones;
import org.jspecify.annotations.Nullable;

public class GemPurifierBlock extends BaseEntityBlock {
    public static final MapCodec<GemPurifierBlock> CODEC = simpleCodec(GemPurifierBlock::new);
    public static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 14, 16);
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty REDSTONE_POWERED = BooleanProperty.create("redstone_powered");
    public static final EnumProperty<PurificationGemstones> IS_POLISHING = EnumProperty.create("is_polishing", PurificationGemstones.class);
    
    public GemPurifierBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(level.isClientSide()) {
            return InteractionResult.CONSUME;
        }
        
        if(level.getBlockEntity(pos) instanceof GemPurifierBlockEntity be) {
            player.openMenu(new SimpleMenuProvider(be, be.getDisplayName()), pos);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, ItemStack toolStack, boolean willHarvest, FluidState fluid) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof GemPurifierBlockEntity blockEntity1) {
            blockEntity1.drops();
        }
        return super.onDestroyedByPlayer(state, level, pos, player, toolStack, willHarvest, fluid);
    }

    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getClockWise()).setValue(REDSTONE_POWERED, ctx.getLevel().hasNeighborSignal(ctx.getClickedPos()))
                .setValue(IS_POLISHING, PurificationGemstones.EMPTY);
    }

    @Override
    protected void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, @org.jetbrains.annotations.Nullable Orientation wireOrientation, boolean notify) {
        if (!world.isClientSide()) {
            boolean bl = state.getValue(REDSTONE_POWERED);
            if (bl != world.hasNeighborSignal(pos)) {
                if (bl) {
                    world.scheduleTick(pos, this, 4);
                } else {
                    world.setBlock(pos, state.cycle(REDSTONE_POWERED), Block.UPDATE_CLIENTS);
                    MoreOresModLoader.LOGGER.info("Receiving Redstone Signal at [{}, {}, {}]", pos.getX(), pos.getY(), pos.getZ());
                }
            }
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockState newState = state;

        if(state.getValue(REDSTONE_POWERED) && !level.hasNeighborSignal(pos)) {
            newState = newState.setValue(REDSTONE_POWERED, false);
        }

        if(level.getBlockEntity(pos) instanceof GemPurifierBlockEntity be) {
            newState = newState.setValue(IS_POLISHING, be.gemstone());
        }

        level.setBlock(pos, newState, Block.UPDATE_ALL);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(REDSTONE_POWERED);
        builder.add(IS_POLISHING);
    }
    
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new GemPurifierBlockEntity(blockPos,  blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if(level.isClientSide()) {
            return null;
        }
        return createTickerHelper(type, ModBlockEntityType.GEM_PURIFIER.get(), ((level1, blockPos, blockState, be) -> 
                be.tick(level1, blockPos, blockState)));
    }
}
