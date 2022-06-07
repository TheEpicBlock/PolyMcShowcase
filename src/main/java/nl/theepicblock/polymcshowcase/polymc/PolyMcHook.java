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
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import nl.theepicblock.polymcshowcase.PlayerDuck;
import nl.theepicblock.polymcshowcase.PolyMcShowcase;
import nl.theepicblock.polymcshowcase.compat.AutomobilityHook;
import nl.theepicblock.polymcshowcase.compat.GlowcasePolyMcStuff;
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
        registry.registerBlockPoly(PolyMcShowcase.TOGGLE_BLOCK, new ToggleBlockPoly(registry.getSharedValues(CustomModelDataManager.KEY)));

        // Prioritize this list of blocks. These are close to PolyMc's booth
        for (var id : Priorities.THESE_SHOULD_HAVE_SMOLL_TRIPWIRE) {
            execIfAvailable(id, block -> {
                registry.registerBlockPoly(block, new FunctionBlockStatePoly(block, profile(registry,
                        BlockStateProfile.TRIPWIRE_SUB_PROFILE.and(state -> state.get(TripwireBlock.ATTACHED)))));
            });
        }

        for (var id : Priorities.PRIORITZED_IDS) {
            execIfAvailable(id, block -> {
                BlockPolyGenerator.addBlockToBuilder(block, registry);
            });
        }

        // I don't want create's rails taking up all the strings
        execIfAvailable("create:controller_rail", block -> {
            var backwardsProperty = (Property<Boolean>)block.getStateManager().getProperty("backwards");
            var powerProperty = (Property<Integer>)block.getStateManager().getProperty("power");
            if (backwardsProperty == null || powerProperty == null) {
                PolyMcShowcase.LOGGER.warn("I messed something minor up, please report this to TEB. Sorry <3");
                return;
            }
            registry.registerBlockPoly(block, new FunctionBlockStatePoly(block, (state, isUniqueCallback) -> {
                if (state.get(backwardsProperty) && state.get(powerProperty) == 5) {
                    return BlockPolyGenerator.registerClientState(state, isUniqueCallback, registry.getSharedValues(BlockStateManager.KEY));
                } else {
                    isUniqueCallback.set(false);
                    return Blocks.POWERED_RAIL.getDefaultState();
                }
            }));
        });

        // These can't be polyd in any other way than to replace them with a vanilla block
        var mod2Vanilla = new HashMap<String, Block>();
        mod2Vanilla.put("bagels_baking:lemon_fence", Blocks.BIRCH_FENCE);
        mod2Vanilla.put("campanion:rope_ladder", Blocks.LADDER);
        mod2Vanilla.put("oxidized:copper_lantern", Blocks.LANTERN);
        mod2Vanilla.put("aurorasdeco:copper_sulfate_lantern", Blocks.LANTERN);
        mod2Vanilla.put("botania:mana_glass", Blocks.BARRIER);
        mod2Vanilla.put("botania:elf_glass", Blocks.LIGHT_BLUE_STAINED_GLASS);
        mod2Vanilla.put("computercraft:cable", Blocks.AIR);
        mod2Vanilla.put("portalcubed:conversion_gel", Blocks.AIR);
        mod2Vanilla.put("portalcubed:propulsion_gel", Blocks.AIR);
        mod2Vanilla.put("portalcubed:repulsion_gel", Blocks.AIR);
        mod2Vanilla.put("portalcubed:adhesion_gel", Blocks.AIR);
        mod2Vanilla.put("automobility:launch_gel", Blocks.AIR);
        mod2Vanilla.put("create:framed_glass", Blocks.GLASS);
        mod2Vanilla.put("create:dark_oak_window_pane", Blocks.GLASS_PANE);

        mod2Vanilla.forEach((mod, vanilla) -> {
            execIfAvailable(mod, modBlock -> {
                registry.registerBlockPoly(modBlock, new BetterPropertyRetainingReplacementPoly(vanilla, modBlock));
            });
        });

        if (GlowcaseSanityWrapper.checkSanity()) {
            GlowcasePolyMcStuff.registerPolys(registry);
        }

        // Fixes an issue with sandwichable
        Registry.ITEM.getOrEmpty(new Identifier("sandwichable","sandwich")).ifPresent(item -> {
            registry.registerItemPoly(item, (input, location) -> new ItemStack(Items.BREAD));
        });
    }

    public static void execIfAvailable(String id, Consumer<Block> consumer) {
        var identifier = Identifier.tryParse(id);
        if (identifier == null) {
            PolyMcShowcase.LOGGER.error(id+" is not a valid identifier. Please report to TEB coz this isn't supposed to happen");
        }

        if (!Registry.BLOCK.containsId(identifier)) {
            PolyMcShowcase.LOGGER.warn("PolyMc can't find "+id+". If the mod was removed this is completely fine. Do note that you should regenerate PolyMc's resource pack though.");
            return;
        }

        try {
            consumer.accept(Registry.BLOCK.get(identifier));
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
