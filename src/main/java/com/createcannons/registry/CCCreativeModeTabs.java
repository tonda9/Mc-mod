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
 * 
 * Item Organization:
 * 1. Standard Cannon Blocks
 * 2. Nova Cannon Multiblock Components
 * 3. Cannonite Ore Blocks
 * 4. Siegite Ore Blocks
 * 5. Basic Projectiles
 * 6. Advanced Projectiles
 * 7. End-Game Ammunition
 * 8. Cannonite Materials
 * 9. Siegite Materials
 * 10. Crafting Components
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
     * organized in progression order from early to end-game.
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
                                // ========== STANDARD CANNON BLOCKS ==========
                                output.accept(CCItems.KINETIC_CANNON.get());
                                output.accept(CCItems.CANNON_BARREL.get());
                                output.accept(CCItems.CANNON_BREECH.get());
                                
                                // ========== NOVA CANNON MULTIBLOCK ==========
                                output.accept(CCItems.NOVA_CANNON_CORE.get());
                                output.accept(CCItems.NOVA_CANNON_FRAME.get());
                                output.accept(CCItems.HEAVY_BARREL_SEGMENT.get());
                                
                                // ========== CANNONITE ORE BLOCKS ==========
                                output.accept(CCItems.CANNONITE_ORE.get());
                                output.accept(CCItems.DEEPSLATE_CANNONITE_ORE.get());
                                output.accept(CCItems.CANNONITE_BLOCK.get());
                                output.accept(CCItems.RAW_CANNONITE_BLOCK.get());
                                
                                // ========== SIEGITE ORE BLOCKS ==========
                                output.accept(CCItems.SIEGITE_ORE.get());
                                output.accept(CCItems.DEEPSLATE_SIEGITE_ORE.get());
                                output.accept(CCItems.SIEGITE_BLOCK.get());
                                output.accept(CCItems.RAW_SIEGITE_BLOCK.get());
                                
                                // ========== BASIC PROJECTILES (TIER 1-2) ==========
                                output.accept(CCItems.IRON_CANNONBALL.get());
                                output.accept(CCItems.STEEL_CANNONBALL.get());
                                output.accept(CCItems.EXPLOSIVE_SHELL.get());
                                output.accept(CCItems.SMOKE_SHELL.get());
                                output.accept(CCItems.GRAPESHOT.get());
                                
                                // ========== MID-TIER PROJECTILES (TIER 2-3) ==========
                                output.accept(CCItems.NUCLEAR_SHELL.get());
                                output.accept(CCItems.ROCKET_AMMO.get());
                                output.accept(CCItems.INCENDIARY_SHELL.get());
                                output.accept(CCItems.ARMOR_PIERCING_ROUND.get());
                                output.accept(CCItems.CLUSTER_BOMB.get());
                                
                                // ========== ADVANCED PROJECTILES (TIER 3) ==========
                                output.accept(CCItems.HIGH_YIELD_SHELL.get());
                                output.accept(CCItems.ROCKET_ASSISTED_SHELL.get());
                                output.accept(CCItems.FRAGMENTATION_SHELL.get());
                                
                                // ========== END-GAME AMMUNITION (TIER 4) ==========
                                output.accept(CCItems.NOVA_SHELL.get());
                                
                                // ========== CANNONITE MATERIALS ==========
                                output.accept(CCItems.RAW_CANNONITE.get());
                                output.accept(CCItems.CANNONITE_INGOT.get());
                                output.accept(CCItems.CANNONITE_NUGGET.get());
                                output.accept(CCItems.ENRICHED_CANNONITE.get());
                                
                                // ========== SIEGITE MATERIALS ==========
                                output.accept(CCItems.RAW_SIEGITE.get());
                                output.accept(CCItems.SIEGITE_INGOT.get());
                                output.accept(CCItems.SIEGITE_NUGGET.get());
                                output.accept(CCItems.REINFORCED_SIEGITE_PLATING.get());
                                output.accept(CCItems.NOVA_CORE_COMPONENT.get());
                                
                                // ========== CRAFTING COMPONENTS ==========
                                output.accept(CCItems.CANNON_CASTING.get());
                                output.accept(CCItems.INCOMPLETE_CANNON_BARREL.get());
                                output.accept(CCItems.GUNPOWDER_CHARGE.get());
                                output.accept(CCItems.ROCKET_FUEL.get());
                                output.accept(CCItems.FUSE.get());
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
