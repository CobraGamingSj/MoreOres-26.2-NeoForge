package org.cobra.moreores.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import org.cobra.moreores.MoreOresModLoader;
import org.cobra.moreores.registry.ResourceHelper;
import org.cobra.moreores.tags.ModItemTags;
import org.cobra.moreores.world.block.ModBlocks;
import org.cobra.moreores.world.item.ModItems;

import java.util.concurrent.CompletableFuture;

public class ItemTagGen extends ItemTagsProvider {
    public ItemTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, MoreOresModLoader.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModItemTags.HAS_ENERGY)
                .add(ResourceHelper.obtainKey(ModItems.ENERGY_INGOT.get()))
                .add(ResourceHelper.obtainKey(ModBlocks.ENERGY_BLOCK.get().asItem()));

        tag(ItemTags.SPEARS)
                .add(ResourceHelper.obtainKey(ModItems.RUBY_SPEAR.get()))
                .add(ResourceHelper.obtainKey(ModItems.SAPPHIRE_SPEAR.get()));

        tag(ItemTags.TRIM_MATERIALS)
                .add(ResourceHelper.obtainKey(ModItems.RUBY.get()))
                .add(ResourceHelper.obtainKey(ModItems.RADIANT.get()))
                .add(ResourceHelper.obtainKey(ModItems.SAPPHIRE.get()))
                .add(ResourceHelper.obtainKey(ModItems.GREEN_SAPPHIRE.get()))
                .add(ResourceHelper.obtainKey(ModItems.BLUE_GARNET.get()))
                .add(ResourceHelper.obtainKey(ModItems.PINK_GARNET.get()))
                .add(ResourceHelper.obtainKey(ModItems.GREEN_GARNET.get()))
                .add(ResourceHelper.obtainKey(ModItems.KYAWTHUITE.get()))
                .add(ResourceHelper.obtainKey(ModItems.TOPAZ.get()))
                .add(ResourceHelper.obtainKey(ModItems.WHITE_TOPAZ.get()))
                .add(ResourceHelper.obtainKey(ModItems.PERIDOT.get()))
                .add(ResourceHelper.obtainKey(ModItems.JADE.get()))
                .add(ResourceHelper.obtainKey(ModItems.PYROPE.get()))
                .add(ResourceHelper.obtainKey(ModItems.CRIMSON_GARNET.get()))
                .add(ResourceHelper.obtainKey(ModItems.CRYSTALLITE.get()))
                .add(ResourceHelper.obtainKey(ModItems.ALEXANDRITE.get()))
                .add(ResourceHelper.obtainKey(ModItems.OPAL.get()))
                .add(ResourceHelper.obtainKey(ModItems.QUARTSIDIAN.get()))
                .add(ResourceHelper.obtainKey(ModItems.RADIANT_AMETHYST.get()))
                .add(ResourceHelper.obtainKey(ModItems.LIMESTONE.get()))
                .add(ResourceHelper.obtainKey(ModItems.MOONSTONE.get()))
                .add(ResourceHelper.obtainKey(ModItems.RED_BERYL.get()));

        tag(ItemTags.SWORDS)
                .add(ResourceHelper.obtainKey(ModItems.RUBY_SWORD.get()))
                .add(ResourceHelper.obtainKey(ModItems.SAPPHIRE_SWORD.get()))
                .add(ResourceHelper.obtainKey(ModItems.RADIANT_SWORD.get()));

        tag(ItemTags.PICKAXES)
                .add(ResourceHelper.obtainKey(ModItems.RUBY_PICKAXE.get()))
                .add(ResourceHelper.obtainKey(ModItems.SAPPHIRE_PICKAXE.get()))
                .add(ResourceHelper.obtainKey(ModItems.RADIANT_PICKAXE.get()));

        tag(ItemTags.AXES)
                .add(ResourceHelper.obtainKey(ModItems.RUBY_AXE.get()))
                .add(ResourceHelper.obtainKey(ModItems.SAPPHIRE_AXE.get()))
                .add(ResourceHelper.obtainKey(ModItems.RADIANT_AXE.get()));

        tag(ItemTags.SHOVELS)
                .add(ResourceHelper.obtainKey(ModItems.RUBY_SHOVEL.get()))
                .add(ResourceHelper.obtainKey(ModItems.SAPPHIRE_SHOVEL.get()))
                .add(ResourceHelper.obtainKey(ModItems.RADIANT_SHOVEL.get()));

        tag(ItemTags.HOES)
                .add(ResourceHelper.obtainKey(ModItems.RUBY_HOE.get()))
                .add(ResourceHelper.obtainKey(ModItems.SAPPHIRE_HOE.get()))
                .add(ResourceHelper.obtainKey(ModItems.RADIANT_HOE.get()));

        tag(ItemTags.FOOT_ARMOR)
                .add(ResourceHelper.obtainKey(ModItems.RUBY_BOOTS.get()))
                .add(ResourceHelper.obtainKey(ModItems.SAPPHIRE_BOOTS.get()))
                .add(ResourceHelper.obtainKey(ModItems.RADIANT_BOOTS.get()));

        tag(ItemTags.LEG_ARMOR)
                .add(ResourceHelper.obtainKey(ModItems.RUBY_LEGGINGS.get()))
                .add(ResourceHelper.obtainKey(ModItems.SAPPHIRE_LEGGINGS.get()))
                .add(ResourceHelper.obtainKey(ModItems.RADIANT_LEGGINGS.get()));

        tag(ItemTags.HEAD_ARMOR)
                .add(ResourceHelper.obtainKey(ModItems.RUBY_HELMET.get()))
                .add(ResourceHelper.obtainKey(ModItems.SAPPHIRE_HELMET.get()))
                .add(ResourceHelper.obtainKey(ModItems.RADIANT_HELMET.get()));

        tag(ItemTags.CHEST_ARMOR)
                .add(ResourceHelper.obtainKey(ModItems.RUBY_CHESTPLATE.get()))
                .add(ResourceHelper.obtainKey(ModItems.SAPPHIRE_CHESTPLATE.get()))
                .add(ResourceHelper.obtainKey(ModItems.RADIANT_CHESTPLATE.get()));

        tag(ItemTags.TRIMMABLE_ARMOR)
                .add(ResourceHelper.obtainKey(ModItems.RUBY_HELMET.get()))
                .add(ResourceHelper.obtainKey(ModItems.RUBY_CHESTPLATE.get()))
                .add(ResourceHelper.obtainKey(ModItems.RUBY_LEGGINGS.get()))
                .add(ResourceHelper.obtainKey(ModItems.RUBY_BOOTS.get()))
                .add(ResourceHelper.obtainKey(ModItems.SAPPHIRE_HELMET.get()))
                .add(ResourceHelper.obtainKey(ModItems.SAPPHIRE_CHESTPLATE.get()))
                .add(ResourceHelper.obtainKey(ModItems.SAPPHIRE_LEGGINGS.get()))
                .add(ResourceHelper.obtainKey(ModItems.RADIANT_HELMET.get()))
                .add(ResourceHelper.obtainKey(ModItems.RADIANT_CHESTPLATE.get()))
                .add(ResourceHelper.obtainKey(ModItems.RADIANT_LEGGINGS.get()))
                .add(ResourceHelper.obtainKey(ModItems.RADIANT_BOOTS.get()));

        tag(ModItemTags.GEMSTONE)
                .add(ResourceHelper.obtainKey(ModItems.RUBY.get()))
                .add(ResourceHelper.obtainKey(ModItems.RADIANT.get()))
                .add(ResourceHelper.obtainKey(ModItems.SAPPHIRE.get()))
                .add(ResourceHelper.obtainKey(ModItems.GREEN_SAPPHIRE.get()))
                .add(ResourceHelper.obtainKey(ModItems.BLUE_GARNET.get()))
                .add(ResourceHelper.obtainKey(ModItems.PINK_GARNET.get()))
                .add(ResourceHelper.obtainKey(ModItems.GREEN_GARNET.get()))
                .add(ResourceHelper.obtainKey(ModItems.KYAWTHUITE.get()))
                .add(ResourceHelper.obtainKey(ModItems.TOPAZ.get()))
                .add(ResourceHelper.obtainKey(ModItems.WHITE_TOPAZ.get()))
                .add(ResourceHelper.obtainKey(ModItems.PERIDOT.get()))
                .add(ResourceHelper.obtainKey(ModItems.JADE.get()))
                .add(ResourceHelper.obtainKey(ModItems.PYROPE.get()))
                .add(ResourceHelper.obtainKey(Items.LAPIS_LAZULI))
                .add(ResourceHelper.obtainKey(Items.QUARTZ))
                .add(ResourceHelper.obtainKey(Items.DIAMOND))
                .add(ResourceHelper.obtainKey(ModItems.CRIMSON_GARNET.get()))
                .add(ResourceHelper.obtainKey(ModItems.CRYSTALLITE.get()))
                .add(ResourceHelper.obtainKey(ModItems.ALEXANDRITE.get()))
                .add(ResourceHelper.obtainKey(ModItems.ORANGE_ZIRCON.get()))
                .add(ResourceHelper.obtainKey(ModItems.OPAL.get()))
                .add(ResourceHelper.obtainKey(ModItems.QUARTSIDIAN.get()))
                .add(ResourceHelper.obtainKey(ModItems.KASHMIR_SAPPHIRE.get()))
                .add(ResourceHelper.obtainKey(ModItems.RADIANT_AMETHYST.get()))
                .add(ResourceHelper.obtainKey(ModItems.LIMESTONE.get()))
                .add(ResourceHelper.obtainKey(ModItems.MOONSTONE.get()))
                .add(ResourceHelper.obtainKey(ModItems.RED_BERYL.get()))
                .add(ResourceHelper.obtainKey(ModItems.GRANDIDIERITE.get()))
                .add(ResourceHelper.obtainKey(ModItems.CRYSTAL_OF_ECLIPSE.get()));

        tag(ModItemTags.GEMSTONE_BLOCKS)
                .add(ResourceHelper.obtainKey(ModBlocks.RUBY_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.SAPPHIRE_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.GREEN_SAPPHIRE_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.BLUE_GARNET_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.PINK_GARNET_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.GREEN_GARNET_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.KYAWTHUITE_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.TOPAZ_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.WHITE_TOPAZ_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.PERIDOT_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.JADE_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.PYROPE_BLOCK.get().asItem()));

        tag(ModItemTags.CRYSTALLIZED_GEMSTONES)
                .add(ResourceHelper.obtainKey(ModItems.CRIMSON_GARNET.get()))
                .add(ResourceHelper.obtainKey(ModItems.CRYSTALLITE.get()))
                .add(ResourceHelper.obtainKey(ModItems.ALEXANDRITE.get()))
                .add(ResourceHelper.obtainKey(ModItems.ORANGE_ZIRCON.get()))
                .add(ResourceHelper.obtainKey(ModItems.OPAL.get()))
                .add(ResourceHelper.obtainKey(ModItems.QUARTSIDIAN.get()))
                .add(ResourceHelper.obtainKey(ModItems.KASHMIR_SAPPHIRE.get()))
                .add(ResourceHelper.obtainKey(ModItems.RADIANT_AMETHYST.get()))
                .add(ResourceHelper.obtainKey(ModItems.LIMESTONE.get()))
                .add(ResourceHelper.obtainKey(ModItems.MOONSTONE.get()))
                .add(ResourceHelper.obtainKey(ModItems.RED_BERYL.get()))
                .add(ResourceHelper.obtainKey(ModItems.GRANDIDIERITE.get()))
                .add(ResourceHelper.obtainKey(ModItems.CRYSTAL_OF_ECLIPSE.get()));

        tag(ModItemTags.CRYSTALLIZED_GEMSTONE_BLOCKS)
                .add(ResourceHelper.obtainKey(ModBlocks.CRIMSON_GARNET_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.ALEXANDRITE_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.CRYSTALLITE_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.MOONSTONE_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.LIMESTONE_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.QUARTSIDIAN_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.RED_BERYL_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.ORANGE_ZIRCON_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.OPAL_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.KASHMIR_SAPPHIRE_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.RADIANT_AMETHYST_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.GRANDIDIERITE_BLOCK.get().asItem()));

        tag(ModItemTags.CRYSTALLIZED)
                .add(ResourceHelper.obtainKey(ModItems.CRIMSON_GARNET.get()))
                .add(ResourceHelper.obtainKey(ModItems.CRYSTALLITE.get()))
                .add(ResourceHelper.obtainKey(ModItems.ALEXANDRITE.get()))
                .add(ResourceHelper.obtainKey(ModItems.OPAL.get()))
                .add(ResourceHelper.obtainKey(ModItems.QUARTSIDIAN.get()))
                .add(ResourceHelper.obtainKey(ModItems.RADIANT_AMETHYST.get()))
                .add(ResourceHelper.obtainKey(ModItems.LIMESTONE.get()))
                .add(ResourceHelper.obtainKey(ModItems.MOONSTONE.get()))
                .add(ResourceHelper.obtainKey(ModItems.RED_BERYL.get()))
                .add(ResourceHelper.obtainKey(ModItems.GRANDIDIERITE.get()))
                .add(ResourceHelper.obtainKey(ModItems.KASHMIR_SAPPHIRE.get()))
                .add(ResourceHelper.obtainKey(ModItems.ORANGE_ZIRCON.get()))
                .add(ResourceHelper.obtainKey(ModBlocks.CRIMSON_GARNET_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.ALEXANDRITE_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.CRYSTALLITE_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.MOONSTONE_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.LIMESTONE_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.QUARTSIDIAN_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.RED_BERYL_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.ORANGE_ZIRCON_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.OPAL_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.KASHMIR_SAPPHIRE_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.RADIANT_AMETHYST_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.GRANDIDIERITE_BLOCK.get().asItem()));

        tag(ModItemTags.RAW_GEMSTONE)
                .add(ResourceHelper.obtainKey(ModItems.RAW_RUBY.get()))
                .add(ResourceHelper.obtainKey(ModItems.RAW_SAPPHIRE.get()))
                .add(ResourceHelper.obtainKey(ModItems.RAW_GREEN_SAPPHIRE.get()))
                .add(ResourceHelper.obtainKey(ModItems.RAW_BLUE_GARNET.get()))
                .add(ResourceHelper.obtainKey(ModItems.RAW_PINK_GARNET.get()))
                .add(ResourceHelper.obtainKey(ModItems.RAW_GREEN_GARNET.get()))
                .add(ResourceHelper.obtainKey(ModItems.RAW_KYAWTHUITE.get()))
                .add(ResourceHelper.obtainKey(ModItems.RAW_TOPAZ.get()))
                .add(ResourceHelper.obtainKey(ModItems.RAW_WHITE_TOPAZ.get()))
                .add(ResourceHelper.obtainKey(ModItems.RAW_PERIDOT.get()))
                .add(ResourceHelper.obtainKey(ModItems.RAW_JADE.get()))
                .add(ResourceHelper.obtainKey(ModItems.RAW_PYROPE.get()));

        tag(ModItemTags.RAW_GEMSTONE_BLOCKS)
                .add(ResourceHelper.obtainKey(ModBlocks.RAW_RUBY_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.RAW_SAPPHIRE_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.RAW_GREEN_SAPPHIRE_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.RAW_BLUE_GARNET_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.RAW_PINK_GARNET_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.RAW_GREEN_GARNET_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.RAW_KYAWTHUITE_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.RAW_TOPAZ_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.RAW_WHITE_TOPAZ_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.RAW_PERIDOT_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.RAW_JADE_BLOCK.get().asItem()))
                .add(ResourceHelper.obtainKey(ModBlocks.RAW_PYROPE_BLOCK.get().asItem()));

        tag(ModItemTags.REPAIRS_RUBY_ARMOR)
                .add(ResourceHelper.obtainKey(ModItems.RUBY.get()));

        tag(ModItemTags.REPAIRS_SAPPHIRE_ARMOR)
                .add(ResourceHelper.obtainKey(ModItems.SAPPHIRE.get()));

        tag(ModItemTags.REPAIRS_RADIANT_ARMOR)
                .add(ResourceHelper.obtainKey(ModItems.RADIANT.get()));

        tag(ModItemTags.RUBY_TOOL_MATERIALS)
                .add(ResourceHelper.obtainKey(ModItems.RUBY.get()));

        tag(ModItemTags.SAPPHIRE_TOOL_MATERIALS)
                .add(ResourceHelper.obtainKey(ModItems.SAPPHIRE.get()));

        tag(ModItemTags.RADIANT_TOOL_MATERIALS)
                .add(ResourceHelper.obtainKey(ModItems.RADIANT.get()));
    }
}