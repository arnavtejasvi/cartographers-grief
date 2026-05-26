package com.arnav.cartographersgrief;

import com.arnav.cartographersgrief.event.ChunkLoadHandler;
import com.arnav.cartographersgrief.hud.WanderlustHud;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class CartographersGriefClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ChunkLoadHandler.register();
        HudRenderCallback.EVENT.register(WanderlustHud::render);
    }
}
