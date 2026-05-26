package com.arnav.cartographersgrief.event;

import com.arnav.cartographersgrief.registry.ModBlocks;
import com.arnav.cartographersgrief.world.DeathRecord;
import com.arnav.cartographersgrief.world.DeathTracker;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public final class ObeliskUpgradeHandler {

    private static final long UPGRADE_TICKS = 168_000L;
    private static final int CHECK_INTERVAL = 200;

    public static void register() {
        ServerTickEvents.END_WORLD_TICK.register(ObeliskUpgradeHandler::onWorldTick);
    }

    private static void onWorldTick(ServerWorld world) {
        if (world.getTime() % CHECK_INTERVAL != 0) return;

        long now = world.getTime();
        DeathTracker tracker = DeathTracker.get(world);

        List<BlockPos> toUpgrade = new ArrayList<>();
        for (DeathRecord record : tracker.getAll()) {
            if (now - record.tick() < UPGRADE_TICKS) continue;
            if (!world.getBlockState(record.pos()).isOf(ModBlocks.GRIEF_SKULL)) continue;
            toUpgrade.add(record.pos());
        }
        for (BlockPos pos : toUpgrade) {
            world.setBlockState(pos, ModBlocks.OBELISK.getDefaultState());
            tracker.remove(pos);
        }
    }
}
