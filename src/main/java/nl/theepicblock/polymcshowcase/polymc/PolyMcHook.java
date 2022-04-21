package nl.theepicblock.polymcshowcase.polymc;

import io.github.theepicblock.polymc.PolyMc;
import io.github.theepicblock.polymc.api.PolyMap;
import io.github.theepicblock.polymc.api.PolyMcEntrypoint;
import io.github.theepicblock.polymc.api.PolyRegistry;
import io.github.theepicblock.polymc.api.item.CustomModelDataManager;
import io.github.theepicblock.polymc.api.misc.PolyMapProvider;
import io.github.theepicblock.polymc.impl.NOPPolyMap;
import nl.theepicblock.polymcshowcase.PlayerDuck;
import nl.theepicblock.polymcshowcase.PolyMcShowcase;

public class PolyMcHook implements PolyMcEntrypoint {
    public static PolyMap NOP_POLY_MAP;

    public static void hook() {
        PolyMapProvider.EVENT.register(player -> {
            if (((PlayerDuck)player).getIsUsingPolyMc()) {
                return null;
            } else {
                if (NOP_POLY_MAP == null) {
                    NOP_POLY_MAP = new ShowcaseDeactivatedPolyMap(PolyMc.getMainMap());
                }
                return NOP_POLY_MAP;
            }
        });
    }

    @Override
    public void registerPolys(PolyRegistry registry) {
        registry.registerBlockPoly(PolyMcShowcase.TOGGLE_BLOCK, new ToggleBlockPoly(registry.getSharedValues(CustomModelDataManager.KEY)));
    }
}
