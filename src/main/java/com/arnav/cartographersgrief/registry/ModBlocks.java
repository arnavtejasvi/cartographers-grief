package com.arnav.cartographersgrief.registry;

import com.arnav.cartographersgrief.block.GriefSkullBlock;
import com.arnav.cartographersgrief.block.ObeliskBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public final class ModBlocks {
    public static final Block GRIEF_SKULL = register("grief_skull",
        new GriefSkullBlock(AbstractBlock.Settings.create()
            .strength(1.5f, 6.0f)
            .sounds(BlockSoundGroup.STONE)
            .luminance(state -> 8)
            .mapColor(MapColor.PURPLE)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresTool()));

    public static final Block OBELISK = register("obelisk",
        new ObeliskBlock(AbstractBlock.Settings.create()
            .strength(2.0f, 8.0f)
            .sounds(BlockSoundGroup.STONE)
            .luminance(state -> 12)
            .mapColor(MapColor.TERRACOTTA_PURPLE)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresTool()));

    private static Block register(String id, Block block) {
        return Registry.register(Registries.BLOCK, Identifier.of("cartographersgrief", id), block);
    }

    public static void register() {}
}
