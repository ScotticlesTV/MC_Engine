package net.scotticles.mcengine.networking.common;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.level.ServerWorldProperties;
import net.scotticles.mcengine.networking.editor.payloads.OpenEditorUIPayload;
import net.scotticles.mcengine.networking.editor.payloads.CheckEditorUIPermsPayload;
import net.scotticles.mcengine.networking.gamerules.payloads.SyncGamerulesDataPayload;
import net.scotticles.mcengine.networking.regions.payloads.SyncRegionsDataPayload;
import net.scotticles.mcengine.networking.time.payloads.SetTimePayload;
import net.scotticles.mcengine.networking.weather.payloads.ChangeWeatherPayload;
import net.scotticles.mcengine.regions.regiondatasaving.RegionDataManager;
import net.scotticles.mcengine.regions.RegionsManager;
import net.scotticles.mcengine.regions.regiondatasaving.RegionData;

import java.util.Objects;
import java.util.Set;

public class C2S {


    // Register Channels For Packets To Travel On From Client To Server
    public static void registerPacketChannels() {
        PayloadTypeRegistry.playC2S().register(SyncRegionsDataPayload.ID, SyncRegionsDataPayload.CODEC);

        PayloadTypeRegistry.playC2S().register(CheckEditorUIPermsPayload.TYPE, CheckEditorUIPermsPayload.CODEC);

        PayloadTypeRegistry.playC2S().register(SyncGamerulesDataPayload.TYPE, SyncGamerulesDataPayload.CODEC);

        PayloadTypeRegistry.playC2S().register(ChangeWeatherPayload.TYPE, ChangeWeatherPayload.CODEC);

        PayloadTypeRegistry.playC2S().register(SetTimePayload.TYPE, SetTimePayload.CODEC);
    }


    // Register Receivers That Handle Actions When The Server Receives A Packet
    public static void registerServerRecievers() {

        // Server SyncRegionsDataPayload Receiver
        ServerPlayNetworking.registerGlobalReceiver(SyncRegionsDataPayload.ID, (payload, context) -> {

            // Set The Updated Region Data Acquired From The Client To a Local Variable To Be Reused
            Set<RegionData> updatedRegions = payload.regions();

            context.server().execute(() -> {
                // Overwrite The Server's Region Data With The Updated Region Data From The Client
                RegionsManager.activeRegions.clear();
                RegionsManager.activeRegions.addAll(updatedRegions);

                // Save The Updated Region Data To The Server's File
                RegionDataManager.save(updatedRegions, context.server());

                // Sync The Updated Region Data To All Players On The Server
                SyncRegionsDataPayload syncPayload = new SyncRegionsDataPayload(updatedRegions);
                for (net.minecraft.server.network.ServerPlayerEntity player : context.server().getPlayerManager().getPlayerList()) {
                    ServerPlayNetworking.send(player, syncPayload);
                }
            });
        });

        // Server RequestEditorUIPermsPayload Receiver
        ServerPlayNetworking.registerGlobalReceiver(CheckEditorUIPermsPayload.TYPE, (payload, context) -> {
            ServerPlayerEntity player = context.player();
            if (player.hasPermissionLevel(2)) {
                ServerPlayNetworking.send(player, new OpenEditorUIPayload(true));
            }
            else {
                ServerPlayNetworking.send(player, new OpenEditorUIPayload(false));
            }
        });


        // Server SyncGamerulesPayload Receiver
        ServerPlayNetworking.registerGlobalReceiver(SyncGamerulesDataPayload.TYPE, (payload, context) -> {
            context.server().execute(() -> {

                // Get The Server's Gamerules
                GameRules gameRules = context.server().getGameRules();

                //Change Server Gamerules Based On The Client's Changes
                gameRules.get(GameRules.DO_DAYLIGHT_CYCLE).set(payload.doDaylightCycle(), context.server());
                gameRules.get(GameRules.DO_WEATHER_CYCLE).set(payload.doWeatherCycle(), context.server());
                gameRules.get(GameRules.SEND_COMMAND_FEEDBACK).set(payload.sendCommandFeedback(), context.server());

                // Sync Gamerules To All Editors
                for (net.minecraft.server.network.ServerPlayerEntity player : context.server().getPlayerManager().getPlayerList()) {
                    ServerPlayNetworking.send(player, new SyncGamerulesDataPayload(
                            payload.doDaylightCycle(),
                            payload.doWeatherCycle(),
                            payload.sendCommandFeedback()
                            )
                    );
                }
            });
        });

        // Server ChangeWeatherPayload Receiver
        ServerPlayNetworking.registerGlobalReceiver(ChangeWeatherPayload.TYPE, (payload, context) -> {
            context.server().execute(() -> {
                MinecraftServer server = context.server();

                ServerWorldProperties worldProperties = (ServerWorldProperties) server.getWorld(World.OVERWORLD).getServer().getOverworld().getLevelProperties();

                if (Objects.equals(payload.weatherType(), "clear")) {
                    // Debug Broadcasting (Maybe A Debug Mode For Receiving Messages Like These Would Be Good)
                    // server.getPlayerManager().broadcast(Text.literal("Weather Packet: Rain - Received"),false);

                    worldProperties.setRaining(false);
                    worldProperties.setThundering(false);
                }
                else if (Objects.equals(payload.weatherType(), "rain")) {
                    worldProperties.setRaining(true);
                    worldProperties.setThundering(false);
                }
                else {
                    worldProperties.setRaining(true);
                    worldProperties.setThundering(true);
                }
            });
        });

        // Server SetTimePayload Receiver
        ServerPlayNetworking.registerGlobalReceiver(SetTimePayload.TYPE, (payload, context) -> {
            context.server().execute(() -> {
                MinecraftServer server = context.server();
                ServerPlayerEntity player = context.player();

                if (Objects.equals(payload.worldTime(), "day")) {
                    player.getServerWorld().setTimeOfDay(24000);
                }
                if (Objects.equals(payload.worldTime(), "noon")) {
                    player.getServerWorld().setTimeOfDay(6000);
                }
                if (Objects.equals(payload.worldTime(), "night")) {
                    player.getServerWorld().setTimeOfDay(13000);
                }
                if (Objects.equals(payload.worldTime(), "midnight")) {
                    player.getServerWorld().setTimeOfDay(18000);
                }
            });
        });
    }
}