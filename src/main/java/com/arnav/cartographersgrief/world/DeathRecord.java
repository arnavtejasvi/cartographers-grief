package com.arnav.cartographersgrief.world;

import net.minecraft.util.math.BlockPos;

public record DeathRecord(BlockPos pos, long tick, String message, String player) {}
