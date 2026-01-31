/**
 * Registry for all entity types in Create Cannons.
 * 
 * Entities are dynamic objects in the world that can move, interact,
 * and have their own logic. This mod uses them for projectiles that
 * travel through the world after being fired from cannons.
 */
package com.createcannons.registry;

import com.createcannons.CreateCannons;
import com.createcannons.entity.CannonballEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Handles registration of all entity types in Create Cannons.
 * 
 * Entity types define the properties and behavior of spawnable entities.
 * Each entity type specifies its category, dimensions, and factory method.
 */
public class CCEntityTypes {
    
    /**
     * Deferred register for entity types.
     */
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = 
            DeferredRegister.create(Registries.ENTITY_TYPE, CreateCannons.MOD_ID);
    
    // ==================== PROJECTILE ENTITIES ====================
    
    /**
     * Cannonball projectile entity type.
     * 
     * This entity represents a fired cannonball traveling through the world.
     * It handles:
     * - Ballistic physics (gravity, air resistance)
     * - Collision detection with blocks and entities
     * - Explosion on impact
     * - Damage calculation
     * 
     * Properties:
     * - Category: MISC (non-living, non-monster)
     * - Size: 0.5 x 0.5 blocks
     * - Tracking range: 64 blocks
     * - Update interval: 10 ticks
     * - Fire immune for visual consistency
     */
    public static final DeferredHolder<EntityType<?>, EntityType<CannonballEntity>> CANNONBALL = 
            ENTITY_TYPES.register(
                    "cannonball",
                    () -> EntityType.Builder.<CannonballEntity>of(CannonballEntity::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)          // Entity hitbox dimensions
                            .clientTrackingRange(64)    // How far clients track this entity
                            .updateInterval(10)         // Ticks between position syncs
                            .fireImmune()               // Cannonballs don't burn
                            .build(ResourceLocation.fromNamespaceAndPath(CreateCannons.MOD_ID, "cannonball").toString())
            );
    
    // ==================== HELPER METHODS ====================
    
    /**
     * Registers all entity types to the mod event bus.
     * Called from the main mod class during initialization.
     * 
     * @param eventBus The mod's event bus
     */
    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
        CreateCannons.LOGGER.info("Create Cannons entities registered!");
    }
}
