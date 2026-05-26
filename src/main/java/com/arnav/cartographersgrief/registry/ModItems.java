package com.arnav.cartographersgrief.registry;

import com.arnav.cartographersgrief.item.MemoryOrbItem;
import com.arnav.cartographersgrief.item.WanderersCompassItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public final class ModItems {
    public static final Item MEMORY_SHARD = register("memory_shard",
        new Item(new Item.Settings().rarity(Rarity.UNCOMMON)));

    public static final Item MEMORY_ORB = register("memory_orb",
        new MemoryOrbItem(new Item.Settings().maxCount(1)));

    public static final Item WANDERERS_COMPASS = register("wanderers_compass",
        new WanderersCompassItem(new Item.Settings().maxCount(1)));

    // Block items
    public static final Item GRIEF_SKULL_ITEM = register("grief_skull",
        new BlockItem(ModBlocks.GRIEF_SKULL, new Item.Settings()));

    public static final Item OBELISK_ITEM = register("obelisk",
        new BlockItem(ModBlocks.OBELISK, new Item.Settings()));

    private static Item register(String id, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of("cartographersgrief", id), item);
    }

    public static void register() {}
}
