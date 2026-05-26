package com.arnav.cartographersgrief.event;

import com.arnav.cartographersgrief.tracker.ExploredChunkTracker;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.chunk.WorldChunk;

public final class ChunkLoadHandler {

    public static void register() {
        ClientChunkEvents.CHUNK_LOAD.register(ChunkLoadHandler::onChunkLoad);

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            String worldId = resolveWorldId(client);
            ExploredChunkTracker.load(worldId);
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            ExploredChunkTracker.save();
        });
    }

    private static void onChunkLoad(ClientWorld world, WorldChunk chunk) {
        ExploredChunkTracker.onChunkLoaded(chunk.getPos().x, chunk.getPos().z);
    }

    private static String resolveWorldId(MinecraftClient client) {
        if (client.getServer() != null) {
            return "singleplayer_" + client.getServer().getSaveProperties().getLevelName();
        }
        if (client.getCurrentServerEntry() != null) {
            return "server_" + client.getCurrentServerEntry().address;
        }
        return "unknown";
    }
}
