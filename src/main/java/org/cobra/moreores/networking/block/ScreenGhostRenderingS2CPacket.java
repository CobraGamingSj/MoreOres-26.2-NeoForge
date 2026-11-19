package org.cobra.moreores.networking.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import org.cobra.moreores.MoreOresModLoader;

public record ScreenGhostRenderingS2CPacket(BlockPos pos, ItemStack result) implements CustomPacketPayload {
    public static final Type<ScreenGhostRenderingS2CPacket> TYPE = new Type<>(MoreOresModLoader.id("screen_sync"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ScreenGhostRenderingS2CPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, ScreenGhostRenderingS2CPacket::pos,
            ItemStack.OPTIONAL_STREAM_CODEC, ScreenGhostRenderingS2CPacket::result,
            ScreenGhostRenderingS2CPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}