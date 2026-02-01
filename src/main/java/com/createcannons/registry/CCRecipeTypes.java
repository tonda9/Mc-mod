/**
 * Registry for recipe types and serializers in Create Cannons.
 * 
 * This class registers custom recipe types that integrate with
 * Create's processing systems, including sequenced assembly recipes.
 */
package com.createcannons.registry;

import com.createcannons.CreateCannons;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Handles registration of recipe types and serializers in Create Cannons.
 * 
 * While most recipes use Create's existing recipe types (like sequenced assembly),
 * this class provides the infrastructure for any custom recipe types needed
 * for cannon-specific crafting processes.
 */
public class CCRecipeTypes {
    
    /**
     * Deferred register for recipe types.
     */
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = 
            DeferredRegister.create(Registries.RECIPE_TYPE, CreateCannons.MOD_ID);
    
    /**
     * Deferred register for recipe serializers.
     */
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = 
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, CreateCannons.MOD_ID);
    
    // Recipe types can be added here as the mod expands
    // For now, we use Create's existing sequenced assembly system
    
    /**
     * Registers all recipe types and serializers to the mod event bus.
     * Called from the main mod class during initialization.
     * 
     * @param eventBus The mod's event bus
     */
    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
        RECIPE_SERIALIZERS.register(eventBus);
        CreateCannons.LOGGER.info("Create Cannons recipe types registered!");
    }
}
