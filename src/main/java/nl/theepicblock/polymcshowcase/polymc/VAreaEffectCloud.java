package nl.theepicblock.polymcshowcase.polymc;

import io.github.theepicblock.polymc.api.wizard.PacketConsumer;
import io.github.theepicblock.polymc.impl.poly.wizard.AbstractVirtualEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class VAreaEffectCloud extends AbstractVirtualEntity {
    @Override
    public EntityType<?> getEntityType() {
        return EntityType.AREA_EFFECT_CLOUD;
    }

    public void spawn(PacketConsumer player, double x, double y, double z) {
        player.sendPacket(new EntitySpawnS2CPacket(
                this.id,
                MathHelper.randomUuid(),
                x,
                y,
                z,
                0,
                0,
                this.getEntityType(),
                0,
                Vec3d.ZERO
        ));
    }
}
