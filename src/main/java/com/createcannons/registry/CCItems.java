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
    
    // ==================== ADVANCED PROJECTILE ITEMS ====================
    
    /**
     * Nuclear Shell - devastating ammunition with massive explosion.
     * 
     * The most powerful ammunition type. Creates a massive explosion
     * with lingering radiation effects. Very expensive to craft.
     * 
     * Stats:
     * - Damage: 100
     * - Explosion radius: 15.0
     * - Stack size: 1
     * - Special: Lingering damage effect
     */
    public static final DeferredItem<CannonballItem> NUCLEAR_SHELL = ITEMS.register(
            "nuclear_shell",
            () -> new CannonballItem(new Item.Properties().stacksTo(1), 100.0f, 15.0f)
    );
    
    /**
     * Rocket Ammo - high velocity self-propelled projectile.
     * 
     * Uses internal propulsion to maintain velocity over long distances.
     * Less affected by gravity, travels faster and further.
     * 
     * Stats:
     * - Damage: 30
     * - Explosion radius: 3.0
     * - Stack size: 8
     * - Special: Reduced gravity effect, flame trail
     */
    public static final DeferredItem<CannonballItem> ROCKET_AMMO = ITEMS.register(
            "rocket_ammo",
            () -> new CannonballItem(new Item.Properties().stacksTo(8), 30.0f, 3.0f)
    );
    
    /**
     * Incendiary Shell - sets targets on fire.
     * 
     * Designed to create fires on impact. Lower direct damage
     * but ignites blocks and entities in the blast area.
     * 
     * Stats:
     * - Damage: 15
     * - Explosion radius: 4.0
     * - Stack size: 8
     * - Special: Sets fire on impact
     */
    public static final DeferredItem<CannonballItem> INCENDIARY_SHELL = ITEMS.register(
            "incendiary_shell",
            () -> new CannonballItem(new Item.Properties().stacksTo(8), 15.0f, 4.0f)
    );
    
    /**
     * Armor Piercing Round - high damage, no explosion.
     * 
     * Designed to penetrate armor and fortifications. Very high
     * direct damage but no area effect. Can penetrate through
     * weaker blocks.
     * 
     * Stats:
     * - Damage: 80
     * - Explosion radius: 0.5
     * - Stack size: 16
     * - Special: Penetrates weak blocks
     */
    public static final DeferredItem<CannonballItem> ARMOR_PIERCING_ROUND = ITEMS.register(
            "armor_piercing_round",
            () -> new CannonballItem(new Item.Properties().stacksTo(16), 80.0f, 0.5f)
    );
    
    /**
     * Cluster Bomb - splits into multiple smaller projectiles.
     * 
     * Releases multiple smaller explosions over a wide area.
     * Effective against groups of enemies or large structures.
     * 
     * Stats:
     * - Damage: 10 (per cluster)
     * - Explosion radius: 1.5 (per cluster)
     * - Stack size: 4
     * - Special: Splits into 8 mini projectiles
     */
    public static final DeferredItem<CannonballItem> CLUSTER_BOMB = ITEMS.register(
            "cluster_bomb",
            () -> new CannonballItem(new Item.Properties().stacksTo(4), 10.0f, 1.5f)
    );
    
    /**
     * Smoke Shell - creates a smoke screen.
     * 
     * Non-damaging projectile that creates a large smoke cloud
     * on impact. Useful for cover and distraction.
     * 
     * Stats:
     * - Damage: 0
     * - Explosion radius: 0
     * - Stack size: 16
     * - Special: Creates smoke particles
     */
    public static final DeferredItem<CannonballItem> SMOKE_SHELL = ITEMS.register(
            "smoke_shell",
            () -> new CannonballItem(new Item.Properties().stacksTo(16), 0.0f, 0.0f)
    );
    
    /**
     * Grapeshot - anti-personnel ammunition.
     * 
     * Fires multiple small projectiles in a spread pattern.
     * Effective against groups but low damage per projectile.
     * 
     * Stats:
     * - Damage: 5 (per pellet)
     * - Explosion radius: 0
     * - Stack size: 16
     * - Special: Fires 12 pellets in spread
     */
    public static final DeferredItem<CannonballItem> GRAPESHOT = ITEMS.register(
            "grapeshot",
            () -> new CannonballItem(new Item.Properties().stacksTo(16), 5.0f, 0.0f)
    );
    
    // ==================== CANNONITE ORE ITEMS ====================
    
    /**
     * Raw Cannonite - unprocessed ore material.
     * 
     * Dropped when mining cannonite ore. Must be smelted
     * or blasted to produce cannonite ingots.
     */
    public static final DeferredItem<Item> RAW_CANNONITE = ITEMS.register(
            "raw_cannonite",
            () -> new Item(new Item.Properties())
    );
    
    /**
     * Cannonite Ingot - processed cannonite metal.
     * 
     * The primary crafting material for advanced ammunition.
     * Obtained by smelting raw cannonite.
     */
    public static final DeferredItem<Item> CANNONITE_INGOT = ITEMS.register(
            "cannonite_ingot",
            () -> new Item(new Item.Properties())
    );
    
    /**
     * Cannonite Nugget - small piece of cannonite.
     * 
     * 9 nuggets can be crafted into an ingot.
     * Useful for precise crafting recipes.
     */
    public static final DeferredItem<Item> CANNONITE_NUGGET = ITEMS.register(
            "cannonite_nugget",
            () -> new Item(new Item.Properties())
    );
    
    /**
     * Enriched Cannonite - processed for nuclear applications.
     * 
     * Highly refined cannonite used in nuclear shells.
     * Requires special processing to create.
     */
    public static final DeferredItem<Item> ENRICHED_CANNONITE = ITEMS.register(
            "enriched_cannonite",
            () -> new Item(new Item.Properties().stacksTo(16))
    );
    
    /**
     * Rocket Fuel - propellant for rocket ammunition.
     * 
     * Crafted from blaze powder and gunpowder.
     * Used in rocket ammo recipes.
     */
    public static final DeferredItem<Item> ROCKET_FUEL = ITEMS.register(
            "rocket_fuel",
            () -> new Item(new Item.Properties().stacksTo(16))
    );
    
    /**
     * Fuse - detonation component.
     * 
     * Required for explosive ammunition types.
     * Crafted from string and gunpowder.
     */
    public static final DeferredItem<Item> FUSE = ITEMS.register(
            "fuse",
            () -> new Item(new Item.Properties().stacksTo(64))
    );
    
    // ==================== BLOCK ITEMS FOR ORES ====================
    
    /**
     * Block item for Cannonite Ore.
     */
    public static final DeferredItem<BlockItem> CANNONITE_ORE = ITEMS.registerSimpleBlockItem(
            "cannonite_ore",
            CCBlocks.CANNONITE_ORE
    );
    
    /**
     * Block item for Deepslate Cannonite Ore.
     */
    public static final DeferredItem<BlockItem> DEEPSLATE_CANNONITE_ORE = ITEMS.registerSimpleBlockItem(
            "deepslate_cannonite_ore",
            CCBlocks.DEEPSLATE_CANNONITE_ORE
    );
    
    /**
     * Block item for Cannonite Block.
     */
    public static final DeferredItem<BlockItem> CANNONITE_BLOCK = ITEMS.registerSimpleBlockItem(
            "cannonite_block",
            CCBlocks.CANNONITE_BLOCK
    );
    
    /**
     * Block item for Raw Cannonite Block.
     */
    public static final DeferredItem<BlockItem> RAW_CANNONITE_BLOCK = ITEMS.registerSimpleBlockItem(
            "raw_cannonite_block",
            CCBlocks.RAW_CANNONITE_BLOCK
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
