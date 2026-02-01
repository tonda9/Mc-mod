/**
 * Kinetic Cannon Block Entity - Handles cannon logic and state.
 * 
 * This block entity manages all the internal state and logic for the
 * kinetic cannon, including:
 * - Stress network integration (checking available stress)
 * - Projectile inventory and loading
 * - Firing cooldown and mechanics
 * - Multiblock structure validation (future expansion)
 * 
 * The cannon requires kinetic stress input to operate. When sufficient
 * stress is available and the cannon is loaded, it can fire projectiles.
 */
package com.createcannons.blockentity;

import com.createcannons.CreateCannons;
import com.createcannons.block.KineticCannonBlock;
import com.createcannons.entity.CannonballEntity;
import com.createcannons.item.CannonballItem;
import com.createcannons.registry.CCBlockEntityTypes;
import com.createcannons.registry.CCEntityTypes;
import com.createcannons.registry.CCItems;
import com.createcannons.stress.StressConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Block entity for the Kinetic Cannon.
 * 
 * Manages the cannon's internal state including loaded projectile,
 * cooldown timer, and stress consumption. Integrates with Create's
 * kinetic system for stress-based operation.
 */
public class KineticCannonBlockEntity extends BlockEntity {
    
    // ==================== CONSTANTS ====================
    
    /**
     * Base stress impact (SU consumed) for firing the cannon.
     * Higher-tier projectiles may multiply this value.
     */
    public static final float BASE_STRESS_IMPACT = StressConfig.CANNON_STRESS_IMPACT;
    
    /**
     * Cooldown time in ticks between shots.
     * 40 ticks = 2 seconds at 20 TPS.
     */
    public static final int FIRE_COOLDOWN_TICKS = 40;
    
    /**
     * Base velocity for fired projectiles.
     * Measured in blocks per tick.
     */
    public static final float BASE_PROJECTILE_VELOCITY = 2.0f;
    
    /**
     * Maximum number of barrel extensions that affect velocity.
     */
    public static final int MAX_BARREL_BONUS = 5;
    
    // ==================== STATE ====================
    
    /**
     * The currently loaded projectile item stack.
     * Empty when no projectile is loaded.
     */
    private ItemStack loadedProjectile = ItemStack.EMPTY;
    
    /**
     * The loaded gunpowder charge item stack.
     * Empty when no charge is loaded.
     */
    private ItemStack loadedCharge = ItemStack.EMPTY;
    
    /**
     * Remaining cooldown ticks before the cannon can fire again.
     * Decrements each tick until reaching 0.
     */
    private int cooldownTicks = 0;
    
    /**
     * Whether the cannon currently has sufficient stress to operate.
     * Updated each tick based on network stress availability.
     */
    private boolean hasSufficientStress = false;
    
    /**
     * Current rotation speed (from kinetic network).
     * Used to calculate stress impact and firing power.
     */
    private float rotationSpeed = 0.0f;
    
    /**
     * Number of barrel blocks extending from this cannon.
     * Affects projectile velocity and accuracy.
     */
    private int barrelLength = 0;
    
    // ==================== CONSTRUCTOR ====================
    
    /**
     * Creates a new Kinetic Cannon Block Entity.
     * 
     * @param pos The block position
     * @param state The current block state
     */
    public KineticCannonBlockEntity(BlockPos pos, BlockState state) {
        super(CCBlockEntityTypes.KINETIC_CANNON.get(), pos, state);
    }
    
    // ==================== TICK LOGIC ====================
    
