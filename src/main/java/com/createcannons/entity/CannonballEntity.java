/**
 * Cannonball Entity - The projectile fired by kinetic cannons.
 * 
 * This entity represents a cannonball in flight. It implements:
 * - Ballistic physics with gravity and air resistance
 * - Collision detection with blocks and entities
 * - Explosion on impact with configurable effects
 * - Damage to entities based on velocity
 * - Multiple projectile types with different physics
 * - Special effects (fire, penetration, nuclear, etc.)
 * 
 * The cannonball uses realistic physics, accounting for gravity,
 * air resistance, and projectile mass to create believable trajectories.
 */
package com.createcannons.entity;

import com.createcannons.CreateCannons;
import com.createcannons.registry.CCEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * Cannonball projectile entity.
 * 
 * This entity handles the flight physics and impact behavior of
 * cannonballs fired from kinetic cannons. It follows a ballistic
 * trajectory affected by gravity and explodes on impact.
 * 
 * Enhanced physics system supports:
 * - Variable gravity based on projectile type
 * - Air resistance affected by projectile mass
 * - Block penetration for armor piercing rounds
 * - Fire effects for incendiary shells
 * - Nuclear explosions with radiation effects
 * - Cluster bomb splitting mechanics
 */
public class CannonballEntity extends Projectile {
    
    // ==================== SYNCED DATA ====================
    
    /**
     * Synced damage value for client display.
     */
    private static final EntityDataAccessor<Float> DATA_DAMAGE = 
            SynchedEntityData.defineId(CannonballEntity.class, EntityDataSerializers.FLOAT);
    
    /**
     * Synced explosion radius for client effects.
     */
    private static final EntityDataAccessor<Float> DATA_EXPLOSION_RADIUS = 
            SynchedEntityData.defineId(CannonballEntity.class, EntityDataSerializers.FLOAT);
    
    /**
     * Synced projectile type ordinal for physics behavior.
     */
    private static final EntityDataAccessor<Integer> DATA_PROJECTILE_TYPE = 
            SynchedEntityData.defineId(CannonballEntity.class, EntityDataSerializers.INT);
    
    // ==================== CONSTANTS ====================
    
    /**
     * Base gravity acceleration in blocks per tick squared.
     * Modified by projectile type's gravity multiplier.
     */
    private static final double BASE_GRAVITY = 0.05;
    
    /**
     * Base air resistance factor.
     * Modified by projectile type's air resistance multiplier.
     */
    private static final double BASE_AIR_RESISTANCE = 0.01;
    
    /**
     * Minimum velocity before the cannonball is considered "stopped".
     * Below this, the cannonball will despawn or explode.
     */
    private static final double MIN_VELOCITY = 0.1;
    
    /**
     * Maximum lifetime in ticks before automatic despawn.
     * Prevents orphaned projectiles from existing forever.
     */
    private static final int MAX_LIFETIME_TICKS = 600; // 30 seconds
    
    /**
     * Wind effect strength - adds slight random deviation.
     * Simulates environmental effects on trajectory.
     */
    private static final double WIND_STRENGTH = 0.002;
    
    /**
     * Blocks that can be penetrated by armor piercing rounds.
     * These blocks are destroyed when hit by AP rounds.
     */
    private static final float PENETRATION_THRESHOLD = 3.0f; // Hardness threshold
    
    // ==================== STATE ====================
    
    /**
     * Direct damage dealt to entities on impact.
     * Set based on the type of cannonball loaded.
     */
    private float damage = 20.0f;
    
    /**
     * Explosion radius on impact.
     * Set based on the type of cannonball loaded.
     */
    private float explosionRadius = 2.0f;
    
    /**
     * The type of this projectile, determining physics behavior.
     */
    private ProjectileType projectileType = ProjectileType.STANDARD;
    
    /**
     * Ticks this entity has existed.
     * Used for lifetime management.
     */
    private int ticksAlive = 0;
    
    /**
     * Whether this cannonball has already exploded.
     * Prevents double explosions.
     */
    private boolean hasExploded = false;
    
    /**
     * Number of blocks penetrated (for armor piercing).
     * Limits how many blocks can be penetrated.
     */
    private int blocksPenetrated = 0;
    
