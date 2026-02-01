/**
 * Registry for all blocks in Create Cannons.
 * 
 * This class uses NeoForge's DeferredRegister system to safely register
 * blocks during the appropriate mod loading phase. All blocks are lazily
 * initialized and registered when the registry event fires.
 */
package com.createcannons.registry;

import com.createcannons.CreateCannons;
import com.createcannons.block.KineticCannonBlock;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Handles registration of all blocks in Create Cannons.
 * 
 * Each block is registered as a DeferredBlock which provides type-safe
 * access to the block after registration. The blocks here integrate
 * with Create's kinetic system for stress-based mechanics.
 */
public class CCBlocks {
    
    /**
     * Deferred register for blocks.
     * All blocks must be registered through this to ensure proper loading order.
     */
    public static final DeferredRegister.Blocks BLOCKS = 
            DeferredRegister.createBlocks(CreateCannons.MOD_ID);
    
    // ==================== CANNON BLOCKS ====================
    
    /**
     * The Kinetic Cannon block - the main firing mechanism for cannons.
     * 
     * This block consumes kinetic stress to charge and fire projectiles.
     * It can be part of a multiblock structure when combined with
     * cannon barrel blocks.
     * 
     * Properties:
     * - Requires 256 SU to operate
     * - 40 tick cooldown between shots
     * - Can rotate to aim at targets
     */
    public static final DeferredBlock<KineticCannonBlock> KINETIC_CANNON = BLOCKS.register(
            "kinetic_cannon",
            () -> new KineticCannonBlock(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .strength(6.0f, 8.0f) // Hardness, blast resistance
                            .requiresCorrectToolForDrops()
                            .noOcclusion() // Allow partial transparency for model
            )
    );
    
