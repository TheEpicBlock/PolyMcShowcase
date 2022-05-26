package nl.theepicblock.polymcshowcase.compat;

import dev.hephaestus.glowcase.block.entity.TextBlockEntity;
import io.github.theepicblock.polymc.api.block.BlockPoly;
import io.github.theepicblock.polymc.api.wizard.PacketConsumer;
import io.github.theepicblock.polymc.api.wizard.Wizard;
import io.github.theepicblock.polymc.api.wizard.WizardInfo;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.util.math.Vec3d;
import nl.theepicblock.polymcshowcase.mixin.AreaEffectCloudEntityAccessor;
import nl.theepicblock.polymcshowcase.mixin.EntityAccessorB;
import nl.theepicblock.polymcshowcase.polymc.VAreaEffectCloud;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class GlowcaseBlockPoly implements BlockPoly {
    @Override
    public BlockState getClientBlock(BlockState input) {
        return Blocks.AIR.getDefaultState();
    }

    @Override
    public boolean hasWizard() {
        return true;
    }

    @Override
    public Wizard createWizard(WizardInfo info) {
        return new GlowcaseWizard(info);
    }

    private static class GlowcaseWizard extends Wizard {
        private final ArrayList<VAreaEffectCloud> clouds = new ArrayList<>();
        private final static double TAG_SIZE = 0.28;

        public GlowcaseWizard(WizardInfo info) {
            super(info);
        }

        @Override
        public void addPlayer(PacketConsumer player) {
            var be = this.getBlockEntity();
            if (be instanceof TextBlockEntity tbe) {
                this.updateCount(tbe);
                var pos = this.getPosition();
                var x = pos.x;
                var y = pos.y - 0.5 + (clouds.size() / 2.0 * TAG_SIZE);
                var z = pos.z;
                var i = 0;

                for (var cloud : clouds) {
                    var txt = tbe.lines.get(i);
                    if (txt instanceof LiteralText t && !Objects.equals(t.getRawString(), "")) {
                        cloud.spawn(player, x, y, z);

                        player.sendPacket(new EntityTrackerUpdateS2CPacket(cloud.getId(),
                                new DataTracker(null) {
                                    @Override
                                    public List<Entry<?>> getDirtyEntries() {
                                        List<Entry<?>> list = new ArrayList<>(3);
                                        list.add(new Entry<>(AreaEffectCloudEntityAccessor.getRADIUS(), 0.0f));
                                        list.add(new Entry<>(EntityAccessorB.getCUSTOM_NAME(), Optional.of(txt)));
                                        list.add(new Entry<>(EntityAccessorB.getNAME_VISIBLE(), true));
                                        return list;
                                    }
                                },
                                false));
                    }
                    y -= TAG_SIZE;
                    i++;
                }
            }
        }

        private void updateCount(TextBlockEntity tbe) {
            if (clouds.size() < tbe.lines.size()) {
                var c = tbe.lines.size() - clouds.size();
                for (int i = 0; i < c; i++) {
                    clouds.add(new VAreaEffectCloud());
                }
                clouds.trimToSize();
            } else if (clouds.size() > tbe.lines.size()) {
                // Yeah this isn't such a great thing to do
                var c = clouds.size() - tbe.lines.size();
                clouds.subList(0, c).clear();
                clouds.trimToSize();
            }
        }

        @Override
        public void removePlayer(PacketConsumer player) {
            for (var cloud : clouds) {
                cloud.remove(player);
            }
        }
    }
}
