/**
 * Cannonball Item - Ammunition for kinetic cannons.
 * 
 * This item represents cannonball ammunition that can be loaded into
 * cannons. Different types of cannonballs have different damage values
 * and explosion radii, affecting the outcome when fired.
 */
package com.createcannons.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/**
 * Item class for cannonball ammunition.
 * 
 * Stores damage and explosion radius values that are used when the
 * cannonball is fired from a cannon. These values determine the
 * impact of the projectile.
 */
public class CannonballItem extends Item {
    
    /**
     * The base damage this cannonball deals on impact.
     * Actual damage may be modified by velocity.
     */
    private final float damage;
    
    /**
     * The explosion radius when this cannonball impacts.
     * Larger radius = more area damage and block destruction.
     */
    private final float explosionRadius;
    
    /**
     * Creates a new cannonball item with specified properties.
     * 
     * @param properties Item properties (stack size, etc.)
     * @param damage Base damage on impact
     * @param explosionRadius Explosion radius on impact
     */
    public CannonballItem(Properties properties, float damage, float explosionRadius) {
        super(properties);
        this.damage = damage;
        this.explosionRadius = explosionRadius;
    }
    
    /**
     * Gets the damage value for this cannonball.
     * 
     * @return The base damage value
     */
    public float getDamage() {
        return damage;
    }
    
    /**
     * Gets the explosion radius for this cannonball.
     * 
     * @return The explosion radius
     */
    public float getExplosionRadius() {
        return explosionRadius;
    }
    
    /**
     * Adds tooltip information to the item.
     * Shows the damage and explosion radius stats.
     * 
     * @param stack The item stack
     * @param context The tooltip context
     * @param tooltipComponents The list to add tooltip lines to
     * @param tooltipFlag Tooltip flag for advanced info
     */
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, 
            List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        
        // Add damage stat
        tooltipComponents.add(Component.translatable("tooltip.createcannons.damage", 
                String.format("%.1f", damage))
                .withStyle(ChatFormatting.RED));
        
        // Add explosion radius stat
        tooltipComponents.add(Component.translatable("tooltip.createcannons.explosion_radius", 
                String.format("%.1f", explosionRadius))
                .withStyle(ChatFormatting.GOLD));
        
        // Add usage instructions
        tooltipComponents.add(Component.translatable("tooltip.createcannons.cannonball_usage")
                .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
    }
}
