package nl.theepicblock.polymcshowcase;

import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class ToggleBlock extends HorizontalFacingBlock {
    public static final VoxelShape NORTH_WALL_SHAPE = Block.createCuboidShape(5.0D, 4.0D, 10.0D, 11.0D, 12.0D, 16.0D);
    public static final VoxelShape SOUTH_WALL_SHAPE = Block.createCuboidShape(5.0D, 4.0D, 0.0D, 11.0D, 12.0D, 6.0D);
    public static final VoxelShape WEST_WALL_SHAPE = Block.createCuboidShape(10.0D, 4.0D, 5.0D, 16.0D, 12.0D, 11.0D);
    public static final VoxelShape EAST_WALL_SHAPE = Block.createCuboidShape(0.0D, 4.0D, 5.0D, 6.0D, 12.0D, 11.0D);

    public ToggleBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case EAST -> EAST_WALL_SHAPE;
            case WEST -> WEST_WALL_SHAPE;
            case SOUTH -> SOUTH_WALL_SHAPE;
            default -> NORTH_WALL_SHAPE;
        };
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            PolyMcShowcase.setPolyMcEnabled((ServerPlayerEntity)player, !((PlayerDuck)player).getIsUsingPolyMc());
//            var duck = (PlayerDuck)player;
//            duck.setIsUsingPolyMc(!duck.getIsUsingPolyMc());
//            PolyMcShowcase.reloadPlayer((ServerPlayerEntity)player);
            return ActionResult.CONSUME;
        }
        return ActionResult.SUCCESS;
    }
}
