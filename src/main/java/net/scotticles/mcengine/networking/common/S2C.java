package net.scotticles.mcengine.networking.common;


import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.scotticles.mcengine.networking.editor.payloads.OpenEditorUIPayload;
import net.scotticles.mcengine.networking.gamerules.SyncGamerulesEditorPayload;
import net.scotticles.mcengine.networking.regions.payloads.SyncRegionsDataPayload;
import net.scotticles.mcengine.regions.RegionsManager;
import net.scotticles.mcengine.regions.regiondatasaving.RegionData;
import net.scotticles.mcengine.ui.UIManager;
import net.scotticles.mcengine.ui.windows.WorldGamerulesUI;

public class S2C {
    public static void registerPacketChannels() {
        // Register Server -> Client Packet Channels
        PayloadTypeRegistry.playS2C().register(SyncRegionsDataPayload.ID, SyncRegionsDataPayload.CODEC);

        PayloadTypeRegistry.playS2C().register(OpenEditorUIPayload.TYPE, OpenEditorUIPayload.CODEC);

        PayloadTypeRegistry.playS2C().register(SyncGamerulesEditorPayload.TYPE, SyncGamerulesEditorPayload.CODEC);
    }

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

        ClientPlayNetworking.registerGlobalReceiver(SyncGamerulesEditorPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                WorldGamerulesUI.doDaylightCycle = payload.doDaylightCycle();
                WorldGamerulesUI.doWeatherCycle = payload.doWeatherCycle();
                WorldGamerulesUI.sendCommandFeedback = payload.sendCommandFeedback();
            });
        });
    }
}
