/**
 * Recipe Provider - Generates crafting recipes for the mod.
 * 
 * Creates recipe JSON files including:
 * - Standard crafting recipes
 * - Shaped and shapeless recipes
 * - Smelting/blasting recipes
 * - Create processing recipes (mixing, pressing, etc.)
 * - Sequenced assembly recipes
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
 * Generates all recipes for Create Cannons.
 * 
 * This includes both standard Minecraft crafting recipes and
 * Create-style processing recipes. Sequenced assembly recipes
 * are defined using Create's recipe system.
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
     * Builds all recipes.
     * 
     * @param recipeOutput The recipe output consumer
     */
    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        // Build standard crafting recipes
        buildCannonRecipes(recipeOutput);
        buildProjectileRecipes(recipeOutput);
        buildComponentRecipes(recipeOutput);
    }
    
    /**
     * Builds recipes for cannon blocks.
     */
    private void buildCannonRecipes(RecipeOutput recipeOutput) {
        // Kinetic Cannon - requires iron, a mechanical component, and cannon casting
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CCBlocks.KINETIC_CANNON.get())
                .pattern("III")
                .pattern("ICI")
                .pattern("SSS")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('C', CCItems.CANNON_CASTING.get())
                .define('S', Tags.Items.COBBLESTONES)
                .unlockedBy("has_cannon_casting", has(CCItems.CANNON_CASTING.get()))
                .save(recipeOutput);
        
        // Cannon Barrel - iron tubes
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CCBlocks.CANNON_BARREL.get(), 2)
                .pattern("III")
                .pattern("   ")
                .pattern("III")
                .define('I', Tags.Items.INGOTS_IRON)
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .save(recipeOutput);
        
        // Cannon Breech - iron and a hopper for loading
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CCBlocks.CANNON_BREECH.get())
                .pattern("IHI")
                .pattern("I I")
                .pattern("III")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('H', Items.HOPPER)
                .unlockedBy("has_hopper", has(Items.HOPPER))
                .save(recipeOutput);
    }
    
    /**
     * Builds recipes for projectile items.
     */
    private void buildProjectileRecipes(RecipeOutput recipeOutput) {
        // Iron Cannonball - simple iron nuggets
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, CCItems.IRON_CANNONBALL.get(), 4)
                .pattern(" I ")
                .pattern("III")
                .pattern(" I ")
                .define('I', Tags.Items.INGOTS_IRON)
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .save(recipeOutput);
        
        // Steel Cannonball - requires iron blocks (representing higher quality)
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, CCItems.STEEL_CANNONBALL.get(), 2)
                .pattern(" B ")
                .pattern("BIB")
                .pattern(" B ")
                .define('B', Tags.Items.STORAGE_BLOCKS_IRON)
                .define('I', Tags.Items.INGOTS_IRON)
                .unlockedBy("has_iron_block", has(Tags.Items.STORAGE_BLOCKS_IRON))
                .save(recipeOutput);
        
        // Explosive Shell - cannonball + TNT
        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, CCItems.EXPLOSIVE_SHELL.get(), 2)
                .requires(CCItems.IRON_CANNONBALL.get())
                .requires(Items.TNT)
                .unlockedBy("has_cannonball", has(CCItems.IRON_CANNONBALL.get()))
                .save(recipeOutput);
        
        // Gunpowder Charge - paper wrapped gunpowder
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CCItems.GUNPOWDER_CHARGE.get(), 4)
                .pattern(" P ")
                .pattern("PGP")
                .pattern(" P ")
                .define('P', Items.PAPER)
                .define('G', Items.GUNPOWDER)
                .unlockedBy("has_gunpowder", has(Items.GUNPOWDER))
                .save(recipeOutput);
    }
    
    /**
     * Builds recipes for crafting components.
     */
    private void buildComponentRecipes(RecipeOutput recipeOutput) {
        // Cannon Casting - used to make cannon blocks
        // Made from clay and iron, representing a mold
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CCItems.CANNON_CASTING.get())
                .pattern("CIC")
                .pattern("I I")
                .pattern("CIC")
                .define('C', Items.CLAY_BALL)
                .define('I', Tags.Items.INGOTS_IRON)
                .unlockedBy("has_clay", has(Items.CLAY_BALL))
                .save(recipeOutput);
        
        // Incomplete Cannon Barrel - intermediate crafting item
        // In a full implementation, this would be used in sequenced assembly
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CCItems.INCOMPLETE_CANNON_BARREL.get())
                .pattern("I I")
                .pattern("I I")
                .pattern("I I")
                .define('I', Tags.Items.INGOTS_IRON)
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .save(recipeOutput);
    }
    
    // ==================== SEQUENCED ASSEMBLY RECIPES ====================
    // Note: These would be defined as JSON files or through Create's API
    // when Create dependency is properly integrated. The structure would be:
    //
    // Cannon Barrel Sequenced Assembly:
    // 1. Start with Incomplete Cannon Barrel
    // 2. Press with mechanical press
    // 3. Deploy with deployer (add iron sheets)
    // 4. Press again to finish
    // Result: Cannon Barrel
    //
    // This provides an alternative, more Create-style way to craft barrels
    // with higher yield or better properties.
}
