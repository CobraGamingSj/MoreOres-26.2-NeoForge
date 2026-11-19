package org.cobra.moreores.networking.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.cobra.moreores.MoreOresModLoader;
import org.cobra.moreores.client.gui.screen.AbstractGemMachineMenu;
import org.cobra.moreores.world.block.entity.gem.machine.AbstractGemMachineBlockEntity;

public record GemMachineButtonHandlingPayload(int buttonIndex, BlockPos pos) implements CustomPacketPayload {
    public static final Type<GemMachineButtonHandlingPayload> TYPE = new Type<>(MoreOresModLoader.id("button_click"));

    public static final StreamCodec<RegistryFriendlyByteBuf, GemMachineButtonHandlingPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, GemMachineButtonHandlingPayload::buttonIndex,
                    BlockPos.STREAM_CODEC, GemMachineButtonHandlingPayload::pos,
                    GemMachineButtonHandlingPayload::new
            );

    public static void handle(GemMachineButtonHandlingPayload packet, ServerPlayer player) {
        ServerLevel world = player.level();

        if(world.getBlockEntity(packet.pos) instanceof AbstractGemMachineBlockEntity blockEntity) {
            switch (packet.buttonIndex) {
                case 0 -> {
                    if(!blockEntity.hasRequiredEnergyAmount()) {
                        player.sendSystemMessage(Component.literal("Not enough energy amount!").withStyle(ChatFormatting.RED), false);
                    }
                    blockEntity.startProcess();
                }
                case 1 -> blockEntity.pauseProcess();
                case 2 -> blockEntity.resumeProcess();
                case 3 -> blockEntity.stopProcess();
            }

            if((player.containerMenu instanceof AbstractGemMachineMenu<?> menu && menu.getBlockPos().equals(packet.pos))) {
                switch (packet.buttonIndex) {
                    case 0 -> blockEntity.startProcess();
                    case 1 -> blockEntity.pauseProcess();
                    case 2 -> blockEntity.resumeProcess();
                    case 3 -> blockEntity.stopProcess();
                }
            }
        }

        MoreOresModLoader.LOGGER.info("Received button click with ID: {} at {}", packet.buttonIndex, "[" + packet.pos.getX() + ", " + packet.pos.getY() + ", " + packet.pos.getZ() + "]");
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}