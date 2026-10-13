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

import java.util.Set;
import java.util.UUID;

public class RegionEffectsManager {
    /**
     * Activate entry and exit commands and sound playing for all region types.
     * @param radialRegions, the actively tracked set of Radial Region Data objects.
     * @param linearRegions, the actively tracked set of Linear Region Data objects.
     */
    public static void regionEffectsInit(Set<RadialRegionData> radialRegions, Set<LinearRegionData> linearRegions) {
        ServerTickEvents.END_SERVER_TICK.register((server) -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                // Get The Player's UUID To More Easily Track Them
                UUID playerUuid = player.getUuid();

                // Loop Through All Radial Regions To Enable Effects For All Of Them
                for  (RadialRegionData region : radialRegions) {


                    // For Condensing This To Less If Statements, Consider:
                    // Checking Dimension and isInRadius in one if statement
                    // Using region.regionEnabled as a wrapper/first check

                    // Confirm That The Player Is In The Overworld
                    // (Can Be Changed Later To Have Regions In Multiple Dimensions)
                    // (Just Need To Add A "Select Dimension" Checkbox To Each Region)
                    if (player.getWorld().getRegistryKey() == World.OVERWORLD) {

                        // Check If The Player Is In Range Of The Region
                        Vec3d regionPos = new Vec3d(region.regionX, region.regionY, region.regionZ);
                        boolean isInRadius = player.getPos().squaredDistanceTo(regionPos) <= region.regionRadius * region.regionRadius;
                        if (isInRadius) {
                            // If The Player Isn't Tracked In The Region, Add Their UUID To Track
                            if (region.playersInside.add(playerUuid)) {
                                // Check If The Region Is Enabled
                                if (region.regionEnabled) {
                                    // Run All Enter Commands
                                    for (String command : region.regionEnterCommands) {
                                        server.getCommandManager().executeWithPrefix(server.getCommandSource().withPosition(player.getPos()), command);
                                    }
                                    // Send A Packet To Have The Player's Client Play The Sound
                                    ServerPlayNetworking.send(player, new RegionSoundStatePayload(region.regionSound, true, region.regionSoundVolume, region.regionSoundFadeDuration));
                                }
                            }
                        }
                        // Check If The Player Isn't In Range Of The Region
                        else {
                            // If They Were In The Radius, Remove Their UUID To Stop Tracking
                            if (region.playersInside.remove(playerUuid)) {
                                // Check If The Region Is Enabled
                                if  (region.regionEnabled) {
                                    // Run All Exit Commands
                                    for (String command : region.regionExitCommands) {
                                        server.getCommandManager().executeWithPrefix(server.getCommandSource().withPosition(player.getPos()), command);
                                    }
                                    ServerPlayNetworking.send(player, new RegionSoundStatePayload(region.regionSound, false, region.regionSoundVolume, region.regionSoundFadeDuration));
                                }
                            }
                        }
                    }
                    // If The Player Isn't In The Overworld, Remove Them From The Region
                    else {
                        region.playersInside.remove(playerUuid);
                    }
                }

                // Loop Through All Linear Regions To Enable Effects For All Of Them
                for (LinearRegionData region : linearRegions) {
                    if (player.getWorld().getRegistryKey() == World.OVERWORLD) {

                        BlockBox box = BlockBox.create(
                                new Vec3i(region.regionX1 - 1, region.regionY1, region.regionZ1), new Vec3i(region.regionX2 - 1, region.regionY2, region.regionZ2)
                        );

                        boolean isInRadius = box.contains(player.getBlockPos());
                        if (isInRadius) {
                            if (region.playersInside.add(playerUuid)) {
                                // Check If The Region Is Enabled
                                if (region.regionEnabled) {
                                    // Run All Enter Commands
                                    for (String command : region.regionEnterCommands) {
                                        server.getCommandManager().executeWithPrefix(server.getCommandSource().withPosition(player.getPos()), command);
                                    }
                                    // Send A Packet To Have The Player's Client Play The Sound
                                    ServerPlayNetworking.send(player, new RegionSoundStatePayload(region.regionSound, true, region.regionSoundVolume, region.regionSoundFadeDuration));
                                }
                            }
                        } else {
                            // If They Were In The Radius, Remove Their UUID To Stop Tracking
                            if (region.playersInside.remove(playerUuid)) {
                                // Check If The Region Is Enabled
                                if  (region.regionEnabled) {
                                    // Run All Exit Commands
                                    for (String command : region.regionExitCommands) {
                                        server.getCommandManager().executeWithPrefix(server.getCommandSource().withPosition(player.getPos()), command);
                                    }
                                    ServerPlayNetworking.send(player, new RegionSoundStatePayload(region.regionSound, false, region.regionSoundVolume, region.regionSoundFadeDuration));
                                }
                            }
                        }
                    } else {
                        region.playersInside.remove(playerUuid);
                    }
                }
            }
        });
    }
}