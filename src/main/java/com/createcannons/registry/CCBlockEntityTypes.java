/**
 * Registry for all block entity types in Create Cannons.
 * 
 * Block entities (formerly tile entities) provide persistent data storage
 * and tick-based logic for blocks. This mod uses them for cannon mechanics,
 * stress handling, and inventory management.
 */
package com.createcannons.registry;

import com.createcannons.CreateCannons;
import com.createcannons.blockentity.KineticCannonBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Handles registration of all block entity types in Create Cannons.
 * 
 * Block entities are associated with specific block types and provide
 * additional functionality like data persistence and per-tick updates.
 */
public class CCBlockEntityTypes {
    
    /**
     * Deferred register for block entity types.
     */
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = 
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, CreateCannons.MOD_ID);
    
    // ==================== CANNON BLOCK ENTITIES ====================
    
    /**
     * Block entity type for the Kinetic Cannon block.
     * 
     * This block entity handles:
     * - Kinetic stress consumption and validation
     * - Projectile loading and inventory
     * - Firing cooldown timing
     * - Aiming and rotation logic
     * - Multiblock structure validation
     */
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<KineticCannonBlockEntity>> KINETIC_CANNON = 
            BLOCK_ENTITY_TYPES.register(
                    "kinetic_cannon",
                    () -> BlockEntityType.Builder.of(
                            KineticCannonBlockEntity::new,
                            CCBlocks.KINETIC_CANNON.get()
                    ).build(null)
            );
    
    // ==================== HELPER METHODS ====================
    
    /**
     * Registers all block entity types to the mod event bus.
     * Called from the main mod class during initialization.
     * 
     * @param eventBus The mod's event bus
     */
    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus);
        CreateCannons.LOGGER.info("Create Cannons block entities registered!");
    }
}
