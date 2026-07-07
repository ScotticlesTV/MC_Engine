package net.scotticles.mcengine.networking.common;


import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.scotticles.mcengine.networking.editor.payloads.OpenEditorUIPayload;
import net.scotticles.mcengine.networking.regions.payloads.sendRegionsDataPayload;
import net.scotticles.mcengine.regions.RegionsManager;
import net.scotticles.mcengine.regions.regiondatasaving.RegionData;
import net.scotticles.mcengine.ui.UIManager;

public class S2C {
    public static void registerPacketChannels() {
        // Register Server -> Client Packet Channels
        PayloadTypeRegistry.playS2C().register(sendRegionsDataPayload.ID, sendRegionsDataPayload.CODEC);

        PayloadTypeRegistry.playS2C().register(OpenEditorUIPayload.TYPE, OpenEditorUIPayload.CODEC);
    }

    public static void registerClientReceivers() {
        ClientPlayNetworking.registerGlobalReceiver(sendRegionsDataPayload.ID, (payload, context) -> {
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
    }
}
