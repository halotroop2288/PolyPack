package com.halotroop.polypack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class PolyPackConfig {
	public static final File CONFIG_FILE;
	private static final Gson GSON;

	static {
		CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "PolyPack.json");
		GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().disableHtmlEscaping().create();
	}

	public String externalIp = "127.0.0.1";
	public int hostPort = 8001;
	public int threadCount = 3;
	public boolean uniqueUrl = false;
	public boolean alwaysRebuild = true;
	public boolean markRequired = true;

	public static PolyPackConfig loadConfigFile(File file) {
		PolyPackConfig config = null;

		if (file.exists()) {
			try {
				BufferedReader fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
				config = GSON.fromJson(fileReader, PolyPackConfig.class);
			} catch (IOException e) {
				PolyPack.LOGGER.error("Failed to load config file. Ignoring and loading defaults.", e);
			}
		}
		if (config == null) {
			config = new PolyPackConfig();
		}
		if (Objects.equals(config.externalIp, "127.0.0.1")) {
			PolyPack.LOGGER.warn("External ip should be set to your external/public ip.");
		}

		config.saveConfigFile(file);
		return config;
	}

	public void saveConfigFile(File file) {
		try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
			GSON.toJson(this, writer);
		} catch (IOException e) {
			PolyPack.LOGGER.error("Failed to save config file.", e);
		}
	}
}
