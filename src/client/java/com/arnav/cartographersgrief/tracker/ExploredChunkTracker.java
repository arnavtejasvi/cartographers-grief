package com.arnav.cartographersgrief.tracker;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ExploredChunkTracker {
    private static final Gson GSON = new Gson();
    private static final Set<Long> explored = new HashSet<>();
    private static int wanderlust = 0;
    private static final int MAX_WANDERLUST = 500;
    private static String currentWorldId = null;

    public static void onChunkLoaded(int chunkX, int chunkZ) {
        long key = ((long) chunkX << 32) | (chunkZ & 0xFFFFFFFFL);
        if (explored.add(key)) {
            wanderlust = Math.min(wanderlust + 1, MAX_WANDERLUST);
        }
    }

    public static float getWanderlustFraction() {
        return wanderlust / (float) MAX_WANDERLUST;
    }

    public static void load(String worldId) {
        currentWorldId = worldId;
        explored.clear();
        wanderlust = 0;
        Path file = getFile(worldId);
        if (!Files.exists(file)) return;

        try (Reader r = Files.newBufferedReader(file)) {
            Type type = new TypeToken<List<Long>>() {}.getType();
            List<Long> list = GSON.fromJson(r, type);
            if (list != null) {
                explored.addAll(list);
                wanderlust = Math.min(explored.size(), MAX_WANDERLUST);
            }
        } catch (IOException ignored) {}
    }

    public static void save() {
        if (currentWorldId == null) return;
        Path file = getFile(currentWorldId);
        try {
            Files.createDirectories(file.getParent());
            try (Writer w = Files.newBufferedWriter(file)) {
                GSON.toJson(new ArrayList<>(explored), w);
            }
        } catch (IOException ignored) {}
    }

    private static Path getFile(String worldId) {
        String safe = worldId.replaceAll("[^a-zA-Z0-9_\\-]", "_");
        return FabricLoader.getInstance().getConfigDir()
            .resolve("cartographersgrief")
            .resolve(safe + ".json");
    }
}
