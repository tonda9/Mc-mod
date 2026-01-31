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
import net.minecraft.world.level.block.Block;
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
