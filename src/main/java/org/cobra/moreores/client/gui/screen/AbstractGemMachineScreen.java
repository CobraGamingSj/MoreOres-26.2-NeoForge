package org.cobra.moreores.client.gui.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.cobra.moreores.MoreOresModLoader;
import org.cobra.moreores.client.gui.widget.MachineButtonWidget;
import org.cobra.moreores.networking.block.MachineStatusDataPayload;
import org.cobra.moreores.world.block.entity.gem.machine.AbstractGemMachineBlockEntity;
import org.joml.Matrix3x2fStack;
import org.lwjgl.glfw.GLFW;

public abstract class AbstractGemMachineScreen<B extends AbstractGemMachineBlockEntity, T extends AbstractGemMachineMenu<B>> extends AbstractContainerScreen<T> {
    protected static final int TEXTURE_WIDTH = 256;
    protected static final int TEXTURE_HEIGHT = 256;

    protected final Identifier SLOT_HIGHLIGHT_BACK_SPRITE_ = MoreOresModLoader.id("container/slot_highlight_back");
    protected final Identifier SLOT_HIGHLIGHT_FRONT_SPRITE_ = MoreOresModLoader.id("container/slot_highlight_front");

    ItemStack previewResultStack = ItemStack.EMPTY;

    public AbstractGemMachineScreen(T menu, Inventory inventory, Component title, int imageWidth, int imageHeight) {
        super(menu, inventory, title, imageWidth, imageHeight);
    }

    @Override
    protected void init() {
        super.init();
        titleLabelY = 1000;
        inventoryLabelY = 1000;

        Button start = this.addButton("gui.button.gp.start", 0, this.leftPos + getStartButtonPosX(), this.topPos + getStartButtonPosY(), getStartButtonTexture(), menu instanceof GemPurifierMenu ? Component.literal("Start Purification") : Component.literal("Start Crystallization"));
        Button pause = this.addButton("gui.button.gp.pause", 1, this.leftPos + getPauseButtonPosX(), this.topPos + getPauseButtonPosY(), getPauseButtonTexture(), menu instanceof GemPurifierMenu ? Component.literal("Pause Purification") : Component.literal("Pause Crystallization"));
        Button resume = this.addButton("gui.button.gp.resume", 2, this.leftPos + getResumeButtonPosX(), this.topPos + getResumeButtonPosY(), getResumeButtonTexture(), menu instanceof GemPurifierMenu ? Component.literal("Resume Purification") : Component.literal("Resume Crystallization"));
        Button stop = this.addButton("gui.button.gp.stop", 3, this.leftPos + getStopButtonPosX(), this.topPos + getStopButtonPosY(), getStopButtonTexture(), menu instanceof GemPurifierMenu ? Component.literal("Stop Purification") : Component.literal("Stop Crystallization"));

        start.visible = true;
        pause.visible = true;
        resume.visible = true;
        stop.visible = true;

//        if((menu instanceof GemPurifierMenu menu1 && menu1.isPurifying())
//                || (menu instanceof GemCrystallizerMenu menu2 && menu2.isCrystallizing())) {
//            pause.visible = true;
//            resume.visible = true;
//            stop.visible = true;
//        }
    }

    private Button addButton(String translation, int buttonIndex, int leftPos, int topPos, Identifier background, Component tooltip) {
        Button button = new MachineButtonWidget(leftPos, topPos, Component.translatable(translation), background, buttonIndex, menu.getBlockPos());
        button.setTooltip(Tooltip.create(tooltip));
        return this.addRenderableWidget(button);
    }

    protected abstract Identifier getBackground();
    protected abstract Identifier getStartButtonTexture();
    protected abstract Identifier getPauseButtonTexture();
    protected abstract Identifier getResumeButtonTexture();
    protected abstract Identifier getStopButtonTexture();

    protected abstract int getStartButtonPosX();
    protected abstract int getStartButtonPosY();

    protected abstract int getPauseButtonPosX();
    protected abstract int getPauseButtonPosY();

    protected abstract int getResumeButtonPosX();
    protected abstract int getResumeButtonPosY();

    protected abstract int getStopButtonPosX();
    protected abstract int getStopButtonPosY();

    @Override
    public boolean keyPressed(KeyEvent input) {
        if(input.input() == GLFW.GLFW_KEY_S) {
            sendPolishControlPacket("start");
            return true;
        }
        if(input.input() == GLFW.GLFW_KEY_P) {
            sendPolishControlPacket("pause");
            return true;
        }
        if(input.input() == GLFW.GLFW_KEY_R) {
            sendPolishControlPacket("resume");
            return true;
        }
        if(input.input() == GLFW.GLFW_KEY_SLASH) {
            sendPolishControlPacket("stop");
            return true;
        }
        return super.keyPressed(input);
    }

