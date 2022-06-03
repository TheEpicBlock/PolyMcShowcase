package nl.theepicblock.polymcshowcase.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.GameOptions;
import nl.theepicblock.polymcshowcase.PolyMcShowcase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Mixin(GameOptions.class)
public class AddRPByDefault {
    @Shadow public List<String> resourcePacks;

    @Inject(method = "accept", at = @At("RETURN"))
    private void injectPolyMcRP(@Coerce Object visitor, CallbackInfo ci) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            if (FabricLoader.getInstance().getGameDir().resolve("resourcepacks/polymc.zip").toFile().exists() &&
                !FabricLoader.getInstance().getConfigDir().resolve("polymcshowcasemarker.txt").toFile().exists()) {
                if (!(this.resourcePacks instanceof ArrayList<String>)) {
                    this.resourcePacks = new ArrayList<>(this.resourcePacks);
                }
                try {
                    Files.createFile(FabricLoader.getInstance().getConfigDir().resolve("polymcshowcasemarker.txt"));
                } catch (IOException e) {
                    PolyMcShowcase.LOGGER.info("Failed to create marker file");
                    e.printStackTrace();
                }
                PolyMcShowcase.LOGGER.info("Adding PolyMc resource pack");
                this.resourcePacks.add("file/polymc.zip");
            }
        }
    }
}
