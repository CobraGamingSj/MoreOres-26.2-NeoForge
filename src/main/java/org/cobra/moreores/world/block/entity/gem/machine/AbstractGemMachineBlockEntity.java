package org.cobra.moreores.world.block.entity.gem.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.cobra.moreores.tags.ModItemTags;
import org.cobra.moreores.world.item.ModItems;
import org.cobra.moreores.world.item.util.GemCategory;
import org.cobra.moreores.world.item.util.impl.CrystallizationGemstones;
import org.cobra.moreores.world.item.util.impl.IGemstone;
import org.cobra.moreores.world.item.util.impl.PurificationGemstones;

public abstract class AbstractGemMachineBlockEntity extends BlockEntity implements MenuProvider {

    public final ItemStacksResourceHandler main;

    protected MachineStatus machineStatus = MachineStatus.IDLE;
    protected MachineStatus.EnergyState energyState = MachineStatus.EnergyState.IDLE;
    protected IGemstone gem = IGemstone.NONE;

    int energyExtracted = 0;

    protected int initialProgressTicks = 0;

    protected int redstone = 0;
    protected int maxRedstone = 10000;
    protected int redstoneTick;

    protected long previousRemovedEnergyMilestone = 0;
    protected long previousRemovedRedstoneMilestone = 0;

