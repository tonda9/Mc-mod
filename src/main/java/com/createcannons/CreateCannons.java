/**
 * Create Cannons - A Create addon for Minecraft 1.21.1 NeoForge
 * 
 * This mod adds large mechanical cannons powered by Create's kinetic stress system.
 * Inspired by Create Big Cannons, it provides multiblock cannon structures that
 * can fire custom projectiles with physics and explosion effects.
 * 
 * Main entry point for the Create Cannons mod.
 */
package com.createcannons;

import com.createcannons.registry.CCBlocks;
import com.createcannons.registry.CCBlockEntityTypes;
import com.createcannons.registry.CCEntityTypes;
import com.createcannons.registry.CCItems;
import com.createcannons.registry.CCCreativeModeTabs;
import com.createcannons.registry.CCRecipeTypes;
import com.createcannons.data.CCDataGenerators;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

/**
 * Main mod class for Create Cannons.
 * 
 * This class handles mod initialization and sets up all registries.
 * It uses NeoForge's event bus system to register components at the
 * appropriate lifecycle stages.
 */
@Mod(CreateCannons.MOD_ID)
public class CreateCannons {
    
    /**
     * The unique mod identifier used throughout the mod for registration.
     */
    public static final String MOD_ID = "createcannons";
    
    /**
     * Logger instance for mod-wide logging.
     */
    public static final Logger LOGGER = LogUtils.getLogger();
    
    /**
     * The main mod event bus for registering events.
     */
    private static IEventBus modEventBus;
    
    /**
     * Constructor called by NeoForge during mod loading.
     * Initializes all registries and sets up event handlers.
     * 
     * @param modEventBus The mod's event bus for registration
     * @param modContainer The mod's container with metadata
     */
    public CreateCannons(IEventBus modEventBus, ModContainer modContainer) {
        CreateCannons.modEventBus = modEventBus;
        
        LOGGER.info("Create Cannons initializing...");
        
        // Register all deferred registries to the mod event bus
        // These will fire during the appropriate registry events
        CCBlocks.register(modEventBus);
        CCItems.register(modEventBus);
        CCBlockEntityTypes.register(modEventBus);
        CCEntityTypes.register(modEventBus);
        CCCreativeModeTabs.register(modEventBus);
        CCRecipeTypes.register(modEventBus);
        
        // Register lifecycle event handlers
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
        
        // Register game event handlers
        NeoForge.EVENT_BUS.addListener(this::onServerStarting);
        
        // Register data generators
        modEventBus.addListener(CCDataGenerators::gatherData);
        
        LOGGER.info("Create Cannons initialization complete!");
    }
    
    /**
     * Gets the mod event bus.
     * 
     * @return The mod's event bus
     */
    public static IEventBus getModEventBus() {
        return modEventBus;
    }
    
    /**
     * Common setup event handler.
     * Called during the common setup phase for both client and server.
     * Used for registering capabilities, network packets, etc.
     * 
     * @param event The common setup event
     */
    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Create Cannons common setup...");
        
        // Enqueue work to run on the main thread
        event.enqueueWork(() -> {
            // Register any cross-mod compatibility here
            LOGGER.info("Create Cannons common setup complete!");
        });
    }
    
    /**
     * Client setup event handler.
     * Called only on the client side for client-specific initialization.
     * Used for registering renderers, key bindings, etc.
     * 
     * @param event The client setup event
     */
    private void clientSetup(final FMLClientSetupEvent event) {
        LOGGER.info("Create Cannons client setup...");
        
        event.enqueueWork(() -> {
            // Register client-only features here
            LOGGER.info("Create Cannons client setup complete!");
        });
    }
    
    /**
     * Server starting event handler.
     * Called when a server (including integrated server) is starting.
     * 
     * @param event The server starting event
     */
    private void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Create Cannons: Server starting!");
    }
}
