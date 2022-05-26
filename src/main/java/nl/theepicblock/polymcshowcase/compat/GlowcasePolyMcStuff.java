package nl.theepicblock.polymcshowcase.compat;

import dev.hephaestus.glowcase.Glowcase;
import io.github.theepicblock.polymc.api.PolyRegistry;

public class GlowcasePolyMcStuff {
    public static void registerPolys(PolyRegistry registry) {
        registry.registerBlockPoly(Glowcase.TEXT_BLOCK, new GlowcaseBlockPoly());
    }
}