    public AbstractGemMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        this.main = new ItemStacksResourceHandler(inventoryStackSize()) {
            @Override
            protected void onContentsChanged(int index, ItemStack previousContents) {
                super.onContentsChanged(index, previousContents);
                setChanged();
                if(level != null && !level.isClientSide()) {
                    level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
                }
            }
        };
    }

    final SimpleEnergyHandler energyHandler = new SimpleEnergyHandler(energyCapacity(), maxEnergyInsertable(), maxEnergyExtractable()) {
        @Override
        protected void onEnergyChanged(int previousAmount) {
            super.onEnergyChanged(previousAmount);
            getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    };

    protected abstract boolean checkRecipe();
    public abstract int inventoryStackSize();
    public abstract int energyCapacity();
    public abstract int maxEnergyInsertable();
    public abstract int maxEnergyExtractable();

    public int energyAmount() {
        return energyHandler.getAmountAsInt();
    }

    @Override
    public void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, main.copyToList());
        output.putInt("Progress", initialProgressTicks);
        output.putInt("Redstone", redstone);
        output.putInt("RedstoneTick", redstoneTick);
        energyHandler.serialize(output);
        output.store("PolishingState", MachineStatus.CODEC, machineStatus);
        output.store("EnergyState", MachineStatus.EnergyState.CODEC, energyState);
    }

    @Override
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        NonNullList<ItemStack> items = NonNullList.withSize(
                main.size(), ItemStack.EMPTY
        );

        ContainerHelper.loadAllItems(input, items);

        for (int i = 0; i < items.size(); i++) {
            ItemStack stack = items.get(i);

            if (!stack.isEmpty()) {
                main.set(i, ItemResource.of(stack), stack.getCount());
            }
        }

        initialProgressTicks = input.getIntOr("Progress", 0);
        redstone = input.getIntOr("Redstone", 0);
        redstoneTick = input.getIntOr("RedstoneTick", 0);
        energyHandler.deserialize(input);
        machineStatus = input.read("PolishingState", MachineStatus.CODEC).orElse(MachineStatus.IDLE);
        energyState = input.read("EnergyState", MachineStatus.EnergyState.CODEC).orElse(MachineStatus.EnergyState.IDLE);
    }

    public IGemstone detectGem(ItemStack stack) {
        Item item = stack.getItem();
        for (PurificationGemstones gems : PurificationGemstones.values()) {
            for (Item item1 : gems.items().get()) {
                if(item1 == item) {
                    return gems;
                }
            }
        }
        for (CrystallizationGemstones gems : CrystallizationGemstones.values()) {
            for(Item item1 : gems.items().get()) {
                if(item1 == item) {
                    return gems;
                }
            }
        }
        return IGemstone.NONE;
    }

    protected void addEnergy() {
        if(!hasEnergySource() || energyHandler.getAmountAsInt() >= 1_000_000) {
            energyState = MachineStatus.EnergyState.IDLE;
            return;
        }
        int amount = energyStack().is(ModItems.ENERGY_INGOT.get()) ? 102 : 154;
        if(level.hasNeighborSignal(worldPosition)) amount *= 3;
        try(Transaction transaction = Transaction.openRoot()) {
            long inserted = energyHandler().insert(amount, transaction);
            transaction.commit();
            if(inserted > 0) energyState = MachineStatus.EnergyState.INSERTING;
            else energyState = MachineStatus.EnergyState.IDLE;
        }
    }

    protected void useEnergy() {
        int amount = level.hasNeighborSignal(worldPosition) ? 64 : 13;
        try(Transaction transaction = Transaction.openRoot()) {
            long extracted = energyHandler().extract(amount, transaction);
            energyExtracted +=  extracted;
            transaction.commit();
        }
        energyState = MachineStatus.EnergyState.EXTRACTING;
    }

    protected void increaseProgressTicks() {
        if(this.level.hasNeighborSignal(getBlockPos())) {
            initialProgressTicks += (int) 2.5;
        } else {
            initialProgressTicks++;
        }
    }

    public int getRedstone() {
        return this.redstone;
    }

    public abstract GemCategory category();

    public IGemstone gemstone() {
        return detectGem(resultStack().toStack());
    }

    public void setGemstone(IGemstone gemstone) {
        this.gem = gemstone;
    }

    public SimpleEnergyHandler energyHandler() {
        return this.energyHandler;
    }

    public void setRedstone(int redstone) {
        this.redstone = redstone;
    }

    protected boolean hasEnergySource() {
        return energyStack().is(ModItemTags.HAS_ENERGY);
    }

    public boolean hasRequiredEnergyAmount() {
        return this.energyHandler.getAmountAsInt() >= 13;
    }

    protected void validateEnergyAmount(int slot) {
        if(energyAmount() > 10000000) {
            energyHandler.set(10000000);
        }

        long energy = this.energyAmount();

        long [] milestones = {1000000, 2000000, 3000000, 4000000, 5000000, 6000000, 7000000, 8000000, 9000000, 10000000};

        for(long milestone : milestones) {
            if(energy >= milestone && previousRemovedEnergyMilestone < milestone) {
                try(Transaction transaction = Transaction.openRoot()) {
                    if(!energyStack().isEmpty()) {
                        main.extract(slot, energyStack(), 1, transaction);
                        previousRemovedEnergyMilestone = milestone;
                        transaction.commit();
                    }
                }
                break;
            }
        }
    }

    protected void validateRedstoneAmount(int slot) {
        if(redstone > 10000) {
            redstone = 10000;
        }

        int amount = redstone;

        int [] milestones = {1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000};

        for(long milestone : milestones) {
            if(amount >= milestone && previousRemovedRedstoneMilestone < milestone) {
                try(Transaction transaction = Transaction.openRoot()) {
                    if(!redstoneStack(slot).isEmpty()) {
                        main.extract(slot, redstoneStack(slot), 1, transaction);
                        previousRemovedRedstoneMilestone = milestone;
                        transaction.commit();
                    }
                }
                break;
            }
        }
    }

    public void startProcess() {
        if(machineStatus.isIdle() && checkRecipe() && hasRequiredEnergyAmount()) {
            machineStatus = MachineStatus.RUNNING;
        }
    }

    public void pauseProcess() {
        if(machineStatus.isRunning()) {
            machineStatus = MachineStatus.PAUSED;
        }
    }

    public void resumeProcess() {
        if(machineStatus.isPaused()&& checkRecipe() && hasRequiredEnergyAmount()) {
            machineStatus = MachineStatus.RUNNING;
        }
    }

    public void stopProcess() {
        if(!machineStatus.isIdle()) {
            machineStatus = MachineStatus.IDLE;
            clearProgress();
            try(Transaction transaction = Transaction.openRoot()) {
                this.energyHandler.insert(energyExtracted, transaction);
                transaction.commit();
            }
            this.energyExtracted = 0;
        }
    }

    protected void clearProgress() {
        initialProgressTicks = 0;
    }

    public ItemResource getStack(int slot) {
        return main.getResource(slot);
    }

    public ItemResource ingredientStack() {
        return getStack(0);
    }

    public ItemResource resultStack() {
        return getStack(1);
    }

    public ItemResource energyStack() {
        return getStack(2);
    }

    public ItemResource redstoneStack(int slot) {
        return getStack(slot);
    }
}