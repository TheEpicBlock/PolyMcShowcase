package nl.theepicblock.polymcshowcase.polymc;

import io.github.theepicblock.polymc.PolyMc;
import io.github.theepicblock.polymc.api.PolyMap;
import io.github.theepicblock.polymc.api.PolyMcEntrypoint;
import io.github.theepicblock.polymc.api.PolyRegistry;
import io.github.theepicblock.polymc.api.block.BlockStateManager;
import io.github.theepicblock.polymc.api.block.BlockStateProfile;
import io.github.theepicblock.polymc.api.item.CustomModelDataManager;
import io.github.theepicblock.polymc.api.misc.PolyMapProvider;
import io.github.theepicblock.polymc.impl.generator.BlockPolyGenerator;
import io.github.theepicblock.polymc.impl.misc.BooleanContainer;
import io.github.theepicblock.polymc.impl.poly.block.FunctionBlockStatePoly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TripwireBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import nl.theepicblock.polymcshowcase.PlayerDuck;
import nl.theepicblock.polymcshowcase.PolyMcShowcase;
import nl.theepicblock.polymcshowcase.compat.AutomobilityHook;
import nl.theepicblock.polymcshowcase.compat.GlowcaseSanityWrapper;

import java.util.HashMap;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class PolyMcHook implements PolyMcEntrypoint {
    public static PolyMap NOP_POLY_MAP;

    public static void hook() {
        PolyMapProvider.EVENT.register(player -> {
            if (((PlayerDuck)player).getIsUsingPolyMc()) {
                return null;
            } else {
                if (NOP_POLY_MAP == null) {
                    NOP_POLY_MAP = new ShowcaseDeactivatedPolyMap(PolyMc.getGeneratedMap());
                }
                return NOP_POLY_MAP;
            }
        });
        AutomobilityHook.doStuff();
    }

    @Override
    public void registerPolys(PolyRegistry registry) {
        return; // TODO
    }

    public static void execIfAvailable(String id, Consumer<Block> consumer) {
        var identifier = Identifier.tryParse(id);
        if (identifier == null) {
            PolyMcShowcase.LOGGER.error(id+" is not a valid identifier. Please report to TEB coz this isn't supposed to happen");
        }

        if (!Registries.BLOCK.containsId(identifier)) {
            PolyMcShowcase.LOGGER.warn("PolyMc can't find "+id+". If the mod was removed this is completely fine. Do note that you should regenerate PolyMc's resource pack though.");
            return;
        }

        try {
            consumer.accept(Registries.BLOCK.get(identifier));
        } catch (Throwable t) {
            PolyMcShowcase.LOGGER.warn("Error in consumer for "+id+". This isn't really major, will just affect PolyMc's booth.");
            t.printStackTrace();
        }
    }

    public static BiFunction<BlockState,BooleanContainer, BlockState> profile(PolyRegistry registry, BlockStateProfile profile) {
        return (state, isUniqueCallback) -> {
            try {
                isUniqueCallback.set(true);
                return registry.getSharedValues(BlockStateManager.KEY).requestBlockState(profile);
            } catch (BlockStateManager.StateLimitReachedException e) {
                isUniqueCallback.set(false);
                return Blocks.STONE.getDefaultState();
            }
        };
    }
}
