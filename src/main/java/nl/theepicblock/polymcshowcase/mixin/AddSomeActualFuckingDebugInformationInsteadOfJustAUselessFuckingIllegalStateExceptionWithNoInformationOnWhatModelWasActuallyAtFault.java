package nl.theepicblock.polymcshowcase.mixin;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import nl.theepicblock.polymcshowcase.PolyMcShowcase;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

@Mixin(JsonUnbakedModel.class)
public class AddSomeActualFuckingDebugInformationInsteadOfJustAUselessFuckingIllegalStateExceptionWithNoInformationOnWhatModelWasActuallyAtFault {
    @Shadow @Nullable protected JsonUnbakedModel parent;

    @Shadow @Nullable protected Identifier parentId;

    @Inject(method = "getTextureDependencies", at = @At(value = "INVOKE", target = "Ljava/lang/IllegalStateException;<init>(Ljava/lang/String;)V"))
    public void addSomeActualFuckingDebugInformationInsteadOfJustAUselessFuckingIllegalStateExceptionWithNoInformationOnWhatModelWasActuallyAtFaultImplementation(Function<Identifier,UnbakedModel> unbakedModelGetter, Set<Pair<String,String>> unresolvedTextureReferences, CallbackInfoReturnable<Collection<SpriteIdentifier>> cir) {
        PolyMcShowcase.LOGGER.error("Hey there, just adding some debug info to the exception below: ");
        var model = this;
        while (model != null) {
            PolyMcShowcase.LOGGER.error(model + " with parent: " + model.parentId);
            model = (AddSomeActualFuckingDebugInformationInsteadOfJustAUselessFuckingIllegalStateExceptionWithNoInformationOnWhatModelWasActuallyAtFault)(Object)model.parent;
        }
    }
}
