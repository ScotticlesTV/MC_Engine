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

    public static final Set<RadialRegionData> activeRadialRegions = new HashSet<>();
    public static final Set<LinearRegionData> activeLinearRegions = new HashSet<>();

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

    private static void loadRegions(MinecraftServer server) {
        activeRadialRegions.clear();
        activeLinearRegions.clear();
        activeRadialRegions.addAll(RegionDataManager.loadRadialRegions(server));
        activeLinearRegions.addAll(RegionDataManager.loadLinearRegions(server));
    }

    private static void saveRegions(MinecraftServer server) {
        RegionDataManager.save(activeRadialRegions, activeLinearRegions , server);
        activeRadialRegions.clear();
        activeLinearRegions.clear();
    }

    //Add A New Region
    public static void addRadialRegion(UUID regionUuid, String regionName, int regionX, int regionY, int regionZ, int regionRadius, List<String> regionEnterCommands, List<String> regionExitCommands, Set<UUID> playersInside, boolean regionEnabled, String regionSound, float regionSoundVolume, float regionSoundFadeDuration) {
        RadialRegionData radialRegionData = new RadialRegionData(regionUuid, regionName, regionX, regionY, regionZ, regionRadius, regionEnterCommands, regionExitCommands, playersInside, regionEnabled, regionSound, regionSoundVolume, regionSoundFadeDuration);

        activeRadialRegions.add(radialRegionData);
//        Needs the Minecraft Server to save
//        RegionDataManager.save(activeRegions);
    }


    //Add A New Region
    public static void addLinearRegion(UUID regionUuid, String regionName, int regionX1, int regionY1, int regionZ1,int regionX2, int regionY2, int regionZ2, List<String> regionEnterCommands, List<String> regionExitCommands, Set<UUID> playersInside, boolean regionEnabled, String regionSound, float regionSoundVolume, float regionSoundFadeDuration) {
        LinearRegionData linearRegionData = new LinearRegionData(regionUuid, regionName, regionX1, regionY1, regionZ1, regionX2, regionY2, regionZ2, regionEnterCommands, regionExitCommands, playersInside, regionEnabled, regionSound, regionSoundVolume, regionSoundFadeDuration);

        activeLinearRegions.add(linearRegionData);
//        Needs the Minecraft Server to save
//        RegionDataManager.save(activeRegions);
    }

    //Clear All Existing Regions
    public static void clearRegions() {
        activeRadialRegions.clear();
        activeLinearRegions.clear();
//        Needs the Minecraft Server to save
//        RegionDataManager.save(activeRegions);
    }
}