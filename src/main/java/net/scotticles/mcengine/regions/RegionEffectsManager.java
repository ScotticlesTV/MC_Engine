package net.scotticles.mcengine.regions;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.scotticles.mcengine.networking.regions.payloads.RegionSoundStatePayload;
import net.scotticles.mcengine.regions.regiondatasaving.LinearRegionData;
import net.scotticles.mcengine.regions.regiondatasaving.RadialRegionData;

import java.util.List;
import java.util.UUID;

public class RegionEffectsManager {
    /**
     * Activate entry and exit commands and sound playing for all region types.
     * @param radialRegions the actively tracked set of Radial Region Data objects.
     * @param linearRegions the actively tracked set of Linear Region Data objects.
     */
    public static void regionEffectsInit(List<RadialRegionData> radialRegions, List<LinearRegionData> linearRegions) {
        ServerTickEvents.END_SERVER_TICK.register((server) -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                // Get The Player's UUID To More Easily Track Them
                UUID playerUuid = player.getUuid();

                // Loop through all Radial Regions to enable effects for all of them.
                for  (RadialRegionData region : radialRegions) {
                    Vec3d regionPos = new Vec3d(region.getX(), region.getY(), region.getZ());
                    // Player is in the Region if they are within its radius and in the correct dimension (currently just overworld).
                    boolean isInRegion = player.getPos().squaredDistanceTo(regionPos) <= region.getRadius() * region.getRadius() && player.getWorld().getRegistryKey() == World.OVERWORLD;
                    // Check if the player is in the region.
                    if (isInRegion) {
                        // If the player wasn't in the Region, but now is, mark them as now within it and run commands only if the Region is enabled.
                        if (region.getPlayersInside().add(playerUuid) && region.isEnabled()) {
                            // Run All Enter Commands
                            for (String command : region.getEnterCommands()) {
                                server.getCommandManager().executeWithPrefix(server.getCommandSource().withPosition(player.getPos()), command);
                            }
                            // Send A Packet To Have The Player's Client Play The Sound
                            ServerPlayNetworking.send(player, new RegionSoundStatePayload(region.getSound(), true, region.getSoundVolume(), region.getSoundFadeDuration()));
                        }
                    } else {
                        // If the player was in the Region, but now isn't, mark them as no longer within it and run commands only if the Region is enabled.
                        if (region.getPlayersInside().remove(playerUuid) && region.isEnabled()) {
                            // Run All Exit Commands
                            for (String command : region.getExitCommands()) {
                                server.getCommandManager().executeWithPrefix(server.getCommandSource().withPosition(player.getPos()), command);
                            }
                            ServerPlayNetworking.send(player, new RegionSoundStatePayload(region.getSound(), false, region.getSoundVolume(), region.getSoundFadeDuration()));
                        }
                    }
                }

                // Loop through all Linear Region to enable effects for all of them.
                for (LinearRegionData region : linearRegions) {

                    BlockBox box = BlockBox.create(
                            new Vec3i(region.getPos1X(), region.getPos1Y(), region.getPos1Z()), new Vec3i(region.getPos2X(), region.getPos2Y(), region.getPos2Z())
                    );

                    boolean isInRegion = box.contains(player.getBlockPos()) && player.getWorld().getRegistryKey() == World.OVERWORLD;
                    if (isInRegion) {
                        // If the player wasn't in the Region, but now is, mark them as now within it and run commands only if the Region is enabled.
                        if (region.getPlayersInside().add(playerUuid) && region.isEnabled()) {
                            // Run All Enter Commands
                            for (String command : region.getEnterCommands()) {
                                server.getCommandManager().executeWithPrefix(server.getCommandSource().withPosition(player.getPos()), command);
                            }
                            // Send A Packet To Have The Player's Client Play The Sound
                            ServerPlayNetworking.send(player, new RegionSoundStatePayload(region.getSound(), true, region.getSoundVolume(), region.getSoundFadeDuration()));
                        }
                    } else {
                        // If the player was in the Region, but now isn't, mark them as no longer within it and run commands only if the Region is enabled.
                        if (region.getPlayersInside().remove(playerUuid) && region.isEnabled()) {
                            // Run All Exit Commands
                            for (String command : region.getExitCommands()) {
                                server.getCommandManager().executeWithPrefix(server.getCommandSource().withPosition(player.getPos()), command);
                            }
                            ServerPlayNetworking.send(player, new RegionSoundStatePayload(region.getSound(), false, region.getSoundVolume(), region.getSoundFadeDuration()));
                        }
                    }

                }
            }
        });
    }
}