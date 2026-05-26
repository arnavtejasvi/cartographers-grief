package com.arnav.cartographersgrief.registry;

import com.arnav.cartographersgrief.block.GriefSkullBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class ModBlockEntityTypes {
    public static final BlockEntityType<GriefSkullBlockEntity> GRIEF_SKULL_ENTITY =
        Registry.register(Registries.BLOCK_ENTITY_TYPE,
            Identifier.of("cartographersgrief", "grief_skull_entity"),
            BlockEntityType.Builder.create(GriefSkullBlockEntity::new, ModBlocks.GRIEF_SKULL).build());

    public static void register() {}
}
