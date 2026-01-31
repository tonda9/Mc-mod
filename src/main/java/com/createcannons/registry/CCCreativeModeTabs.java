/**
 * Registry for creative mode tabs in Create Cannons.
 * 
 * Creative tabs organize items in the creative inventory for easy access.
 * This mod creates a dedicated tab for all cannon-related items.
 */
package com.createcannons.registry;

import com.createcannons.CreateCannons;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Handles registration of creative mode tabs in Create Cannons.
 * 
 * Provides a dedicated creative tab that contains all mod items,
 * organized logically for easy discovery and access.
 */
public class CCCreativeModeTabs {
    
    /**
     * Deferred register for creative mode tabs.
     */
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = 
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateCannons.MOD_ID);
    
    /**
     * The main Create Cannons creative tab.
     * 
     * This tab contains all blocks, items, and components from the mod,
     * organized in a logical order:
     * 1. Cannon blocks (cannon, barrel, breech)
     * 2. Projectiles (cannonballs, shells)
     * 3. Components and crafting materials
     */
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB = 
            CREATIVE_MODE_TABS.register(
                    "main",
                    () -> CreativeModeTab.builder()
                            // Tab icon shown in the creative inventory
                            .icon(() -> new ItemStack(CCItems.KINETIC_CANNON.get()))
                            // Tab title displayed when hovering
                            .title(Component.translatable("itemGroup." + CreateCannons.MOD_ID + ".main"))
                            // Items to display in this tab
                            .displayItems((parameters, output) -> {
                                // === Cannon Blocks ===
                                output.accept(CCItems.KINETIC_CANNON.get());
                                output.accept(CCItems.CANNON_BARREL.get());
                                output.accept(CCItems.CANNON_BREECH.get());
                                
                                // === Projectiles ===
                                output.accept(CCItems.IRON_CANNONBALL.get());
                                output.accept(CCItems.STEEL_CANNONBALL.get());
                                output.accept(CCItems.EXPLOSIVE_SHELL.get());
                                
                                // === Components ===
                                output.accept(CCItems.CANNON_CASTING.get());
                                output.accept(CCItems.INCOMPLETE_CANNON_BARREL.get());
                                output.accept(CCItems.GUNPOWDER_CHARGE.get());
                            })
                            .build()
            );
    
    /**
     * Registers all creative mode tabs to the mod event bus.
     * Called from the main mod class during initialization.
     * 
     * @param eventBus The mod's event bus
     */
    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
        CreateCannons.LOGGER.info("Create Cannons creative tabs registered!");
    }
}
