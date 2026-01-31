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
 * 2. Caliber-Specific Cannons
 * 3. Caliber Barrels
 * 4. Nova Cannon Multiblock Components
 * 5. Create Integration Blocks
 * 6. Armor Blocks
 * 7. Ore Blocks
 * 8. Projectiles by Tier
 * 9. Materials
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
                                
                                // ========== CALIBER-SPECIFIC CANNONS ==========
                                output.accept(CCItems.AUTOCANNON_30MM.get());
                                output.accept(CCItems.FIELD_GUN_75MM.get());
                                output.accept(CCItems.TANK_CANNON_120MM.get());
                                output.accept(CCItems.HOWITZER_155MM.get());
                                output.accept(CCItems.SIEGE_GUN_280MM.get());
                                output.accept(CCItems.RAILWAY_GUN_400MM.get());
                                output.accept(CCItems.GUSTAV_CANNON_800MM.get());
                                output.accept(CCItems.SIEGE_MORTAR_1000MM.get());
                                
                                // ========== CALIBER BARRELS ==========
                                output.accept(CCItems.BARREL_30MM.get());
                                output.accept(CCItems.BARREL_75MM.get());
                                output.accept(CCItems.BARREL_120MM.get());
                                output.accept(CCItems.BARREL_155MM.get());
                                output.accept(CCItems.BARREL_280MM.get());
                                output.accept(CCItems.BARREL_400MM.get());
                                output.accept(CCItems.BARREL_800MM.get());
                                output.accept(CCItems.BARREL_1000MM.get());
                                
                                // ========== NOVA CANNON MULTIBLOCK ==========
                                output.accept(CCItems.NOVA_CANNON_CORE.get());
                                output.accept(CCItems.NOVA_CANNON_FRAME.get());
                                output.accept(CCItems.HEAVY_BARREL_SEGMENT.get());
                                
                                // ========== CREATE INTEGRATION BLOCKS ==========
                                output.accept(CCItems.CANNON_TURRET_BEARING.get());
                                output.accept(CCItems.AMMO_FEED_MECHANISM.get());
                                output.accept(CCItems.AUTOLOADER.get());
                                output.accept(CCItems.TARGETING_COMPUTER.get());
                                output.accept(CCItems.GUN_STABILIZER.get());
                                
                                // ========== ARMOR BLOCKS ==========
                                output.accept(CCItems.LIGHT_ARMOR_PLATE.get());
                                output.accept(CCItems.HEAVY_ARMOR_PLATE.get());
                                output.accept(CCItems.REINFORCED_ARMOR_BLOCK.get());
                                output.accept(CCItems.SPACED_ARMOR_BLOCK.get());
                                output.accept(CCItems.REACTIVE_ARMOR_BLOCK.get());
                                output.accept(CCItems.SLOPED_ARMOR_BLOCK.get());
                                output.accept(CCItems.ARMOR_GRATING.get());
                                output.accept(CCItems.BLAST_DOOR.get());
                                
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
                                
                                // ========== TUNGSTEN ORE BLOCKS ==========
                                output.accept(CCItems.TUNGSTEN_ORE.get());
                                output.accept(CCItems.DEEPSLATE_TUNGSTEN_ORE.get());
                                output.accept(CCItems.TUNGSTEN_BLOCK.get());
                                output.accept(CCItems.RAW_TUNGSTEN_BLOCK.get());
                                
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
                                
                                // ========== CALIBER-SPECIFIC AMMUNITION ==========
                                // 30mm
                                output.accept(CCItems.SHELL_30MM_AP.get());
                                output.accept(CCItems.SHELL_30MM_HE.get());
                                output.accept(CCItems.SHELL_30MM_TRACER.get());
                                // 75mm
                                output.accept(CCItems.SHELL_75MM_AP.get());
                                output.accept(CCItems.SHELL_75MM_HE.get());
                                output.accept(CCItems.SHELL_75MM_SMOKE.get());
                                // 120mm
                                output.accept(CCItems.SHELL_120MM_AP.get());
                                output.accept(CCItems.SHELL_120MM_HE.get());
                                output.accept(CCItems.SHELL_120MM_HEAT.get());
                                output.accept(CCItems.SHELL_120MM_SABOT.get());
                                // 155mm
                                output.accept(CCItems.SHELL_155MM_HE.get());
                                output.accept(CCItems.SHELL_155MM_INCENDIARY.get());
                                output.accept(CCItems.SHELL_155MM_CLUSTER.get());
                                // 280mm
                                output.accept(CCItems.SHELL_280MM_HE.get());
                                output.accept(CCItems.SHELL_280MM_BUNKER_BUSTER.get());
                                // 400mm
                                output.accept(CCItems.SHELL_400MM_SIEGE.get());
                                output.accept(CCItems.SHELL_400MM_DEVASTATOR.get());
                                // 800mm
                                output.accept(CCItems.SHELL_800MM_GUSTAV.get());
                                output.accept(CCItems.SHELL_800MM_ANNIHILATOR.get());
                                // 1000mm
                                output.accept(CCItems.SHELL_1000MM_CATACLYSM.get());
                                output.accept(CCItems.SHELL_1000MM_WORLDBREAKER.get());
                                
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
                                
                                // ========== TUNGSTEN MATERIALS ==========
                                output.accept(CCItems.RAW_TUNGSTEN.get());
                                output.accept(CCItems.TUNGSTEN_INGOT.get());
                                output.accept(CCItems.TUNGSTEN_NUGGET.get());
                                output.accept(CCItems.HARDENED_STEEL_INGOT.get());
                                output.accept(CCItems.COMPOSITE_PLATING.get());
                                
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
