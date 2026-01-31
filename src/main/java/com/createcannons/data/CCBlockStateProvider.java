/**
 * Block State Provider - Generates blockstate and block model JSON files.
 * 
 * Creates the necessary files for block rendering:
 * - Blockstate JSON (maps block states to models)
 * - Block model JSON (defines the visual appearance)
 */
package com.createcannons.data;

import com.createcannons.CreateCannons;
import com.createcannons.registry.CCBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

/**
 * Generates block state and block model files.
 * 
 * This provider creates the JSON files that Minecraft uses to
 * determine how blocks should be rendered based on their state.
 */
public class CCBlockStateProvider extends BlockStateProvider {
    
    /**
     * Creates a new block state provider.
     * 
     * @param output The pack output
     * @param exFileHelper The existing file helper
     */
    public CCBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, CreateCannons.MOD_ID, exFileHelper);
    }
    
    /**
     * Registers all block state definitions.
     */
    @Override
    protected void registerStatesAndModels() {
        // Simple cube blocks for cannon parts
        simpleBlockWithItem(CCBlocks.CANNON_BARREL);
        simpleBlockWithItem(CCBlocks.CANNON_BREECH);
        
        // Ore blocks
        simpleBlockWithItem(CCBlocks.CANNONITE_ORE);
        simpleBlockWithItem(CCBlocks.DEEPSLATE_CANNONITE_ORE);
        simpleBlockWithItem(CCBlocks.CANNONITE_BLOCK);
        simpleBlockWithItem(CCBlocks.RAW_CANNONITE_BLOCK);
        
        // Kinetic Cannon - directional block
        // Uses a directional model that rotates based on facing
        directionalBlock(CCBlocks.KINETIC_CANNON.get(), 
                models().cubeAll("kinetic_cannon", 
                        modLoc("block/kinetic_cannon")));
        simpleBlockItem(CCBlocks.KINETIC_CANNON.get(), 
                models().getExistingFile(modLoc("block/kinetic_cannon")));
    }
    
    /**
     * Helper method to create a simple block with its item model.
     * 
     * @param block The deferred block
     */
    private void simpleBlockWithItem(DeferredBlock<? extends Block> block) {
        simpleBlockWithItem(block.get(), cubeAll(block.get()));
    }
}
