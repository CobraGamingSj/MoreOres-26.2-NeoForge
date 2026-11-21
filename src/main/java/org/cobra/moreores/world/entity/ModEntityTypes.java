package org.cobra.moreores.world.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.cobra.moreores.MoreOresModLoader;

import java.util.function.Supplier;

public class ModEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES  = DeferredRegister.createEntities(MoreOresModLoader.MOD_ID);

    private static final ResourceKey<EntityType<?>> GEM_ARROW_KEY = ResourceKey.create(Registries.ENTITY_TYPE, MoreOresModLoader.id("gem_arrow"));

    public static final Supplier<EntityType<GemArrow>> GEM_ARROW = ENTITY_TYPES.register(
            "gem_arrow",
            () -> EntityType.Builder.<GemArrow>of(GemArrow::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f).clientTrackingRange(4).noLootTable().updateInterval(20).eyeHeight(0.13f).build(GEM_ARROW_KEY));

    public static void register(IEventBus schoolBus) {
        ENTITY_TYPES.register(schoolBus);
    }
}