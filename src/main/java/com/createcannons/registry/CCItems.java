/**
 * Registry for all items in Create Cannons.
 * 
 * This class handles registration of standalone items as well as
 * BlockItems for all registered blocks. Items include projectiles,
 * components, and ammunition.
 * 
 * Item Progression Tiers:
 * - Tier 1 (Early): Iron cannonball, grapeshot, smoke shell
 * - Tier 2 (Mid): Steel cannonball, explosive shell, incendiary, rocket
 * - Tier 3 (Late): Armor piercing, cluster bomb, high-yield, fragmentation, rocket-assisted
 * - Tier 4 (End): Nova shell (requires Nova Cannon multiblock)
 */
package com.createcannons.registry;

import com.createcannons.CreateCannons;
import com.createcannons.entity.ProjectileType;
import com.createcannons.item.AdvancedCannonballItem;
import com.createcannons.item.CannonballItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
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
    
    // ==================== NEW ADVANCED AMMUNITION (TIER 3) ====================
    
    /**
     * High-Yield Shell - Warhammer-style devastating siege ammunition.
     * 
     * Creates massive AoE damage with lingering effects.
     * Renamed from "Nuclear Shell" to follow thematic naming.
     * 
     * Stats:
     * - Damage: 120
     * - Explosion radius: 18.0
     * - Stack size: 1
     * - Stress multiplier: 3.0x
     * - Special: Lingering damage effect, terrain deformation
     * - Tier: Late game (requires enriched cannonite)
     */
    public static final DeferredItem<AdvancedCannonballItem> HIGH_YIELD_SHELL = ITEMS.register(
            "high_yield_shell",
            () -> new AdvancedCannonballItem(
                    new Item.Properties().stacksTo(1).rarity(Rarity.EPIC),
                    120.0f, 18.0f, ProjectileType.HIGH_YIELD
            )
    );
    
    /**
     * Rocket-Assisted Shell - Self-propelled artillery round.
     * 
     * Accelerates mid-flight for extended range and accuracy.
     * Uses sustained thrust after initial launch.
     * 
     * Stats:
     * - Damage: 45
     * - Explosion radius: 5.0
     * - Stack size: 4
     * - Stress multiplier: 2.5x
     * - Special: Mid-flight acceleration, extended range, reduced gravity
     * - Tier: Late game (requires siegite)
     */
    public static final DeferredItem<AdvancedCannonballItem> ROCKET_ASSISTED_SHELL = ITEMS.register(
            "rocket_assisted_shell",
            () -> new AdvancedCannonballItem(
                    new Item.Properties().stacksTo(4).rarity(Rarity.RARE),
                    45.0f, 5.0f, ProjectileType.ROCKET_ASSISTED
            )
    );
    
    /**
     * Fragmentation Shell - Anti-personnel shrapnel round.
     * 
     * Splits into multiple fragments on impact, each causing damage.
     * Effective against groups of enemies or area denial.
     * 
     * Stats:
     * - Damage: 15 (per fragment, 12 fragments)
     * - Explosion radius: 1.0 (per fragment)
     * - Stack size: 4
     * - Stress multiplier: 2.2x
     * - Special: Splits into 12 fragments, shrapnel spread
     * - Tier: Late game (requires siegite)
     */
    public static final DeferredItem<AdvancedCannonballItem> FRAGMENTATION_SHELL = ITEMS.register(
            "fragmentation_shell",
            () -> new AdvancedCannonballItem(
                    new Item.Properties().stacksTo(4).rarity(Rarity.RARE),
                    15.0f, 1.0f, ProjectileType.FRAGMENTATION
            )
    );
    
    // ==================== NOVA CANNON AMMUNITION (TIER 4 - END GAME) ====================
    
    /**
     * Nova Shell - Ultimate siege ammunition for Nova Cannon.
     * 
     * Massive, slow-moving shell with devastating destruction.
     * Only usable in the Nova Cannon multiblock structure.
     * 
     * Stats:
     * - Damage: 200
     * - Explosion radius: 25.0 (with 2.5x multiplier = 62.5 effective)
     * - Stack size: 1
     * - Stress multiplier: 5.0x
     * - Special: Requires Nova Cannon, extreme AoE, visual effects
     * - Tier: End game (requires complete Nova Cannon multiblock)
     */
    public static final DeferredItem<AdvancedCannonballItem> NOVA_SHELL = ITEMS.register(
            "nova_shell",
            () -> new AdvancedCannonballItem(
                    new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant(),
                    200.0f, 25.0f, ProjectileType.NOVA
            )
    );
    
    // ==================== SIEGITE ORE MATERIALS ====================
    
    /**
     * Raw Siegite - unprocessed late-game ore material.
     * 
     * Dropped when mining siegite ore at deep levels.
     * Must be smelted or processed to produce siegite ingots.
     * Key material for Nova Cannon and advanced ammunition.
     */
    public static final DeferredItem<Item> RAW_SIEGITE = ITEMS.register(
            "raw_siegite",
            () -> new Item(new Item.Properties())
    );
    
    /**
     * Siegite Ingot - processed siegite metal.
     * 
     * The primary crafting material for Nova Cannon components
     * and late-game ammunition. Harder to obtain than cannonite.
     */
    public static final DeferredItem<Item> SIEGITE_INGOT = ITEMS.register(
            "siegite_ingot",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON))
    );
    
    /**
     * Siegite Nugget - small piece of siegite.
     * 
     * 9 nuggets can be crafted into an ingot.
     * Useful for precise crafting recipes.
     */
    public static final DeferredItem<Item> SIEGITE_NUGGET = ITEMS.register(
            "siegite_nugget",
            () -> new Item(new Item.Properties())
    );
    
    /**
     * Reinforced Siegite Plating - advanced crafting component.
     * 
     * Used in Nova Cannon construction and heavy barrel segments.
     * Requires siegite sheets pressed with brass.
     */
    public static final DeferredItem<Item> REINFORCED_SIEGITE_PLATING = ITEMS.register(
            "reinforced_siegite_plating",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON))
    );
    
    /**
     * Nova Core Component - key part of Nova Cannon.
     * 
     * The central power regulation component for the Nova Cannon.
     * Requires enriched cannonite and siegite to craft.
     */
    public static final DeferredItem<Item> NOVA_CORE_COMPONENT = ITEMS.register(
            "nova_core_component",
            () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant())
    );
    
    // ==================== BLOCK ITEMS FOR SIEGITE ====================
    
    /**
     * Block item for Siegite Ore.
     */
    public static final DeferredItem<BlockItem> SIEGITE_ORE = ITEMS.registerSimpleBlockItem(
            "siegite_ore",
            CCBlocks.SIEGITE_ORE
    );
    
    /**
     * Block item for Deepslate Siegite Ore.
     */
    public static final DeferredItem<BlockItem> DEEPSLATE_SIEGITE_ORE = ITEMS.registerSimpleBlockItem(
            "deepslate_siegite_ore",
            CCBlocks.DEEPSLATE_SIEGITE_ORE
    );
    
    /**
     * Block item for Siegite Block.
     */
    public static final DeferredItem<BlockItem> SIEGITE_BLOCK = ITEMS.registerSimpleBlockItem(
            "siegite_block",
            CCBlocks.SIEGITE_BLOCK
    );
    
    /**
     * Block item for Raw Siegite Block.
     */
    public static final DeferredItem<BlockItem> RAW_SIEGITE_BLOCK = ITEMS.registerSimpleBlockItem(
            "raw_siegite_block",
            CCBlocks.RAW_SIEGITE_BLOCK
    );
    
    // ==================== BLOCK ITEMS FOR NOVA CANNON ====================
    
    /**
     * Block item for Nova Cannon Core.
     */
    public static final DeferredItem<BlockItem> NOVA_CANNON_CORE = ITEMS.registerSimpleBlockItem(
            "nova_cannon_core",
            CCBlocks.NOVA_CANNON_CORE
    );
    
    /**
     * Block item for Nova Cannon Frame.
     */
    public static final DeferredItem<BlockItem> NOVA_CANNON_FRAME = ITEMS.registerSimpleBlockItem(
            "nova_cannon_frame",
            CCBlocks.NOVA_CANNON_FRAME
    );
    
    /**
     * Block item for Heavy Barrel Segment.
     */
    public static final DeferredItem<BlockItem> HEAVY_BARREL_SEGMENT = ITEMS.registerSimpleBlockItem(
            "heavy_barrel_segment",
            CCBlocks.HEAVY_BARREL_SEGMENT
    );
    
    // ==================== ARMOR BLOCK ITEMS ====================
    
    public static final DeferredItem<BlockItem> LIGHT_ARMOR_PLATE = ITEMS.registerSimpleBlockItem(
            "light_armor_plate", CCBlocks.LIGHT_ARMOR_PLATE);
    
    public static final DeferredItem<BlockItem> HEAVY_ARMOR_PLATE = ITEMS.registerSimpleBlockItem(
            "heavy_armor_plate", CCBlocks.HEAVY_ARMOR_PLATE);
    
    public static final DeferredItem<BlockItem> REINFORCED_ARMOR_BLOCK = ITEMS.registerSimpleBlockItem(
            "reinforced_armor_block", CCBlocks.REINFORCED_ARMOR_BLOCK);
    
    public static final DeferredItem<BlockItem> SPACED_ARMOR_BLOCK = ITEMS.registerSimpleBlockItem(
            "spaced_armor_block", CCBlocks.SPACED_ARMOR_BLOCK);
    
    public static final DeferredItem<BlockItem> REACTIVE_ARMOR_BLOCK = ITEMS.registerSimpleBlockItem(
            "reactive_armor_block", CCBlocks.REACTIVE_ARMOR_BLOCK);
    
    public static final DeferredItem<BlockItem> SLOPED_ARMOR_BLOCK = ITEMS.registerSimpleBlockItem(
            "sloped_armor_block", CCBlocks.SLOPED_ARMOR_BLOCK);
    
    public static final DeferredItem<BlockItem> ARMOR_GRATING = ITEMS.registerSimpleBlockItem(
            "armor_grating", CCBlocks.ARMOR_GRATING);
    
    public static final DeferredItem<BlockItem> BLAST_DOOR = ITEMS.registerSimpleBlockItem(
            "blast_door", CCBlocks.BLAST_DOOR);
    
    // ==================== TUNGSTEN ORE ITEMS ====================
    
    public static final DeferredItem<BlockItem> TUNGSTEN_ORE = ITEMS.registerSimpleBlockItem(
            "tungsten_ore", CCBlocks.TUNGSTEN_ORE);
    
    public static final DeferredItem<BlockItem> DEEPSLATE_TUNGSTEN_ORE = ITEMS.registerSimpleBlockItem(
            "deepslate_tungsten_ore", CCBlocks.DEEPSLATE_TUNGSTEN_ORE);
    
    public static final DeferredItem<BlockItem> TUNGSTEN_BLOCK = ITEMS.registerSimpleBlockItem(
            "tungsten_block", CCBlocks.TUNGSTEN_BLOCK);
    
    public static final DeferredItem<BlockItem> RAW_TUNGSTEN_BLOCK = ITEMS.registerSimpleBlockItem(
            "raw_tungsten_block", CCBlocks.RAW_TUNGSTEN_BLOCK);
    
    /**
     * Raw Tungsten - unprocessed dense ore material.
     * Essential for armor-piercing rounds and heavy armor.
     */
    public static final DeferredItem<Item> RAW_TUNGSTEN = ITEMS.register(
            "raw_tungsten",
            () -> new Item(new Item.Properties())
    );
    
    /**
     * Tungsten Ingot - processed heavy metal.
     * Key material for AP rounds and reinforced armor.
     */
    public static final DeferredItem<Item> TUNGSTEN_INGOT = ITEMS.register(
            "tungsten_ingot",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON))
    );
    
    /**
     * Tungsten Nugget - small piece of tungsten.
     */
    public static final DeferredItem<Item> TUNGSTEN_NUGGET = ITEMS.register(
            "tungsten_nugget",
            () -> new Item(new Item.Properties())
    );
    
    /**
     * Hardened Steel Ingot - alloy for armor crafting.
     * Made by combining iron, carbon, and tungsten.
     */
    public static final DeferredItem<Item> HARDENED_STEEL_INGOT = ITEMS.register(
            "hardened_steel_ingot",
            () -> new Item(new Item.Properties())
    );
    
    /**
     * Composite Plating - advanced armor component.
     * Layered material combining multiple metals.
     */
    public static final DeferredItem<Item> COMPOSITE_PLATING = ITEMS.register(
            "composite_plating",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON))
    );
    
    // ==================== CALIBER CANNON BLOCK ITEMS ====================
    
    public static final DeferredItem<BlockItem> AUTOCANNON_30MM = ITEMS.registerSimpleBlockItem(
            "autocannon_30mm", CCBlocks.AUTOCANNON_30MM);
    
    public static final DeferredItem<BlockItem> FIELD_GUN_75MM = ITEMS.registerSimpleBlockItem(
            "field_gun_75mm", CCBlocks.FIELD_GUN_75MM);
    
    public static final DeferredItem<BlockItem> TANK_CANNON_120MM = ITEMS.registerSimpleBlockItem(
            "tank_cannon_120mm", CCBlocks.TANK_CANNON_120MM);
    
    public static final DeferredItem<BlockItem> HOWITZER_155MM = ITEMS.registerSimpleBlockItem(
            "howitzer_155mm", CCBlocks.HOWITZER_155MM);
    
    public static final DeferredItem<BlockItem> SIEGE_GUN_280MM = ITEMS.registerSimpleBlockItem(
            "siege_gun_280mm", CCBlocks.SIEGE_GUN_280MM);
    
    public static final DeferredItem<BlockItem> RAILWAY_GUN_400MM = ITEMS.registerSimpleBlockItem(
            "railway_gun_400mm", CCBlocks.RAILWAY_GUN_400MM);
    
    public static final DeferredItem<BlockItem> GUSTAV_CANNON_800MM = ITEMS.registerSimpleBlockItem(
            "gustav_cannon_800mm", CCBlocks.GUSTAV_CANNON_800MM);
    
    public static final DeferredItem<BlockItem> SIEGE_MORTAR_1000MM = ITEMS.registerSimpleBlockItem(
            "siege_mortar_1000mm", CCBlocks.SIEGE_MORTAR_1000MM);
    
    // ==================== CALIBER BARREL BLOCK ITEMS ====================
    
    public static final DeferredItem<BlockItem> BARREL_30MM = ITEMS.registerSimpleBlockItem(
            "barrel_30mm", CCBlocks.BARREL_30MM);
    
    public static final DeferredItem<BlockItem> BARREL_75MM = ITEMS.registerSimpleBlockItem(
            "barrel_75mm", CCBlocks.BARREL_75MM);
    
    public static final DeferredItem<BlockItem> BARREL_120MM = ITEMS.registerSimpleBlockItem(
            "barrel_120mm", CCBlocks.BARREL_120MM);
    
    public static final DeferredItem<BlockItem> BARREL_155MM = ITEMS.registerSimpleBlockItem(
            "barrel_155mm", CCBlocks.BARREL_155MM);
    
    public static final DeferredItem<BlockItem> BARREL_280MM = ITEMS.registerSimpleBlockItem(
            "barrel_280mm", CCBlocks.BARREL_280MM);
    
    public static final DeferredItem<BlockItem> BARREL_400MM = ITEMS.registerSimpleBlockItem(
            "barrel_400mm", CCBlocks.BARREL_400MM);
    
    public static final DeferredItem<BlockItem> BARREL_800MM = ITEMS.registerSimpleBlockItem(
            "barrel_800mm", CCBlocks.BARREL_800MM);
    
    public static final DeferredItem<BlockItem> BARREL_1000MM = ITEMS.registerSimpleBlockItem(
            "barrel_1000mm", CCBlocks.BARREL_1000MM);
    
    // ==================== CREATE INTEGRATION BLOCK ITEMS ====================
    
    public static final DeferredItem<BlockItem> CANNON_TURRET_BEARING = ITEMS.registerSimpleBlockItem(
            "cannon_turret_bearing", CCBlocks.CANNON_TURRET_BEARING);
    
    public static final DeferredItem<BlockItem> AMMO_FEED_MECHANISM = ITEMS.registerSimpleBlockItem(
            "ammo_feed_mechanism", CCBlocks.AMMO_FEED_MECHANISM);
    
    public static final DeferredItem<BlockItem> AUTOLOADER = ITEMS.registerSimpleBlockItem(
            "autoloader", CCBlocks.AUTOLOADER);
    
    public static final DeferredItem<BlockItem> TARGETING_COMPUTER = ITEMS.registerSimpleBlockItem(
            "targeting_computer", CCBlocks.TARGETING_COMPUTER);
    
    public static final DeferredItem<BlockItem> GUN_STABILIZER = ITEMS.registerSimpleBlockItem(
            "gun_stabilizer", CCBlocks.GUN_STABILIZER);
    
    // ==================== CALIBER-SPECIFIC AMMUNITION ====================
    
    // 30mm Ammunition
    public static final DeferredItem<Item> SHELL_30MM_AP = ITEMS.register("shell_30mm_ap",
            () -> new Item(new Item.Properties().stacksTo(64)));
    
    public static final DeferredItem<Item> SHELL_30MM_HE = ITEMS.register("shell_30mm_he",
            () -> new Item(new Item.Properties().stacksTo(64)));
    
    public static final DeferredItem<Item> SHELL_30MM_TRACER = ITEMS.register("shell_30mm_tracer",
            () -> new Item(new Item.Properties().stacksTo(64)));
    
    // 75mm Ammunition
    public static final DeferredItem<Item> SHELL_75MM_AP = ITEMS.register("shell_75mm_ap",
            () -> new Item(new Item.Properties().stacksTo(32)));
    
    public static final DeferredItem<Item> SHELL_75MM_HE = ITEMS.register("shell_75mm_he",
            () -> new Item(new Item.Properties().stacksTo(32)));
    
    public static final DeferredItem<Item> SHELL_75MM_SMOKE = ITEMS.register("shell_75mm_smoke",
            () -> new Item(new Item.Properties().stacksTo(32)));
    
    // 120mm Ammunition
    public static final DeferredItem<Item> SHELL_120MM_AP = ITEMS.register("shell_120mm_ap",
            () -> new Item(new Item.Properties().stacksTo(16)));
    
    public static final DeferredItem<Item> SHELL_120MM_HE = ITEMS.register("shell_120mm_he",
            () -> new Item(new Item.Properties().stacksTo(16)));
    
    public static final DeferredItem<Item> SHELL_120MM_HEAT = ITEMS.register("shell_120mm_heat",
            () -> new Item(new Item.Properties().stacksTo(16)));
    
    public static final DeferredItem<Item> SHELL_120MM_SABOT = ITEMS.register("shell_120mm_sabot",
            () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    
    // 155mm Ammunition
    public static final DeferredItem<Item> SHELL_155MM_HE = ITEMS.register("shell_155mm_he",
            () -> new Item(new Item.Properties().stacksTo(8)));
    
    public static final DeferredItem<Item> SHELL_155MM_INCENDIARY = ITEMS.register("shell_155mm_incendiary",
            () -> new Item(new Item.Properties().stacksTo(8)));
    
    public static final DeferredItem<Item> SHELL_155MM_CLUSTER = ITEMS.register("shell_155mm_cluster",
            () -> new Item(new Item.Properties().stacksTo(8).rarity(Rarity.UNCOMMON)));
    
    // 280mm Ammunition
    public static final DeferredItem<Item> SHELL_280MM_HE = ITEMS.register("shell_280mm_he",
            () -> new Item(new Item.Properties().stacksTo(4).rarity(Rarity.UNCOMMON)));
    
    public static final DeferredItem<Item> SHELL_280MM_BUNKER_BUSTER = ITEMS.register("shell_280mm_bunker_buster",
            () -> new Item(new Item.Properties().stacksTo(4).rarity(Rarity.RARE)));
    
    // 400mm Ammunition
    public static final DeferredItem<Item> SHELL_400MM_SIEGE = ITEMS.register("shell_400mm_siege",
            () -> new Item(new Item.Properties().stacksTo(2).rarity(Rarity.RARE)));
    
    public static final DeferredItem<Item> SHELL_400MM_DEVASTATOR = ITEMS.register("shell_400mm_devastator",
            () -> new Item(new Item.Properties().stacksTo(2).rarity(Rarity.RARE)));
    
    // 800mm Ammunition
    public static final DeferredItem<Item> SHELL_800MM_GUSTAV = ITEMS.register("shell_800mm_gustav",
            () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    
    public static final DeferredItem<Item> SHELL_800MM_ANNIHILATOR = ITEMS.register("shell_800mm_annihilator",
            () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant()));
    
    // 1000mm Ammunition
    public static final DeferredItem<Item> SHELL_1000MM_CATACLYSM = ITEMS.register("shell_1000mm_cataclysm",
            () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant()));
    
    public static final DeferredItem<Item> SHELL_1000MM_WORLDBREAKER = ITEMS.register("shell_1000mm_worldbreaker",
            () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant()));
    
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
