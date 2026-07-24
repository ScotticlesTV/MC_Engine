package net.scotticles.mcengine.networking.common;


import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameRules;
import net.scotticles.mcengine.networking.editor.payloads.OpenEditorUIPayload;
import net.scotticles.mcengine.networking.gamerules.payloads.SyncGamerulesDataPayload;
import net.scotticles.mcengine.networking.regions.payloads.SyncRegionsDataPayload;
import net.scotticles.mcengine.regions.RegionsManager;
import net.scotticles.mcengine.regions.regiondatasaving.RegionData;
import net.scotticles.mcengine.ui.UIManager;
import net.scotticles.mcengine.ui.windows.WorldGamerulesUI;

public class S2C {

    // Register Channels For Packets To Travel On From Server To Client
    public static void registerPacketChannels() {
        PayloadTypeRegistry.playS2C().register(SyncRegionsDataPayload.ID, SyncRegionsDataPayload.CODEC);

        PayloadTypeRegistry.playS2C().register(OpenEditorUIPayload.TYPE, OpenEditorUIPayload.CODEC);

        PayloadTypeRegistry.playS2C().register(SyncGamerulesDataPayload.TYPE, SyncGamerulesDataPayload.CODEC);
    }

    // Register Receivers That Handle Actions When The Client Receives A Packet
    public static void registerClientReceivers() {
        ClientPlayNetworking.registerGlobalReceiver(SyncRegionsDataPayload.ID, (payload, context) -> {
            java.util.Set<RegionData> synchronizedSet = payload.regions();

            context.client().execute(() -> {
                // Clear The Current Local Regions Set And Replace It With The One From The Server
                RegionsManager.activeRegions.clear();
                RegionsManager.activeRegions.addAll(synchronizedSet);
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
                    gameRules.get(GameRules.SEND_COMMAND_FEEDBACK).get()));


            //Sync Regions
            ServerPlayNetworking.send(player, new SyncRegionsDataPayload(RegionsManager.activeRegions));
        });
    }
}
