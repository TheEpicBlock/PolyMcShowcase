package nl.theepicblock.polymcshowcase.mixin;

import eu.pb4.polymer.impl.compat.polymc.PolyMcHelpers;
import io.github.theepicblock.polymc.PolyMc;
import io.github.theepicblock.polymc.api.PolyMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = PolyMcHelpers.class, remap = false)
public class FixPolymer {
    @Redirect(method = "createResources(Ljava/lang/String;)V", at = @At(value = "INVOKE", target = "Lio/github/theepicblock/polymc/PolyMc;getMainMap()Lio/github/theepicblock/polymc/api/PolyMap;"))
    private static PolyMap replacePolymapCall() {
        // This method is newly added and doesn't exist in stable releases yet. That's why Polymer doesn't use it.
        return PolyMc.getGeneratedMap();
    }
}
