package net.scotticles.mcengine.networking.common;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameRules;
import net.scotticles.mcengine.networking.editor.payloads.OpenEditorUIPayload;
import net.scotticles.mcengine.networking.editor.payloads.RequestEditorUIPermsPayload;
import net.scotticles.mcengine.networking.gamerules.SyncGamerulesEditorPayload;
import net.scotticles.mcengine.networking.regions.payloads.SyncRegionsDataPayload;
import net.scotticles.mcengine.regions.RegionDataManager;
import net.scotticles.mcengine.regions.RegionsManager;
import net.scotticles.mcengine.regions.regiondatasaving.RegionData;

public class C2S {

    public static void registerPacketChannels() {
        // Register Client -> Server Packet Channel
        PayloadTypeRegistry.playC2S().register(SyncRegionsDataPayload.ID, SyncRegionsDataPayload.CODEC);

        PayloadTypeRegistry.playC2S().register(RequestEditorUIPermsPayload.TYPE, RequestEditorUIPermsPayload.CODEC);

        PayloadTypeRegistry.playC2S().register(SyncGamerulesEditorPayload.TYPE, SyncGamerulesEditorPayload.CODEC);
    }

    public static void registerServerRecievers() {
        // Server SyncRegionsDataPayload Receiver
        ServerPlayNetworking.registerGlobalReceiver(SyncRegionsDataPayload.ID, (payload, context) -> {
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
                SyncRegionsDataPayload syncPayload = new SyncRegionsDataPayload(updatedRegions);
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


        // Server SyncRegionsDataPayload Receiver
        ServerPlayNetworking.registerGlobalReceiver(SyncGamerulesEditorPayload.TYPE, (payload, context) -> {
            context.server().execute(() -> {
                boolean doDaylightCycle = payload.doDaylightCycle();
                boolean doWeatherCycle = payload.doWeatherCycle();
                boolean sendCommandFeedback = payload.sendCommandFeedback();



                MinecraftServer server = context.server();
                GameRules gameRules = server.getGameRules();

                //Change Server Gamerules
                gameRules.get(GameRules.DO_DAYLIGHT_CYCLE).set(doDaylightCycle, server);
                gameRules.get(GameRules.DO_WEATHER_CYCLE).set(doWeatherCycle, server);
                gameRules.get(GameRules.SEND_COMMAND_FEEDBACK).set(sendCommandFeedback, server);

                // Sync Change To All Editors
                for (net.minecraft.server.network.ServerPlayerEntity player : context.server().getPlayerManager().getPlayerList()) {
                    ServerPlayNetworking.send(player, new SyncGamerulesEditorPayload(doDaylightCycle, doWeatherCycle, sendCommandFeedback));
                }
            });
        });
    }
}
