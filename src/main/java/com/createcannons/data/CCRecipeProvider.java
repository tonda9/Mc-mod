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
        buildAdvancedProjectileRecipes(recipeOutput);
        buildComponentRecipes(recipeOutput);
        buildCannoniteRecipes(recipeOutput);
        buildSmeltingRecipes(recipeOutput);
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
     * Builds recipes for basic projectile items.
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
     * Builds recipes for advanced projectile items.
     */
    private void buildAdvancedProjectileRecipes(RecipeOutput recipeOutput) {
        // Nuclear Shell - enriched cannonite + TNT + cannonball
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, CCItems.NUCLEAR_SHELL.get())
                .pattern("ECE")
                .pattern("CTC")
                .pattern("ECE")
                .define('E', CCItems.ENRICHED_CANNONITE.get())
                .define('C', CCItems.CANNONITE_INGOT.get())
                .define('T', Items.TNT)
                .unlockedBy("has_enriched_cannonite", has(CCItems.ENRICHED_CANNONITE.get()))
                .save(recipeOutput);
        
        // Rocket Ammo - cannonball + rocket fuel + fuse
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, CCItems.ROCKET_AMMO.get(), 2)
                .pattern(" F ")
                .pattern("RCR")
                .pattern("RRR")
                .define('F', CCItems.FUSE.get())
                .define('R', CCItems.ROCKET_FUEL.get())
                .define('C', CCItems.CANNONITE_INGOT.get())
                .unlockedBy("has_rocket_fuel", has(CCItems.ROCKET_FUEL.get()))
                .save(recipeOutput);
        
        // Incendiary Shell - cannonball + blaze powder + magma cream
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, CCItems.INCENDIARY_SHELL.get(), 4)
                .pattern(" B ")
                .pattern("MCM")
                .pattern(" F ")
                .define('B', Items.BLAZE_POWDER)
                .define('M', Items.MAGMA_CREAM)
                .define('C', CCItems.IRON_CANNONBALL.get())
                .define('F', CCItems.FUSE.get())
                .unlockedBy("has_blaze_powder", has(Items.BLAZE_POWDER))
                .save(recipeOutput);
        
        // Armor Piercing Round - cannonite + diamond + iron
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, CCItems.ARMOR_PIERCING_ROUND.get(), 4)
                .pattern(" D ")
                .pattern("CIC")
                .pattern(" C ")
                .define('D', Tags.Items.GEMS_DIAMOND)
                .define('C', CCItems.CANNONITE_INGOT.get())
                .define('I', Tags.Items.INGOTS_IRON)
                .unlockedBy("has_cannonite", has(CCItems.CANNONITE_INGOT.get()))
                .save(recipeOutput);
        
        // Cluster Bomb - multiple small explosives + fuse
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, CCItems.CLUSTER_BOMB.get(), 2)
                .pattern("GGG")
                .pattern("GFG")
                .pattern("GGG")
                .define('G', Items.GUNPOWDER)
                .define('F', CCItems.FUSE.get())
                .unlockedBy("has_fuse", has(CCItems.FUSE.get()))
                .save(recipeOutput);
        
        // Smoke Shell - gunpowder + dye + wool
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, CCItems.SMOKE_SHELL.get(), 4)
                .pattern(" W ")
                .pattern("DGD")
                .pattern(" I ")
                .define('W', ItemTags.WOOL)
                .define('D', Tags.Items.DYES_GRAY)
                .define('G', Items.GUNPOWDER)
                .define('I', Tags.Items.INGOTS_IRON)
                .unlockedBy("has_wool", has(ItemTags.WOOL))
                .save(recipeOutput);
        
        // Grapeshot - iron nuggets packed together
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, CCItems.GRAPESHOT.get(), 8)
                .pattern("NNN")
                .pattern("NIN")
                .pattern("NNN")
                .define('N', Tags.Items.NUGGETS_IRON)
                .define('I', Tags.Items.INGOTS_IRON)
                .unlockedBy("has_iron_nugget", has(Tags.Items.NUGGETS_IRON))
                .save(recipeOutput);
    }
    
    /**
     * Builds recipes for crafting components.
     */
    private void buildComponentRecipes(RecipeOutput recipeOutput) {
        // Cannon Casting - used to make cannon blocks
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CCItems.CANNON_CASTING.get())
                .pattern("CIC")
                .pattern("I I")
                .pattern("CIC")
                .define('C', Items.CLAY_BALL)
                .define('I', Tags.Items.INGOTS_IRON)
                .unlockedBy("has_clay", has(Items.CLAY_BALL))
                .save(recipeOutput);
        
        // Incomplete Cannon Barrel - intermediate crafting item
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CCItems.INCOMPLETE_CANNON_BARREL.get())
                .pattern("I I")
                .pattern("I I")
                .pattern("I I")
                .define('I', Tags.Items.INGOTS_IRON)
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .save(recipeOutput);
        
        // Fuse - string + gunpowder
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CCItems.FUSE.get(), 8)
                .pattern("GSG")
                .pattern("SGS")
                .pattern("GSG")
                .define('G', Items.GUNPOWDER)
                .define('S', Tags.Items.STRINGS)
                .unlockedBy("has_string", has(Tags.Items.STRINGS))
                .save(recipeOutput);
        
        // Rocket Fuel - blaze powder + gunpowder + coal
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CCItems.ROCKET_FUEL.get(), 4)
                .pattern("BGB")
                .pattern("GCG")
                .pattern("BGB")
                .define('B', Items.BLAZE_POWDER)
                .define('G', Items.GUNPOWDER)
                .define('C', Items.COAL)
                .unlockedBy("has_blaze_powder", has(Items.BLAZE_POWDER))
                .save(recipeOutput);
        
        // Enriched Cannonite - cannonite + blaze powder + nether star
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CCItems.ENRICHED_CANNONITE.get(), 4)
                .pattern("CBC")
                .pattern("BNB")
                .pattern("CBC")
                .define('C', CCItems.CANNONITE_INGOT.get())
                .define('B', Items.BLAZE_POWDER)
                .define('N', Items.NETHER_STAR)
                .unlockedBy("has_nether_star", has(Items.NETHER_STAR))
                .save(recipeOutput);
    }
    
    /**
     * Builds recipes for cannonite materials (storage blocks, nuggets).
     */
    private void buildCannoniteRecipes(RecipeOutput recipeOutput) {
        // Cannonite Block from ingots
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, CCBlocks.CANNONITE_BLOCK.get())
                .pattern("III")
                .pattern("III")
                .pattern("III")
                .define('I', CCItems.CANNONITE_INGOT.get())
                .unlockedBy("has_cannonite_ingot", has(CCItems.CANNONITE_INGOT.get()))
                .save(recipeOutput);
        
        // Cannonite Ingot from block
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CCItems.CANNONITE_INGOT.get(), 9)
                .requires(CCBlocks.CANNONITE_BLOCK.get())
                .unlockedBy("has_cannonite_block", has(CCBlocks.CANNONITE_BLOCK.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(CreateCannons.MOD_ID, "cannonite_ingot_from_block"));
        
        // Cannonite Ingot from nuggets
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CCItems.CANNONITE_INGOT.get())
                .pattern("NNN")
                .pattern("NNN")
                .pattern("NNN")
                .define('N', CCItems.CANNONITE_NUGGET.get())
                .unlockedBy("has_cannonite_nugget", has(CCItems.CANNONITE_NUGGET.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(CreateCannons.MOD_ID, "cannonite_ingot_from_nuggets"));
        
        // Cannonite Nugget from ingot
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CCItems.CANNONITE_NUGGET.get(), 9)
                .requires(CCItems.CANNONITE_INGOT.get())
                .unlockedBy("has_cannonite_ingot", has(CCItems.CANNONITE_INGOT.get()))
                .save(recipeOutput);
        
        // Raw Cannonite Block from raw
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, CCBlocks.RAW_CANNONITE_BLOCK.get())
                .pattern("RRR")
                .pattern("RRR")
                .pattern("RRR")
                .define('R', CCItems.RAW_CANNONITE.get())
                .unlockedBy("has_raw_cannonite", has(CCItems.RAW_CANNONITE.get()))
                .save(recipeOutput);
        
        // Raw Cannonite from block
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CCItems.RAW_CANNONITE.get(), 9)
                .requires(CCBlocks.RAW_CANNONITE_BLOCK.get())
                .unlockedBy("has_raw_cannonite_block", has(CCBlocks.RAW_CANNONITE_BLOCK.get()))
                .save(recipeOutput);
    }
    
    /**
     * Builds smelting and blasting recipes.
     */
    private void buildSmeltingRecipes(RecipeOutput recipeOutput) {
        // Smelt raw cannonite to ingot
        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(CCItems.RAW_CANNONITE.get()),
                        RecipeCategory.MISC,
                        CCItems.CANNONITE_INGOT.get(),
                        1.0f,
                        200
                )
                .unlockedBy("has_raw_cannonite", has(CCItems.RAW_CANNONITE.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(CreateCannons.MOD_ID, "cannonite_ingot_from_smelting"));
        
        // Blast raw cannonite to ingot (faster)
        SimpleCookingRecipeBuilder.blasting(
                        Ingredient.of(CCItems.RAW_CANNONITE.get()),
                        RecipeCategory.MISC,
                        CCItems.CANNONITE_INGOT.get(),
                        1.0f,
                        100
                )
                .unlockedBy("has_raw_cannonite", has(CCItems.RAW_CANNONITE.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(CreateCannons.MOD_ID, "cannonite_ingot_from_blasting"));
        
        // Smelt cannonite ore to ingot
        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(CCBlocks.CANNONITE_ORE.get()),
                        RecipeCategory.MISC,
                        CCItems.CANNONITE_INGOT.get(),
                        1.0f,
                        200
                )
                .unlockedBy("has_cannonite_ore", has(CCBlocks.CANNONITE_ORE.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(CreateCannons.MOD_ID, "cannonite_ingot_from_ore_smelting"));
        
        // Smelt deepslate cannonite ore to ingot
        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(CCBlocks.DEEPSLATE_CANNONITE_ORE.get()),
                        RecipeCategory.MISC,
                        CCItems.CANNONITE_INGOT.get(),
                        1.0f,
                        200
                )
                .unlockedBy("has_deepslate_cannonite_ore", has(CCBlocks.DEEPSLATE_CANNONITE_ORE.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(CreateCannons.MOD_ID, "cannonite_ingot_from_deepslate_ore_smelting"));
    }
}