    /**
     * Cannon Barrel block - extends the cannon's reach.
     * 
     * Multiple barrel blocks can be placed in a line to increase
     * projectile velocity and range. Each additional barrel adds
     * to the stress cost but improves accuracy.
     */
    public static final DeferredBlock<Block> CANNON_BARREL = BLOCKS.register(
            "cannon_barrel",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .strength(5.0f, 6.0f)
                            .requiresCorrectToolForDrops()
            )
    );
    
    /**
     * Cannon Breech block - the loading mechanism.
     * 
     * Projectiles and propellant are loaded through this block.
     * It includes an inventory for ammunition storage.
     */
    public static final DeferredBlock<Block> CANNON_BREECH = BLOCKS.register(
            "cannon_breech",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .strength(5.0f, 6.0f)
                            .requiresCorrectToolForDrops()
            )
    );
    
    // ==================== ORE BLOCKS ====================
    
    /**
     * Cannonite Ore - a new ore found deep underground.
     * 
     * This rare ore is essential for crafting advanced ammunition.
     * Found at Y levels -64 to 16, primarily in deepslate.
     * Drops 1-3 Raw Cannonite when mined with iron pickaxe or better.
     */
    public static final DeferredBlock<Block> CANNONITE_ORE = BLOCKS.register(
            "cannonite_ore",
            () -> new DropExperienceBlock(
                    UniformInt.of(3, 7),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.STONE)
                            .strength(3.0f, 3.0f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.STONE)
            )
    );
    
    /**
     * Deepslate Cannonite Ore - variant found in deepslate.
     * 
     * Slightly harder to mine than regular cannonite ore.
     * More common at very low Y levels.
     */
    public static final DeferredBlock<Block> DEEPSLATE_CANNONITE_ORE = BLOCKS.register(
            "deepslate_cannonite_ore",
            () -> new DropExperienceBlock(
                    UniformInt.of(3, 7),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.DEEPSLATE)
                            .strength(4.5f, 3.0f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.DEEPSLATE)
            )
    );
    
    /**
     * Cannonite Block - storage block for cannonite ingots.
     * 
     * Crafted from 9 cannonite ingots. Can be used as decoration
     * or for compact storage of the material.
     */
    public static final DeferredBlock<Block> CANNONITE_BLOCK = BLOCKS.register(
            "cannonite_block",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_GREEN)
                            .strength(5.0f, 6.0f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.METAL)
            )
    );
    
    /**
     * Raw Cannonite Block - storage block for raw cannonite.
     * 
     * Crafted from 9 raw cannonite. Useful for bulk storage
     * before processing the ore.
     */
    public static final DeferredBlock<Block> RAW_CANNONITE_BLOCK = BLOCKS.register(
            "raw_cannonite_block",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_GREEN)
                            .strength(4.0f, 5.0f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.STONE)
            )
    );
    
    // ==================== SIEGITE ORE BLOCKS (LATE-GAME) ====================
    
    /**
     * Siegite Ore - late-game ore found at the deepest levels.
     * 
     * Essential for Nova Cannon construction and advanced ammunition.
     * Found at Y levels -64 to -16, primarily in deepslate.
     * Requires diamond pickaxe or better to mine.
     * 
     * Balance Note: Rarer than cannonite, gates end-game content
     */
    public static final DeferredBlock<Block> SIEGITE_ORE = BLOCKS.register(
            "siegite_ore",
            () -> new DropExperienceBlock(
                    UniformInt.of(5, 10),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.STONE)
                            .strength(4.0f, 4.0f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.STONE)
            )
    );
    
    /**
     * Deepslate Siegite Ore - variant found in deepslate.
     * 
     * More common than regular siegite ore at very low Y levels.
     * Harder to mine than the stone variant.
     */
    public static final DeferredBlock<Block> DEEPSLATE_SIEGITE_ORE = BLOCKS.register(
            "deepslate_siegite_ore",
            () -> new DropExperienceBlock(
                    UniformInt.of(5, 10),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.DEEPSLATE)
                            .strength(5.5f, 4.0f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.DEEPSLATE)
            )
    );
    
    /**
     * Siegite Block - storage block for siegite ingots.
     * 
     * Crafted from 9 siegite ingots. Can be used as decoration
     * or compact storage.
     */
    public static final DeferredBlock<Block> SIEGITE_BLOCK = BLOCKS.register(
            "siegite_block",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_PURPLE)
                            .strength(6.0f, 8.0f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.NETHERITE_BLOCK)
            )
    );
    
    /**
     * Raw Siegite Block - storage block for raw siegite.
     * 
     * Crafted from 9 raw siegite. Useful for bulk storage
     * before processing the ore.
     */
    public static final DeferredBlock<Block> RAW_SIEGITE_BLOCK = BLOCKS.register(
            "raw_siegite_block",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_PURPLE)
                            .strength(5.0f, 6.0f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.STONE)
            )
    );
    
    // ==================== NOVA CANNON MULTIBLOCK COMPONENTS ====================
    
    /**
     * Nova Cannon Core - the central control block of the Nova Cannon.
     * 
     * The heart of the Nova Cannon multiblock structure.
     * Manages stress consumption, firing logic, and multiblock validation.
     * Requires a complete structure to operate:
     * - 1 Nova Cannon Core (this block)
     * - 2+ Nova Cannon Frames (support structure)
     * - 3+ Heavy Barrel Segments (extended barrel)
     * 
     * Properties:
     * - Requires 512 SU base + 64 per barrel segment
     * - 80 tick cooldown (4 seconds)
     * - Only fires Nova Shells
     * 
     * Tier: End-game
     * 
     * NOTE: This currently uses a generic Block. Full multiblock validation,
     * stress management, and firing logic will be implemented in a future
     * update with a dedicated NovaCannonCoreBlock class and BlockEntity.
     * See: https://github.com/tonda9/Mc-mod/issues - tracking issue TBD
     */
    public static final DeferredBlock<Block> NOVA_CANNON_CORE = BLOCKS.register(
            "nova_cannon_core",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .strength(8.0f, 12.0f)
                            .requiresCorrectToolForDrops()
                            .noOcclusion()
                            .sound(SoundType.NETHERITE_BLOCK)
            )
    );
    
    /**
     * Nova Cannon Frame - structural support for Nova Cannon.
     * 
     * Provides structural integrity for the Nova Cannon.
     * Must be placed adjacent to Nova Cannon Core or other frames.
     * Minimum 2 frames required for functional cannon.
     * 
     * Placement rules:
     * - Can be placed horizontally around the core
     * - Forms the support structure
     * - Each frame adds 16 SU to stress cost
     */
    public static final DeferredBlock<Block> NOVA_CANNON_FRAME = BLOCKS.register(
            "nova_cannon_frame",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .strength(7.0f, 10.0f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.METAL)
            )
    );
    
    /**
     * Heavy Barrel Segment - extended barrel for Nova Cannon.
     * 
     * Must be placed in front of Nova Cannon Core in a line.
     * Each segment increases projectile velocity and range.
     * Minimum 3 segments required for functional cannon.
     * 
     * Properties:
     * - Adds 10% velocity per segment
     * - Adds 64 SU stress cost per segment
     * - Maximum 8 segments
     */
    public static final DeferredBlock<Block> HEAVY_BARREL_SEGMENT = BLOCKS.register(
            "heavy_barrel_segment",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .strength(7.0f, 10.0f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.METAL)
            )
    );
    
    // ==================== ARMOR BLOCKS ====================
    
    /**
     * Light Armor Plate - basic protection for vehicles and structures.
     * 
     * Entry-level armor providing modest protection against small arms.
     * Can be used to construct vehicle hulls and basic fortifications.
     * 
     * Properties:
     * - Hardness: 3.5
     * - Blast Resistance: 6.0
     * - Armor Value: Low
     * - Tier: Early game (iron-based)
     */
    public static final DeferredBlock<Block> LIGHT_ARMOR_PLATE = BLOCKS.register(
            "light_armor_plate",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .strength(3.5f, 6.0f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.METAL)
            )
    );
    
    /**
     * Heavy Armor Plate - high protection armor block.
     * 
     * Thick steel plating offering significant protection.
     * Used for heavy vehicles and fortified structures.
     * 
     * Properties:
     * - Hardness: 5.0
     * - Blast Resistance: 10.0
     * - Armor Value: Medium-High
     * - Tier: Mid game (steel-based)
     */
    public static final DeferredBlock<Block> HEAVY_ARMOR_PLATE = BLOCKS.register(
            "heavy_armor_plate",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_GRAY)
                            .strength(5.0f, 10.0f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.NETHERITE_BLOCK)
            )
    );
    
    /**
     * Reinforced Armor Block - siege-grade protection.
     * 
     * Maximum conventional protection using layered composite materials.
     * Essential for bunkers and heavy assault vehicles.
     * 
     * Properties:
     * - Hardness: 6.0
     * - Blast Resistance: 15.0
     * - Armor Value: High
     * - Tier: Late game (siegite-based)
     */
    public static final DeferredBlock<Block> REINFORCED_ARMOR_BLOCK = BLOCKS.register(
            "reinforced_armor_block",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .strength(6.0f, 15.0f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.NETHERITE_BLOCK)
            )
    );
    
    /**
     * Spaced Armor Block - anti-shaped charge protection.
     * 
     * Two layers of armor with air gap to defeat HEAT rounds.
     * Effective against explosives but weaker against kinetic rounds.
     * 
     * Properties:
     * - Hardness: 4.0
     * - Blast Resistance: 12.0
     * - Special: Reduces explosive damage
     * - Tier: Mid game
     */
    public static final DeferredBlock<Block> SPACED_ARMOR_BLOCK = BLOCKS.register(
            "spaced_armor_block",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .strength(4.0f, 12.0f)
                            .requiresCorrectToolForDrops()
                            .noOcclusion()
                            .sound(SoundType.METAL)
            )
    );
    
    /**
     * Reactive Armor Block - explosion-resistant with active protection.
     * 
     * Contains explosive elements that detonate outward to counter
     * incoming projectiles. Single-use protection.
     * 
     * Properties:
     * - Hardness: 5.0
     * - Blast Resistance: 20.0
     * - Special: Explosive countermeasure (consumes on large impact)
     * - Tier: Late game
     */
    public static final DeferredBlock<Block> REACTIVE_ARMOR_BLOCK = BLOCKS.register(
            "reactive_armor_block",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_RED)
                            .strength(5.0f, 20.0f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.NETHERITE_BLOCK)
            )
    );
    
    /**
     * Sloped Armor Block - angled for ricochet.
     * 
     * 45-degree angled armor that increases effective thickness
     * and causes projectiles to ricochet.
     * 
     * Properties:
     * - Hardness: 4.5
     * - Blast Resistance: 8.0
     * - Special: Ricochet chance for kinetic rounds
     * - Tier: Mid game
     */
    public static final DeferredBlock<Block> SLOPED_ARMOR_BLOCK = BLOCKS.register(
            "sloped_armor_block",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .strength(4.5f, 8.0f)
                            .requiresCorrectToolForDrops()
                            .noOcclusion()
                            .sound(SoundType.METAL)
            )
    );
    
    /**
     * Armor Grating - walkable partial protection.
     * 
     * Open metal grating providing partial cover and allowing
     * movement on top. Used for vehicle floors and platforms.
     * 
     * Properties:
     * - Hardness: 2.5
     * - Blast Resistance: 4.0
     * - Special: Partial protection, can walk through
     * - Tier: Early game
     */
    public static final DeferredBlock<Block> ARMOR_GRATING = BLOCKS.register(
            "armor_grating",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .strength(2.5f, 4.0f)
                            .requiresCorrectToolForDrops()
                            .noOcclusion()
                            .sound(SoundType.LANTERN)
            )
    );
    
    /**
     * Blast Door - functional reinforced door.
     * 
     * Heavy armored door for bunkers and secure facilities.
     * Operates with redstone signal.
     * 
     * Properties:
     * - Hardness: 7.0
     * - Blast Resistance: 18.0
     * - Special: Redstone controlled
     * - Tier: Mid-Late game
     */
    public static final DeferredBlock<Block> BLAST_DOOR = BLOCKS.register(
            "blast_door",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_GRAY)
                            .strength(7.0f, 18.0f)
                            .requiresCorrectToolForDrops()
                            .noOcclusion()
                            .sound(SoundType.NETHERITE_BLOCK)
            )
    );
    
    // ==================== TUNGSTEN ORE ====================
    
    /**
     * Tungsten Ore - rare heavy metal ore.
     * 
     * Found deep underground, essential for armor-piercing rounds
     * and heavy armor construction. Very dense material.
     * 
     * Properties:
     * - Found at Y -64 to -20
     * - Requires diamond pickaxe
     * - Drops raw tungsten
     */
    public static final DeferredBlock<Block> TUNGSTEN_ORE = BLOCKS.register(
            "tungsten_ore",
            () -> new DropExperienceBlock(
                    UniformInt.of(4, 8),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.STONE)
                            .strength(4.5f, 4.0f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.STONE)
            )
    );
    
    /**
     * Deepslate Tungsten Ore - variant found in deepslate.
     */
    public static final DeferredBlock<Block> DEEPSLATE_TUNGSTEN_ORE = BLOCKS.register(
            "deepslate_tungsten_ore",
            () -> new DropExperienceBlock(
                    UniformInt.of(4, 8),
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.DEEPSLATE)
                            .strength(6.0f, 4.0f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.DEEPSLATE)
            )
    );
    
    /**
     * Tungsten Block - storage block for tungsten ingots.
     */
    public static final DeferredBlock<Block> TUNGSTEN_BLOCK = BLOCKS.register(
            "tungsten_block",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_GRAY)
                            .strength(7.0f, 10.0f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.NETHERITE_BLOCK)
            )
    );
    
    /**
     * Raw Tungsten Block - storage block for raw tungsten.
     */
    public static final DeferredBlock<Block> RAW_TUNGSTEN_BLOCK = BLOCKS.register(
            "raw_tungsten_block",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_GRAY)
                            .strength(5.0f, 6.0f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.STONE)
            )
    );
    
    // ==================== CALIBER-SPECIFIC CANNON BLOCKS ====================
    
    /**
     * 30mm Autocannon - rapid fire light cannon.
     * 
     * High rate of fire, low damage per shot. Ideal for anti-personnel
     * and light vehicle engagements.
     * 
     * Stats:
     * - SU Cost: 64
     * - Cooldown: 5 ticks
     * - Damage Multiplier: 0.3x
     */
    public static final DeferredBlock<Block> AUTOCANNON_30MM = BLOCKS.register(
            "autocannon_30mm",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .strength(4.0f, 6.0f)
                            .requiresCorrectToolForDrops()
                            .noOcclusion()
                            .sound(SoundType.METAL)
            )
    );
    
    /**
     * 75mm Field Gun - balanced medium cannon.
     * 
     * Versatile artillery piece effective against most targets.
     * Good balance of rate of fire and damage.
     * 
     * Stats:
     * - SU Cost: 128
     * - Cooldown: 20 ticks
     * - Damage Multiplier: 0.6x
     */
    public static final DeferredBlock<Block> FIELD_GUN_75MM = BLOCKS.register(
            "field_gun_75mm",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .strength(5.0f, 7.0f)
                            .requiresCorrectToolForDrops()
                            .noOcclusion()
                            .sound(SoundType.METAL)
            )
    );
    
    /**
     * 120mm Tank Cannon - high power main battle cannon.
     * 
     * Standard heavy combat cannon with excellent penetration.
     * 
     * Stats:
     * - SU Cost: 256
     * - Cooldown: 40 ticks
     * - Damage Multiplier: 1.0x
     */
    public static final DeferredBlock<Block> TANK_CANNON_120MM = BLOCKS.register(
            "tank_cannon_120mm",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .strength(6.0f, 8.0f)
                            .requiresCorrectToolForDrops()
                            .noOcclusion()
                            .sound(SoundType.METAL)
            )
    );
    
    /**
     * 155mm Howitzer - heavy artillery cannon.
     * 
     * Indirect fire artillery with high explosive shells.
     * Excellent for siege warfare.
     * 
     * Stats:
     * - SU Cost: 384
     * - Cooldown: 60 ticks
     * - Damage Multiplier: 1.4x
     */
    public static final DeferredBlock<Block> HOWITZER_155MM = BLOCKS.register(
            "howitzer_155mm",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .strength(6.5f, 9.0f)
                            .requiresCorrectToolForDrops()
                            .noOcclusion()
                            .sound(SoundType.METAL)
            )
    );
    
    /**
     * 280mm Siege Gun - very heavy siege cannon.
     * 
     * Devastating power against fortifications.
     * 
     * Stats:
     * - SU Cost: 512
     * - Cooldown: 100 ticks
     * - Damage Multiplier: 1.8x
     */
    public static final DeferredBlock<Block> SIEGE_GUN_280MM = BLOCKS.register(
            "siege_gun_280mm",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_GRAY)
                            .strength(7.0f, 10.0f)
                            .requiresCorrectToolForDrops()
                            .noOcclusion()
                            .sound(SoundType.NETHERITE_BLOCK)
            )
    );
    
    /**
     * 400mm Railway Gun - massive transportable artillery.
     * 
     * Extreme range and power, requires substantial infrastructure.
     * 
     * Stats:
     * - SU Cost: 768
     * - Cooldown: 160 ticks
     * - Damage Multiplier: 2.5x
     */
    public static final DeferredBlock<Block> RAILWAY_GUN_400MM = BLOCKS.register(
            "railway_gun_400mm",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_GRAY)
                            .strength(8.0f, 12.0f)
                            .requiresCorrectToolForDrops()
                            .noOcclusion()
                            .sound(SoundType.NETHERITE_BLOCK)
            )
    );
    
    /**
     * 800mm Gustav Cannon - super heavy siege weapon.
     * 
     * Legendary destructive power. Requires extensive support.
     * 
     * Stats:
     * - SU Cost: 1024
     * - Cooldown: 240 ticks
     * - Damage Multiplier: 4.0x
     */
    public static final DeferredBlock<Block> GUSTAV_CANNON_800MM = BLOCKS.register(
            "gustav_cannon_800mm",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .strength(10.0f, 15.0f)
                            .requiresCorrectToolForDrops()
                            .noOcclusion()
                            .sound(SoundType.NETHERITE_BLOCK)
            )
    );
    
    /**
     * 1000mm Siege Mortar - ultimate destructive weapon.
     * 
     * The pinnacle of artillery technology. Catastrophic damage.
     * 
     * Stats:
     * - SU Cost: 1536
     * - Cooldown: 400 ticks
     * - Damage Multiplier: 6.0x
     */
    public static final DeferredBlock<Block> SIEGE_MORTAR_1000MM = BLOCKS.register(
            "siege_mortar_1000mm",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_BLACK)
                            .strength(12.0f, 20.0f)
                            .requiresCorrectToolForDrops()
                            .noOcclusion()
                            .sound(SoundType.NETHERITE_BLOCK)
            )
    );
    
    // ==================== CALIBER BARRELS ====================
    
    public static final DeferredBlock<Block> BARREL_30MM = BLOCKS.register(
            "barrel_30mm",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL).strength(3.5f, 5.0f)
                    .requiresCorrectToolForDrops().sound(SoundType.METAL))
    );
    
    public static final DeferredBlock<Block> BARREL_75MM = BLOCKS.register(
            "barrel_75mm",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL).strength(4.0f, 6.0f)
                    .requiresCorrectToolForDrops().sound(SoundType.METAL))
    );
    
    public static final DeferredBlock<Block> BARREL_120MM = BLOCKS.register(
            "barrel_120mm",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL).strength(5.0f, 7.0f)
                    .requiresCorrectToolForDrops().sound(SoundType.METAL))
    );
    
    public static final DeferredBlock<Block> BARREL_155MM = BLOCKS.register(
            "barrel_155mm",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL).strength(5.5f, 8.0f)
                    .requiresCorrectToolForDrops().sound(SoundType.METAL))
    );
    
    public static final DeferredBlock<Block> BARREL_280MM = BLOCKS.register(
            "barrel_280mm",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY).strength(6.0f, 9.0f)
                    .requiresCorrectToolForDrops().sound(SoundType.NETHERITE_BLOCK))
    );
    
    public static final DeferredBlock<Block> BARREL_400MM = BLOCKS.register(
            "barrel_400mm",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY).strength(7.0f, 10.0f)
                    .requiresCorrectToolForDrops().sound(SoundType.NETHERITE_BLOCK))
    );
    
    public static final DeferredBlock<Block> BARREL_800MM = BLOCKS.register(
            "barrel_800mm",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK).strength(8.0f, 12.0f)
                    .requiresCorrectToolForDrops().sound(SoundType.NETHERITE_BLOCK))
    );
    
    public static final DeferredBlock<Block> BARREL_1000MM = BLOCKS.register(
            "barrel_1000mm",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK).strength(10.0f, 15.0f)
                    .requiresCorrectToolForDrops().sound(SoundType.NETHERITE_BLOCK))
    );
    
    // ==================== CREATE INTEGRATION BLOCKS ====================
    
    /**
     * Cannon Turret Bearing - allows 360° rotation.
     * 
     * Enables mounting cannons on rotating platforms.
     * Integrates with Create's rotation system.
     * 
     * Stats:
     * - SU Cost: 128
     * - Rotation Speed: Variable
     */
    public static final DeferredBlock<Block> CANNON_TURRET_BEARING = BLOCKS.register(
            "cannon_turret_bearing",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .strength(5.0f, 8.0f)
                            .requiresCorrectToolForDrops()
                            .noOcclusion()
                            .sound(SoundType.METAL)
            )
    );
    
    /**
     * Ammo Feed Mechanism - belt-fed ammunition system.
     * 
     * Automatically feeds ammunition from storage to cannon.
     * Requires Create belts or chutes.
     */
    public static final DeferredBlock<Block> AMMO_FEED_MECHANISM = BLOCKS.register(
            "ammo_feed_mechanism",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .strength(4.0f, 6.0f)
                            .requiresCorrectToolForDrops()
                            .noOcclusion()
                            .sound(SoundType.METAL)
            )
    );
    
    /**
     * Autoloader Block - automatic shell loading.
     * 
     * Reduces reload time by automatically chambering rounds.
     * Consumes stress when operating.
     * 
     * Stats:
     * - SU Cost: 64
     * - Reload Reduction: 50%
     */
    public static final DeferredBlock<Block> AUTOLOADER = BLOCKS.register(
            "autoloader",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .strength(5.0f, 7.0f)
                            .requiresCorrectToolForDrops()
                            .noOcclusion()
                            .sound(SoundType.METAL)
            )
    );
    
    /**
     * Targeting Computer - improves accuracy.
     * 
     * Calculates ballistic trajectories for improved hit chance.
     * Requires redstone power.
     * 
     * Stats:
     * - SU Cost: 32
     * - Accuracy Bonus: +25%
     */
    public static final DeferredBlock<Block> TARGETING_COMPUTER = BLOCKS.register(
            "targeting_computer",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_LIGHT_BLUE)
                            .strength(3.0f, 4.0f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.METAL)
            )
    );
    
    /**
     * Gun Stabilizer - reduces recoil.
     * 
     * Dampens cannon movement for accurate firing while moving.
     * Essential for vehicle-mounted weapons.
     * 
     * Stats:
     * - SU Cost: 48
     * - Recoil Reduction: 75%
     */
    public static final DeferredBlock<Block> GUN_STABILIZER = BLOCKS.register(
            "gun_stabilizer",
            () -> new Block(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .strength(4.0f, 6.0f)
                            .requiresCorrectToolForDrops()
                            .sound(SoundType.METAL)
            )
    );
    
    // ==================== HELPER METHODS ====================
    
    /**
     * Registers all blocks to the mod event bus.
     * Called from the main mod class during initialization.
     * 
     * @param eventBus The mod's event bus
     */
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        CreateCannons.LOGGER.info("Create Cannons blocks registered!");
    }
}
