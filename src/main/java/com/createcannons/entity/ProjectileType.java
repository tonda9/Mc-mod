/**
 * Projectile Type - Defines different behaviors for cannon projectiles.
 * 
 * Each projectile type has unique physics characteristics that affect:
 * - Gravity influence
 * - Air resistance
 * - Special effects on impact
 * - Penetration capabilities
 * - Trail particles
 * - Stress cost multipliers
 * - Mass-based velocity calculations
 * 
 * Balance Notes:
 * - Standard/Heavy: Early game, low cost, simple physics
 * - Rocket/Incendiary: Mid game, moderate stress, special effects
 * - High-Yield/Fragmentation: Late game, high stress, powerful effects
 * - Nova: End game, extreme stress, devastating power
 */
package com.createcannons.entity;

/**
 * Enum defining different projectile behaviors.
 * 
 * Each type has specific physics modifiers and impact effects
 * that make it suitable for different combat situations.
 * 
 * Physics Parameters (all configurable):
 * - gravityMultiplier: How much gravity affects trajectory (1.0 = normal)
 * - airResistanceMultiplier: How much drag slows the projectile (1.0 = normal)
 * - mass: Projectile mass in kg, affects momentum and penetration
 * - stressCostMultiplier: How much extra stress is consumed when firing
 */
public enum ProjectileType {
    
    // ==================== BASIC AMMUNITION ====================
    
    /**
     * Standard cannonball - basic ballistic trajectory.
     * Normal gravity and air resistance.
     * Tier: Early game
     * Cost: Low stress, easy to craft
     */
    STANDARD(1.0, 1.0, 5.0, 1.0, false, false, false, false, false, false),
    
    /**
     * Heavy shot - affected more by gravity.
     * High mass means less air resistance effect but more drop.
     * Tier: Early game
     * Cost: Low stress, iron-based
     */
    HEAVY(1.3, 0.7, 12.0, 1.2, false, false, false, false, false, false),
    
    // ==================== MID-TIER AMMUNITION ====================
    
    /**
     * Rocket propelled - reduced gravity effect.
     * Self-propelled, maintains velocity better over distance.
     * Tier: Mid game (requires blaze powder)
     * Cost: Moderate stress
     */
    ROCKET(0.3, 0.5, 4.0, 1.5, false, false, true, false, false, false),
    
    /**
     * Armor piercing - can penetrate blocks.
     * Minimal explosion, focused kinetic damage.
     * Tier: Mid game
     * Cost: Moderate stress, diamond required
     */
    ARMOR_PIERCING(1.0, 0.8, 15.0, 1.3, true, false, false, false, false, false),
    
    /**
     * Incendiary - sets fires on impact.
     * Standard physics with fire effect.
     * Tier: Mid game (requires Nether materials)
     * Cost: Moderate stress
     */
    INCENDIARY(1.0, 1.0, 6.0, 1.2, false, true, false, false, false, false),
    
    /**
     * Smoke - creates smoke screen.
     * Very light, no damage.
     * Tier: Early-Mid game
     * Cost: Very low stress
     */
    SMOKE(0.8, 1.2, 2.0, 0.5, false, false, false, false, false, false),
    
    /**
     * Grapeshot - multiple small projectiles.
     * Spreads immediately on firing.
     * Tier: Early game
     * Cost: Low stress
     */
    GRAPESHOT(1.2, 1.5, 0.5, 0.8, false, false, false, false, false, false),
    
    // ==================== ADVANCED AMMUNITION ====================
    
    /**
     * Cluster - splits into multiple projectiles.
     * Lighter, splits at apex of trajectory.
     * Tier: Mid-Late game
     * Cost: High stress
     */
    CLUSTER(0.9, 1.1, 8.0, 1.8, false, false, false, false, false, false),
    
    /**
     * High-Yield Shell - renamed from Nuclear, massive explosion.
     * Warhammer-style "Nova" themed ammunition.
     * Tier: Late game (requires enriched cannonite)
     * Cost: Very high stress
     */
    HIGH_YIELD(1.1, 0.9, 20.0, 3.0, false, false, false, true, false, false),
    
    /**
     * Rocket-Assisted Shell - accelerates mid-flight.
     * Combines initial launch with sustained thrust.
     * Tier: Late game (requires siegite)
     * Cost: High stress
     */
    ROCKET_ASSISTED(0.4, 0.4, 10.0, 2.5, false, false, true, false, true, false),
    
    /**
     * Fragmentation Shell - splits into shrapnel on impact.
     * Each fragment causes smaller explosions.
     * Tier: Late game (requires siegite)
     * Cost: High stress
     */
    FRAGMENTATION(0.95, 1.0, 12.0, 2.2, false, false, false, false, false, true),
    
    // ==================== END-GAME AMMUNITION ====================
    
    /**
     * Nova Cannon Shell - ultimate destructive power.
     * Massive, slow-moving projectile with devastating AoE.
     * Only usable in Nova Cannon multiblock.
     * Tier: End game (requires Nova Cannon, siegite, enriched materials)
     * Cost: Extreme stress (512+ SU)
     */
    NOVA(1.4, 0.6, 50.0, 5.0, false, false, false, true, false, false);
    
    // ==================== FIELDS ====================
    
    /**
     * Multiplier for gravity effect.
     * > 1.0 = more gravity (drops faster)
     * < 1.0 = less gravity (flies further)
     */
    private final double gravityMultiplier;
    
    /**
     * Multiplier for air resistance.
     * > 1.0 = more drag (slows faster)
     * < 1.0 = less drag (maintains speed)
     */
    private final double airResistanceMultiplier;
    
