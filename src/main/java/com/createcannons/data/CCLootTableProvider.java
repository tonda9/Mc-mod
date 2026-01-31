/**
 * Loot Table Provider - Generates block loot tables.
 * 
 * Loot tables define what items drop when blocks are broken.
 * By default, blocks drop themselves, but this can be customized.
 */
package com.createcannons.data;

import com.createcannons.registry.CCBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.WritableRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Generates loot table files for blocks.
 */
public class CCLootTableProvider extends LootTableProvider {
    
    /**
     * Creates a new loot table provider.
     * 
     * @param output The pack output
     * @param registries The registry lookup provider
     */
    public CCLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, Set.of(), List.of(
                new SubProviderEntry(CCBlockLootTables::new, LootContextParamSets.BLOCK)
        ), registries);
    }
    
    /**
     * Block loot table sub-provider.
     * Defines loot tables for each block.
     */
    public static class CCBlockLootTables extends BlockLootSubProvider {
        
        /**
         * Creates a new block loot table provider.
         * 
         * @param registries The holder lookup provider
         */
        protected CCBlockLootTables(HolderLookup.Provider registries) {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
        }
        
        /**
         * Generates loot tables for all blocks.
         */
        @Override
        protected void generate() {
            // All cannon blocks drop themselves
            dropSelf(CCBlocks.KINETIC_CANNON.get());
            dropSelf(CCBlocks.CANNON_BARREL.get());
            dropSelf(CCBlocks.CANNON_BREECH.get());
        }
        
        /**
         * Gets all blocks that need loot tables.
         */
        @Override
        protected Iterable<Block> getKnownBlocks() {
            return List.of(
                    CCBlocks.KINETIC_CANNON.get(),
                    CCBlocks.CANNON_BARREL.get(),
                    CCBlocks.CANNON_BREECH.get()
            );
        }
    }
}
