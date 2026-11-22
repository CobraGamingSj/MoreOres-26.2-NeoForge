package org.cobra.moreores.core.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import org.cobra.moreores.MoreOresModLoader;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class RewardSavedData extends SavedData {
    private final Set<UUID> playerClaimedRewards = new HashSet<>();
    public static final Codec<RewardSavedData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.list(Codec.STRING).fieldOf("players").forGetter(state ->
                            state.playerClaimedRewards.stream().map(UUID::toString).toList()
                    )
            ).apply(instance, list -> {
                RewardSavedData state = new RewardSavedData();
                for (String s : list) {
                    state.playerClaimedRewards.add(UUID.fromString(s));
                }
                return state;
            })
    );

    public static final SavedDataType<RewardSavedData> TYPE =
            new SavedDataType<>(
                    MoreOresModLoader.id("moreores_birthday_rewards"),
                    RewardSavedData::new,
                    CODEC,
                    null // Not required
            );

    public boolean hasClaimed(UUID uuid) {
        return playerClaimedRewards.contains(uuid);
    }

    public void setClaimed(UUID uuid) {
        playerClaimedRewards.add(uuid);
        setDirty();
    }

    public static RewardSavedData get(ServerLevel world) {
       return world.getDataStorage().computeIfAbsent(TYPE);
    }
}
