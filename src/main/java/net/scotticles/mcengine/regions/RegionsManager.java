package net.scotticles.mcengine.regions;


import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
import net.scotticles.mcengine.networking.regions.payloads.SyncLinearRegionsDataPayload;
import net.scotticles.mcengine.networking.regions.payloads.SyncRadialRegionsDataPayload;
import net.scotticles.mcengine.regions.regiondatasaving.LinearRegionData;
import net.scotticles.mcengine.regions.regiondatasaving.RadialRegionData;
import net.scotticles.mcengine.regions.regiondatasaving.RegionDataManager;

import java.util.*;

public class RegionsManager {

    // Active Region Sets For Tracking
    public static final List<RadialRegionData> activeRadialRegions = new ArrayList<>();
    public static final List<LinearRegionData> activeLinearRegions = new ArrayList<>();

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
        clearRegions();
        activeRadialRegions.addAll(RegionDataManager.loadRadialRegions(server));
        activeLinearRegions.addAll(RegionDataManager.loadLinearRegions(server));
    }

    /**
     * Save all region data to the active world's region data file.
     * @param server, the server used to get the world save file.
     */
    private static void saveRegions(MinecraftServer server) {
        RegionDataManager.save(activeRadialRegions, activeLinearRegions, server);
        clearRegions();
    }

    /**
     *Make a new Radial Region object and add it to the actively tracked list of Radial Regions.
     * @param playerPos the player's current position.
     * @param enabled whether or not the region starts enabled.
     */
    public static void addRadialRegion(Vec3d playerPos, boolean enabled) {
        // Make a new radial region data object.
        UUID uuid = UUID.randomUUID();
        RadialRegionData radialRegionData = new RadialRegionData(uuid, "Region " + uuid, (int) playerPos.getX(), (int) playerPos.getY(), (int) playerPos.getZ(), 20, new ArrayList<>(), new ArrayList<>(), new HashSet<>(), enabled, "", 1.0f, 2.0f);
        // Add the new object to the active radial regions list to track it.
        activeRadialRegions.add(radialRegionData);
    }

    /**
     * Make a new Linear Region object and add it to the actively tracked list of Linear Regions.
     * @param playerPos the player's current position.
     * @param enabled whether or not the region starts enabled.
     */
    public static void addLinearRegion(Vec3d playerPos, boolean enabled) {
        // Make a new linear region data object.
        UUID uuid = UUID.randomUUID();
        LinearRegionData linearRegionData = new LinearRegionData(uuid, "Region " + uuid, (int) playerPos.getX(), (int) playerPos.getY(), (int) playerPos.getZ(), (int) playerPos.getX(), (int) playerPos.getY(), (int) playerPos.getZ(), new ArrayList<>(), new ArrayList<>(), new HashSet<>(), enabled, "", 1.0f, 2.0f);
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

    /**
     * Send packets to sync all region lists with the server and other clients.
     */
    public static void syncAllRegions() {
        ClientPlayNetworking.send(new SyncRadialRegionsDataPayload(new ArrayList<>(RegionsManager.activeRadialRegions)));
        ClientPlayNetworking.send(new SyncLinearRegionsDataPayload(new ArrayList<>(RegionsManager.activeLinearRegions)));
    }
}