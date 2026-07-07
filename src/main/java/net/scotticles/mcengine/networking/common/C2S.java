package net.scotticles.mcengine.networking.common;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.scotticles.mcengine.networking.editor.payloads.OpenEditorUIPayload;
import net.scotticles.mcengine.networking.editor.payloads.RequestEditorUIPermsPayload;
import net.scotticles.mcengine.networking.regions.payloads.sendRegionsDataPayload;
import net.scotticles.mcengine.regions.RegionDataManager;
import net.scotticles.mcengine.regions.RegionsManager;
import net.scotticles.mcengine.regions.regiondatasaving.RegionData;

public class C2S {

    public static void registerPacketChannels() {
        // Register Client -> Server Packet Channel
        PayloadTypeRegistry.playC2S().register(sendRegionsDataPayload.ID, sendRegionsDataPayload.CODEC);

        PayloadTypeRegistry.playC2S().register(RequestEditorUIPermsPayload.TYPE, RequestEditorUIPermsPayload.CODEC);
    }

    public static void registerServerRecievers() {
        ServerPlayNetworking.registerGlobalReceiver(sendRegionsDataPayload.ID, (payload, context) -> {
            // 1. Unpack incoming dataset from the client editor
            java.util.Set<RegionData> updatedRegions = payload.regions();

            context.server().execute(() -> {
                // 2. Overwrite the server's master list
                RegionsManager.activeRegions.clear();
                RegionsManager.activeRegions.addAll(updatedRegions);

                // 3. PASS BOTH THE SERVER CONTEXT AND REGION DATA SET
                // This supplies your save method with the server instance and the fresh data
                RegionDataManager.save(updatedRegions, context.server());

                // 4. Broadcast the update to all active players to keep them synchronized
                sendRegionsDataPayload syncPayload = new sendRegionsDataPayload(updatedRegions);
                for (net.minecraft.server.network.ServerPlayerEntity player : context.server().getPlayerManager().getPlayerList()) {
                    ServerPlayNetworking.send(player, syncPayload);
                }
            });
        });

        // Server EditorUIKeybindPayload Receiver
        ServerPlayNetworking.registerGlobalReceiver(RequestEditorUIPermsPayload.TYPE, (payload, context) -> {
            ServerPlayerEntity player = context.player();
            if (player.hasPermissionLevel(2)) {
                ServerPlayNetworking.send(player, new OpenEditorUIPayload(true));
            }
            else {
                ServerPlayNetworking.send(player, new OpenEditorUIPayload(false));
            }
        });
    }
}
