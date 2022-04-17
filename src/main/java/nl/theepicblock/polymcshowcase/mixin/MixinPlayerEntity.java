package nl.theepicblock.polymcshowcase.mixin;

import net.minecraft.entity.player.PlayerEntity;
import nl.theepicblock.polymcshowcase.PlayerDuck;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerEntity.class)
public class MixinPlayerEntity implements PlayerDuck {
    @Unique
    private boolean usingPolyMc = false;
    @Unique
    private long lastSwapTick = -1;


    @Override
    public boolean getIsUsingPolyMc() {
        return usingPolyMc;
    }

    @Override
    public void setIsUsingPolyMc(boolean v) {
        usingPolyMc = v;
    }

    @Override
    public long getPolyMcLastSwapTick() {
        return lastSwapTick;
    }

    @Override
    public void setPolyMcLastSwapTick(long v) {
        lastSwapTick = v;
    }
}
