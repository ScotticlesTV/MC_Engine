package net.scotticles.mcengine.regions;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.scotticles.mcengine.regions.regiondatasaving.RegionData;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


//Not Functional If There Are Multiple Regions
public class RegionEffectsManager {

    //Initiate Regions Functionality
    public static void regionEffectsInit(Set<RegionData> regions) {
        ServerTickEvents.END_SERVER_TICK.register((server) -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                UUID playerUuid = player.getUuid();
                for  (RegionData region : regions) {
                    if (player.getWorld().getRegistryKey() == World.OVERWORLD) {
                        Vec3d regionPos = new Vec3d(region.regionX, region.regionY, region.regionZ);
                        boolean isInRadius = player.getPos().squaredDistanceTo(regionPos) <= region.regionRadius * region.regionRadius;
                        if ((isInRadius)) {
                            if (region.playersInside.add(playerUuid)) {
                                if (region.regionEnabled) {
                                    for (String command : region.regionEnterCommands) {
                                        server.getCommandManager().executeWithPrefix(server.getCommandSource().withPosition(player.getPos()), command);
                                    }
                                }
                            }
                        }
                        else {
                            if (region.playersInside.remove(playerUuid)) {
                                if  (region.regionEnabled) {
                                    for (String command : region.regionExitCommands) {
                                        server.getCommandManager().executeWithPrefix(server.getCommandSource().withPosition(player.getPos()), command);
                                    }
                                }
                            }
                        }
                    }
                    else {
                        region.playersInside.remove(playerUuid);
                    }
                }
            }
        });
    }
}