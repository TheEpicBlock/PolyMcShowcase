package nl.theepicblock.polymcshowcase.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;

@Mixin(Entity.class)
public interface EntityAccessorB {
    @Accessor
    static TrackedData<Optional<Text>> getCUSTOM_NAME() {
        throw new UnsupportedOperationException();
    }

    @Accessor
    static TrackedData<Boolean> getNAME_VISIBLE() {
        throw new UnsupportedOperationException();
    }
}
