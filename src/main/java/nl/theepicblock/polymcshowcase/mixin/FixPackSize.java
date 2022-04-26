package nl.theepicblock.polymcshowcase.mixin;

import io.github.theepicblock.polymc.api.resource.AssetWithDependencies;
import io.github.theepicblock.polymc.api.resource.ModdedResources;
import io.github.theepicblock.polymc.api.resource.PolyMcResourcePack;
import io.github.theepicblock.polymc.api.resource.json.JBlockStateVariant;
import io.github.theepicblock.polymc.impl.misc.logging.SimpleLogger;
import io.github.theepicblock.polymc.impl.resource.ResourcePackImplementation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ResourcePackImplementation.class)
public class FixPackSize {
    /**
     * @author TheEpicBlock_TEB
     */
    @Overwrite(remap = false)
    public void importRequirements(ModdedResources input, AssetWithDependencies asset, SimpleLogger logger) {
        // Don't
    }
}
