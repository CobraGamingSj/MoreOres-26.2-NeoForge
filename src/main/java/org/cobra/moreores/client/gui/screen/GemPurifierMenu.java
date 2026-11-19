package org.cobra.moreores.client.gui.screen;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;
import org.cobra.moreores.tags.ModItemTags;
import org.cobra.moreores.world.block.ModBlocks;
import org.cobra.moreores.world.block.entity.gem.machine.GemPurifierBlockEntity;
import org.cobra.moreores.world.item.ModItems;

public class GemPurifierMenu extends AbstractGemMachineMenu<GemPurifierBlockEntity> {
    private final Level level;
    private final ContainerData containerData;
    
    public GemPurifierMenu(int containerId, Inventory inventory, FriendlyByteBuf extra) {
        this(containerId, inventory, inventory.player.level().getBlockEntity(extra.readBlockPos()), new ItemStacksResourceHandler(17), new SimpleContainerData(3));
    }

    public GemPurifierMenu(int containerId, Inventory inventory, BlockEntity blockEntity, ItemStacksResourceHandler handler, ContainerData containerData) {
        super(ModMenuType.GEM_PURIFIER.get(), containerId, blockEntity.getBlockPos(), (GemPurifierBlockEntity) blockEntity);
        this.level = inventory.player.level();
        this.containerData = containerData;

        this.addSlot(new ResourceHandlerSlot(handler, handler::set, 0, 79, 11) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(ModItemTags.RAW_GEMSTONE) || stack.is(ModItemTags.RAW_GEMSTONE_BLOCKS) || stack.is(ModItems.RAW_RUBY.get());
            }
        }); // Input
        
        this.addSlot(new ResourceHandlerSlot(handler, handler::set, 1, 75, 61) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(ModItemTags.GEMSTONE) || stack.is(ModItemTags.GEMSTONE_BLOCKS) || stack.is(ModItems.RUBY.get());
            }
        }); // Result
        
        this.addSlot(new ResourceHandlerSlot(handler, handler::set, 2, 40, 20) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(ModItems.ENERGY_INGOT.get()) || stack.is(ModBlocks.ENERGY_BLOCK.get().asItem());
            }
        }); // Energy Input

        this.addSlot(new ResourceHandlerSlot(handler, handler::set, 3, 12, 20)); // Water Source

        this.addSlot(new ResourceHandlerSlot(handler, handler::set, 4, 109, 33)); // Redstone Source

        for (int i = 0; i < 12; i++) {
            this.addSlot(new ResourceHandlerSlot(handler, handler::set, 5 + i, 6 + i * 18, 178));
        }
        
        addPlayerGenericInventory(inventory);
        addPlayerHotbarInventory(inventory);
        
        addDataSlots(containerData);
    }

    public int getRedstoneDust() {
        return containerData.get(2);
    }

    public boolean isPurifying() {
        return containerData.get(0) > 0;
    }

    public int progressGetter() {
        int progress = this.containerData.get(0); //Progress
        int maxProgress = this.containerData.get(1); //Max Progress
        int progressArrowSize = 27; //Height of progress arrow

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize/ maxProgress : 0;
    }

    @Override
    public void addPlayerGenericInventory(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            for (int l = 0; l < 3; ++l) {
                this.addSlot(new Slot(playerInventory, i * 3 + l + 9, 142 + l * 18, 11 + i * 18));
            }
        }
    }

    @Override
    public void addPlayerHotbarInventory(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 201, 11 + i * 18));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);

        if(slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            stack = originalStack.copy();

            if(invSlot == 2) {
                if(!this.moveItemStackTo(originalStack, 15, 51, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(originalStack, stack);
            } else if(invSlot >= 17 && invSlot < 51) {
                if(isValidInput(originalStack)) {
                    if(!this.moveItemStackTo(originalStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (isValidEnergyItem(originalStack)) {
                    if(!this.moveItemStackTo(originalStack, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if(!this.moveItemStackTo(originalStack, 3, 15, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else {
                if(!this.moveItemStackTo(originalStack, 15, 51, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if(originalStack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return stack;
    }

    private boolean isValidInput(ItemStack stack) {
        return stack.is(ModItemTags.RAW_GEMSTONE);
    }

    private boolean isValidEnergyItem(ItemStack stack) {
        return stack.is(ModItems.ENERGY_INGOT) || stack.is(ModBlocks.ENERGY_BLOCK.asItem());
    }

    private boolean isWaterBucket(ItemStack stack) {
        return stack.is(Items.WATER_BUCKET);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, ModBlocks.GEM_PURIFIER_BLOCK.get());
    }

}