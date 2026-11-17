package net.scotticles.mcengine.networking.common;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameRules;
import net.scotticles.mcengine.lights.LightsManager;
import net.scotticles.mcengine.lights.lightdata.*;
import net.scotticles.mcengine.networking.console.SendMessagePayload;
import net.scotticles.mcengine.networking.editor.payloads.OpenEditorUIPayload;
import net.scotticles.mcengine.networking.gamerules.payloads.SyncGamerulesDataPayload;
import net.scotticles.mcengine.networking.lights.payloads.lightprofiles.SyncLightProfileEditorPayload;
import net.scotticles.mcengine.networking.lights.payloads.lights.SyncLightDataPayload;
import net.scotticles.mcengine.networking.regions.payloads.RegionSoundStatePayload;
import net.scotticles.mcengine.networking.regions.payloads.SyncLinearRegionsDataPayload;
import net.scotticles.mcengine.networking.regions.payloads.SyncRadialRegionsDataPayload;
import net.scotticles.mcengine.networking.sound.SyncSoundSettingsPayload;
import net.scotticles.mcengine.regions.RegionsManager;
import net.scotticles.mcengine.settings.MCEngineServerSettings;
import java.util.ArrayList;

public class S2C {
    // Register Channels For Packets To Travel On From Server To Client
    public static void registerPacketChannels() {
        PayloadTypeRegistry.playS2C().register(SyncRadialRegionsDataPayload.ID, SyncRadialRegionsDataPayload.CODEC);

        PayloadTypeRegistry.playS2C().register(SyncLinearRegionsDataPayload.ID, SyncLinearRegionsDataPayload.CODEC);

        PayloadTypeRegistry.playS2C().register(OpenEditorUIPayload.TYPE, OpenEditorUIPayload.CODEC);

        PayloadTypeRegistry.playS2C().register(SyncGamerulesDataPayload.TYPE, SyncGamerulesDataPayload.CODEC);

        PayloadTypeRegistry.playS2C().register(RegionSoundStatePayload.TYPE, RegionSoundStatePayload.CODEC);

        PayloadTypeRegistry.playS2C().register(SyncLightDataPayload.ID, SyncLightDataPayload.CODEC);

        PayloadTypeRegistry.playS2C().register(SyncLightProfileEditorPayload.TYPE, SyncLightProfileEditorPayload.CODEC);

        PayloadTypeRegistry.playS2C().register(SendMessagePayload.TYPE, SendMessagePayload.CODEC);

        PayloadTypeRegistry.playS2C().register(SyncSoundSettingsPayload.TYPE, SyncSoundSettingsPayload.CODEC);
    }

    // Register Actions To Give The Player Server Side Data When They Join The Server
    public static void registerServerJoinSyncingEvents() {
        //Sync Server Side Data When A Player Joins
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            // Get Joined Player
            ServerPlayerEntity player = handler.player;

            //Sync Gamerules
            GameRules gameRules = server.getGameRules();

            ServerPlayNetworking.send(player, new SyncGamerulesDataPayload(
                    gameRules.get(GameRules.DO_DAYLIGHT_CYCLE).get(),
                    gameRules.get(GameRules.DO_WEATHER_CYCLE).get(),
                    gameRules.get(GameRules.SEND_COMMAND_FEEDBACK).get(),
                    gameRules.get(GameRules.COMMAND_BLOCK_OUTPUT).get(),
                    gameRules.get(GameRules.DO_FIRE_TICK).get(),
                    gameRules.get(GameRules.DO_IMMEDIATE_RESPAWN).get(),
                    gameRules.get(GameRules.DO_MOB_SPAWNING).get(),
                    gameRules.get(GameRules.FALL_DAMAGE).get(),
                    gameRules.get(GameRules.KEEP_INVENTORY).get(),
                    gameRules.get(GameRules.DO_MOB_GRIEFING).get(),
                    gameRules.get(GameRules.NATURAL_REGENERATION).get(),
                    gameRules.get(GameRules.SHOW_DEATH_MESSAGES).get()
            ));

            //Sync Regions
            ServerPlayNetworking.send(player, new SyncRadialRegionsDataPayload(RegionsManager.activeRadialRegions));
            ServerPlayNetworking.send(player, new SyncLinearRegionsDataPayload(RegionsManager.activeLinearRegions));

            //Sync Lights
            ServerPlayNetworking.send(player, new SyncLightDataPayload(LightsManager.activeAreaLights, LightsManager.activePointLights, LightsManager.activeDirectionalLights, LightsManager.activeSpotLights));

            // Sync LP Editor
            ServerPlayNetworking.send(player, new SyncLightProfileEditorPayload(new ArrayList<>(LightsManager.lightProfileNames), MCEngineServerSettings.lastLPLoaded));

            // Sync Sound Settings
            ServerPlayNetworking.send(player, new SyncSoundSettingsPayload(MCEngineServerSettings.playVanillaMusic));
        });
    }
}
