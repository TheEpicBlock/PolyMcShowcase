package nl.theepicblock.polymcshowcase.polymc;

import io.github.theepicblock.polymc.api.block.BlockPoly;
import io.github.theepicblock.polymc.api.item.CustomModelDataManager;
import io.github.theepicblock.polymc.api.resource.ModdedResources;
import io.github.theepicblock.polymc.api.resource.PolyMcResourcePack;
import io.github.theepicblock.polymc.api.resource.json.JModelOverride;
import io.github.theepicblock.polymc.api.wizard.PacketConsumer;
import io.github.theepicblock.polymc.api.wizard.Wizard;
import io.github.theepicblock.polymc.api.wizard.WizardInfo;
import io.github.theepicblock.polymc.impl.misc.logging.SimpleLogger;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;

public class ToggleBlockPoly implements BlockPoly {
    private final ItemStack stack;

    public ToggleBlockPoly(CustomModelDataManager manager) {
        this.stack = CMDHelper.itemStackFromManager(manager, new Item[]{Items.WHITE_CANDLE});
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
        var cmdValue = stack.getNbt().getInt("CustomModelData");
        var clientitemId = Registries.ITEM.getId(stack.getItem());

        // Get the json for the vanilla item, so we can inject an override into it
        var clientItemModel = pack.getOrDefaultVanillaItemModel(clientitemId.getNamespace(), clientitemId.getPath());

        // Add the override
        clientItemModel.getOverrides().add(JModelOverride.ofCMD(cmdValue, "polymc-showcase:block/toggle_block_logo"));

        var blockLogoModel = moddedResources.getModel("polymc-showcase", "block/toggle_block_logo");
        VSmallItemStand.applyDisplay(blockLogoModel);
        pack.setModel("polymc-showcase", "block/toggle_block_logo", blockLogoModel);
        pack.importRequirements(moddedResources, blockLogoModel, logger);
    }

    @Override
    public Wizard createWizard(WizardInfo info) {
        return new ToggleBlockWizard(info, stack);
    }

    @Override
    public boolean hasWizard() {
        return true;
    }

    public static class ToggleBlockWizard extends Wizard {
        private final VSmallItemStand stand;

        public ToggleBlockWizard(WizardInfo info, ItemStack stack) {
            super(info);
            stand = new VSmallItemStand(stack);
        }

        @Override
        public void addPlayer(PacketConsumer players) {
            var state = this.getBlockState();
            if (state == null) return;

            var x = 7.95f / 16;
            var height = 23f/16;
            var facing = state.get(Properties.HORIZONTAL_FACING);
            switch (facing) {
                case SOUTH -> stand.spawn(players, this.getPosition().add(0, height, -x));
                case EAST -> stand.spawn(players, this.getPosition().add(-x, height, 0));
                case WEST -> stand.spawn(players, this.getPosition().add(x, height, 0));
                default -> stand.spawn(players, this.getPosition().add(0, height, x));
            }

            if (facing == Direction.EAST || facing == Direction.WEST) {
                stand.sendHeadRotation(players, 0, 90, 0);
            }
        }

        @Override
        public void onMove(PacketConsumer players) {
            stand.move(players, this.getPosition(), (byte)0, (byte)0, false);
        }

        @Override
        public void removePlayer(PacketConsumer players) {
            stand.remove(players);
        }
    }
}
