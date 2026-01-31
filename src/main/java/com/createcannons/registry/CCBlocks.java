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
