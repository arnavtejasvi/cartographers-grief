package com.arnav.cartographersgrief.event;

import com.arnav.cartographersgrief.block.GriefSkullBlockEntity;
import com.arnav.cartographersgrief.registry.ModBlocks;
import com.arnav.cartographersgrief.world.DeathRecord;
import com.arnav.cartographersgrief.world.DeathTracker;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public final class DeathEventHandler {

    public static void register() {
        ServerLivingEntityEvents.ALLOW_DEATH.register(DeathEventHandler::onDeath);
    }

    private static boolean onDeath(LivingEntity entity, DamageSource source, float amount) {
        if (!(entity instanceof ServerPlayerEntity player)) return true;

        ServerWorld world = (ServerWorld) player.getWorld();
        BlockPos pos = findSolidSurface(world, player.getBlockPos());

        world.setBlockState(pos, ModBlocks.GRIEF_SKULL.getDefaultState());

        if (world.getBlockEntity(pos) instanceof GriefSkullBlockEntity skull) {
            String message = source.getDeathMessage(player).getString();
            String name = player.getNameForScoreboard();
            long tick = world.getTime();
            skull.setData(tick, message, name);
            DeathTracker.get(world).add(new DeathRecord(pos, tick, message, name));
        }

        return true;
    }

    private static BlockPos findSolidSurface(ServerWorld world, BlockPos origin) {
        BlockPos pos = origin;
        for (int i = 0; i < 8; i++) {
            if (!world.getBlockState(pos).isAir()) {
                return pos.up();
            }
            pos = pos.down();
        }
        return origin;
    }
}
