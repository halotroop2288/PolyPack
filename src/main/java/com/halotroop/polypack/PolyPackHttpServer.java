package com.halotroop.polypack;

import com.sun.net.httpserver.HttpServer;
import eu.pb4.polymer.api.resourcepack.PolymerRPUtils;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.halotroop.polypack.PolyPack.*;

public class PolyPackHttpServer {
	private static HttpServer server = null;
	private static ExecutorService threadPool = null;

	private PolyPackHttpServer() {}

	public static void stop() {
		if (server != null) server.stop(0);
		if (threadPool != null) threadPool.shutdownNow();
	}

	public static void init(MinecraftServer server) {
		try {
			int port = CONFIG.hostPort;

			String externalIp = CONFIG.externalIp;

			String listening = "0.0.0.0";
			final var df  = new SimpleDateFormat("yyyyMMdd-HH.mm.ss");
			String subUrl = (CONFIG.uniqueUrl ? df.format(new Date()) : "resources") + ".zip";

			if (CONFIG.alwaysRebuild || !POLYMER_PACK_FILE.toFile().exists()) {
				LOGGER.info("Building polymer resource pack...");
				PolymerRPUtils.build(POLYMER_PACK_FILE);
			}

			PolyPackHttpServer.server = HttpServer.create(new InetSocketAddress(listening, port), 0);
			PolyPackHttpServer.server.createContext("/" + subUrl, new PolyPackHttpHandler());
			threadPool = Executors.newFixedThreadPool(CONFIG.threadCount);
			PolyPackHttpServer.server.setExecutor(threadPool);
			PolyPackHttpServer.server.start();

			LOGGER.info("Server listening on {}:{}/{}", listening, port, subUrl);

			String packIp = String.format("http://%s:%s/%s", externalIp, port, subUrl);

			LOGGER.info("Polymer resource pack host started at {}", packIp);

			String hash = DigestUtils.sha1Hex(new FileInputStream(POLYMER_PACK_FILE.toFile()).readAllBytes());
			server.setResourcePack(packIp, hash);
		} catch (IOException e) {
			LOGGER.error("Failed to start the resource pack server!", e);
			e.printStackTrace();
			stop();
		}
	}
}