    /**
     * Mass of the projectile in kg.
     * Affects momentum transfer, penetration depth, and velocity loss.
     */
    private final double mass;
    
    /**
     * Multiplier for stress cost when firing this projectile.
     * Base stress is multiplied by this value.
     */
    private final double stressCostMultiplier;
    
    /**
     * Whether this projectile can penetrate through weak blocks.
     */
    private final boolean canPenetrate;
    
    /**
     * Whether this projectile sets fire on impact.
     */
    private final boolean causesFire;
    
    /**
     * Whether this projectile has a rocket trail effect.
     */
    private final boolean hasRocketTrail;
    
    /**
     * Whether this projectile creates a high-yield (nuclear-style) explosion.
     */
    private final boolean isHighYield;
    
    /**
     * Whether this projectile accelerates mid-flight (rocket-assisted).
     */
    private final boolean hasThrust;
    
    /**
     * Whether this projectile fragments into shrapnel on impact.
     */
    private final boolean isFragmentation;
    
    // ==================== CONSTRUCTOR ====================
    
    /**
     * Constructor for projectile types with full physics configuration.
     * 
     * @param gravityMultiplier Gravity effect multiplier (1.0 = normal)
     * @param airResistanceMultiplier Air resistance multiplier (1.0 = normal)
     * @param mass Projectile mass in kg
     * @param stressCostMultiplier Stress cost multiplier for firing
     * @param canPenetrate Whether can penetrate blocks
     * @param causesFire Whether causes fire on impact
     * @param hasRocketTrail Whether has rocket trail effect
     * @param isHighYield Whether creates high-yield explosion
     * @param hasThrust Whether accelerates mid-flight
     * @param isFragmentation Whether splits into fragments
     */
    ProjectileType(double gravityMultiplier, double airResistanceMultiplier,
                   double mass, double stressCostMultiplier,
                   boolean canPenetrate, boolean causesFire, 
                   boolean hasRocketTrail, boolean isHighYield,
                   boolean hasThrust, boolean isFragmentation) {
        this.gravityMultiplier = gravityMultiplier;
        this.airResistanceMultiplier = airResistanceMultiplier;
        this.mass = mass;
        this.stressCostMultiplier = stressCostMultiplier;
        this.canPenetrate = canPenetrate;
        this.causesFire = causesFire;
        this.hasRocketTrail = hasRocketTrail;
        this.isHighYield = isHighYield;
        this.hasThrust = hasThrust;
        this.isFragmentation = isFragmentation;
    }
    
    // ==================== GETTERS ====================
    
    public double getGravityMultiplier() {
        return gravityMultiplier;
    }
    
    public double getAirResistanceMultiplier() {
        return airResistanceMultiplier;
    }
    
    public double getMass() {
        return mass;
    }
    
    public double getStressCostMultiplier() {
        return stressCostMultiplier;
    }
    
    public boolean canPenetrate() {
        return canPenetrate;
    }
    
    public boolean causesFire() {
        return causesFire;
    }
    
    public boolean hasRocketTrail() {
        return hasRocketTrail;
    }
    
    /**
     * @deprecated Use isHighYield() instead. This method was renamed to avoid
     * real-world nuclear terminology per design guidelines.
     */
    @Deprecated(since = "1.0.0", forRemoval = true)
    public boolean isNuclear() {
        return isHighYield;
    }
    
    public boolean isHighYield() {
        return isHighYield;
    }
    
    public boolean hasThrust() {
        return hasThrust;
    }
    
    public boolean isFragmentation() {
        return isFragmentation;
    }
    
    // ==================== PHYSICS CALCULATIONS ====================
    
    /**
     * Calculates the velocity loss due to air resistance for this projectile type.
     * Heavier projectiles lose less velocity.
     * 
     * @param currentVelocity Current velocity magnitude
     * @return Velocity after air resistance is applied
     */
    public double calculateVelocityAfterDrag(double currentVelocity) {
        // Drag coefficient inversely proportional to mass
        double dragCoefficient = 0.01 * airResistanceMultiplier / Math.sqrt(mass);
        return currentVelocity * (1.0 - dragCoefficient);
    }
    
    /**
     * Calculates the thrust acceleration for rocket-assisted projectiles.
     * 
     * @param ticksAlive How long the projectile has been flying
     * @return Thrust acceleration in blocks/tick²
     */
    public double calculateThrust(int ticksAlive) {
        if (!hasThrust) {
            return 0.0;
        }
        
        // Thrust active for first 60 ticks (3 seconds), then fuel exhausted
        if (ticksAlive < 60) {
            // Rocket-assisted shells accelerate more gradually
            return hasRocketTrail ? 0.015 : 0.025;
        }
        return 0.0;
    }
    
    /**
     * Calculates the number of fragments for fragmentation shells.
     * 
     * @return Number of fragments to spawn on impact
     */
    public int getFragmentCount() {
        if (this == CLUSTER) {
            return 8;
        }
        if (isFragmentation) {
            return 12;
        }
        return 0;
    }
    
    /**
     * Gets the explosion radius multiplier for high-yield shells.
     * Nova shells have the largest explosion.
     * 
     * @return Explosion radius multiplier
     */
    public float getExplosionMultiplier() {
        if (this == NOVA) {
            return 2.5f;
        }
        if (isHighYield) {
            return 1.5f;
        }
        return 1.0f;
    }
    
    /**
     * Checks if this projectile can ricochet off hard surfaces.
     * Only applies to armor-piercing rounds at shallow angles.
     * 
     * @return True if ricochet is possible
     */
    public boolean canRicochet() {
        return canPenetrate && mass >= 10.0;
    }
}
