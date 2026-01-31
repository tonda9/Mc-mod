/**
 * Item Model Provider - Generates item model JSON files.
 * 
 * Creates item model files that define how items appear in:
 * - Inventory
 * - Hand (first and third person)
 * - Ground (dropped items)
 * - Item frames
 */
package com.createcannons.data;

import com.createcannons.CreateCannons;
import com.createcannons.registry.CCItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

/**
 * Generates item model files.
 * 
 * Most items use simple 2D textures, while some may use
 * more complex 3D models (like block items).
 */
public class CCItemModelProvider extends ItemModelProvider {
    
    /**
     * Creates a new item model provider.
     * 
     * @param output The pack output
     * @param existingFileHelper The existing file helper
     */
    public CCItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, CreateCannons.MOD_ID, existingFileHelper);
    }
    
    /**
     * Registers all item models.
     */
    @Override
    protected void registerModels() {
        // Projectile items - simple 2D generated textures
        simpleItem(CCItems.IRON_CANNONBALL);
        simpleItem(CCItems.STEEL_CANNONBALL);
        simpleItem(CCItems.EXPLOSIVE_SHELL);
        
        // Component items
        simpleItem(CCItems.CANNON_CASTING);
        simpleItem(CCItems.INCOMPLETE_CANNON_BARREL);
        simpleItem(CCItems.GUNPOWDER_CHARGE);
        
        // Block items are handled by BlockStateProvider
    }
    
    /**
     * Creates a simple item model using the standard item/generated parent.
     * 
     * @param item The deferred item
     */
    private void simpleItem(DeferredItem<? extends Item> item) {
        withExistingParent(item.getId().getPath(),
                ResourceLocation.withDefaultNamespace("item/generated"))
                .texture("layer0", modLoc("item/" + item.getId().getPath()));
    }
}
