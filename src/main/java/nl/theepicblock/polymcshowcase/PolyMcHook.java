package nl.theepicblock.polymcshowcase;

import io.github.theepicblock.polymc.api.misc.PolyMapProvider;
import io.github.theepicblock.polymc.impl.NOPPolyMap;

public class PolyMcHook {
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
}
