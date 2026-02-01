/**
 * Advanced Cannonball Item - Extended ammunition with projectile type.
 * 
 * This item extends CannonballItem to include a specific projectile type,
 * enabling different physics behaviors and special effects when fired.
 * Used for advanced ammunition like High-Yield Shells, Rocket-Assisted
 * Shells, and Fragmentation Shells.
 * 
 * Design Notes:
 * - Each advanced ammo type has a fixed ProjectileType
 * - Tooltips display type-specific information
 * - Stress cost is calculated from projectile type
 */
package com.createcannons.item;

import com.createcannons.entity.ProjectileType;
import com.createcannons.stress.StressConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/**
 * Extended cannonball item with projectile type for advanced ammunition.
 * 
 * This class stores the ProjectileType alongside damage and explosion values,
 * allowing the cannon to configure the projectile entity correctly when fired.
 */
public class AdvancedCannonballItem extends CannonballItem {
    
    /**
     * The type of projectile this ammunition creates when fired.
     */
    private final ProjectileType projectileType;
    
    /**
     * Creates a new advanced cannonball item.
     * 
     * @param properties Item properties (stack size, rarity, etc.)
     * @param damage Base damage on impact
     * @param explosionRadius Explosion radius on impact
     * @param projectileType The projectile type for physics behavior
     */
    public AdvancedCannonballItem(Properties properties, float damage, float explosionRadius, 
                                   ProjectileType projectileType) {
        super(properties, damage, explosionRadius);
        this.projectileType = projectileType;
    }
    
    /**
     * Gets the projectile type for this ammunition.
     * 
     * @return The projectile type
     */
    public ProjectileType getProjectileType() {
        return projectileType;
    }
    
    /**
     * Gets the stress cost multiplier for firing this ammunition.
     * Derived from the projectile type.
     * 
     * @return The stress cost multiplier
     */
    public double getStressCostMultiplier() {
        return projectileType.getStressCostMultiplier();
    }
    
    /**
     * Calculates the total stress cost for firing this projectile.
     * 
     * @return Total stress impact in SU
     */
    public float getTotalStressCost() {
        return (float) (StressConfig.CANNON_STRESS_IMPACT * projectileType.getStressCostMultiplier());
    }
    
    /**
     * Adds detailed tooltip information for advanced ammunition.
     * 
     * @param stack The item stack
     * @param context The tooltip context
     * @param tooltipComponents The list to add tooltip lines to
     * @param tooltipFlag Tooltip flag for advanced info
     */
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, 
            List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        // Add base stats from parent
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        
        // Add projectile type indicator
        tooltipComponents.add(Component.translatable("tooltip.createcannons.projectile_type", 
                getProjectileTypeName())
                .withStyle(ChatFormatting.AQUA));
        
        // Add stress cost
        tooltipComponents.add(Component.translatable("tooltip.createcannons.stress_impact", 
                String.format("%.0f", getTotalStressCost()))
                .withStyle(ChatFormatting.YELLOW));
        
        // Add type-specific tooltips
        addTypeSpecificTooltips(tooltipComponents);
    }
    
    /**
     * Gets a human-readable name for the projectile type.
     * 
     * @return Localized type name
     */
    private String getProjectileTypeName() {
        return switch (projectileType) {
            case HIGH_YIELD -> "High-Yield";
            case ROCKET_ASSISTED -> "Rocket-Assisted";
            case FRAGMENTATION -> "Fragmentation";
            case NOVA -> "Nova";
            case ROCKET -> "Rocket";
            case ARMOR_PIERCING -> "Armor Piercing";
            case INCENDIARY -> "Incendiary";
            case CLUSTER -> "Cluster";
            case SMOKE -> "Smoke";
            case GRAPESHOT -> "Grapeshot";
            default -> "Standard";
        };
    }
    
    /**
     * Adds tooltips specific to the projectile type.
     * 
     * @param tooltipComponents The list to add tooltip lines to
     */
    private void addTypeSpecificTooltips(List<Component> tooltipComponents) {
        switch (projectileType) {
            case HIGH_YIELD:
                tooltipComponents.add(Component.translatable("tooltip.createcannons.high_yield_warning")
                        .withStyle(ChatFormatting.DARK_RED, ChatFormatting.BOLD));
                tooltipComponents.add(Component.translatable("tooltip.createcannons.high_yield_effect")
                        .withStyle(ChatFormatting.GRAY));
                break;
                
            case ROCKET_ASSISTED:
                tooltipComponents.add(Component.translatable("tooltip.createcannons.rocket_assisted_boost")
                        .withStyle(ChatFormatting.GREEN));
                tooltipComponents.add(Component.translatable("tooltip.createcannons.rocket_assisted_range")
                        .withStyle(ChatFormatting.GRAY));
                break;
                
            case FRAGMENTATION:
                tooltipComponents.add(Component.translatable("tooltip.createcannons.fragmentation_split",
                        projectileType.getFragmentCount())
                        .withStyle(ChatFormatting.LIGHT_PURPLE));
                tooltipComponents.add(Component.translatable("tooltip.createcannons.fragmentation_spread")
                        .withStyle(ChatFormatting.GRAY));
                break;
                
            case NOVA:
                tooltipComponents.add(Component.translatable("tooltip.createcannons.nova_power")
                        .withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.BOLD));
                tooltipComponents.add(Component.translatable("tooltip.createcannons.nova_requires")
                        .withStyle(ChatFormatting.RED));
                break;
                
            default:
                // No additional tooltips for other types
                break;
        }
    }
}