    /**
     * Maximum blocks that can be penetrated.
     */
    private static final int MAX_PENETRATION = 3;
    
    // ==================== CONSTRUCTORS ====================
    
    /**
     * Constructor for entity type registration.
     * 
     * @param entityType The entity type
     * @param level The world
     */
    public CannonballEntity(EntityType<? extends CannonballEntity> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(false); // We handle gravity manually for better control
    }
    
    /**
     * Constructor for spawning at a specific position.
     * 
     * @param entityType The entity type
     * @param level The world
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     */
    public CannonballEntity(EntityType<? extends CannonballEntity> entityType, Level level, 
            double x, double y, double z) {
        this(entityType, level);
        this.setPos(x, y, z);
    }
    
    // ==================== DATA DEFINITION ====================
    
    /**
     * Defines the synced entity data parameters.
     * Called during entity initialization.
     * 
     * @param builder The data builder
     */
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_DAMAGE, 20.0f);
        builder.define(DATA_EXPLOSION_RADIUS, 2.0f);
        builder.define(DATA_PROJECTILE_TYPE, 0);
    }
    
    // ==================== TICK LOGIC ====================
    
    /**
     * Main tick update for the cannonball.
     * Handles physics, collision detection, and lifetime management.
     * 
     * Updated to support new projectile types:
     * - Cluster bombs split at trajectory apex
     * - Rocket-assisted shells continue thrust
     * - Nova shells have extended lifetime
     */
    @Override
    public void tick() {
        super.tick();
        
        ticksAlive++;
        
        // Check lifetime (extended for heavy/nova projectiles)
        int maxLifetime = projectileType == ProjectileType.NOVA ? MAX_LIFETIME_TICKS * 2 : MAX_LIFETIME_TICKS;
        if (ticksAlive > maxLifetime) {
            CreateCannons.LOGGER.debug("Cannonball despawned due to max lifetime");
            this.discard();
            return;
        }
        
        // Apply physics
        applyPhysics();
        
        // Check for cluster bomb split (at apex of trajectory)
        if (projectileType == ProjectileType.CLUSTER && ticksAlive > 20) {
            Vec3 vel = getDeltaMovement();
            // Split when velocity is mostly horizontal (near apex)
            if (vel.y > -0.1 && vel.y < 0.1 && !hasExploded) {
                splitClusterBomb();
                return;
            }
        }
        
        // Check for collisions
        checkCollisions();
        
        // Move the entity
        Vec3 velocity = getDeltaMovement();
        this.move(MoverType.SELF, velocity);
        
        // Check if stopped (very low velocity)
        if (velocity.lengthSqr() < MIN_VELOCITY * MIN_VELOCITY && this.onGround()) {
            // Explode when coming to rest
            if (!hasExploded) {
                explode();
            }
            return;
        }
        
        // Spawn trail particles
        spawnTrailParticles();
    }
    
    /**
     * Applies physics forces to the cannonball.
     * 
     * Enhanced physics system handles:
     * - Gravity with mass-based modification
     * - Air resistance with drag coefficient
     * - Wind effects for realism
     * - Rocket thrust for self-propelled projectiles
     * - Mass-based velocity loss calculations
     * 
     * All parameters are derived from ProjectileType for configurability.
     */
    private void applyPhysics() {
        Vec3 velocity = getDeltaMovement();
        
        // Get physics parameters from projectile type
        double mass = projectileType.getMass();
        double gravityMult = projectileType.getGravityMultiplier();
        double airResMult = projectileType.getAirResistanceMultiplier();
        
        // Calculate effective gravity based on projectile type
        // Heavier projectiles affected slightly more by gravity
        double effectiveGravity = BASE_GRAVITY * gravityMult * (1.0 + mass * 0.005);
        
        // Apply gravity (downward acceleration)
        velocity = velocity.add(0, -effectiveGravity, 0);
        
        // Apply mass-based air resistance using projectile type's calculation
        double newSpeed = projectileType.calculateVelocityAfterDrag(velocity.length());
        if (velocity.length() > 0.001) {
            velocity = velocity.normalize().scale(newSpeed);
        }
        
        // Apply wind effect (slight random deviation for realism)
        // Lighter projectiles are more affected by wind
        double windEffect = WIND_STRENGTH / Math.sqrt(mass);
        if (level().random.nextFloat() < 0.3) {
            double windX = (level().random.nextDouble() - 0.5) * windEffect;
            double windZ = (level().random.nextDouble() - 0.5) * windEffect;
            velocity = velocity.add(windX, 0, windZ);
        }
        
        // Apply thrust for rocket and rocket-assisted projectiles
        if (projectileType.hasRocketTrail() || projectileType.hasThrust()) {
            double thrust = projectileType.calculateThrust(ticksAlive);
            if (thrust > 0 && velocity.length() > 0.01) {
                Vec3 direction = velocity.normalize();
                velocity = velocity.add(direction.scale(thrust));
            }
        }
        
        setDeltaMovement(velocity);
    }
    
    /**
     * Splits a cluster bomb or fragmentation shell into multiple projectiles.
     * 
     * Cluster bombs split at apex of trajectory.
     * Fragmentation shells split on impact (called from explode()).
     */
    private void splitClusterBomb() {
        if (level().isClientSide || hasExploded) {
            return;
        }
        
        hasExploded = true;
        
        // Get fragment count from projectile type
        int fragmentCount = projectileType.getFragmentCount();
        if (fragmentCount <= 0) fragmentCount = 8;
        
        // Create smaller projectiles in a spread pattern
        for (int i = 0; i < fragmentCount; i++) {
            CannonballEntity cluster = new CannonballEntity(
                    CCEntityTypes.CANNONBALL.get(),
                    level(),
                    this.getX(), this.getY(), this.getZ()
            );
            
            // Set reduced damage for cluster/fragment pieces
            cluster.setDamage(damage * 0.5f);
            cluster.setExplosionRadius(explosionRadius * 0.5f);
            cluster.setProjectileType(ProjectileType.STANDARD);
            
            // Calculate spread velocity
            double angle = (2 * Math.PI * i) / fragmentCount;
            double spreadX = Math.cos(angle) * 0.3;
            double spreadZ = Math.sin(angle) * 0.3;
            Vec3 baseVel = getDeltaMovement();
            cluster.setDeltaMovement(
                    baseVel.x * 0.5 + spreadX,
                    baseVel.y * 0.5 - 0.1,
                    baseVel.z * 0.5 + spreadZ
            );
            
            level().addFreshEntity(cluster);
        }
        
        // Play split sound
        level().playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.BLOCKS,
                1.0f, 1.0f);
        
        this.discard();
    }
    
    /**
     * Splits a fragmentation shell into shrapnel on impact.
     */
    private void splitFragmentationShell() {
        if (level().isClientSide) {
            return;
        }
        
        int fragmentCount = projectileType.getFragmentCount();
        
        // Create shrapnel in all directions
        for (int i = 0; i < fragmentCount; i++) {
            CannonballEntity fragment = new CannonballEntity(
                    CCEntityTypes.CANNONBALL.get(),
                    level(),
                    this.getX(), this.getY(), this.getZ()
            );
            
            // Shrapnel has lower damage but spreads widely
            fragment.setDamage(damage);
            fragment.setExplosionRadius(explosionRadius);
            fragment.setProjectileType(ProjectileType.STANDARD);
            
            // Random spherical spread
            double theta = level().random.nextDouble() * Math.PI * 2;
            double phi = level().random.nextDouble() * Math.PI;
            double speed = 0.5 + level().random.nextDouble() * 0.3;
            
            fragment.setDeltaMovement(
                    Math.sin(phi) * Math.cos(theta) * speed,
                    Math.cos(phi) * speed,
                    Math.sin(phi) * Math.sin(theta) * speed
            );
            
            level().addFreshEntity(fragment);
        }
    }
    
    /**
     * Checks for collisions with blocks and entities.
     */
    private void checkCollisions() {
        Vec3 startPos = this.position();
        Vec3 endPos = startPos.add(getDeltaMovement());
        
        // Check block collisions using ray tracing
        BlockHitResult blockHit = level().clip(new ClipContext(
                startPos, endPos, 
                ClipContext.Block.COLLIDER, 
                ClipContext.Fluid.NONE, 
                this
        ));
        
        // Check entity collisions
        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(
                level(), this, startPos, endPos,
                getBoundingBox().expandTowards(getDeltaMovement()).inflate(0.5),
                this::canHitEntity
        );
        
        // Handle collision (entity takes priority)
        if (entityHit != null) {
            onHitEntity(entityHit);
        } else if (blockHit.getType() != HitResult.Type.MISS) {
            onHitBlock(blockHit);
        }
    }
    
    /**
     * Determines if this cannonball can hit a specific entity.
     * 
     * @param entity The potential target
     * @return True if the entity can be hit
     */
    @Override
    protected boolean canHitEntity(Entity entity) {
        // Don't hit the shooter or other cannonballs
        if (entity == this.getOwner()) {
            return false;
        }
        if (entity instanceof CannonballEntity) {
            return false;
        }
        return super.canHitEntity(entity);
    }
    
    // ==================== COLLISION HANDLERS ====================
    
    /**
     * Handles collision with an entity.
     * 
     * @param result The hit result containing collision information
     */
    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        
        if (hasExploded || level().isClientSide) {
            return;
        }
        
        Entity target = result.getEntity();
        
        // Calculate damage based on velocity
        float velocityDamageBonus = (float) (getDeltaMovement().length() * 5);
        float totalDamage = damage + velocityDamageBonus;
        
        // Apply damage to entity
        if (target instanceof LivingEntity livingTarget) {
            livingTarget.hurt(level().damageSources().thrown(this, this.getOwner()), totalDamage);
        }
        
        CreateCannons.LOGGER.debug("Cannonball hit entity {} for {} damage", 
                target.getName().getString(), totalDamage);
        
        // Explode on impact
        explode();
    }
    
    /**
     * Handles collision with a block.
     * 
     * @param result The hit result containing collision information
     */
    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        
        if (hasExploded || level().isClientSide) {
            return;
        }
        
        BlockState hitState = level().getBlockState(result.getBlockPos());
        BlockPos hitPos = result.getBlockPos();
        
        CreateCannons.LOGGER.debug("Cannonball hit block {} at {}", 
                hitState.getBlock().getName().getString(), hitPos);
        
        // Check for armor piercing penetration
        if (projectileType.canPenetrate() && blocksPenetrated < MAX_PENETRATION) {
            float hardness = hitState.getDestroySpeed(level(), hitPos);
            
            if (hardness >= 0 && hardness < PENETRATION_THRESHOLD) {
                // Penetrate this block
                level().destroyBlock(hitPos, false);
                blocksPenetrated++;
                
                // Play penetration sound
                level().playSound(null, hitPos.getX(), hitPos.getY(), hitPos.getZ(),
                        SoundEvents.IRON_GOLEM_DAMAGE, SoundSource.BLOCKS,
                        1.0f, 0.5f);
                
                // Reduce velocity slightly
                setDeltaMovement(getDeltaMovement().scale(0.8));
                
                // Don't explode yet, continue through
                return;
            }
        }
        
        // Explode on impact
        explode();
    }
    
    // ==================== EXPLOSION ====================
    
    /**
     * Creates an explosion at the cannonball's position.
     * Called when the cannonball impacts something or comes to rest.
     * 
     * Handles different explosion types based on projectile type:
     * - Standard: Normal explosion
     * - Smoke: No explosion, creates smoke cloud
     * - Grapeshot: No explosion, damage already dealt
     * - Incendiary: Explosion + fire
     * - High-Yield: Massive explosion with lingering effects
     * - Fragmentation: Explosion + shrapnel spread
     * - Nova: Devastating AoE with visual effects
     */
    private void explode() {
        if (hasExploded || level().isClientSide) {
            return;
        }
        
        hasExploded = true;
        
        // Handle smoke shell - no explosion, just smoke
        if (projectileType == ProjectileType.SMOKE) {
            createSmokeCloud();
            this.discard();
            return;
        }
        
        // Handle grapeshot - no explosion, already dealt damage
        if (projectileType == ProjectileType.GRAPESHOT) {
            this.discard();
            return;
        }
        
        // Handle fragmentation shell - explosion + shrapnel
        if (projectileType.isFragmentation()) {
            // Create initial explosion
            level().explode(
                    this,
                    this.getX(), this.getY(), this.getZ(),
                    explosionRadius,
                    false,
                    Level.ExplosionInteraction.TNT
            );
            
            // Split into fragments
            splitFragmentationShell();
            this.discard();
            return;
        }
        
        // Determine if explosion should cause fire (incendiary shells)
        boolean causesFire = projectileType.causesFire();
        
        // Create explosion with appropriate interaction type
        Level.ExplosionInteraction interactionType = projectileType.isHighYield() 
                ? Level.ExplosionInteraction.MOB 
                : Level.ExplosionInteraction.TNT;
        
        // Calculate effective radius using projectile type multiplier
        float effectiveRadius = explosionRadius * projectileType.getExplosionMultiplier();
        
        // Create the main explosion
        level().explode(
                this,
                this.getX(), this.getY(), this.getZ(),
                effectiveRadius,
                causesFire,
                interactionType
        );
        
        // High-yield and Nova shells create additional effects
        if (projectileType.isHighYield()) {
            createHighYieldEffects();
        }
        
        // Incendiary shells set additional fires
        if (projectileType.causesFire()) {
            createFireArea();
        }
        
        // Play explosion sound (louder for high-yield)
        float volume = projectileType.isHighYield() ? 4.0f : 2.0f;
        level().playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS,
                volume, 0.9f + level().random.nextFloat() * 0.2f);
        
        CreateCannons.LOGGER.debug("Cannonball exploded at {} with radius {}", 
                position(), effectiveRadius);
        
        // Remove the entity
        this.discard();
    }
    
    /**
     * Creates a smoke cloud effect for smoke shells.
     */
    private void createSmokeCloud() {
        if (level() instanceof ServerLevel serverLevel) {
            // Create lingering smoke effect
            AreaEffectCloud cloud = new AreaEffectCloud(level(), this.getX(), this.getY(), this.getZ());
            cloud.setRadius(5.0f);
            cloud.setDuration(200); // 10 seconds
            cloud.setRadiusOnUse(-0.5f);
            cloud.setWaitTime(0);
            cloud.setParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE);
            level().addFreshEntity(cloud);
            
            // Spawn immediate smoke particles
            for (int i = 0; i < 50; i++) {
                double offsetX = (level().random.nextDouble() - 0.5) * 4;
                double offsetY = level().random.nextDouble() * 3;
                double offsetZ = (level().random.nextDouble() - 0.5) * 4;
                serverLevel.sendParticles(
                        ParticleTypes.CAMPFIRE_COSY_SMOKE,
                        this.getX() + offsetX, this.getY() + offsetY, this.getZ() + offsetZ,
                        1, 0.1, 0.1, 0.1, 0.02
                );
            }
        }
        
        // Play smoke sound
        level().playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS,
                1.0f, 1.0f);
    }
    
    /**
     * Creates high-yield explosion effects - lingering damage and visual effects.
     * Used for High-Yield Shell and Nova Shell.
     * 
     * Effects:
     * - Lingering area effect cloud with wither and weakness
     * - Massive particle effects
     * - Scaling based on projectile type (Nova is more powerful)
     */
    private void createHighYieldEffects() {
        if (level() instanceof ServerLevel serverLevel) {
            // Calculate scale based on projectile type
            float radiusScale = projectileType == ProjectileType.NOVA ? 2.0f : 1.0f;
            int durationScale = projectileType == ProjectileType.NOVA ? 2 : 1;
            
            // Create lingering damage area effect
            AreaEffectCloud damageCloud = new AreaEffectCloud(level(), this.getX(), this.getY(), this.getZ());
            damageCloud.setRadius(10.0f * radiusScale);
            damageCloud.setDuration(600 * durationScale); // 30-60 seconds
            damageCloud.setRadiusOnUse(-0.2f);
            damageCloud.setWaitTime(10);
            damageCloud.addEffect(new MobEffectInstance(MobEffects.WITHER, 100 * durationScale, 1));
            damageCloud.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200 * durationScale, 1));
            damageCloud.setParticle(ParticleTypes.SOUL_FIRE_FLAME);
            level().addFreshEntity(damageCloud);
            
            // Create massive particle effect (mushroom cloud style)
            int particleCount = projectileType == ProjectileType.NOVA ? 200 : 100;
            for (int i = 0; i < particleCount; i++) {
                double angle = level().random.nextDouble() * Math.PI * 2;
                double distance = level().random.nextDouble() * 15 * radiusScale;
                double height = level().random.nextDouble() * 20 * radiusScale;
                serverLevel.sendParticles(
                        ParticleTypes.EXPLOSION,
                        this.getX() + Math.cos(angle) * distance,
                        this.getY() + height,
                        this.getZ() + Math.sin(angle) * distance,
                        1, 0, 0, 0, 0
                );
            }
            
            // Add extra visual flair for Nova shells
            if (projectileType == ProjectileType.NOVA) {
                // Create ascending column of particles
                for (int i = 0; i < 50; i++) {
                    serverLevel.sendParticles(
                            ParticleTypes.SOUL_FIRE_FLAME,
                            this.getX(), this.getY() + i * 0.5, this.getZ(),
                            5, 2, 0.5, 2, 0.01
                    );
                }
            }
        }
    }
    
    /**
     * @deprecated Use createHighYieldEffects() instead. This method was renamed to
     * avoid real-world nuclear terminology per design guidelines.
     */
    @Deprecated(since = "1.0.0", forRemoval = true)
    private void createNuclearEffects() {
        createHighYieldEffects();
    }
    
    /**
     * Creates fire in the area for incendiary shells.
     */
    private void createFireArea() {
        int radius = (int) explosionRadius + 2;
        BlockPos center = this.blockPosition();
        
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x * x + y * y + z * z <= radius * radius) {
                        BlockPos pos = center.offset(x, y, z);
                        BlockState state = level().getBlockState(pos);
                        
                        // Place fire on flammable or air blocks
                        if (state.isAir() && level().random.nextFloat() < 0.3) {
                            BlockState below = level().getBlockState(pos.below());
                            if (below.isSolid()) {
                                level().setBlock(pos, Blocks.FIRE.defaultBlockState(), 3);
                            }
                        }
                    }
                }
            }
        }
    }
    
    // ==================== PARTICLES ====================
    
    /**
     * Spawns trail particles behind the cannonball.
     * Particle type depends on projectile type.
     * 
     * Updated to support new projectile types:
     * - Rocket/Rocket-Assisted: Intense flame trail
     * - High-Yield/Nova: Eerie soul flame glow
     * - Fragmentation: Spark trail
     * - Incendiary: Fire particles
     */
    private void spawnTrailParticles() {
        if (level() instanceof ServerLevel serverLevel) {
            // Rocket and rocket-assisted trail - intense flame effect
            if (projectileType.hasRocketTrail() || projectileType.hasThrust()) {
                int particleCount = projectileType.hasThrust() ? 5 : 3;
                serverLevel.sendParticles(
                        ParticleTypes.FLAME,
                        this.getX(), this.getY(), this.getZ(),
                        particleCount,
                        0.1, 0.1, 0.1,
                        0.02
                );
                serverLevel.sendParticles(
                        ParticleTypes.SMOKE,
                        this.getX(), this.getY(), this.getZ(),
                        2,
                        0.1, 0.1, 0.1,
                        0.01
                );
                return;
            }
            
            // High-yield and Nova - eerie glow
            if (projectileType.isHighYield()) {
                int particleCount = projectileType == ProjectileType.NOVA ? 5 : 2;
                serverLevel.sendParticles(
                        ParticleTypes.SOUL_FIRE_FLAME,
                        this.getX(), this.getY(), this.getZ(),
                        particleCount,
                        0.05, 0.05, 0.05,
                        0.01
                );
                // Nova shells have additional visual effect
                if (projectileType == ProjectileType.NOVA && ticksAlive % 2 == 0) {
                    serverLevel.sendParticles(
                            ParticleTypes.END_ROD,
                            this.getX(), this.getY(), this.getZ(),
                            1,
                            0.1, 0.1, 0.1,
                            0.005
                    );
                }
                return;
            }
            
            // Fragmentation - spark trail
            if (projectileType.isFragmentation()) {
                serverLevel.sendParticles(
                        ParticleTypes.CRIT,
                        this.getX(), this.getY(), this.getZ(),
                        2,
                        0.1, 0.1, 0.1,
                        0.02
                );
                return;
            }
            
            // Incendiary - fire particles
            if (projectileType.causesFire()) {
                serverLevel.sendParticles(
                        ParticleTypes.FLAME,
                        this.getX(), this.getY(), this.getZ(),
                        2,
                        0.05, 0.05, 0.05,
                        0.01
                );
                return;
            }
            
            // Standard smoke trail
            serverLevel.sendParticles(
                    ParticleTypes.SMOKE,
                    this.getX(), this.getY(), this.getZ(),
                    1,
                    0.05, 0.05, 0.05,
                    0.01
            );
            
            // Occasionally spawn flame particles for visual flair
            if (ticksAlive % 3 == 0) {
                serverLevel.sendParticles(
                        ParticleTypes.FLAME,
                        this.getX(), this.getY(), this.getZ(),
                        1,
                        0.02, 0.02, 0.02,
                        0.005
                );
            }
        }
    }
    
    // ==================== GETTERS AND SETTERS ====================
    
    /**
     * Sets the damage this cannonball deals.
     * 
     * @param damage The damage value
     */
    public void setDamage(float damage) {
        this.damage = damage;
        this.entityData.set(DATA_DAMAGE, damage);
    }
    
    /**
     * Gets the damage this cannonball deals.
     * 
     * @return The damage value
     */
    public float getDamage() {
        return this.entityData.get(DATA_DAMAGE);
    }
    
    /**
     * Sets the explosion radius.
     * 
     * @param radius The explosion radius
     */
    public void setExplosionRadius(float radius) {
        this.explosionRadius = radius;
        this.entityData.set(DATA_EXPLOSION_RADIUS, radius);
    }
    
    /**
     * Gets the explosion radius.
     * 
     * @return The explosion radius
     */
    public float getExplosionRadius() {
        return this.entityData.get(DATA_EXPLOSION_RADIUS);
    }
    
    /**
     * Sets the projectile type for physics behavior.
     * 
     * @param type The projectile type
     */
    public void setProjectileType(ProjectileType type) {
        this.projectileType = type;
        this.entityData.set(DATA_PROJECTILE_TYPE, type.ordinal());
    }
    
    /**
     * Gets the projectile type.
     * 
     * @return The projectile type
     */
    public ProjectileType getProjectileType() {
        return ProjectileType.values()[this.entityData.get(DATA_PROJECTILE_TYPE)];
    }
    
    // ==================== NBT SERIALIZATION ====================
    
    /**
     * Saves additional entity data to NBT.
     * 
     * @param tag The compound tag to save to
     */
    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("Damage", damage);
        tag.putFloat("ExplosionRadius", explosionRadius);
        tag.putInt("TicksAlive", ticksAlive);
        tag.putBoolean("HasExploded", hasExploded);
        tag.putInt("ProjectileType", projectileType.ordinal());
        tag.putInt("BlocksPenetrated", blocksPenetrated);
    }
    
    /**
     * Loads additional entity data from NBT.
     * 
     * @param tag The compound tag to load from
     */
    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        damage = tag.getFloat("Damage");
        explosionRadius = tag.getFloat("ExplosionRadius");
        ticksAlive = tag.getInt("TicksAlive");
        hasExploded = tag.getBoolean("HasExploded");
        blocksPenetrated = tag.getInt("BlocksPenetrated");
        
        // Load projectile type
        int typeOrdinal = tag.getInt("ProjectileType");
        if (typeOrdinal >= 0 && typeOrdinal < ProjectileType.values().length) {
            projectileType = ProjectileType.values()[typeOrdinal];
        }
        
        // Sync to entity data
        this.entityData.set(DATA_DAMAGE, damage);
        this.entityData.set(DATA_EXPLOSION_RADIUS, explosionRadius);
        this.entityData.set(DATA_PROJECTILE_TYPE, projectileType.ordinal());
    }
    
    // ==================== MISC ====================
    
    /**
     * Determines if this entity should render at a given distance.
     * Cannonballs should be visible from far away.
     * 
     * @param distance The squared distance from the camera
     * @return True if the entity should render
     */
    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        double d = 64.0;
        return distance < d * d;
    }
    
    /**
     * Gets whether this entity is attackable.
     * Cannonballs cannot be attacked.
     * 
     * @return False
     */
    @Override
    public boolean isAttackable() {
        return false;
    }
    
    /**
     * Gets whether this entity is pickable (can be targeted).
     * 
     * @return False
     */
    @Override
    public boolean isPickable() {
        return false;
    }
}
