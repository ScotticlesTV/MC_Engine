package net.scotticles.mcengine;

import net.fabricmc.api.ModInitializer;
import net.scotticles.mcengine.commands.LightCommands;
import net.scotticles.mcengine.lights.LightsManager;
import net.scotticles.mcengine.networking.common.C2S;
import net.scotticles.mcengine.networking.common.S2C;
import net.scotticles.mcengine.regions.RegionsManager;
import net.scotticles.mcengine.settings.MCEngineClientConfig;
import net.scotticles.mcengine.settings.MCEngineServerSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MCEngine implements ModInitializer {
	public static final String MOD_ID = "mc-engine";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		// Load The Client Engine Settings Config
		MCEngineClientConfig.load();

		// Load The Server Settings Config
		MCEngineServerSettings.load();

		// Load Region Systems
		RegionsManager.regionsInit();

		// Load Light Systems
		LightsManager.lightDataInit();

		// Register Packet Channels (Both C2S and S2C)
		C2S.registerPacketChannels();
		S2C.registerPacketChannels();

		//Register Server Side Packet Recievers
		C2S.registerServerRecievers();

		//Register Data Syncing Events For When A Player Joins The Server
		S2C.registerServerJoinSyncingEvents();

		LightCommands.registerCommands();

		LOGGER.info("Welcome MC Engine users!");
		// LOGGER.info("MC Engine is initializing. Welcome!");
	}
}