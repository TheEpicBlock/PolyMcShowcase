package nl.theepicblock.polymcshowcase.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import qouteall.imm_ptl.core.chunk_loading.ChunkDataSyncManager;
import qouteall.imm_ptl.core.chunk_loading.DimensionalChunkPos;

@Mixin(ChunkDataSyncManager.class)
public interface IpChunkManagerAccessor {
    @Invoker
    void callOnEndWatch(ServerPlayerEntity player, DimensionalChunkPos chunkPos);


    @Invoker
    void callOnBeginWatch(ServerPlayerEntity player, DimensionalChunkPos chunkPos);
}
