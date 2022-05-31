package nl.theepicblock.polymcshowcase.compat;

import net.fabricmc.loader.api.FabricLoader;
import nl.theepicblock.polymcshowcase.PolyMcShowcase;

import java.lang.reflect.InvocationTargetException;

public class AutomobilityHook {
    public static void doStuff() {
        if (FabricLoader.getInstance().isModLoaded("automobility")) {
            try {
                var clazz = Class.forName("io.github.foundationgames.automobility.resource.AutomobilityAssets");
                clazz.getMethod("setup").invoke(null);
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
            }
        }
    }
}
