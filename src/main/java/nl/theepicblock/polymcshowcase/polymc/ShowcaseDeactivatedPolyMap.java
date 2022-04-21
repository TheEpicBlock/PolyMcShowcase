package nl.theepicblock.polymcshowcase.polymc;

import io.github.theepicblock.polymc.api.PolyMap;
import io.github.theepicblock.polymc.api.block.BlockPoly;
import io.github.theepicblock.polymc.api.block.BlockStateProfile;
import io.github.theepicblock.polymc.api.entity.EntityPoly;
import io.github.theepicblock.polymc.api.gui.GuiPoly;
import io.github.theepicblock.polymc.api.item.ItemLocation;
import io.github.theepicblock.polymc.api.item.ItemPoly;
import io.github.theepicblock.polymc.api.resource.PolyMcResourcePack;
import io.github.theepicblock.polymc.impl.NOPPolyMap;
import io.github.theepicblock.polymc.impl.PolyMapImpl;
import io.github.theepicblock.polymc.impl.misc.logging.SimpleLogger;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ShowcaseDeactivatedPolyMap implements PolyMap {
    private final Map<Block, BlockPoly> blockPolys;

    public ShowcaseDeactivatedPolyMap(PolyMap toCopy) {
        this.blockPolys = new HashMap<>();

        for (var profile : new BlockStateProfile[]{
                BlockStateProfile.FULL_BLOCK_PROFILE,
                BlockStateProfile.LEAVES_PROFILE,
                BlockStateProfile.NO_COLLISION_PROFILE,
                BlockStateProfile.FARMLAND_PROFILE,
                BlockStateProfile.CACTUS_PROFILE,
                BlockStateProfile.DOOR_PROFILE,
                BlockStateProfile.TRAPDOOR_PROFILE,
                BlockStateProfile.METAL_DOOR_PROFILE,
                BlockStateProfile.METAL_TRAPDOOR_PROFILE,
                BlockStateProfile.WAXED_COPPER_STAIR_PROFILE,
                BlockStateProfile.SLAB_PROFILE,
                BlockStateProfile.SCULK_SENSOR_PROFILE
        }) {
            for (var block : profile.blocks) {
                if (toCopy.getBlockPoly(block) != null) {
                    this.blockPolys.put(block, toCopy.getBlockPoly(block));
                }
            }
        }
    }

    @Override
    public int getClientStateRawId(BlockState state, ServerPlayerEntity playerEntity) {
        // I've got no clue why the heck I've got to duplicate this code??
        var poly = this.getBlockPoly(state.getBlock());
        if (poly == null) {
            return Block.STATE_IDS.getRawId(state);
        } else {
            return Block.STATE_IDS.getRawId(poly.getClientBlock(state));
        }
    }

    @Override
    public BlockPoly getBlockPoly(Block block) {
        return blockPolys.get(block);
    }

    @Override
    public ItemStack getClientItem(ItemStack serverItem, @Nullable ServerPlayerEntity player, @Nullable ItemLocation location) {
        return serverItem;
    }

    @Override
    public BlockState getClientBlock(BlockState serverBlock) {
        return serverBlock;
    }

    @Override
    public ItemPoly getItemPoly(Item item) {
        return null;
    }

    @Override
    public GuiPoly getGuiPoly(ScreenHandlerType<?> serverGuiType) {
        return null;
    }

    @Override
    public <T extends Entity> EntityPoly<T> getEntityPoly(EntityType<T> entity) {
        return null;
    }

    @Override
    public ItemStack reverseClientItem(ItemStack clientItem) {
        return clientItem;
    }

    @Override
    public boolean isVanillaLikeMap() {
        return false; //This disables patches meant for vanilla clients
    }

    @Override
    public boolean hasBlockWizards() {
        return false;
    }

    @Override
    public @Nullable PolyMcResourcePack generateResourcePack(SimpleLogger logger) {
        return null;
    }

    @Override
    public String dumpDebugInfo() {
        return "";
    }
}
