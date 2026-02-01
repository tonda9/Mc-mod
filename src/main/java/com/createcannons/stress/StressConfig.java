/**
 * Stress Configuration - Defines stress values for Create integration.
 * 
 * This class centralizes all stress-related configuration for the mod,
 * making it easy to tune the kinetic requirements for cannon operation.
 * 
 * In Create, stress is the measure of mechanical load on a kinetic network.
 * Machines consume stress when operating, and sources (like waterwheels)
 * provide stress capacity. The network fails if consumption exceeds capacity.
 * 
 * Balance Notes:
 * - Kinetic Cannon: 256 SU base (mid-game requirement)
 * - Nova Cannon: 512 SU base (end-game requirement)
 * - Projectile multipliers scale stress cost by ammo type
 * - Heavy barrel segments add incremental stress cost
 */
package com.createcannons.stress;

/**
 * Configuration values for stress impact and capacity.
 * 
 * These values define how cannons interact with Create's kinetic system:
 * - Stress Impact: How much stress the cannon consumes when operating
 * - Stress Capacity: How much stress a block can provide (if any)
 * 
 * Values are measured in Stress Units (SU) and are multiplied by
 * rotation speed (RPM) to get the actual network impact.
 */
public final class StressConfig {
    
    // ==================== KINETIC CANNON STRESS VALUES ====================
    
    /**
     * Stress impact of the Kinetic Cannon per RPM.
     * 
     * At 256 SU/RPM, a cannon running at 32 RPM would consume:
     * 256 * 32 = 8,192 SU
     * 
     * This makes cannons a significant stress consumer, requiring
     * substantial infrastructure to power multiple cannons.
     */
    public static final float CANNON_STRESS_IMPACT = 256.0f;
    
    /**
     * Stress impact per barrel extension.
     * 
     * Each additional barrel block adds to the stress cost,
     * representing the increased mechanical complexity.
     */
    public static final float BARREL_STRESS_IMPACT = 32.0f;
    
    /**
     * Stress multiplier for firing.
     * 
     * When actually firing, the cannon temporarily consumes
     * additional stress. This is applied as a burst on firing.
     */
    public static final float FIRING_STRESS_MULTIPLIER = 2.0f;
    
    // ==================== NOVA CANNON STRESS VALUES ====================
    
    /**
     * Base stress impact for the Nova Cannon multiblock.
     * 
     * At 512 SU/RPM, a Nova Cannon at 32 RPM would consume:
     * 512 * 32 = 16,384 SU (base, without barrels)
     * 
     * This is double the standard cannon, reflecting its end-game status.
     */
    public static final float NOVA_CANNON_STRESS_IMPACT = 512.0f;
    
    /**
     * Stress impact per heavy barrel segment.
     * 
     * Each heavy barrel segment adds to the Nova Cannon's stress cost.
     * These are more expensive than standard barrels.
     */
    public static final float HEAVY_BARREL_STRESS_IMPACT = 64.0f;
    
    /**
     * Stress impact per Nova Cannon frame block.
     * 
     * Each frame adds structural support but also stress cost.
     */
    public static final float NOVA_FRAME_STRESS_IMPACT = 16.0f;
    
    /**
     * Stress multiplier for firing Nova Cannon.
     * 
     * Nova Cannon firing creates a massive stress spike.
     */
    public static final float NOVA_FIRING_STRESS_MULTIPLIER = 3.0f;
    
    // ==================== MINIMUM SPEED REQUIREMENTS ====================
    
    /**
     * Minimum rotation speed (RPM) required to operate the cannon.
     * Below this speed, the cannon cannot charge or fire.
     */
    public static final float MIN_OPERATING_SPEED = 4.0f;
    
    /**
     * Optimal rotation speed for maximum efficiency.
     * Operating at this speed provides the best fire rate.
     */
    public static final float OPTIMAL_SPEED = 32.0f;
    
    /**
     * Maximum useful rotation speed.
     * Above this, no additional benefits are gained.
     */
    public static final float MAX_SPEED = 256.0f;
    
    /**
     * Minimum rotation speed for Nova Cannon.
     * Higher than standard due to its power requirements.
     */
    public static final float NOVA_MIN_OPERATING_SPEED = 16.0f;
    
    // ==================== PROJECTILE STRESS MULTIPLIERS ====================
    
    /**
     * Stress multiplier for standard projectiles (iron cannonball, etc.)
     */
    public static final float STANDARD_PROJECTILE_MULTIPLIER = 1.0f;
    
