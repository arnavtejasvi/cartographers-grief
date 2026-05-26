package com.arnav.cartographersgrief.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.Structure;

import java.util.List;
import java.util.Optional;

public class WanderersCompassItem extends Item {

    private static final List<String> STRUCTURE_IDS = List.of(
        "minecraft:village_plains",
        "minecraft:village_savanna",
        "minecraft:village_taiga",
        "minecraft:village_desert",
        "minecraft:village_snowy",
        "minecraft:ruined_portal",
        "minecraft:pillager_outpost",
        "minecraft:desert_pyramid",
        "minecraft:jungle_pyramid",
        "minecraft:swamp_hut",
        "minecraft:igloo",
        "minecraft:shipwreck",
        "minecraft:buried_treasure"
    );

    public WanderersCompassItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (world.isClient()) return TypedActionResult.success(stack);

        ServerWorld serverWorld = (ServerWorld) world;
        BlockPos origin = player.getBlockPos();

        Optional<BlockPos> found = locateNearestStructure(serverWorld, origin);

        if (found.isEmpty()) {
            player.sendMessage(Text.literal(
                "§5[Cartographer] §7The compass spins wildly. No uncharted territory found nearby."
            ), false);
        } else {
            BlockPos target = found.get();
            int dx = target.getX() - origin.getX();
            int dz = target.getZ() - origin.getZ();
            double dist = Math.sqrt((double) dx * dx + (double) dz * dz);
            String arrow = directionArrow(dx, dz);
            player.sendMessage(Text.literal(
                "§5[Cartographer] §7Uncharted territory: §5" + target.getX() + ", " + target.getZ() +
                "§7 — §5" + (int) dist + " §7blocks " + arrow
            ), false);
        }

        return TypedActionResult.success(stack);
    }

    private Optional<BlockPos> locateNearestStructure(ServerWorld world, BlockPos origin) {
        var structureRegistry = world.getRegistryManager().get(RegistryKeys.STRUCTURE);

        for (String id : STRUCTURE_IDS) {
            RegistryKey<Structure> key = RegistryKey.of(RegistryKeys.STRUCTURE, Identifier.of(id));
            Optional<RegistryEntry.Reference<Structure>> entryOpt = structureRegistry.getEntry(key);
            if (entryOpt.isEmpty()) continue;

            RegistryEntryList<Structure> list = RegistryEntryList.of(entryOpt.get());
            var result = world.getChunkManager().getChunkGenerator()
                .locateStructure(world, list, origin, 100, false);
            if (result != null) return Optional.of(result.getFirst());
        }
        return Optional.empty();
    }

    private String directionArrow(int dx, int dz) {
        double angle = Math.toDegrees(Math.atan2(dz, dx));
        if (angle < 0) angle += 360;

        if (angle < 22.5 || angle >= 337.5) return "→ E";
        if (angle < 67.5)  return "↘ SE";
        if (angle < 112.5) return "↓ S";
        if (angle < 157.5) return "↙ SW";
        if (angle < 202.5) return "← W";
        if (angle < 247.5) return "↖ NW";
        if (angle < 292.5) return "↑ N";
        return "↗ NE";
    }
}
