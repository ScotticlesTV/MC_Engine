package net.scotticles.mcengine.networking.common;

import foundry.veil.api.client.render.VeilRenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;
import net.scotticles.mcengine.lights.LightsManager;
import net.scotticles.mcengine.lights.lightdatasaving.MCEAreaLightData;
import net.scotticles.mcengine.lights.lightdatasaving.MCEDirectionalLightData;
import net.scotticles.mcengine.lights.lightdatasaving.MCEPointLightData;
import net.scotticles.mcengine.lights.lightdatasaving.MCESpotLightData;
import net.scotticles.mcengine.networking.editor.payloads.OpenEditorUIPayload;
import net.scotticles.mcengine.networking.gamerules.payloads.SyncGamerulesDataPayload;
import net.scotticles.mcengine.networking.lights.payloads.*;
import net.scotticles.mcengine.networking.regions.payloads.RegionSoundStatePayload;
import net.scotticles.mcengine.networking.regions.payloads.SyncLinearRegionsDataPayload;
import net.scotticles.mcengine.networking.regions.payloads.SyncRadialRegionsDataPayload;
import net.scotticles.mcengine.regions.RegionSoundInstance;
import net.scotticles.mcengine.regions.RegionsManager;
import net.scotticles.mcengine.regions.regiondatasaving.LinearRegionData;
import net.scotticles.mcengine.regions.regiondatasaving.RadialRegionData;
import net.scotticles.mcengine.ui.UIManager;
import net.scotticles.mcengine.ui.windows.WorldGamerulesUI;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class S2C {
    private static final Map<String, RegionSoundInstance> ACTIVE_SOUNDS = new HashMap<>();

    // Register Channels For Packets To Travel On From Server To Client
    public static void registerPacketChannels() {
        PayloadTypeRegistry.playS2C().register(SyncRadialRegionsDataPayload.ID, SyncRadialRegionsDataPayload.CODEC);

        PayloadTypeRegistry.playS2C().register(SyncLinearRegionsDataPayload.ID, SyncLinearRegionsDataPayload.CODEC);

        PayloadTypeRegistry.playS2C().register(OpenEditorUIPayload.TYPE, OpenEditorUIPayload.CODEC);

        PayloadTypeRegistry.playS2C().register(SyncGamerulesDataPayload.TYPE, SyncGamerulesDataPayload.CODEC);

        PayloadTypeRegistry.playS2C().register(RegionSoundStatePayload.TYPE, RegionSoundStatePayload.CODEC);

        PayloadTypeRegistry.playS2C().register(SyncAreaLightsDataPayload.ID, SyncAreaLightsDataPayload.CODEC);

        PayloadTypeRegistry.playS2C().register(SyncPointLightsDataPayload.ID, SyncPointLightsDataPayload.CODEC);

        PayloadTypeRegistry.playS2C().register(SyncDirectionalLightsDataPayload.ID, SyncDirectionalLightsDataPayload.CODEC);

        PayloadTypeRegistry.playS2C().register(SyncSpotLightsDataPayload.ID, SyncSpotLightsDataPayload.CODEC);

        PayloadTypeRegistry.playS2C().register(ClearLightRendererPayload.TYPE, ClearLightRendererPayload.CODEC);
    }

    // Register Receivers That Handle Actions When The Client Receives A Packet
    public static void registerClientReceivers() {
        // Client Receiver For Clear Light Renderer Payloads
        ClientPlayNetworking.registerGlobalReceiver(ClearLightRendererPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                // Clear The Light Renderer To Prep For The Incoming New Lights
                VeilRenderSystem.renderer().getLightRenderer().free();
            });
        });

        // Client Receiver For Sync Area Light Data Payloads
        ClientPlayNetworking.registerGlobalReceiver(SyncAreaLightsDataPayload.ID, (payload, context) -> {
            List<MCEAreaLightData> synchronizedSet = payload.areaLights();

            context.client().execute(() -> {
                // Clear The Current Local Regions Set And Replace It With The One From The Server
                LightsManager.activeAreaLights.clear();
                LightsManager.activeAreaLights.addAll(synchronizedSet);
                for (MCEAreaLightData mceAreaLightData : synchronizedSet) {
                    VeilRenderSystem.renderer().getLightRenderer().addLight(mceAreaLightData);
                }
            });
        });

        // Client Receiver For Sync Point Light Data Payloads
        ClientPlayNetworking.registerGlobalReceiver(SyncPointLightsDataPayload.ID, (payload, context) -> {
            List<MCEPointLightData> synchronizedSet = payload.pointLights();

            context.client().execute(() -> {
                // Clear The Current Local Regions Set And Replace It With The One From The Server
                LightsManager.activePointLights.clear();
                LightsManager.activePointLights.addAll(synchronizedSet);
                for (MCEPointLightData mcePointLightData : synchronizedSet) {
                    VeilRenderSystem.renderer().getLightRenderer().addLight(mcePointLightData);
                }
            });
        });

        // Client Receiver For Sync Directional Light Data Payloads
        ClientPlayNetworking.registerGlobalReceiver(SyncDirectionalLightsDataPayload.ID, (payload, context) -> {
            List<MCEDirectionalLightData> synchronizedSet = payload.directionalLights();

            context.client().execute(() -> {
                // Clear The Current Local Regions Set And Replace It With The One From The Server
                LightsManager.activeDirectionalLights.clear();
                LightsManager.activeDirectionalLights.addAll(synchronizedSet);
                for (MCEDirectionalLightData mceDirectionalLightData : synchronizedSet) {
                    VeilRenderSystem.renderer().getLightRenderer().addLight(mceDirectionalLightData);
                }
            });
        });

        // Client Receiver For Sync Spot Light Data Payloads
        ClientPlayNetworking.registerGlobalReceiver(SyncSpotLightsDataPayload.ID, (payload, context) -> {
            List<MCESpotLightData> synchronizedSet = payload.spotLights();

            context.client().execute(() -> {
                // Clear The Current Local Regions Set And Replace It With The One From The Server
                LightsManager.activeSpotLights.clear();
                LightsManager.activeSpotLights.addAll(synchronizedSet);
                for (MCESpotLightData mceSpotLightData : synchronizedSet) {
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

            //Sync Lights
//            ServerPlayNetworking.send(player, new ClearLightRendererPayload());
            ServerPlayNetworking.send(player, new SyncAreaLightsDataPayload(LightsManager.activeAreaLights));
            ServerPlayNetworking.send(player, new SyncPointLightsDataPayload(LightsManager.activePointLights));
            ServerPlayNetworking.send(player, new SyncDirectionalLightsDataPayload(LightsManager.activeDirectionalLights));
            ServerPlayNetworking.send(player, new SyncSpotLightsDataPayload(LightsManager.activeSpotLights));
        });
    }
}
