/**
 * Block Tags Provider - Generates block tag JSON files.
 * 
 * Tags are used to group blocks/items for:
 * - Recipe ingredients (e.g., any planks)
 * - Tool requirements (e.g., needs iron tool)
 * - Game mechanics (e.g., mineable with pickaxe)
 */
package com.createcannons.data;

import com.createcannons.CreateCannons;
import com.createcannons.registry.CCBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * Generates block tag files.
 * 
 * Adds blocks to various tags for game mechanics integration.
 */
public class CCBlockTagsProvider extends BlockTagsProvider {
    
    /**
     * Creates a new block tags provider.
     * 
     * @param output The pack output
     * @param lookupProvider The registry lookup provider
     * @param existingFileHelper The existing file helper
     */
    public CCBlockTagsProvider(PackOutput output, 
            CompletableFuture<HolderLookup.Provider> lookupProvider,
            @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, CreateCannons.MOD_ID, existingFileHelper);
    }
    
    /**
     * Adds all block tags.
     */
    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // All cannon blocks require a pickaxe to mine
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(CCBlocks.KINETIC_CANNON.get())
                .add(CCBlocks.CANNON_BARREL.get())
                .add(CCBlocks.CANNON_BREECH.get());
        
        // Require iron tool or better
        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(CCBlocks.KINETIC_CANNON.get())
                .add(CCBlocks.CANNON_BARREL.get())
                .add(CCBlocks.CANNON_BREECH.get());
    }
}