    /**
     * Server-side tick update.
     * Called every game tick to update cannon state.
     * 
     * @param level The world
     * @param pos Block position
     * @param state Current block state
     */
    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide) {
            return;
        }
        
        // Decrement cooldown
        if (cooldownTicks > 0) {
            cooldownTicks--;
        }
        
        // Update stress status
        // In a full implementation, this would query Create's stress network
        // For now, we simulate having stress when powered by redstone
        boolean powered = level.hasNeighborSignal(pos);
        hasSufficientStress = powered || simulateStressAvailable();
        
        // Update block state to reflect power status
        if (state.getValue(KineticCannonBlock.POWERED) != hasSufficientStress) {
            level.setBlock(pos, state.setValue(KineticCannonBlock.POWERED, hasSufficientStress), 3);
        }
        
        // Validate and count barrel extensions
        updateBarrelLength(level, pos, state);
    }
    
    /**
     * Simulates stress availability for testing purposes.
     * In a full Create integration, this would query the kinetic network.
     * 
     * @return True if sufficient stress is available
     */
    private boolean simulateStressAvailable() {
        // For demonstration, return true so cannon can always fire
        // Real implementation would check Create's stress network:
        // return getSpeed() != 0 && !isOverStressed();
        return true;
    }
    
    /**
     * Updates the barrel length count by scanning for barrel blocks.
     * 
     * @param level The world
     * @param pos Cannon position
     * @param state Cannon block state
     */
    private void updateBarrelLength(Level level, BlockPos pos, BlockState state) {
        Direction facing = state.getValue(KineticCannonBlock.FACING);
        int count = 0;
        BlockPos checkPos = pos.relative(facing);
        
        // Count barrel blocks in front of the cannon
        while (count < MAX_BARREL_BONUS) {
            BlockState checkState = level.getBlockState(checkPos);
            if (checkState.is(com.createcannons.registry.CCBlocks.CANNON_BARREL.get())) {
                count++;
                checkPos = checkPos.relative(facing);
            } else {
                break;
            }
        }
        
        this.barrelLength = count;
    }
    
    // ==================== LOADING ====================
    
    /**
     * Attempts to load a projectile or charge into the cannon.
     * 
     * @param stack The item stack to load
     * @param player The player loading the cannon
     * @return True if the item was successfully loaded
     */
    public boolean tryLoadProjectile(ItemStack stack, Player player) {
        if (level == null || level.isClientSide) {
            return false;
        }
        
        // Check if this is a cannonball item
        if (stack.getItem() instanceof CannonballItem) {
            if (loadedProjectile.isEmpty()) {
                // Load the projectile
                loadedProjectile = stack.copyWithCount(1);
                stack.shrink(1);
                
                // Play loading sound
                level.playSound(null, worldPosition, SoundEvents.IRON_DOOR_CLOSE, 
                        SoundSource.BLOCKS, 1.0f, 0.8f);
                
                // Send update to clients
                setChanged();
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
                
                player.displayClientMessage(
                        Component.translatable("message.createcannons.projectile_loaded"), true);
                
                return true;
            } else {
                player.displayClientMessage(
                        Component.translatable("message.createcannons.projectile_already_loaded"), true);
            }
        }
        
        // Check if this is a gunpowder charge
        if (stack.is(CCItems.GUNPOWDER_CHARGE.get())) {
            if (loadedCharge.isEmpty()) {
                loadedCharge = stack.copyWithCount(1);
                stack.shrink(1);
                
                level.playSound(null, worldPosition, SoundEvents.SAND_PLACE,
                        SoundSource.BLOCKS, 1.0f, 1.2f);
                
                setChanged();
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
                
                player.displayClientMessage(
                        Component.translatable("message.createcannons.charge_loaded"), true);
                
                return true;
            } else {
                player.displayClientMessage(
                        Component.translatable("message.createcannons.charge_already_loaded"), true);
            }
        }
        
        return false;
    }
    
    // ==================== FIRING ====================
    
    /**
     * Attempts to fire the cannon.
     * 
     * @param player The player firing the cannon (can be null for automated firing)
     * @return True if the cannon successfully fired
     */
    public boolean tryFire(@Nullable Player player) {
        if (level == null || level.isClientSide) {
            return false;
        }
        
        // Check cooldown
        if (cooldownTicks > 0) {
            if (player != null) {
                player.displayClientMessage(
                        Component.translatable("message.createcannons.cooldown", cooldownTicks / 20.0f), true);
            }
            return false;
        }
        
        // Check for loaded projectile
        if (loadedProjectile.isEmpty()) {
            if (player != null) {
                player.displayClientMessage(
                        Component.translatable("message.createcannons.no_projectile"), true);
            }
            return false;
        }
        
        // Check for loaded charge
        if (loadedCharge.isEmpty()) {
            if (player != null) {
                player.displayClientMessage(
                        Component.translatable("message.createcannons.no_charge"), true);
            }
            return false;
        }
        
        // Check stress availability
        if (!hasSufficientStress) {
            if (player != null) {
                player.displayClientMessage(
                        Component.translatable("message.createcannons.insufficient_stress"), true);
            }
            return false;
        }
        
        // Fire the cannon!
        fire();
        
        return true;
    }
    
    /**
     * Performs the actual firing logic.
     * Creates a projectile entity and launches it.
     */
    private void fire() {
        if (level == null) return;
        
        BlockState state = getBlockState();
        Direction facing = state.getValue(KineticCannonBlock.FACING);
        
        // Calculate spawn position (in front of the cannon/barrel)
        Vec3 spawnPos = Vec3.atCenterOf(worldPosition)
                .add(Vec3.atLowerCornerOf(facing.getNormal()).scale(1.5 + barrelLength));
        
        // Calculate velocity based on barrel length
        float velocityMultiplier = 1.0f + (barrelLength * 0.2f); // 20% increase per barrel
        float velocity = BASE_PROJECTILE_VELOCITY * velocityMultiplier;
        
        // Get projectile properties
        float damage = 20.0f;
        float explosionRadius = 2.0f;
        
        if (loadedProjectile.getItem() instanceof CannonballItem cannonballItem) {
            damage = cannonballItem.getDamage();
            explosionRadius = cannonballItem.getExplosionRadius();
        }
        
        // Create the projectile entity
        CannonballEntity projectile = new CannonballEntity(
                CCEntityTypes.CANNONBALL.get(), 
                level,
                spawnPos.x, spawnPos.y, spawnPos.z
        );
        
        // Set projectile properties
        projectile.setDamage(damage);
        projectile.setExplosionRadius(explosionRadius);
        
        // Calculate and set velocity
        Vec3 velocityVec = Vec3.atLowerCornerOf(facing.getNormal()).scale(velocity);
        projectile.setDeltaMovement(velocityVec);
        
        // Add some random spread for realism
        projectile.setDeltaMovement(projectile.getDeltaMovement().add(
                (level.random.nextDouble() - 0.5) * 0.05,
                (level.random.nextDouble() - 0.5) * 0.05,
                (level.random.nextDouble() - 0.5) * 0.05
        ));
        
        // Spawn the projectile
        level.addFreshEntity(projectile);
        
        // Consume ammunition
        loadedProjectile = ItemStack.EMPTY;
        loadedCharge = ItemStack.EMPTY;
        
        // Start cooldown
        cooldownTicks = FIRE_COOLDOWN_TICKS;
        
        // Play firing sound
        level.playSound(null, worldPosition, SoundEvents.GENERIC_EXPLODE.value(),
                SoundSource.BLOCKS, 2.0f, 0.8f + level.random.nextFloat() * 0.2f);
        
        // Update clients
        setChanged();
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        
        CreateCannons.LOGGER.debug("Cannon fired at {} with velocity {}", worldPosition, velocity);
    }
    
    // ==================== INVENTORY ====================
    
    /**
     * Drops the cannon's contents when broken.
     * 
     * @param level The world
     * @param pos Block position
     */
    public void dropContents(Level level, BlockPos pos) {
        if (!loadedProjectile.isEmpty()) {
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), loadedProjectile);
            loadedProjectile = ItemStack.EMPTY;
        }
        if (!loadedCharge.isEmpty()) {
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), loadedCharge);
            loadedCharge = ItemStack.EMPTY;
        }
    }
    
    /**
     * Gets the currently loaded projectile.
     * 
     * @return The loaded projectile stack, or empty if none
     */
    public ItemStack getLoadedProjectile() {
        return loadedProjectile;
    }
    
    /**
     * Gets the currently loaded charge.
     * 
     * @return The loaded charge stack, or empty if none
     */
    public ItemStack getLoadedCharge() {
        return loadedCharge;
    }
    
    /**
     * Checks if the cannon is ready to fire.
     * 
     * @return True if the cannon can fire
     */
    public boolean isReadyToFire() {
        return !loadedProjectile.isEmpty() && !loadedCharge.isEmpty() 
                && cooldownTicks == 0 && hasSufficientStress;
    }
    
    /**
     * Gets the remaining cooldown time.
     * 
     * @return Cooldown ticks remaining
     */
    public int getCooldownTicks() {
        return cooldownTicks;
    }
    
    /**
     * Gets the current barrel length.
     * 
     * @return Number of barrel blocks
     */
    public int getBarrelLength() {
        return barrelLength;
    }
    
    // ==================== NBT SERIALIZATION ====================
    
    /**
     * Saves the block entity data to NBT.
     * 
     * @param tag The compound tag to save to
     * @param registries The registry access
     */
    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        
        // Save loaded projectile
        if (!loadedProjectile.isEmpty()) {
            tag.put("LoadedProjectile", loadedProjectile.save(registries));
        }
        
        // Save loaded charge
        if (!loadedCharge.isEmpty()) {
            tag.put("LoadedCharge", loadedCharge.save(registries));
        }
        
        // Save cooldown
        tag.putInt("CooldownTicks", cooldownTicks);
        
        // Save stress status
        tag.putBoolean("HasStress", hasSufficientStress);
        tag.putFloat("RotationSpeed", rotationSpeed);
        tag.putInt("BarrelLength", barrelLength);
    }
    
    /**
     * Loads the block entity data from NBT.
     * 
     * @param tag The compound tag to load from
     * @param registries The registry access
     */
    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        
        // Load projectile
        if (tag.contains("LoadedProjectile")) {
            loadedProjectile = ItemStack.parse(registries, tag.getCompound("LoadedProjectile"))
                    .orElse(ItemStack.EMPTY);
        } else {
            loadedProjectile = ItemStack.EMPTY;
        }
        
        // Load charge
        if (tag.contains("LoadedCharge")) {
            loadedCharge = ItemStack.parse(registries, tag.getCompound("LoadedCharge"))
                    .orElse(ItemStack.EMPTY);
        } else {
            loadedCharge = ItemStack.EMPTY;
        }
        
        // Load cooldown
        cooldownTicks = tag.getInt("CooldownTicks");
        
        // Load stress status
        hasSufficientStress = tag.getBoolean("HasStress");
        rotationSpeed = tag.getFloat("RotationSpeed");
        barrelLength = tag.getInt("BarrelLength");
    }
    
    // ==================== CLIENT SYNC ====================
    
    /**
     * Gets the update packet for syncing to clients.
     * 
     * @return The update packet
     */
    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    
    /**
     * Gets the NBT tag for client synchronization.
     * 
     * @param registries The registry access
     * @return The update tag
     */
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }
}
