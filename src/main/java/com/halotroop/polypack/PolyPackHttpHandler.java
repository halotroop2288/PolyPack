package com.halotroop.polypack;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.Objects;

import static com.halotroop.polypack.PolyPack.*;

public class PolyPackHttpHandler implements HttpHandler {
	PolyPackHttpHandler() {}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		if (Objects.equals(exchange.getRequestMethod(), "GET")) {
			String username = exchange.getRequestHeaders().getFirst("X-Minecraft-Username");

			LOGGER.info("Supplying resource pack to " + (username != null ? "Minecraft player: {}" : "a non-Minecraft client"), username);

			OutputStream outputStream = exchange.getResponseBody();
			File pack = POLYMER_PACK_FILE.toFile();

			exchange.getResponseHeaders().add("User-Agent", "Java/polypack");
			exchange.sendResponseHeaders(200, pack.length());

			FileInputStream fis = new FileInputStream(pack);
			BufferedInputStream bis = new BufferedInputStream(fis);
			bis.transferTo(outputStream);
			bis.close();
			fis.close();

			outputStream.flush();
			outputStream.close();
		} else {
			try {
				exchange.sendResponseHeaders(200, 0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
