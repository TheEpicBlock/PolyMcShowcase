package nl.theepicblock.polymcshowcase.mixin;

import io.github.theepicblock.polymc.PolyMc;
import io.github.theepicblock.polymc.api.PolyMap;
import nl.theepicblock.polymcshowcase.polymc.PolyMcHook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(PolyMc.class)
public class SetMainMap {
    @Overwrite(remap = false)
    public static PolyMap getMainMap() {
        return PolyMcHook.NOP_POLY_MAP;
    }
}
