package net.scotticles.mcengine.regions;


import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.scotticles.mcengine.regions.regiondatasaving.LinearRegionData;
import net.scotticles.mcengine.regions.regiondatasaving.RadialRegionData;
import net.scotticles.mcengine.regions.regiondatasaving.RegionDataManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class RegionsManager {

    // Active Region Sets For Tracking
    public static final Set<RadialRegionData> activeRadialRegions = new HashSet<>();
    public static final Set<LinearRegionData> activeLinearRegions = new HashSet<>();

    /**
     * Activate all region functionality.
     */
    public static void regionsInit() {
        // Load Regions Data On Server Start
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            loadRegions(server);
        });

        // Save Regions Data On Server Stop
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            saveRegions(server);
        });

        // Activate Region Effects
        RegionEffectsManager.regionEffectsInit(activeRadialRegions, activeLinearRegions);
    }

    /**
     * Load all region data from the active world's region data file.
     * @param server, the server used to get the world save file.
     */
    private static void loadRegions(MinecraftServer server) {
        activeRadialRegions.clear();
        activeLinearRegions.clear();
        activeRadialRegions.addAll(RegionDataManager.loadRadialRegions(server));
        activeLinearRegions.addAll(RegionDataManager.loadLinearRegions(server));
    }

    /**
     * Save all region data to the active world's region data file.
     * @param server, the server used to get the world save file.
     */
    private static void saveRegions(MinecraftServer server) {
        RegionDataManager.save(activeRadialRegions, activeLinearRegions , server);
        activeRadialRegions.clear();
        activeLinearRegions.clear();
    }

    /**
     * Make a new Radial Region object and add it to the actively tracked list of Radial Regions.
     * @param regionUuid, the region's UUID.
     * @param regionName, the region's name.
     * @param regionX, the region's x-position.
     * @param regionY, the region's y-position.
     * @param regionZ, the region's z-position.
     * @param regionRadius, the region's radius.
     * @param regionEnterCommands, the region's list of enter commands.
     * @param regionExitCommands, the region's list of exit commands.
     * @param playersInside, the list of players currently within the region.
     * @param regionEnabled, whether or not the region is enabled as a boolean.
     * @param regionSound, the soundID the region should play when a player enters the region.
     * @param regionSoundVolume, the volume at which the region sound should play.
     * @param regionSoundFadeDuration, the time it takes for the region sound to fade when the player exits the region.
     */
    public static void addRadialRegion(UUID regionUuid, String regionName, int regionX, int regionY, int regionZ, int regionRadius, List<String> regionEnterCommands, List<String> regionExitCommands, Set<UUID> playersInside, boolean regionEnabled, String regionSound, float regionSoundVolume, float regionSoundFadeDuration) {
        // Make a new radial region data object.
        RadialRegionData radialRegionData = new RadialRegionData(regionUuid, regionName, regionX, regionY, regionZ, regionRadius, regionEnterCommands, regionExitCommands, playersInside, regionEnabled, regionSound, regionSoundVolume, regionSoundFadeDuration);
        // Add the new object to the active radial regions list to track it.
        activeRadialRegions.add(radialRegionData);
    }

    /**
     * Make a new Linear Region object and add it to the actively tracked list of Linear Regions.
     * @param regionUuid, the region's UUID.
     * @param regionName, the region's name.
     * @param regionX1, the region's position 1 x-value.
     * @param regionY1, the region's position 1 y-value.
     * @param regionZ1, the region's position 1 z-value.
     * @param regionX2, the region's position 2 x-value.
     * @param regionY2, the region's position 2 y-value.
     * @param regionZ2, the region's position 1 z-value.
     * @param regionEnterCommands, the region's list of enter commands.
     * @param regionExitCommands, the region's list of exit commands.
     * @param playersInside, the list of players currently within the region.
     * @param regionEnabled, whether or not the region is enabled as a boolean.
     * @param regionSound, the soundID the region should play when a player enters the region.
     * @param regionSoundVolume, the volume at which the region sound should play.
     * @param regionSoundFadeDuration, the time it takes for the region sound to fade when the player exits the region.
     */
    public static void addLinearRegion(UUID regionUuid, String regionName, int regionX1, int regionY1, int regionZ1,int regionX2, int regionY2, int regionZ2, List<String> regionEnterCommands, List<String> regionExitCommands, Set<UUID> playersInside, boolean regionEnabled, String regionSound, float regionSoundVolume, float regionSoundFadeDuration) {
        // Make a new linear region data object.
        LinearRegionData linearRegionData = new LinearRegionData(regionUuid, regionName, regionX1, regionY1, regionZ1, regionX2, regionY2, regionZ2, regionEnterCommands, regionExitCommands, playersInside, regionEnabled, regionSound, regionSoundVolume, regionSoundFadeDuration);
        // Add the new object to the active linear regions list to track it.
        activeLinearRegions.add(linearRegionData);
    }

    /**
     * Clear all existing regions.
     */
    public static void clearRegions() {
        activeRadialRegions.clear();
        activeLinearRegions.clear();
    }
}