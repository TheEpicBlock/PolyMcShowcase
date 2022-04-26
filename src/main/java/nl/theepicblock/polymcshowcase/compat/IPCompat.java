package nl.theepicblock.polymcshowcase.compat;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.ChunkPos;
import nl.theepicblock.polymcshowcase.mixin.IpChunkManagerAccessor;
import qouteall.imm_ptl.core.IPGlobal;
import qouteall.imm_ptl.core.chunk_loading.DimensionalChunkPos;

public class IPCompat {
    public static void loadChunk(ServerPlayerEntity player, int x, int z) {
        ((IpChunkManagerAccessor)IPGlobal.chunkDataSyncManager).callOnBeginWatch(
                player,
                new DimensionalChunkPos(player.getWorld().getRegistryKey(), x, z)
        );
    }

    public static void unloadChunk(ServerPlayerEntity player, int x, int z) {
        ((IpChunkManagerAccessor)IPGlobal.chunkDataSyncManager).callOnEndWatch(
                player,
                new DimensionalChunkPos(player.getWorld().getRegistryKey(), x, z)
        );
    }
}
