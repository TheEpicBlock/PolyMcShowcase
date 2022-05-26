package nl.theepicblock.polymcshowcase.compat;

import net.fabricmc.loader.api.FabricLoader;

public class GlowcaseSanityWrapper {
    public static boolean checkSanity() {
        if (FabricLoader.getInstance().isModLoaded("glowcase")) {
            try {
                var tbe = Class.forName("dev.hephaestus.glowcase.block.entity.TextBlockEntity");
                tbe.getField("lines");
                return true;
            } catch (Throwable t) {
                return false;
            }
        } else {
            return false;
        }
    }
}
