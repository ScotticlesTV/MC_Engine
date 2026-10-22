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
     * @param radialRegions, the actively tracked set of Radial Region Data objects.
     * @param linearRegions, the actively tracked set of Linear Region Data objects.
     */
    public static void regionEffectsInit(List<RadialRegionData> radialRegions, List<LinearRegionData> linearRegions) {
        ServerTickEvents.END_SERVER_TICK.register((server) -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                // Get The Player's UUID To More Easily Track Them
                UUID playerUuid = player.getUuid();

                // Loop Through All Radial Regions To Enable Effects For All Of Them
                for  (RadialRegionData region : radialRegions) {

                    // TODO(priority:low category:Cleanup @Scott): Rework + simplify region effect functionality.
                    // For Condensing This To Less If Statements, Consider:
                    // Checking Dimension and isInRadius in one if statement
                    // Using region.regionEnabled as a wrapper/first check

                    // Confirm That The Player Is In The Overworld
                    // (Can Be Changed Later To Have Regions In Multiple Dimensions)
                    // (Just Need To Add A "Select Dimension" Checkbox To Each Region)
                    if (player.getWorld().getRegistryKey() == World.OVERWORLD) {

                        // Check If The Player Is In Range Of The Region
                        Vec3d regionPos = new Vec3d(region.getX(), region.getY(), region.getZ());
                        boolean isInRadius = player.getPos().squaredDistanceTo(regionPos) <= region.getRadius() * region.getRadius();
                        if (isInRadius) {
                            // If The Player Isn't Tracked In The Region, Add Their UUID To Track
                            if (region.getPlayersInside().add(playerUuid)) {
                                // Check If The Region Is Enabled
                                if (region.isEnabled()) {
                                    // Run All Enter Commands
                                    for (String command : region.getEnterCommands()) {
                                        server.getCommandManager().executeWithPrefix(server.getCommandSource().withPosition(player.getPos()), command);
                                    }
                                    // Send A Packet To Have The Player's Client Play The Sound
                                    ServerPlayNetworking.send(player, new RegionSoundStatePayload(region.getSound(), true, region.getSoundVolume(), region.getSoundFadeDuration()));
                                }
                            }
                        }
                        // Check If The Player Isn't In Range Of The Region
                        else {
                            // If They Were In The Radius, Remove Their UUID To Stop Tracking
                            if (region.getPlayersInside().remove(playerUuid)) {
                                // Check If The Region Is Enabled
                                if  (region.isEnabled()) {
                                    // Run All Exit Commands
                                    for (String command : region.getExitCommands()) {
                                        server.getCommandManager().executeWithPrefix(server.getCommandSource().withPosition(player.getPos()), command);
                                    }
                                    ServerPlayNetworking.send(player, new RegionSoundStatePayload(region.getSound(), false, region.getSoundVolume(), region.getSoundFadeDuration()));
                                }
                            }
                        }
                    }
                    // If The Player Isn't In The Overworld, Remove Them From The Region
                    else {
                        region.getPlayersInside().remove(playerUuid);
                    }
                }

                // TODO (priority:high category:Bug-Fix @Scott): North/South facing straight line regions offset by 1.
                // Loop Through All Linear Regions To Enable Effects For All Of Them
                for (LinearRegionData region : linearRegions) {
                    if (player.getWorld().getRegistryKey() == World.OVERWORLD) {

                        BlockBox box = BlockBox.create(
                                new Vec3i(region.getPos1X() - 1, region.getPos1Y(), region.getPos1Z()), new Vec3i(region.getPos2X() - 1, region.getPos2Y(), region.getPos2Z())
                        );

                        boolean isInRadius = box.contains(player.getBlockPos());
                        if (isInRadius) {
                            if (region.getPlayersInside().add(playerUuid)) {
                                // Check If The Region Is Enabled
                                if (region.isEnabled()) {
                                    // Run All Enter Commands
                                    for (String command : region.getEnterCommands()) {
                                        server.getCommandManager().executeWithPrefix(server.getCommandSource().withPosition(player.getPos()), command);
                                    }
                                    // Send A Packet To Have The Player's Client Play The Sound
                                    ServerPlayNetworking.send(player, new RegionSoundStatePayload(region.getSound(), true, region.getSoundVolume(), region.getSoundFadeDuration()));
                                }
                            }
                        } else {
                            // If They Were In The Radius, Remove Their UUID To Stop Tracking
                            if (region.getPlayersInside().remove(playerUuid)) {
                                // Check If The Region Is Enabled
                                if  (region.isEnabled()) {
                                    // Run All Exit Commands
                                    for (String command : region.getExitCommands()) {
                                        server.getCommandManager().executeWithPrefix(server.getCommandSource().withPosition(player.getPos()), command);
                                    }
                                    ServerPlayNetworking.send(player, new RegionSoundStatePayload(region.getSound(), false, region.getSoundVolume(), region.getSoundFadeDuration()));
                                }
                            }
                        }
                    } else {
                        region.getPlayersInside().remove(playerUuid);
                    }
                }
            }
        });
    }
}