package com.arnav.cartographersgrief.hud;

import com.arnav.cartographersgrief.tracker.ExploredChunkTracker;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

public final class WanderlustHud {

    private static final int BAR_WIDTH  = 200;
    private static final int BAR_HEIGHT = 4;
    private static final int COLOR_DIM  = 0x6A5ACD;
    private static final int COLOR_FULL = 0xE0C8FF;

    public static void render(DrawContext ctx, RenderTickCounter ticker) {
        float fraction = ExploredChunkTracker.getWanderlustFraction();
        if (fraction <= 0f) return;

        int screenW = ctx.getScaledWindowWidth();
        int x = screenW / 2 - BAR_WIDTH / 2;
        int y = 4;

        // Background
        ctx.fill(x, y, x + BAR_WIDTH, y + BAR_HEIGHT, 0x80000000);

        // Filled portion
        int fillW = (int) (fraction * BAR_WIDTH);
        int color = lerpColor(COLOR_DIM, COLOR_FULL, fraction);

        if (fraction >= 1.0f) {
            double pulse = (Math.sin(System.currentTimeMillis() / 400.0) + 1.0) / 2.0;
            color = lerpColor(COLOR_DIM, COLOR_FULL, (float) pulse);
        }

        ctx.fill(x, y, x + fillW, y + BAR_HEIGHT, 0xFF000000 | color);
    }

    private static int lerpColor(int a, int b, float t) {
        int ar = (a >> 16) & 0xFF;
        int ag = (a >> 8)  & 0xFF;
        int ab =  a        & 0xFF;
        int br = (b >> 16) & 0xFF;
        int bg = (b >> 8)  & 0xFF;
        int bb =  b        & 0xFF;
        int r = (int)(ar + (br - ar) * t);
        int g = (int)(ag + (bg - ag) * t);
        int bv = (int)(ab + (bb - ab) * t);
        return (r << 16) | (g << 8) | bv;
    }
}
