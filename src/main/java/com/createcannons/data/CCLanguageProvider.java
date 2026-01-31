/**
 * Language Provider - Generates translation files for the mod.
 * 
 * Creates en_us.json with all translatable strings including:
 * - Block names
 * - Item names
 * - Creative tab names
 * - Tooltips
 * - GUI text
 * - Messages
 */
package com.createcannons.data;

import com.createcannons.CreateCannons;
import com.createcannons.registry.CCBlocks;
import com.createcannons.registry.CCItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

/**
 * Generates language translation files.
 * 
 * All translatable strings in the mod should be added here to ensure
 * they have proper translations. Additional languages can be supported
 * by creating additional providers with different locale codes.
 */
public class CCLanguageProvider extends LanguageProvider {
    
    /**
     * Creates a new language provider.
     * 
     * @param output The pack output for generated files
     * @param locale The locale code (e.g., "en_us")
     */
    public CCLanguageProvider(PackOutput output, String locale) {
        super(output, CreateCannons.MOD_ID, locale);
    }
    
    /**
     * Adds all translations.
     */
    @Override
    protected void addTranslations() {
        // ==================== CREATIVE TAB ====================
        add("itemGroup." + CreateCannons.MOD_ID + ".main", "Create Cannons");
        
        // ==================== BLOCKS ====================
        addBlock(CCBlocks.KINETIC_CANNON, "Kinetic Cannon");
        addBlock(CCBlocks.CANNON_BARREL, "Cannon Barrel");
        addBlock(CCBlocks.CANNON_BREECH, "Cannon Breech");
        
        // ==================== ITEMS ====================
        // Block items are handled automatically by addBlock
        
        // Projectiles
        addItem(CCItems.IRON_CANNONBALL, "Iron Cannonball");
        addItem(CCItems.STEEL_CANNONBALL, "Steel Cannonball");
        addItem(CCItems.EXPLOSIVE_SHELL, "Explosive Shell");
        
        // Components
        addItem(CCItems.CANNON_CASTING, "Cannon Casting");
        addItem(CCItems.INCOMPLETE_CANNON_BARREL, "Incomplete Cannon Barrel");
        addItem(CCItems.GUNPOWDER_CHARGE, "Gunpowder Charge");
        
        // ==================== TOOLTIPS ====================
        add("tooltip.createcannons.damage", "Damage: %s");
        add("tooltip.createcannons.explosion_radius", "Explosion Radius: %s");
        add("tooltip.createcannons.cannonball_usage", "Load into a Kinetic Cannon to fire");
        add("tooltip.createcannons.stress_impact", "Stress Impact: %s SU");
        add("tooltip.createcannons.requires_rotation", "Requires Rotational Force");
        
        // ==================== MESSAGES ====================
        add("message.createcannons.projectile_loaded", "Projectile loaded");
        add("message.createcannons.projectile_already_loaded", "Projectile already loaded");
        add("message.createcannons.charge_loaded", "Charge loaded");
        add("message.createcannons.charge_already_loaded", "Charge already loaded");
        add("message.createcannons.no_projectile", "No projectile loaded");
        add("message.createcannons.no_charge", "No charge loaded");
        add("message.createcannons.insufficient_stress", "Insufficient rotational force");
        add("message.createcannons.cooldown", "Cooling down... %.1fs");
        add("message.createcannons.fired", "FIRE!");
        
        // ==================== GUI ====================
        add("gui.createcannons.kinetic_cannon.title", "Kinetic Cannon");
        add("gui.createcannons.kinetic_cannon.loaded", "Loaded: %s");
        add("gui.createcannons.kinetic_cannon.not_loaded", "Not Loaded");
        add("gui.createcannons.kinetic_cannon.cooldown", "Cooldown: %s");
        add("gui.createcannons.kinetic_cannon.ready", "Ready to Fire");
        add("gui.createcannons.kinetic_cannon.stress", "Stress: %s / %s SU");
        
        // ==================== ENTITY ====================
        add("entity.createcannons.cannonball", "Cannonball");
        
        // ==================== ADVANCEMENT (FUTURE) ====================
        add("advancement.createcannons.root.title", "Create Cannons");
        add("advancement.createcannons.root.description", "Build powerful siege weapons");
        add("advancement.createcannons.first_cannon.title", "Armed and Dangerous");
        add("advancement.createcannons.first_cannon.description", "Craft your first Kinetic Cannon");
        add("advancement.createcannons.first_fire.title", "BOOM!");
        add("advancement.createcannons.first_fire.description", "Fire a cannon for the first time");
        
        // ==================== JEI INTEGRATION (FUTURE) ====================
        add("createcannons.jei.cannon_loading", "Cannon Loading");
        add("createcannons.jei.cannon_firing", "Cannon Firing");
        
        // ==================== CONFIG (FUTURE) ====================
        add("config.createcannons.stress.cannon_impact", "Kinetic Cannon Stress Impact");
        add("config.createcannons.stress.barrel_impact", "Cannon Barrel Stress Impact");
        add("config.createcannons.cannon.cooldown", "Cannon Fire Cooldown (ticks)");
        add("config.createcannons.cannon.base_velocity", "Base Projectile Velocity");
    }
}
