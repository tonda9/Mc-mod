/**
 * Data Generators - Automated generation of JSON data files.
 * 
 * This class coordinates all data generation for the mod, including:
 * - Language files (translations)
 * - Recipe files
 * - Block/item model files
 * - Loot tables
 * - Tags
 * 
 * Running the 'runData' Gradle task will generate these files
 * into src/generated/resources.
 */
package com.createcannons.data;

import com.createcannons.CreateCannons;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

/**
 * Main data generator coordinator.
 * 
 * Registers all data providers when the GatherDataEvent fires.
 * Each provider generates a specific type of data file.
 */
public class CCDataGenerators {
    
    /**
     * Handles the GatherDataEvent to register all data providers.
     * 
     * @param event The data gathering event
     */
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        
        // Register language provider (always needed)
        generator.addProvider(
                event.includeClient(),
                new CCLanguageProvider(packOutput, "en_us")
        );
        
        // Register recipe provider
        generator.addProvider(
                event.includeServer(),
                new CCRecipeProvider(packOutput, lookupProvider)
        );
        
        // Register block state and model providers
        generator.addProvider(
                event.includeClient(),
                new CCBlockStateProvider(packOutput, existingFileHelper)
        );
        
        generator.addProvider(
                event.includeClient(),
                new CCItemModelProvider(packOutput, existingFileHelper)
        );
        
        // Register loot table provider
        generator.addProvider(
                event.includeServer(),
                new CCLootTableProvider(packOutput, lookupProvider)
        );
        
        // Register tag providers
        generator.addProvider(
                event.includeServer(),
                new CCBlockTagsProvider(packOutput, lookupProvider, existingFileHelper)
        );
        
        CreateCannons.LOGGER.info("Create Cannons data generators registered!");
    }
}
