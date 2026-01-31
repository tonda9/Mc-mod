/**
 * Registry for all items in Create Cannons.
 * 
 * This class handles registration of standalone items as well as
 * BlockItems for all registered blocks. Items include projectiles,
 * components, and ammunition.
 */
package com.createcannons.registry;

import com.createcannons.CreateCannons;
import com.createcannons.item.CannonballItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Handles registration of all items in Create Cannons.
 * 
 * This includes:
 * - Block items for all registered blocks
 * - Projectile items (cannonballs, shells, etc.)
 * - Crafting components
 * - Ammunition and propellant
 */
public class CCItems {
    
    /**
     * Deferred register for items.
     * All items must be registered through this to ensure proper loading order.
     */
    public static final DeferredRegister.Items ITEMS = 
            DeferredRegister.createItems(CreateCannons.MOD_ID);
    
    // ==================== BLOCK ITEMS ====================
    
    /**
     * Block item for the Kinetic Cannon block.
     * Automatically created from the corresponding block.
     */
    public static final DeferredItem<BlockItem> KINETIC_CANNON = ITEMS.registerSimpleBlockItem(
            "kinetic_cannon", 
            CCBlocks.KINETIC_CANNON
    );
    
    /**
     * Block item for the Cannon Barrel block.
     */
    public static final DeferredItem<BlockItem> CANNON_BARREL = ITEMS.registerSimpleBlockItem(
            "cannon_barrel",
            CCBlocks.CANNON_BARREL
    );
    
    /**
     * Block item for the Cannon Breech block.
     */
    public static final DeferredItem<BlockItem> CANNON_BREECH = ITEMS.registerSimpleBlockItem(
            "cannon_breech",
            CCBlocks.CANNON_BREECH
    );
    
    // ==================== PROJECTILE ITEMS ====================
    
    /**
     * Standard iron cannonball.
     * 
     * The basic ammunition for cannons. Deals moderate damage
     * and has a small explosion radius.
     * 
     * Stats:
     * - Damage: 20
     * - Explosion radius: 2.0
     * - Stack size: 16
     */
    public static final DeferredItem<CannonballItem> IRON_CANNONBALL = ITEMS.register(
            "iron_cannonball",
            () -> new CannonballItem(new Item.Properties().stacksTo(16), 20.0f, 2.0f)
    );
    
    /**
     * Heavy steel cannonball.
     * 
     * An upgraded projectile that deals more damage and has
     * a larger explosion radius, but requires more stress to fire.
     * 
     * Stats:
     * - Damage: 35
     * - Explosion radius: 3.5
     * - Stack size: 8
     */
    public static final DeferredItem<CannonballItem> STEEL_CANNONBALL = ITEMS.register(
            "steel_cannonball",
            () -> new CannonballItem(new Item.Properties().stacksTo(8), 35.0f, 3.5f)
    );
    
    /**
     * Explosive shell with larger blast radius.
     * 
     * A specialized projectile designed for area damage.
     * Lower direct damage but much larger explosion.
     * 
     * Stats:
     * - Damage: 15
     * - Explosion radius: 5.0
     * - Stack size: 4
     */
    public static final DeferredItem<CannonballItem> EXPLOSIVE_SHELL = ITEMS.register(
            "explosive_shell",
            () -> new CannonballItem(new Item.Properties().stacksTo(4), 15.0f, 5.0f)
    );
    
    // ==================== CRAFTING COMPONENTS ====================
    
    /**
     * Cannon Casting form - used in sequenced assembly recipes.
     * 
     * This component is used as an intermediate step when
     * crafting cannon blocks through Create's sequenced assembly.
     */
    public static final DeferredItem<Item> CANNON_CASTING = ITEMS.register(
            "cannon_casting",
            () -> new Item(new Item.Properties())
    );
    
    /**
     * Incomplete cannon barrel - sequenced assembly intermediate.
     * 
     * Represents a partially completed cannon barrel during
     * the manufacturing process.
     */
    public static final DeferredItem<Item> INCOMPLETE_CANNON_BARREL = ITEMS.register(
            "incomplete_cannon_barrel",
            () -> new Item(new Item.Properties())
    );
    
    /**
     * Gunpowder charge - propellant for cannons.
     * 
     * Required to fire projectiles. Larger charges result
     * in higher projectile velocity and range.
     */
    public static final DeferredItem<Item> GUNPOWDER_CHARGE = ITEMS.register(
            "gunpowder_charge",
            () -> new Item(new Item.Properties().stacksTo(16))
    );
    
    // ==================== HELPER METHODS ====================
    
    /**
     * Registers all items to the mod event bus.
     * Called from the main mod class during initialization.
     * 
     * @param eventBus The mod's event bus
     */
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
        CreateCannons.LOGGER.info("Create Cannons items registered!");
    }
}
