/**
 * Cannonball Entity - The projectile fired by kinetic cannons.
 * 
 * This entity represents a cannonball in flight. It implements:
 * - Ballistic physics with gravity
 * - Collision detection with blocks and entities
 * - Explosion on impact
 * - Damage to entities based on velocity
 * 
 * The cannonball uses realistic physics, accounting for gravity
 * and air resistance to create believable trajectories.
 */
package com.createcannons.entity;

import com.createcannons.CreateCannons;
import com.createcannons.registry.CCEntityTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Cannonball projectile entity.
 * 
 * This entity handles the flight physics and impact behavior of
 * cannonballs fired from kinetic cannons. It follows a ballistic
 * trajectory affected by gravity and explodes on impact.
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
    
    // ==================== CONSTANTS ====================
    
    /**
     * Gravity acceleration in blocks per tick squared.
     * Standard Minecraft gravity is ~0.04.
     */
    private static final double GRAVITY = 0.05;
    
    /**
     * Air resistance factor. Higher values = more drag.
     * Applied as velocity *= (1 - AIR_RESISTANCE) each tick.
     */
    private static final double AIR_RESISTANCE = 0.01;
    
    /**
     * Minimum velocity before the cannonball is considered "stopped".
     * Below this, the cannonball will despawn.
     */
    private static final double MIN_VELOCITY = 0.1;
    
    /**
     * Maximum lifetime in ticks before automatic despawn.
     * Prevents orphaned projectiles from existing forever.
     */
    private static final int MAX_LIFETIME_TICKS = 600; // 30 seconds
    
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
     * Ticks this entity has existed.
     * Used for lifetime management.
     */
    private int ticksAlive = 0;
    
    /**
     * Whether this cannonball has already exploded.
     * Prevents double explosions.
     */
    private boolean hasExploded = false;
    
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
    }
    
    // ==================== TICK LOGIC ====================
    
    /**
     * Main tick update for the cannonball.
     * Handles physics, collision detection, and lifetime management.
     */
    @Override
    public void tick() {
        super.tick();
        
        ticksAlive++;
        
        // Check lifetime
        if (ticksAlive > MAX_LIFETIME_TICKS) {
            CreateCannons.LOGGER.debug("Cannonball despawned due to max lifetime");
            this.discard();
            return;
        }
        
        // Apply physics
        applyPhysics();
        
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
     * Handles gravity and air resistance.
     */
    private void applyPhysics() {
        Vec3 velocity = getDeltaMovement();
        
        // Apply gravity (downward acceleration)
        velocity = velocity.add(0, -GRAVITY, 0);
        
        // Apply air resistance (velocity dampening)
        velocity = velocity.scale(1 - AIR_RESISTANCE);
        
        setDeltaMovement(velocity);
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
        
        CreateCannons.LOGGER.debug("Cannonball hit block {} at {}", 
                hitState.getBlock().getName().getString(), result.getBlockPos());
        
        // Explode on impact
        explode();
    }
    
    // ==================== EXPLOSION ====================
    
    /**
     * Creates an explosion at the cannonball's position.
     * Called when the cannonball impacts something or comes to rest.
     */
    private void explode() {
        if (hasExploded || level().isClientSide) {
            return;
        }
        
        hasExploded = true;
        
        // Create explosion
        // Parameters: level, entity, x, y, z, radius, fire, mode
        level().explode(
                this,                               // Source entity
                this.getX(), this.getY(), this.getZ(), // Position
                explosionRadius,                    // Radius
                Level.ExplosionInteraction.TNT      // Explosion type (destroys blocks)
        );
        
        // Play explosion sound (in case the explosion itself doesn't)
        level().playSound(null, this.getX(), this.getY(), this.getZ(),
                SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS,
                2.0f, 0.9f + level().random.nextFloat() * 0.2f);
        
        CreateCannons.LOGGER.debug("Cannonball exploded at {} with radius {}", 
                position(), explosionRadius);
        
        // Remove the entity
        this.discard();
    }
    
    // ==================== PARTICLES ====================
    
    /**
     * Spawns trail particles behind the cannonball.
     * Creates a smoke trail effect during flight.
     */
    private void spawnTrailParticles() {
        if (level() instanceof ServerLevel serverLevel) {
            // Spawn smoke particles
            serverLevel.sendParticles(
                    ParticleTypes.SMOKE,
                    this.getX(), this.getY(), this.getZ(),
                    1,           // Count
                    0.05, 0.05, 0.05,  // Spread
                    0.01         // Speed
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
        
        // Sync to entity data
        this.entityData.set(DATA_DAMAGE, damage);
        this.entityData.set(DATA_EXPLOSION_RADIUS, explosionRadius);
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
