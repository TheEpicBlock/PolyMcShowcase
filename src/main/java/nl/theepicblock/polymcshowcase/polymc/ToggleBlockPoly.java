package nl.theepicblock.polymcshowcase.polymc;

import io.github.theepicblock.polymc.api.block.BlockPoly;
import io.github.theepicblock.polymc.api.item.CustomModelDataManager;
import io.github.theepicblock.polymc.api.resource.ModdedResources;
import io.github.theepicblock.polymc.api.resource.PolyMcResourcePack;
import io.github.theepicblock.polymc.impl.misc.logging.SimpleLogger;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.state.property.Properties;

public class ToggleBlockPoly implements BlockPoly {
    public ToggleBlockPoly(CustomModelDataManager manager) {
    }

    @Override
    public BlockState getClientBlock(BlockState input) {
        return Blocks.LEVER.getDefaultState()
                .with(Properties.POWERED, true)
                .with(Properties.WALL_MOUNT_LOCATION, WallMountLocation.WALL)
                .with(Properties.HORIZONTAL_FACING, input.get(Properties.HORIZONTAL_FACING));
    }

    @Override
    public void addToResourcePack(Block block, ModdedResources moddedResources, PolyMcResourcePack pack, SimpleLogger logger) {
    }
}
