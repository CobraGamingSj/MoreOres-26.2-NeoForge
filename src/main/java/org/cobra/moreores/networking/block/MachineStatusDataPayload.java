package org.cobra.moreores.networking.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import org.cobra.moreores.MoreOresModLoader;
import org.cobra.moreores.world.block.entity.gem.machine.AbstractGemMachineBlockEntity;

public record MachineStatusDataPayload(BlockPos blockPos, String action) implements CustomPacketPayload {
    public static final Type<MachineStatusDataPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MoreOresModLoader.MOD_ID, "polishing_state"));

    public static final StreamCodec<RegistryFriendlyByteBuf, MachineStatusDataPayload> STREAM_CODEC = StreamCodec.ofMember((payload, buf) -> {
        buf.writeBlockPos(payload.blockPos);
        buf.writeUtf(payload.action);
    }, buf -> new MachineStatusDataPayload(buf.readBlockPos(), buf.readUtf()));

    public static void handle(MachineStatusDataPayload packet, ServerPlayer player) {
        if(player.level().getBlockEntity(packet.blockPos()) instanceof AbstractGemMachineBlockEntity be) {
            switch(packet.action) {
                case "start" -> be.startProcess();
                case "pause" -> be.pauseProcess();
                case "resume" -> be.resumeProcess();
                case "stop" -> be.stopProcess();
            }
            be.setChanged();
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}