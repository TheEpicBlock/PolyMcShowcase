package nl.theepicblock.polymcshowcase.polymc;

import io.github.theepicblock.polymc.api.PolyMcEntrypoint;
import io.github.theepicblock.polymc.api.PolyRegistry;
import io.github.theepicblock.polymc.api.item.CustomModelDataManager;
import io.github.theepicblock.polymc.api.misc.PolyMapProvider;
import io.github.theepicblock.polymc.impl.NOPPolyMap;
import nl.theepicblock.polymcshowcase.PlayerDuck;
import nl.theepicblock.polymcshowcase.PolyMcShowcase;

public class PolyMcHook implements PolyMcEntrypoint {
    public static final NOPPolyMap NOP_POLY_MAP = new NOPPolyMap();

    public static void hook() {
        PolyMapProvider.EVENT.register(player -> {
            if (((PlayerDuck)player).getIsUsingPolyMc()) {
                return null;
            } else {
                return NOP_POLY_MAP;
            }
        });
    }

    @Override
    public void registerPolys(PolyRegistry registry) {
        registry.registerBlockPoly(PolyMcShowcase.TOGGLE_BLOCK, new ToggleBlockPoly(registry.getSharedValues(CustomModelDataManager.KEY)));
    }
}
