package nl.theepicblock.polymcshowcase.polymc;

import io.github.theepicblock.polymc.api.PolyMap;
import io.github.theepicblock.polymc.api.PolyMcEntrypoint;
import io.github.theepicblock.polymc.api.PolyRegistry;
import io.github.theepicblock.polymc.api.block.BlockPoly;
import io.github.theepicblock.polymc.api.block.BlockStateManager;
import io.github.theepicblock.polymc.api.block.BlockStateProfile;
import io.github.theepicblock.polymc.api.misc.PolyMapProvider;
import io.github.theepicblock.polymc.impl.NOPPolyMap;
import io.github.theepicblock.polymc.impl.misc.BooleanContainer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import nl.theepicblock.polymcshowcase.PlayerDuck;
import nl.theepicblock.polymcshowcase.PolyMcShowcase;
import nl.theepicblock.polymcshowcase.compat.AutomobilityHook;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

public class PolyMcHook implements PolyMcEntrypoint {
    public static PolyMap NOP_POLY_MAP;

    static {
        PolyRegistry registry = new PolyRegistry() {@Override
            public PolyMap build() {
                var blocks = this.blockPolys;
                return new NOPPolyMap() {
                    @Override
                    public BlockState getClientState(BlockState serverBlock, @Nullable ServerPlayerEntity player) {
                        BlockPoly poly = this.getBlockPoly(serverBlock.getBlock());
                        if (poly == null) return serverBlock;
                
                        return poly.getClientBlock(serverBlock);
                    }
        
                    
                    @Override
                    public BlockPoly getBlockPoly(Block block) {
                        return blocks.get(block);
                    }
                };
            }
        };
        for (var profile : BlockStateProfile.ALL_PROFILES) {
            for (var block : profile.blocks) {
                profile.onFirstRegister.accept(block, registry);
            }
        }
        NOP_POLY_MAP = registry.build();
    }

    public static void hook() {
        PolyMapProvider.EVENT.register(player -> {
            if (((PlayerDuck)player).getIsUsingPolyMc()) {
                return null;
            } else {
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
