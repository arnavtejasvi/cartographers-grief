package com.arnav.cartographersgrief;

import com.arnav.cartographersgrief.event.DeathEventHandler;
import com.arnav.cartographersgrief.event.ObeliskUpgradeHandler;
import com.arnav.cartographersgrief.registry.ModBlockEntityTypes;
import com.arnav.cartographersgrief.registry.ModBlocks;
import com.arnav.cartographersgrief.registry.ModItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CartographersGrief implements ModInitializer {
    public static final String MOD_ID = "cartographersgrief";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModBlocks.register();
        ModItems.register();
        ModBlockEntityTypes.register();
        DeathEventHandler.register();
        ObeliskUpgradeHandler.register();
        LOGGER.info("The Cartographer's Grief has awakened.");
    }
}
