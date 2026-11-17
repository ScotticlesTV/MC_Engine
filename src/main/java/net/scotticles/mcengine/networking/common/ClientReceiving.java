package net.scotticles.mcengine.networking.common;

import foundry.veil.api.client.render.VeilRenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.scotticles.mcengine.console.ConsoleManager;
import net.scotticles.mcengine.lights.LightsManager;
import net.scotticles.mcengine.lights.lightdata.MCEAreaLightData;
import net.scotticles.mcengine.lights.lightdata.MCEDirectionalLightData;
import net.scotticles.mcengine.lights.lightdata.MCEPointLightData;
import net.scotticles.mcengine.lights.lightdata.MCESpotLightData;
import net.scotticles.mcengine.networking.console.SendMessagePayload;
import net.scotticles.mcengine.networking.editor.payloads.OpenEditorUIPayload;
import net.scotticles.mcengine.networking.gamerules.payloads.SyncGamerulesDataPayload;
import net.scotticles.mcengine.networking.lights.payloads.lightprofiles.SyncLightProfileEditorPayload;
import net.scotticles.mcengine.networking.lights.payloads.lights.SyncLightDataPayload;
import net.scotticles.mcengine.networking.regions.payloads.RegionSoundStatePayload;
import net.scotticles.mcengine.networking.regions.payloads.SyncLinearRegionsDataPayload;
import net.scotticles.mcengine.networking.regions.payloads.SyncRadialRegionsDataPayload;
import net.scotticles.mcengine.networking.sound.SyncSoundSettingsPayload;
import net.scotticles.mcengine.regions.RegionSoundInstance;
import net.scotticles.mcengine.regions.RegionsManager;
import net.scotticles.mcengine.regions.regiondatasaving.LinearRegionData;
import net.scotticles.mcengine.regions.regiondatasaving.RadialRegionData;
import net.scotticles.mcengine.sound.SoundManager;
import net.scotticles.mcengine.ui.UIManager;
import net.scotticles.mcengine.ui.windows.WorldGamerulesUI;
import net.scotticles.mcengine.ui.windows.lights.LightProfilesEditorUI;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientReceiving {
    private static final Map<String, RegionSoundInstance> ACTIVE_SOUNDS = new HashMap<>();

    // Register Receivers That Handle Actions When The Client Receives A Packet
    public static void registerClientReceivers() {
        // Client SyncSoundSettingsPayload Receiver
        ClientPlayNetworking.registerGlobalReceiver(SyncSoundSettingsPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                SoundManager.shouldPlayVanillaMusic = payload.shouldPlayVanillaMusic();
            });
        });

        // Client SendMessagePayload Receiver
        ClientPlayNetworking.registerGlobalReceiver(SendMessagePayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                ConsoleManager.handleConsoleMessages(payload.message());
            });
        });


        // Client SyncLightProfileEditorPayload Receiver
        ClientPlayNetworking.registerGlobalReceiver(SyncLightProfileEditorPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                LightProfilesEditorUI.lpName.set(payload.newLPName());
                LightsManager.lightProfileNames.clear();
                LightsManager.lightProfileNames.addAll(payload.lpNames());
            });
        });

        // Client SyncLightDataPayload Receiver
        ClientPlayNetworking.registerGlobalReceiver(SyncLightDataPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                LightsManager.clearLights();
                VeilRenderSystem.renderer().getLightRenderer().free();

                LightsManager.activeAreaLights.addAll(payload.areaLights());
                LightsManager.activePointLights.addAll(payload.pointLights());
                LightsManager.activeDirectionalLights.addAll(payload.directionalLights());
                LightsManager.activeSpotLights.addAll(payload.spotLights());

                for (MCEAreaLightData mceAreaLightData : LightsManager.activeAreaLights) {
                    VeilRenderSystem.renderer().getLightRenderer().addLight(mceAreaLightData);
                }
                for (MCEPointLightData mcePointLightData : LightsManager.activePointLights) {
                    VeilRenderSystem.renderer().getLightRenderer().addLight(mcePointLightData);
                }
                for (MCEDirectionalLightData mceDirectionalLightData : LightsManager.activeDirectionalLights) {
                    VeilRenderSystem.renderer().getLightRenderer().addLight(mceDirectionalLightData);
                }
                for (MCESpotLightData mceSpotLightData : LightsManager.activeSpotLights) {
                    VeilRenderSystem.renderer().getLightRenderer().addLight(mceSpotLightData);
                }
            });
        });

        // Client Receiver For Sync Radial Regions Data Payloads
        ClientPlayNetworking.registerGlobalReceiver(SyncRadialRegionsDataPayload.ID, (payload, context) -> {
            List<RadialRegionData> synchronizedSet = payload.radialRegions();

            context.client().execute(() -> {
                // Clear The Current Local Regions Set And Replace It With The One From The Server
                RegionsManager.activeRadialRegions.clear();
                RegionsManager.activeRadialRegions.addAll(synchronizedSet);
            });
        });

        // Client Receiver For Sync Linear Regions Data Payloads
        ClientPlayNetworking.registerGlobalReceiver(SyncLinearRegionsDataPayload.ID, (payload, context) -> {
            List<LinearRegionData> synchronizedSet = payload.linearRegionData();

            context.client().execute(() -> {
                // Clear The Current Local Regions Set And Replace It With The One From The Server
                RegionsManager.activeLinearRegions.clear();
                RegionsManager.activeLinearRegions.addAll(synchronizedSet);
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(OpenEditorUIPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                if (payload.allowed()) {
                    UIManager.showEngineUI = !UIManager.showEngineUI;
                }
                else {
                    UIManager.showEngineUI = false;
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(SyncGamerulesDataPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                WorldGamerulesUI.doDaylightCycle = payload.doDaylightCycle();
                WorldGamerulesUI.doWeatherCycle = payload.doWeatherCycle();
                WorldGamerulesUI.sendCommandFeedback = payload.sendCommandFeedback();
                WorldGamerulesUI.commandBlockOutput = payload.commandBlockOutput();
                WorldGamerulesUI.doFireTick = payload.doFireTick();
                WorldGamerulesUI.doImmediateRespawn = payload.doImmediateRespawn();
                WorldGamerulesUI.doMobSpawning = payload.doMobSpawning();
                WorldGamerulesUI.fallDamage = payload.fallDamage();
                WorldGamerulesUI.keepInventory = payload.keepInventory();
                WorldGamerulesUI.mobGriefing = payload.mobGriefing();
                WorldGamerulesUI.naturalRegeneration = payload.naturalRegeneration();
                WorldGamerulesUI.showDeathMessages = payload.showDeathMessages();
            });
        });

        // Looping Sound Packet Reciever
        ClientPlayNetworking.registerGlobalReceiver(RegionSoundStatePayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                ClientPlayerEntity player = context.player();
                String soundID = payload.soundID();

                if (payload.play()) {
                    if (!ACTIVE_SOUNDS.containsKey(soundID)) {
                        Identifier identifer = Identifier.of(soundID);

                        SoundEvent event = SoundEvent.of(identifer);

                        RegionSoundInstance sound = new RegionSoundInstance(player, event, payload.volume(), payload.fadeDuration());
                        MinecraftClient.getInstance().getSoundManager().play(sound);
                        ACTIVE_SOUNDS.put(soundID, sound);
                    }
                }
                else
                {
                    RegionSoundInstance sound = ACTIVE_SOUNDS.remove(soundID);
                    if (sound != null) {
                        sound.fadeAndStop();
                    }
                }
            });
        });
    }


}