    private void sendPolishControlPacket(String action) {
        Minecraft.getInstance().getConnection().send(new MachineStatusDataPayload(menu.getBlockPos(), action));
    }

    protected abstract void extractEnergyHandler(GuiGraphicsExtractor graphics, int leftPos, int topPos);
    protected abstract void extractProgressArrow(GuiGraphicsExtractor graphics, int leftPos, int topPos);
    protected abstract void extractRedstoneDust(GuiGraphicsExtractor graphics, int leftPos, int topPos);

    private Slot getOutputSlot() {
        if (this.menu instanceof GemPurifierMenu) {
            return this.menu.getSlot(1);
        }

        if (this.menu instanceof GemCrystallizerMenu) {
            return this.menu.getSlot(2);
        }

        return null;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor extractor, int mouseX, int mouseY, float a) {
        int i = this.leftPos;
        int j = this.topPos;

        extractor.blit(RenderPipelines.GUI_TEXTURED, getBackground(), i, j, 0f, 0f, this.imageWidth, this.imageHeight, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        extractEnergyHandler(extractor, i, j);
        extractProgressArrow(extractor,i, j);
        extractRedstoneDust(extractor, i, j);

        ItemStack resultStack = this.previewResultStack;
        Slot outputSlot = getOutputSlot();

        if(outputSlot != null && outputSlot.getItem().isEmpty()) {
            int x = this.leftPos + outputSlot.x;
            int y = this.topPos + outputSlot.y;

            Matrix3x2fStack matrixStack = extractor.pose();
            matrixStack.pushMatrix();
            matrixStack.translate(x, y);
            matrixStack.scale(1.5f, 1.5f);
            extractor.fakeItem(resultStack, 0, 0);
            matrixStack.popMatrix();

            if (isHovering(outputSlot, mouseX, mouseY)) {
                extractor.setTooltipForNextFrame(this.font, Component.literal("Result: " + resultStack.getItemName().getString()), mouseX, mouseY);
            }
        }

//        extractor.blitSprite(RenderPipelines.GUI_TEXTURED, REI_HELP, i - 30, j, 25, 20);
    }

    private boolean isOutputSlot(Slot slot) {
        return (this.menu instanceof GemPurifierMenu && slot.index == 1) || (this.menu instanceof GemCrystallizerMenu && slot.index == 2);
    }

    @Override
    public void extractSlotHighlightBack(GuiGraphicsExtractor graphics) {
        if (this.hoveredSlot != null && this.hoveredSlot.isHighlightable() && isOutputSlot(this.hoveredSlot)) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_HIGHLIGHT_BACK_SPRITE_, this.hoveredSlot.x - 4, this.hoveredSlot.y - 4, 32, 32);
            return;
        }
        super.extractSlotHighlightBack(graphics);
    }

    @Override
    public void extractSlotHighlightFront(GuiGraphicsExtractor graphics) {
        if (this.hoveredSlot != null && this.hoveredSlot.isHighlightable() && isOutputSlot(this.hoveredSlot)) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_HIGHLIGHT_FRONT_SPRITE_, this.hoveredSlot.x - 4, this.hoveredSlot.y - 4, 32, 32);
            return;
        }
        super.extractSlotHighlightFront(graphics);
    }

    @Override
    public boolean isHovering(Slot slot, double mouseX, double mouseY) {
        if(isOutputSlot(slot)) {
            return this.isHovering(slot.x, slot.y, 24, 24, mouseX, mouseY);
        }
        return super.isHovering(slot, mouseX, mouseY);
    }

    @Override
    protected void extractSlot(GuiGraphicsExtractor graphics, Slot slot, int mouseX, int mouseY) {
        if(isOutputSlot(slot)) {
            graphics.pose().pushMatrix();

            graphics.pose().translate(slot.x, slot.y);
            graphics.pose().scale(1.5f, 1.5f);

            if(!slot.getItem().isEmpty()) {
                graphics.item(slot.getItem(), 0, 0);
                graphics.itemCount(this.font, slot.getItem(), slot.x - 75, slot.y - 61, null);
            }

            graphics.pose().popMatrix();
            return;
        }
        super.extractSlot(graphics, slot, mouseX, mouseY);
    }

    @Override
    public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        extractBackground(graphics, mouseX, mouseY, delta);
        super.extractContents(graphics, mouseX, mouseY, delta);
        extractTooltip(graphics, mouseX, mouseY);
    }

    public void setPreviewResultStack(ItemStack previewResultStack) {
        this.previewResultStack = previewResultStack;
    }
}