package net.scotticles.mcengine.regions;


import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.scotticles.mcengine.regions.regiondatasaving.RegionData;
import net.scotticles.mcengine.regions.regiondatasaving.RegionDataManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class RegionsManager {

    public static final Set<RegionData> activeRegions = new HashSet<>();

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
        RegionEffectsManager.regionEffectsInit(activeRegions);
    }

    private static void loadRegions(MinecraftServer server) {
        activeRegions.clear();
        activeRegions.addAll(RegionDataManager.load(server));
    }

    private static void saveRegions(MinecraftServer server) {
        RegionDataManager.save(activeRegions, server);
        activeRegions.clear();
    }

    //Add A New Region
    public static void addRegion(UUID regionUuid, String regionName, int regionX, int regionY, int regionZ, int regionRadius, List<String> regionEnterCommands, List<String> regionExitCommands, Set<UUID> playersInside, boolean regionEnabled, String regionSound, float regionSoundVolume, float regionSoundFadeDuration) {
        RegionData regionData = new RegionData(regionUuid, regionName, regionX, regionY, regionZ, regionRadius, regionEnterCommands, regionExitCommands, playersInside, regionEnabled, regionSound, regionSoundVolume, regionSoundFadeDuration);

        activeRegions.add(regionData);
//        Needs the Minecraft Server to save
//        RegionDataManager.save(activeRegions);
    }

    //Clear All Existing Regions
    public static void clearRegions() {
        activeRegions.clear();
//        Needs the Minecraft Server to save
//        RegionDataManager.save(activeRegions);
    }
}
