package net.scotticles.mcengine.networking.common;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.level.ServerWorldProperties;
import net.scotticles.mcengine.lights.LightsManager;
import net.scotticles.mcengine.lights.lightdatasaving.MCEAreaLightData;
import net.scotticles.mcengine.lights.lightdatasaving.MCEDirectionalLightData;
import net.scotticles.mcengine.lights.lightdatasaving.MCEPointLightData;
import net.scotticles.mcengine.networking.editor.payloads.OpenEditorUIPayload;
import net.scotticles.mcengine.networking.editor.payloads.CheckEditorUIPermsPayload;
import net.scotticles.mcengine.networking.gamerules.payloads.SyncGamerulesDataPayload;
import net.scotticles.mcengine.networking.lights.payloads.ClearLightRendererPayload;
import net.scotticles.mcengine.networking.lights.payloads.SyncAreaLightsDataPayload;
import net.scotticles.mcengine.networking.lights.payloads.SyncDirectionalLightsDataPayload;
import net.scotticles.mcengine.networking.lights.payloads.SyncPointLightsDataPayload;
import net.scotticles.mcengine.networking.regions.payloads.SyncLinearRegionsDataPayload;
import net.scotticles.mcengine.networking.regions.payloads.SyncRadialRegionsDataPayload;
import net.scotticles.mcengine.networking.time.payloads.SetTimePayload;
import net.scotticles.mcengine.networking.weather.payloads.ChangeWeatherPayload;
import net.scotticles.mcengine.regions.regiondatasaving.LinearRegionData;
import net.scotticles.mcengine.regions.regiondatasaving.RegionDataManager;
import net.scotticles.mcengine.regions.RegionsManager;
import net.scotticles.mcengine.regions.regiondatasaving.RadialRegionData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class C2S {


    // Register Channels For Packets To Travel On From Client To Server
    public static void registerPacketChannels() {
        PayloadTypeRegistry.playC2S().register(SyncRadialRegionsDataPayload.ID, SyncRadialRegionsDataPayload.CODEC);

        PayloadTypeRegistry.playC2S().register(SyncLinearRegionsDataPayload.ID, SyncLinearRegionsDataPayload.CODEC);

        PayloadTypeRegistry.playC2S().register(CheckEditorUIPermsPayload.TYPE, CheckEditorUIPermsPayload.CODEC);

        PayloadTypeRegistry.playC2S().register(SyncGamerulesDataPayload.TYPE, SyncGamerulesDataPayload.CODEC);

        PayloadTypeRegistry.playC2S().register(ChangeWeatherPayload.TYPE, ChangeWeatherPayload.CODEC);

        PayloadTypeRegistry.playC2S().register(SetTimePayload.TYPE, SetTimePayload.CODEC);

        PayloadTypeRegistry.playC2S().register(SyncAreaLightsDataPayload.ID, SyncAreaLightsDataPayload.CODEC);

        PayloadTypeRegistry.playC2S().register(SyncPointLightsDataPayload.ID, SyncPointLightsDataPayload.CODEC);

        PayloadTypeRegistry.playC2S().register(SyncDirectionalLightsDataPayload.ID, SyncDirectionalLightsDataPayload.CODEC);

        PayloadTypeRegistry.playC2S().register(ClearLightRendererPayload.TYPE, ClearLightRendererPayload.CODEC);
    }


    // Register Receivers That Handle Actions When The Server Receives A Packet
    public static void registerServerRecievers() {

        // Server ClearLightRenderer Receiver
        ServerPlayNetworking.registerGlobalReceiver(ClearLightRendererPayload.TYPE, (payload, context) -> {
            context.server().execute(() -> {
                // Send A Packet To All Connected Clients To Clear Their Light Renderers
                ClearLightRendererPayload syncPayload = new ClearLightRendererPayload();
                for (net.minecraft.server.network.ServerPlayerEntity player : context.server().getPlayerManager().getPlayerList()) {
                    ServerPlayNetworking.send(player, syncPayload);
                }
            });
        });

        // Server SyncAreaLightsDataPayload Receiver
        ServerPlayNetworking.registerGlobalReceiver(SyncAreaLightsDataPayload.ID, (payload, context) -> {
            List<MCEAreaLightData> updatedAreaLights = payload.areaLights();

            context.server().execute(() -> {
                // Overwrite The Server's Area Light Data List With The Updated Light Data From The Client
                LightsManager.activeAreaLights.clear();
                LightsManager.activeAreaLights.addAll(updatedAreaLights);

                // Sync The Updated Area Lights To All Players On The Server
                SyncAreaLightsDataPayload syncPayload = new SyncAreaLightsDataPayload(updatedAreaLights);
                for (net.minecraft.server.network.ServerPlayerEntity player : context.server().getPlayerManager().getPlayerList()) {
                    ServerPlayNetworking.send(player, syncPayload);
                }
            });
        });

        // Server SyncPointLightsDataPayload Receiver
        ServerPlayNetworking.registerGlobalReceiver(SyncPointLightsDataPayload.ID, (payload, context) -> {
            List<MCEPointLightData> updatedPointLights = payload.pointLights();

            context.server().execute(() -> {
                // Overwrite The Server's Area Light Data List With The Updated Light Data From The Client
                LightsManager.activePointLights.clear();
                LightsManager.activePointLights.addAll(updatedPointLights);

                // Sync The Updated Area Lights To All Players On The Server
                SyncPointLightsDataPayload syncPayload = new SyncPointLightsDataPayload(updatedPointLights);
                for (net.minecraft.server.network.ServerPlayerEntity player : context.server().getPlayerManager().getPlayerList()) {
                    ServerPlayNetworking.send(player, syncPayload);
                }
            });
        });

        // Server SyncDirectionalLightsDataPayload Receiver
        ServerPlayNetworking.registerGlobalReceiver(SyncDirectionalLightsDataPayload.ID, (payload, context) -> {
            List<MCEDirectionalLightData> updatedDirectionalLights = payload.directionalLights();

            context.server().execute(() -> {
                // Overwrite The Server's Area Light Data List With The Updated Light Data From The Client
                LightsManager.activeDirectionalLights.clear();
                LightsManager.activeDirectionalLights.addAll(updatedDirectionalLights);

                // Sync The Updated Area Lights To All Players On The Server
                SyncDirectionalLightsDataPayload syncPayload = new SyncDirectionalLightsDataPayload(updatedDirectionalLights);
                for (net.minecraft.server.network.ServerPlayerEntity player : context.server().getPlayerManager().getPlayerList()) {
                    ServerPlayNetworking.send(player, syncPayload);
                }
            });
        });

        // Server SyncRadialRegionsDataPayload Receiver
        ServerPlayNetworking.registerGlobalReceiver(SyncRadialRegionsDataPayload.ID, (payload, context) -> {

            // Set The Updated Region Data Acquired From The Client To a Local Variable To Be Reused
            List<RadialRegionData> updatedRadialRegions = payload.radialRegions();

            context.server().execute(() -> {
                // Overwrite The Server's Region Data With The Updated Region Data From The Client
                RegionsManager.activeRadialRegions.clear();
                RegionsManager.activeRadialRegions.addAll(updatedRadialRegions);

                // Save The Updated Region Data To The Server's File
                RegionDataManager.save(updatedRadialRegions, RegionsManager.activeLinearRegions, context.server());

                // Sync The Updated Region Data To All Players On The Server
                SyncRadialRegionsDataPayload syncPayload = new SyncRadialRegionsDataPayload(updatedRadialRegions);
                for (net.minecraft.server.network.ServerPlayerEntity player : context.server().getPlayerManager().getPlayerList()) {
                    ServerPlayNetworking.send(player, syncPayload);
                }
            });
        });

        // Server SyncLinearRegionsDataPayload Receiver
        ServerPlayNetworking.registerGlobalReceiver(SyncLinearRegionsDataPayload.ID, (payload, context) -> {

            // Set The Updated Region Data Acquired From The Client To a Local Variable To Be Reused
            List<LinearRegionData> updatedLinearRegions = payload.linearRegionData();

            context.server().execute(() -> {
                // Overwrite The Server's Region Data With The Updated Region Data From The Client
                RegionsManager.activeLinearRegions.clear();
                RegionsManager.activeLinearRegions.addAll(updatedLinearRegions);

                // Save The Updated Region Data To The Server's File
                RegionDataManager.save(RegionsManager.activeRadialRegions, updatedLinearRegions, context.server());

                // Sync The Updated Region Data To All Players On The Server
                SyncLinearRegionsDataPayload syncPayload = new SyncLinearRegionsDataPayload(updatedLinearRegions);
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
                gameRules.get(GameRules.COMMAND_BLOCK_OUTPUT).set(payload.commandBlockOutput(), context.server());
                gameRules.get(GameRules.DO_FIRE_TICK).set(payload.doFireTick(), context.server());
                gameRules.get(GameRules.DO_IMMEDIATE_RESPAWN).set(payload.doImmediateRespawn(), context.server());
                gameRules.get(GameRules.DO_MOB_SPAWNING).set(payload.doMobSpawning(), context.server());
                gameRules.get(GameRules.FALL_DAMAGE).set(payload.fallDamage(), context.server());
                gameRules.get(GameRules.KEEP_INVENTORY).set(payload.keepInventory(), context.server());
                gameRules.get(GameRules.DO_MOB_GRIEFING).set(payload.mobGriefing(), context.server());
                gameRules.get(GameRules.NATURAL_REGENERATION).set(payload.naturalRegeneration(), context.server());
                gameRules.get(GameRules.SHOW_DEATH_MESSAGES).set(payload.showDeathMessages(), context.server());

                // Sync Gamerules To All Editors
                for (net.minecraft.server.network.ServerPlayerEntity player : context.server().getPlayerManager().getPlayerList()) {
                    ServerPlayNetworking.send(player, new SyncGamerulesDataPayload(
                            payload.doDaylightCycle(),
                            payload.doWeatherCycle(),
                            payload.sendCommandFeedback(),
                            payload.commandBlockOutput(),
                            payload.doFireTick(),
                            payload.doImmediateRespawn(),
                            payload.doMobSpawning(),
                            payload.fallDamage(),
                            payload.keepInventory(),
                            payload.mobGriefing(),
                            payload.naturalRegeneration(),
                            payload.showDeathMessages()
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