/**
 * Kinetic Cannon Block - The main firing mechanism for Create Cannons.
 * 
 * This block implements a kinetic machine that consumes rotational stress
 * to charge and fire projectiles. It integrates with Create's kinetic
 * network system and provides the core cannon functionality.
 * 
 * Features:
 * - Consumes 256 SU (Stress Units) when operating
 * - Supports directional placement facing any direction
 * - Handles right-click interaction for firing and loading
 * - Integrates with Create's kinetic block entity system
 */
package com.createcannons.block;

import com.createcannons.blockentity.KineticCannonBlockEntity;
import com.createcannons.registry.CCBlockEntityTypes;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

/**
 * The Kinetic Cannon block implementation.
 * 
 * This extends DirectionalBlock to support 6-directional placement and
 * implements EntityBlock to associate with a block entity for logic.
 * 
 * The cannon can be placed facing any direction and will fire projectiles
 * in that direction when activated with sufficient stress.
 */
public class KineticCannonBlock extends DirectionalBlock implements EntityBlock {
    
    /**
     * Property indicating if the cannon is currently powered by sufficient stress.
     * When true, the cannon has enough kinetic energy to fire.
     */
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    
    /**
     * The direction the cannon is facing (and will fire in).
     * Inherited from DirectionalBlock for 6-directional support.
     */
    public static final DirectionProperty FACING = DirectionalBlock.FACING;
    
    /**
     * Codec for serialization (required by modern Minecraft).
     */
    public static final MapCodec<KineticCannonBlock> CODEC = simpleCodec(KineticCannonBlock::new);
    
    /**
     * Collision shape for the cannon block.
     * Slightly smaller than a full block for visual appeal.
     */
    private static final VoxelShape SHAPE = Shapes.or(
            Block.box(2, 2, 2, 14, 14, 14),     // Main body
            Block.box(4, 4, 0, 12, 12, 2),      // Barrel base
            Block.box(5, 5, -2, 11, 11, 0)      // Barrel tip
    );
    
    /**
     * Constructs a new Kinetic Cannon Block.
     * 
     * @param properties Block properties defining material, hardness, etc.
     */
    public KineticCannonBlock(Properties properties) {
        super(properties);
        
        // Set default state with facing north and not powered
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(POWERED, false)
        );
    }
    
    /**
     * Gets the codec for this block type.
     * 
     * @return The map codec for serialization
     */
    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }
    
    /**
     * Creates the state container with all block state properties.
     * 
     * @param builder The state definition builder
     */
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED);
    }
    
    /**
     * Determines the block state when placing this block.
     * The cannon faces away from the player (toward where they're looking).
     * 
     * @param context The placement context with player and position info
     * @return The block state to place, or null to cancel placement
     */
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // Face opposite to the player's looking direction (fires away from player)
        return this.defaultBlockState()
                .setValue(FACING, context.getNearestLookingDirection().getOpposite())
                .setValue(POWERED, false);
    }
    
    /**
     * Gets the collision/interaction shape for this block.
     * 
     * @param state Current block state
     * @param level The world
     * @param pos Block position
     * @param context Collision context
     * @return The voxel shape for collision
     */
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        // TODO: Rotate shape based on facing direction
        return SHAPE;
    }
    
    /**
     * Gets the render shape for this block.
     * Returns MODEL to use the block model for rendering.
     * 
     * @param state Current block state
     * @return The render shape type
     */
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
    
    /**
     * Handles right-click interaction with an item in hand.
     * Used for loading projectiles into the cannon.
     * 
     * @param stack The item stack being used
     * @param state Current block state
     * @param level The world
     * @param pos Block position
     * @param player The interacting player
     * @param hand Which hand is being used
     * @param hitResult Where on the block the interaction occurred
     * @return The interaction result
     */
    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, 
            BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        
        // Get the block entity for this cannon
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof KineticCannonBlockEntity cannon) {
            // Try to load the item into the cannon
            if (cannon.tryLoadProjectile(stack, player)) {
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
    
    /**
     * Handles right-click interaction with empty hand.
     * Used for firing the cannon or opening GUI.
     * 
     * @param state Current block state
     * @param level The world
     * @param pos Block position
     * @param player The interacting player
     * @param hitResult Where on the block the interaction occurred
     * @return The interaction result
     */
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, 
            BlockPos pos, Player player, BlockHitResult hitResult) {
        
        // Get the block entity for this cannon
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof KineticCannonBlockEntity cannon) {
            // Try to fire the cannon
            if (cannon.tryFire(player)) {
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        
        return InteractionResult.PASS;
    }
    
    // ==================== BLOCK ENTITY METHODS ====================
    
    /**
     * Creates a new block entity for this block.
     * 
     * @param pos Block position
     * @param state Current block state
     * @return A new KineticCannonBlockEntity instance
     */
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new KineticCannonBlockEntity(pos, state);
    }
    
    /**
     * Gets the ticker for this block entity.
     * The ticker is called every game tick to update the cannon logic.
     * 
     * @param level The world
     * @param state Current block state
     * @param blockEntityType The block entity type being ticked
     * @return The ticker function, or null if no ticking is needed
     */
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, 
            BlockEntityType<T> blockEntityType) {
        
        // Only tick on server side, and only for our block entity type
        if (level.isClientSide) {
            return null;
        }
        
        // Return ticker if this is our block entity type
        return blockEntityType == CCBlockEntityTypes.KINETIC_CANNON.get() 
                ? (lvl, pos, st, be) -> ((KineticCannonBlockEntity) be).tick(lvl, pos, st)
                : null;
    }
    
    /**
     * Called when the block is removed from the world.
     * Handles dropping inventory contents.
     * 
     * @param state Current block state
     * @param level The world
     * @param pos Block position
     * @param newState The state replacing this block
     * @param movedByPiston Whether this was moved by a piston
     */
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        // Drop contents if the block is being destroyed (not just state change)
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof KineticCannonBlockEntity cannon) {
                cannon.dropContents(level, pos);
            }
        }
        
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
