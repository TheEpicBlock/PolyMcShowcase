package nl.theepicblock.polymcshowcase;

import io.github.theepicblock.polymc.api.misc.PolyMapProvider;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import nl.theepicblock.polymcshowcase.mixin.TacsAccessor;
import nl.theepicblock.polymcshowcase.polymc.PolyMcHook;
import org.apache.commons.lang3.mutable.MutableObject;

public class PolyMcShowcase implements ModInitializer {
	public static final int COOLDOWN = 5 * 20; // 5 seconds
	public static final ToggleBlock TOGGLE_BLOCK = new ToggleBlock(FabricBlockSettings.copyOf(Blocks.LEVER));

	@Override
	public void onInitialize() {
		// Inject into PolyMc api to disable it by default
		if (FabricLoader.getInstance().isModLoaded("polymc")) {
			PolyMcHook.hook();
		}

		CommandRegistrationCallback.EVENT.register(Commands::register);

		Registry.register(Registry.BLOCK, new Identifier("polymc-showcase", "toggle_block"), TOGGLE_BLOCK);
		Registry.register(Registry.ITEM, new Identifier("polymc-showcase", "toggle_block"), new BlockItem(TOGGLE_BLOCK, new FabricItemSettings()));
	}

	public static void setPolyMcEnabled(ServerPlayerEntity player, boolean enabled) {
		var tickTime = player.world.getTime();
		var duck = (PlayerDuck)player;
		if (tickTime-duck.getPolyMcLastSwapTick() < COOLDOWN) {
			if (tickTime-duck.getPolyMcLastSwapTick() > 2) {
				player.sendMessage(new LiteralText("Please wait at least 5 seconds between toggling PolyMc"), false);
			}
			return;
		}
		duck.setPolyMcLastSwapTick(tickTime);
		duck.setIsUsingPolyMc(enabled);
		reloadPlayer(player);
	}

	public static void reloadPlayer(ServerPlayerEntity player) {
		// It's not recommended to change polymaps on the fly
		// But I was the one that said it wasn't recommended and I shall ignore myself

		var tacs = player.getWorld().getChunkManager().threadedAnvilChunkStorage;
		var playerChunk = player.getWatchedSection();
		var playerX = playerChunk.getSectionX();
		var playerZ = playerChunk.getSectionZ();
		var watchDistance = ((TacsAccessor)tacs).getWatchDistance();

		// Stop tracking all entities
		((TacsAccessor)tacs).getEntityTrackers().values().forEach(entityTracker -> {
			entityTracker.stopTracking(player);
		});

		// Unload all chunks
		for (int x = playerX-watchDistance; x <= playerX+watchDistance; x++) {
			for (int z = playerZ-watchDistance; z <= playerZ+watchDistance; z++) {
				if (ThreadedAnvilChunkStorage.isWithinDistance(x, z, playerX, playerZ, watchDistance)) {
					((TacsAccessor)tacs).callSendWatchPackets(player,
							new ChunkPos(x, z),
							new MutableObject<>(),
							true, false);
				}
			}
		}

		// Swap out the polymap
		((PolyMapProvider)player).refreshUsedPolyMap();

		// Update the entity trackers again
		((TacsAccessor)tacs).getEntityTrackers().values().forEach(entityTracker -> {
			entityTracker.updateTrackedStatus(player);
		});

		// Reload all chunks
		for (int x = playerX-watchDistance; x <= playerX+watchDistance; x++) {
			for (int z = playerZ-watchDistance; z <= playerZ+watchDistance; z++) {
				if (ThreadedAnvilChunkStorage.isWithinDistance(x, z, playerX, playerZ, watchDistance)) {
					((TacsAccessor)tacs).callSendWatchPackets(
							player,
							new ChunkPos(x, z),
							new MutableObject<>(),
							false, true);
				}
			}
		}

		// Reload inventory
		player.playerScreenHandler.syncState();
	}
}
