package org.cobra.moreores.data;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.triggers.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import org.cobra.moreores.MoreOresModLoader;
import org.cobra.moreores.world.item.ModItems;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class AdvancementsCreator extends AdvancementProvider {
    public AdvancementsCreator(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, List<AdvancementSubProvider> subProviders) {
        super(output, registries, subProviders);
    }

    public static class AdvancementsSubCreator implements AdvancementSubProvider {

        @Override
        public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> output) {
            AdvancementHolder rubyGem = Advancement.Builder.advancement()
                    .display(
                            ModItems.RUBY, // The display icon
                            Component.translatable("advancement.moreores.gems"), // The title
                            Component.translatable("advancement.moreores.gems.desc"), // The description
                            MoreOresModLoader.id("block/radiant_block"), // Background image used
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    // The first string used in criterion is the name referenced by other advancements when they want to have 'requirements'
                    .addCriterion("ruby", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.RUBY))
                    .addCriterion("radiant", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.RADIANT))
                    .addCriterion("sapphire", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.SAPPHIRE))
                    .addCriterion("green_sapphire", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.GREEN_SAPPHIRE))
                    .addCriterion("blue_garnet", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.BLUE_GARNET))
                    .addCriterion("pink_garnet", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.PINK_GARNET))
                    .addCriterion("green_garnet", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.GREEN_GARNET))
                    .addCriterion("kyawthuite", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.KYAWTHUITE))
                    .addCriterion("topaz", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.TOPAZ))
                    .addCriterion("white_topaz", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.WHITE_TOPAZ))
                    .addCriterion("peridot", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.PERIDOT))
                    .addCriterion("jade", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.JADE))
                    .addCriterion("pyrope", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.PYROPE))
                    .save(output, MoreOresModLoader.id("is_that_a_gem"));

            AdvancementHolder ruby_armor = Advancement.Builder.advancement()
                    .display(
                            ModItems.RUBY_CHESTPLATE.get(),
                            Component.translatable("advancement.moreores.ruby_armor").withStyle(ChatFormatting.DARK_AQUA),
                            Component.translatable("advancement.moreores.ruby_armor.desc"),
                            null,
                            AdvancementType.GOAL,
                            true,
                            true,
                            false
                    )
                    .parent(rubyGem)
                    .addCriterion("ruby_armor", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.RUBY_HELMET, ModItems.RUBY_CHESTPLATE, ModItems.RUBY_LEGGINGS, ModItems.RUBY_BOOTS))
                    .save(output, MoreOresModLoader.id("ruby_armor"));

            AdvancementHolder radiant_sword = Advancement.Builder.advancement()
                    .display(
                            ModItems.RADIANT_SWORD.get(),
                            Component.translatable("advancement.moreores.radiant_sword").withStyle(ChatFormatting.DARK_PURPLE),
                            Component.translatable("advancement.moreores.radiant_sword.desc"),
                            null,
                            AdvancementType.CHALLENGE,
                            true,
                            true,
                            false
                    )
                    .parent(rubyGem)
                    .addCriterion("radiant_sword", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.RADIANT_SWORD))
                    .save(output, MoreOresModLoader.id("overpowered"));
        }
    }
}