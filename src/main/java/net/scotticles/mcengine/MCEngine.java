package net.scotticles.mcengine;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameRules;
import net.scotticles.mcengine.networking.common.C2S;
import net.scotticles.mcengine.networking.common.S2C;
import net.scotticles.mcengine.networking.gamerules.SyncGamerulesEditorPayload;
import net.scotticles.mcengine.networking.regions.payloads.SyncRegionsDataPayload;
import net.scotticles.mcengine.regions.RegionsManager;
import net.scotticles.mcengine.settings.MCEngineConfig;
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

		// Load The Client Side Engine Config
		MCEngineConfig.load();
		RegionsManager.regionsInit();

		// Register Packet Channels
		C2S.registerPacketChannels();
		S2C.registerPacketChannels();

		//Register Server Side Packet Recievers
		C2S.registerServerRecievers();

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			// Get Joined Player
			ServerPlayerEntity player = handler.player;

			//Sync Gamerules
			GameRules gameRules = server.getGameRules();

			ServerPlayNetworking.send(player, new SyncGamerulesEditorPayload(
					gameRules.get(GameRules.DO_DAYLIGHT_CYCLE).get(),
					gameRules.get(GameRules.DO_WEATHER_CYCLE).get(),
					gameRules.get(GameRules.SEND_COMMAND_FEEDBACK).get()));


			//Sync Regions
			ServerPlayNetworking.send(player, new SyncRegionsDataPayload(RegionsManager.activeRegions));
		});

		LOGGER.info("Hello MC Engine users!");
	}
}