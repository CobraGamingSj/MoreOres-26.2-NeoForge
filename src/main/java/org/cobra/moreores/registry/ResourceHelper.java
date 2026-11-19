package org.cobra.moreores.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

import static org.cobra.moreores.MoreOresModLoader.id;

public class ResourceHelper {

    
    public static ResourceKey<Block> obtainKey(Block value) {
        Optional<ResourceKey<Block>> key = BuiltInRegistries.BLOCK.getResourceKey(value);
        if(key.isPresent()) {
            return key.get();
        }
        throw new IllegalArgumentException("Block " + value + " not found");
    }

    public static ResourceKey<Item> obtainKey(Item value) {
        Optional<ResourceKey<Item>> key = BuiltInRegistries.ITEM.getResourceKey(value);
        if(key.isPresent()) {
            return key.get();
        }
        throw new IllegalArgumentException("Item " + value + " not found");
    }

    public static ResourceKey<Item> itemKey(String id) {
        return ResourceKey.create(Registries.ITEM, id(id));
    }
    
    public static ResourceKey<Block> blockKey(String id) {
        return ResourceKey.create(Registries.BLOCK, id(id));
    }

    public static ResourceKey<Recipe<?>> recipeKey(String id) {
        return ResourceKey.create(Registries.RECIPE, id(id));
    }
}