package org.cobra.moreores.world.block.entity.gem.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.cobra.moreores.client.gui.screen.GemCrystallizerMenu;
import org.cobra.moreores.networking.block.ScreenGhostRenderingS2CPacket;
import org.cobra.moreores.recipe.GemCrystallizerRecipe;
import org.cobra.moreores.recipe.ModRecipeType;
import org.cobra.moreores.recipe.input.GemCrystallizerRecipeInput;
import org.cobra.moreores.tags.ModItemTags;
import org.cobra.moreores.world.block.GemCrystallizerBlock;
import org.cobra.moreores.world.block.ModBlocks;
import org.cobra.moreores.world.block.entity.ModBlockEntityType;
import org.cobra.moreores.world.item.ModItems;
import org.cobra.moreores.world.item.util.GemCategory;
import org.cobra.moreores.world.item.util.impl.CrystallizationGemstones;
import org.cobra.moreores.world.item.util.impl.IGemstone;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class GemCrystallizerBlockEntity extends AbstractGemMachineBlockEntity {
    private long previousRemovedRadiantDustMilestone = 0;

    private ItemStack lastPreviewResult = ItemStack.EMPTY;

    protected final ContainerData containerData;
    private int maxProgressTicks = 300;

    public int radiantDust = 0;
    public int maxDust = 10000;
    private int dustTick;
    
    public static final int INGREDIENT_BEFORE_SLOT = 0;
    public static final int INGREDIENT_AFTER_SLOT = 1;
    public static final int RESULT_SLOT = 2;
    public static final int ENERGY_SOURCE_SLOT = 3;
    public static final int RADIANT_DUST_SLOT = 4;
    public static final int REDSTONE_SLOT = 5;

    public GemCrystallizerBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntityType.GEM_CRYSTALLIZER.get(), pos, blockState);
        this.containerData = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> GemCrystallizerBlockEntity.this.initialProgressTicks;
                    case 1 -> GemCrystallizerBlockEntity.this.maxProgressTicks;
                    case 2 ->  GemCrystallizerBlockEntity.this.radiantDust;
                    case 3 ->  GemCrystallizerBlockEntity.this.redstone;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> GemCrystallizerBlockEntity.this.initialProgressTicks = value;
                    case 1 -> GemCrystallizerBlockEntity.this.maxProgressTicks = value;
                    case 2 -> GemCrystallizerBlockEntity.this.radiantDust = value;
                    case 3 -> GemCrystallizerBlockEntity.this.redstone = value;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    @Override
    public int inventoryStackSize() {
        return 11;
    }

    @Override
    public int energyCapacity() {
        return 1000000;
    }

    @Override
    public int maxEnergyInsertable() {
        return 19200;
    }

    @Override
    public int maxEnergyExtractable() {
        return 64000;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("DustCount", radiantDust);
        output.putInt("DustTick", dustTick);
        output.store("GemType", CrystallizationGemstones.CODEC, gemstone());
    }

    @Override
    public void loadAdditional(ValueInput view) {
        super.loadAdditional(view);
        radiantDust = view.getIntOr("DustCount", 0);
        dustTick = view.getIntOr("DustTick", 0);
        gem = view.read("GemType", CrystallizationGemstones.CODEC).orElse(CrystallizationGemstones.EMPTY);
    }

    @Override
    public GemCategory category() {
        return GemCategory.CRYSTALLIZATION;
    }

    @Override
    public CrystallizationGemstones gemstone() {
        IGemstone gemstone = super.gemstone();
        if(gemstone instanceof CrystallizationGemstones c) {
            return c;
        }
        return CrystallizationGemstones.EMPTY;
    }

    @Override
    public Component getDisplayName() {
        return ModBlocks.GEM_CRYSTALLIZER_BLOCK.get().getName();
    }

    public ItemResource radiantDustStack() {
        return getStack(RADIANT_DUST_SLOT);
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new GemCrystallizerMenu(containerId, inventory, this, this.main, containerData);
    }

    @Override
    public ItemResource ingredientStack() {
        return getStack(INGREDIENT_BEFORE_SLOT);
    }

    public ItemResource ingredientAfterStack() {
        return getStack(INGREDIENT_AFTER_SLOT);
    }

    @Override
    public ItemResource energyStack() {
        return getStack(ENERGY_SOURCE_SLOT);
    }

    @Override
    public ItemResource resultStack() {
        return getStack(RESULT_SLOT);
    }

    @Override
    public ItemResource redstoneStack(int slot) {
        return getStack(REDSTONE_SLOT);
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) {
            return;
        }

        syncPreviewResult();

        dustTick++;
        redstoneTick++;

        IGemstone newGem = gemstone();

        if (newGem != this.gem) {
            setGemstone(newGem);

            level.sendBlockUpdated(pos, getBlockState(), getBlockState(), Block.UPDATE_ALL);
            setChanged(level, pos, state);
        }

        ItemResource main = radiantDustStack();
        if(main.is(ModItems.RADIANT_DUST.get()) && radiantDust <= maxDust) {
            radiantDust += 100;
            setChanged(level, pos, state);
        }
        ItemResource stack1 = redstoneStack(REDSTONE_SLOT);
        if((stack1.is(Items.REDSTONE) || level.hasNeighborSignal(pos)) && redstone <= maxRedstone) {
            redstone += 10;
            setChanged(level, pos, state);
        }

        modifyState();
        if(machineStatus == MachineStatus.RUNNING) {
            energyState = MachineStatus.EnergyState.EXTRACTING;
            setChanged(level, pos, state);
            if (canResultAccept() && checkRecipe() && hasRequiredEnergyAmount() && radiantDust >= 15) {
                this.increaseProgressTicks();
                if((!level.hasNeighborSignal(pos) || redstone > 0) && redstoneTick >= 20) {
                    redstone--;
                    redstoneTick = 0;
                }
                this.useEnergy();
                if(radiantDust > 0 && dustTick >= 20) {
                    radiantDust--;
                    dustTick = 0;
                    setChanged(level, pos, state);
                }
                setChanged(level, pos, state);
                if (hasCrystallizationFinished()) {
                    this.getCrystallizedGem();
                    this.clearProgress();
                    setChanged(level, pos, state);
                }
                setChanged(level, pos, state);
            } else {
                this.clearProgress();
                this.machineStatus = MachineStatus.IDLE;
                setChanged(level, pos, state);
            }
        } else if (machineStatus.isPaused()) {
            energyState = MachineStatus.EnergyState.INSERTING;
            addEnergy();
            setChanged(level, pos, state);
        } else {
            if((energyAmount() < 1_000_000 && hasEnergySource())) {
                energyState = MachineStatus.EnergyState.INSERTING;
                addEnergy();
                setChanged(level, pos, state);
            } else {
                energyState = MachineStatus.EnergyState.IDLE;
                setChanged(level, pos, state);
            }
        }

        validateEnergyAmount(ENERGY_SOURCE_SLOT);
        validateRedstoneAmount(REDSTONE_SLOT);
        validateRadiantDust();
        setChanged(level, pos, state);
    }

    private void syncPreviewResult() {
        Optional<RecipeHolder<GemCrystallizerRecipe>> recipe = getCurrentRecipe();
        ItemStack result = recipe.map(holder -> holder.value().result().copy())
                .orElse(ItemStack.EMPTY);

        if (ItemStack.matches(lastPreviewResult, result)) {
            return;
        }
        lastPreviewResult = result.copy();
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        PacketDistributor.sendToPlayersTrackingChunk(serverLevel, serverLevel.getChunkAt(getBlockPos()).getPos(), new ScreenGhostRenderingS2CPacket(getBlockPos(), lastPreviewResult));
    }

    private void getCrystallizedGem() {
        RecipeHolder<GemCrystallizerRecipe> recipe = getCurrentRecipe().orElseThrow();

        ContainerHelper.removeItem(main.copyToList(), INGREDIENT_BEFORE_SLOT, 1);
        ContainerHelper.removeItem(main.copyToList(), INGREDIENT_AFTER_SLOT, 1);

        main.set(RESULT_SLOT, ItemResource.of(recipe.value().result()),
                this.resultStack().toStack().getCount() + recipe.value().result().getCount());
    }
    private boolean hasCrystallizationFinished() {
        return initialProgressTicks >= maxProgressTicks;
    }

    @Override
    protected void validateEnergyAmount(int energySlot) {
        if(energyAmount() > 1000000) {
            energyHandler().set(1000000);
        }

        int energy = this.energyAmount();

        int [] milestones = {100000, 200000, 300000, 400000, 500000, 600000, 700000, 800000, 900000, 1000000};

        for(long milestone : milestones) {
            if(energy >= milestone && previousRemovedEnergyMilestone < milestone) {
                try(Transaction transaction = Transaction.openRoot()) {
                    if(!energyStack().isEmpty()) {
                        main.extract(ENERGY_SOURCE_SLOT, energyStack(), 1, transaction);
                        previousRemovedEnergyMilestone = milestone;
                        transaction.commit();
                    }
                }
                break;
            }
        }
    }

    private void validateRadiantDust() {
        if(radiantDust > 10000) {
            radiantDust = 10000;
        }

        long energy = radiantDust;

        long [] milestones = {2000, 4000, 6000, 8000, 10000};

        for(long milestone : milestones) {
            if(energy >= milestone && previousRemovedRadiantDustMilestone < milestone) {
                try(Transaction transaction = Transaction.openRoot()) {
                    if(!radiantDustStack().isEmpty()) {
                        main.extract(RADIANT_DUST_SLOT, radiantDustStack(), 1, transaction);
                        previousRemovedRadiantDustMilestone = milestone;
                        transaction.commit();
                    }
                }
                break;
            }
        }
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(main.size());
        for (int i = 0; i < main.size(); i++) {
            ItemAccess itemAccess = ItemAccess.forHandlerIndex(main, i);
            inv.setItem(i, new ItemStack(itemAccess.getResource().getItem(), itemAccess.getAmount()));
        }
        if(level == null) {
            return;
        }
        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    private void modifyState() {
        BlockState state = getBlockState();

        state = state.setValue(GemCrystallizerBlock.IS_CRYSTALLIZING, gemstone());


        if(state != getBlockState()) {
            level.setBlock(worldPosition, state, Block.UPDATE_ALL);
        }
    }

    @Override
    protected boolean checkRecipe() {
        Optional<RecipeHolder<GemCrystallizerRecipe>> recipe = getCurrentRecipe();

        return recipe.isPresent() && hasRequiredEnergyAmount() && canInsertCountIntoResultSlot(recipe.get().value().result())
                && canInsertItemIntoResultSlot(recipe.get().value().result().getItem());
    }

    private Optional<RecipeHolder<GemCrystallizerRecipe>> getCurrentRecipe() {
        if(level == null) {
            return Optional.empty();
        }
        return ((ServerLevel) level).recipeAccess()
                .getRecipeFor(ModRecipeType.GEM_CRYSTALLIZER.get(),
                        new GemCrystallizerRecipeInput(main.getResource(INGREDIENT_BEFORE_SLOT).toStack(),
                                main.getResource(INGREDIENT_AFTER_SLOT).toStack()), level);
    }

    private boolean canInsertItemIntoResultSlot(Item item) {
        return this.resultStack().getItem() == item || this.resultStack().isEmpty() || this.resultStack().is(ModItemTags.GEMSTONE)
                || this.resultStack().is(ModItemTags.RAW_GEMSTONE);
    }

    private boolean canInsertCountIntoResultSlot(ItemStack result) {
        return this.resultStack().toStack().getCount() + result.getCount() <= this.resultStack().getMaxStackSize();
    }

    private boolean canResultAccept() {
        return this.resultStack().isEmpty() || this.resultStack().toStack().getCount() < this.resultStack().getMaxStackSize();
    }
}