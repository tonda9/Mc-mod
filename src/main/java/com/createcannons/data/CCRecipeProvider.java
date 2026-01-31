/**
 * Recipe Provider - Generates crafting recipes for the mod.
 * 
 * NOTE: Recipes are now defined as JSON files in:
 * src/main/resources/data/createcannons/recipes/
 * 
 * This file is kept for reference and potential future Create
 * integration recipes that require programmatic generation.
 * 
 * Design decisions for recipes:
 * - Cannon blocks require Create items (brass casing, iron sheets, funnels)
 *   to integrate with Create progression
 * - Basic ammo (iron cannonball) is cheap and accessible
 * - Advanced ammo requires Nether materials (blaze powder, magma cream)
 * - End-game ammo (nuclear shell) gates behind Wither (nether star)
 * - Material conversions follow standard 9:1 ratios
 */
package com.createcannons.data;

import com.createcannons.CreateCannons;
import com.createcannons.registry.CCBlocks;
import com.createcannons.registry.CCItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

/**
 * Recipe Provider for Create Cannons.
 * 
 * Recipes are now defined as JSON files for easier editing and Create-style
 * integration. This provider is kept for potential future programmatic recipes.
 * 
 * See: src/main/resources/data/createcannons/recipes/
 */
public class CCRecipeProvider extends RecipeProvider {
    
    /**
     * Creates a new recipe provider.
     * 
     * @param output The pack output
     * @param registries The registry lookup provider
     */
    public CCRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }
    
    /**
     * Builds recipes via datagen.
     * 
     * NOTE: Primary recipes are now in JSON files.
     * This method is intentionally empty to avoid conflicts.
     * 
     * @param recipeOutput The recipe output consumer
     */
    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        // Recipes are defined in JSON files at:
        // src/main/resources/data/createcannons/recipes/
        // 
        // This approach allows for:
        // - Easier recipe editing without recompilation
        // - Better Create mod integration
        // - Cleaner version control diffs
        //
        // See README or recipe files for design decisions.
    }
}
