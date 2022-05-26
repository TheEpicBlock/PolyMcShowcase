package nl.theepicblock.polymcshowcase.mixin;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.data.TrackedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AreaEffectCloudEntity.class)
public interface AreaEffectCloudEntityAccessor {
    @Accessor
    static TrackedData<Float> getRADIUS() {
        throw new UnsupportedOperationException();
    }
}
