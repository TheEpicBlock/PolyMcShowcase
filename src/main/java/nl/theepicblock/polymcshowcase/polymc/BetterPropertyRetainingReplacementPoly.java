package nl.theepicblock.polymcshowcase.polymc;

import io.github.theepicblock.polymc.api.block.BlockPoly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Property;

public class BetterPropertyRetainingReplacementPoly implements BlockPoly {
    protected final Block clientBlock;
    protected final Property<?>[] properties;

    public BetterPropertyRetainingReplacementPoly(Block clientBlock, Block moddedBlock) {
        this.clientBlock = clientBlock;
        this.properties = clientBlock
                .getStateManager()
                .getProperties()
                .stream()
                .filter(property -> moddedBlock.getStateManager().getProperties().contains(property))
                .toArray(Property[]::new);
    }

    @Override
    public BlockState getClientBlock(BlockState input) {
        BlockState output = clientBlock.getDefaultState();
        for (Property<?> p : properties) {
            output = copyProperty(output, input, p);
        }
        return output;
    }

    /**
     * Copies Property p from BlockState b into BlockState a
     */
    private <T extends Comparable<T>> BlockState copyProperty(BlockState a, BlockState b, Property<T> p) {
        return a.with(p, b.get(p));
    }

    @Override
    public String getDebugInfo(Block obj) {
        return clientBlock.getTranslationKey();
    }
}