    /**
     * Stress multiplier for heavy projectiles (steel cannonball, etc.)
     */
    public static final float HEAVY_PROJECTILE_MULTIPLIER = 1.2f;
    
    /**
     * Stress multiplier for advanced projectiles (rocket-assisted, etc.)
     */
    public static final float ADVANCED_PROJECTILE_MULTIPLIER = 2.0f;
    
    /**
     * Stress multiplier for high-yield shells.
     */
    public static final float HIGH_YIELD_PROJECTILE_MULTIPLIER = 3.0f;
    
    /**
     * Stress multiplier for Nova shells.
     */
    public static final float NOVA_PROJECTILE_MULTIPLIER = 5.0f;
    
    // ==================== CALCULATIONS ====================
    
    /**
     * Calculates the total stress impact for a cannon setup.
     * 
     * @param barrelCount Number of barrel extensions
     * @param speed Current rotation speed
     * @return Total stress impact in SU
     */
    public static float calculateTotalStressImpact(int barrelCount, float speed) {
        float baseImpact = CANNON_STRESS_IMPACT;
        float barrelImpact = BARREL_STRESS_IMPACT * barrelCount;
        return (baseImpact + barrelImpact) * Math.abs(speed);
    }
    
    /**
     * Calculates the total stress impact for a Nova Cannon setup.
     * 
     * @param barrelCount Number of heavy barrel segments
     * @param frameCount Number of frame blocks
     * @param speed Current rotation speed
     * @return Total stress impact in SU
     */
    public static float calculateNovaStressImpact(int barrelCount, int frameCount, float speed) {
        float baseImpact = NOVA_CANNON_STRESS_IMPACT;
        float barrelImpact = HEAVY_BARREL_STRESS_IMPACT * barrelCount;
        float frameImpact = NOVA_FRAME_STRESS_IMPACT * frameCount;
        return (baseImpact + barrelImpact + frameImpact) * Math.abs(speed);
    }
    
    /**
     * Calculates the firing stress burst.
     * 
     * @param baseImpact The current operating stress
     * @return The temporary stress spike when firing
     */
    public static float calculateFiringStress(float baseImpact) {
        return baseImpact * FIRING_STRESS_MULTIPLIER;
    }
    
    /**
     * Calculates the firing stress burst for Nova Cannon.
     * 
     * @param baseImpact The current operating stress
     * @return The temporary stress spike when firing Nova Cannon
     */
    public static float calculateNovaFiringStress(float baseImpact) {
        return baseImpact * NOVA_FIRING_STRESS_MULTIPLIER;
    }
    
    /**
     * Calculates the stress cost for a specific projectile type.
     * 
     * @param baseStress The cannon's base stress
     * @param projectileMultiplier The projectile type's stress multiplier
     * @return Total stress cost for firing
     */
    public static float calculateProjectileStress(float baseStress, double projectileMultiplier) {
        return (float) (baseStress * projectileMultiplier);
    }
    
    /**
     * Checks if the given speed is sufficient for operation.
     * 
     * @param speed The current rotation speed
     * @return True if the cannon can operate
     */
    public static boolean isSpeedSufficient(float speed) {
        return Math.abs(speed) >= MIN_OPERATING_SPEED;
    }
    
    /**
     * Checks if the given speed is sufficient for Nova Cannon operation.
     * 
     * @param speed The current rotation speed
     * @return True if the Nova Cannon can operate
     */
    public static boolean isNovaSpeedSufficient(float speed) {
        return Math.abs(speed) >= NOVA_MIN_OPERATING_SPEED;
    }
    
    /**
     * Calculates the efficiency multiplier based on speed.
     * Higher speed = faster cooldown, up to the optimal point.
     * 
     * @param speed Current rotation speed
     * @return Efficiency multiplier (0.0 to 1.0)
     */
    public static float calculateEfficiency(float speed) {
        float absSpeed = Math.abs(speed);
        
        if (absSpeed < MIN_OPERATING_SPEED) {
            return 0.0f;
        }
        
        if (absSpeed >= OPTIMAL_SPEED) {
            return 1.0f;
        }
        
        // Linear interpolation between min and optimal
        return (absSpeed - MIN_OPERATING_SPEED) / (OPTIMAL_SPEED - MIN_OPERATING_SPEED);
    }
    
    // Private constructor to prevent instantiation
    private StressConfig() {
        throw new UnsupportedOperationException("Utility class");
    }
}
