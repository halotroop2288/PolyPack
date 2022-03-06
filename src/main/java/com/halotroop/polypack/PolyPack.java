package com.halotroop.polypack;

import eu.pb4.polymer.api.resourcepack.PolymerRPUtils;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.halotroop.polypack.PolyPackConfig.CONFIG_FILE;
import static com.halotroop.polypack.PolyPackConfig.loadConfigFile;

public class PolyPack implements DedicatedServerModInitializer {
	public static final Path POLYMER_PACK_FILE;
	public static final Logger LOGGER;
	public static final PolyPackConfig CONFIG;

	static {
		POLYMER_PACK_FILE = Path.of(FabricLoader.getInstance().getGameDir().toFile() + "/PolyPack.zip");
		LOGGER = LogManager.getLogger("PolyPack");
		CONFIG = loadConfigFile(CONFIG_FILE);
	}

	@Override
	public void onInitializeServer() {
		LOGGER.info("Initializing on server...");
		if (CONFIG.markRequired) PolymerRPUtils.markAsRequired();

		final AtomicBoolean running = new AtomicBoolean(false);

		ServerWorldEvents.LOAD.register(((server, world) -> {
			if (!running.get()) {
				PolyPackHttpServer.init(server);
				running.set(true);
			}
		}));
		ServerWorldEvents.UNLOAD.register((server, world) -> {
			if (running.get()) {
				PolyPackHttpServer.stop();
				running.set(false);
			}
		});
	}
}
