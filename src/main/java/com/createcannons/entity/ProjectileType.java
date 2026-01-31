/**
 * Projectile Type - Defines different behaviors for cannon projectiles.
 * 
 * Each projectile type has unique physics characteristics that affect:
 * - Gravity influence
 * - Air resistance
 * - Special effects on impact
 * - Penetration capabilities
 * - Trail particles
 */
package com.createcannons.entity;

/**
 * Enum defining different projectile behaviors.
 * 
 * Each type has specific physics modifiers and impact effects
 * that make it suitable for different combat situations.
 */
public enum ProjectileType {
    
    /**
     * Standard cannonball - basic ballistic trajectory.
     * Normal gravity and air resistance.
     */
    STANDARD(1.0, 1.0, false, false, false, false),
    
    /**
     * Heavy shot - affected more by gravity.
     * High mass means less air resistance effect but more drop.
     */
    HEAVY(1.3, 0.7, false, false, false, false),
    
    /**
     * Rocket propelled - reduced gravity effect.
     * Self-propelled, maintains velocity better over distance.
     */
    ROCKET(0.3, 0.5, false, false, true, false),
    
    /**
     * Armor piercing - can penetrate blocks.
     * Minimal explosion, focused kinetic damage.
     */
    ARMOR_PIERCING(1.0, 0.8, true, false, false, false),
    
    /**
     * Incendiary - sets fires on impact.
     * Standard physics with fire effect.
     */
    INCENDIARY(1.0, 1.0, false, true, false, false),
    
    /**
     * Nuclear - massive explosion.
     * Slightly heavier, creates devastating blast.
     */
    NUCLEAR(1.1, 0.9, false, false, false, true),
    
    /**
     * Cluster - splits into multiple projectiles.
     * Lighter, splits at apex of trajectory.
     */
    CLUSTER(0.9, 1.1, false, false, false, false),
    
    /**
     * Smoke - creates smoke screen.
     * Very light, no damage.
     */
    SMOKE(0.8, 1.2, false, false, false, false),
    
    /**
     * Grapeshot - multiple small projectiles.
     * Spreads immediately on firing.
     */
    GRAPESHOT(1.2, 1.5, false, false, false, false);
    
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
     * Whether this projectile creates a nuclear explosion.
     */
    private final boolean isNuclear;
    
    /**
     * Constructor for projectile types.
     * 
     * @param gravityMultiplier Gravity effect multiplier
     * @param airResistanceMultiplier Air resistance multiplier
     * @param canPenetrate Whether can penetrate blocks
     * @param causesFire Whether causes fire on impact
     * @param hasRocketTrail Whether has rocket trail effect
     * @param isNuclear Whether creates nuclear explosion
     */
    ProjectileType(double gravityMultiplier, double airResistanceMultiplier,
                   boolean canPenetrate, boolean causesFire, 
                   boolean hasRocketTrail, boolean isNuclear) {
        this.gravityMultiplier = gravityMultiplier;
        this.airResistanceMultiplier = airResistanceMultiplier;
        this.canPenetrate = canPenetrate;
        this.causesFire = causesFire;
        this.hasRocketTrail = hasRocketTrail;
        this.isNuclear = isNuclear;
    }
    
    public double getGravityMultiplier() {
        return gravityMultiplier;
    }
    
    public double getAirResistanceMultiplier() {
        return airResistanceMultiplier;
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
    
    public boolean isNuclear() {
        return isNuclear;
    }
}
