package net.scotticles.mcengine.lights;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
import net.scotticles.mcengine.lights.lightdatasaving.*;
import net.scotticles.mcengine.networking.lights.payloads.*;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LightsManager {

    // Tracked Light Data Object Lists
    public static final List<MCEAreaLightData> activeAreaLights = new ArrayList<>();
    public static final List<MCEPointLightData> activePointLights = new ArrayList<>();
    public static final List<MCEDirectionalLightData> activeDirectionalLights = new ArrayList<>();
    public static final List<MCESpotLightData> activeSpotLights = new ArrayList<>();

    public static void lightDataInit() {
        // Load Light Data On Server Start
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            loadLights(server);
        });

        // Save Light Data On Server Stop
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            saveLights(server);
        });
    }

    private static void loadLights(MinecraftServer server) {
        activeAreaLights.clear();
        activePointLights.clear();
        activeDirectionalLights.clear();
        activeSpotLights.clear();
        activeAreaLights.addAll(LightDataManager.loadAreaLights(server));
        activePointLights.addAll(LightDataManager.loadPointLights(server));
        activeDirectionalLights.addAll(LightDataManager.loadDirectionalLights(server));
        activeSpotLights.addAll(LightDataManager.loadSpotLights(server));
    }

    private static void saveLights(MinecraftServer server) {
        LightDataManager.save(activeAreaLights, activePointLights, activeDirectionalLights, activeSpotLights, server);
        activeAreaLights.clear();
        activePointLights.clear();
        activeDirectionalLights.clear();
        activeSpotLights.clear();
    }

     /**
     * Make a new MCE Area Light Data object, track it, and sync it to the server.
     * @param playerPos, the player's position to set the area light to.
     * @param playerOrientation, the player's view orientation to set the area light to.
     */
    public static void addAreaLight(Vec3d playerPos, Quaternionf playerOrientation) {
        // Make A New MCE Area Light Object
        MCEAreaLightData mceAreaLightData = new MCEAreaLightData("Area Light", UUID.randomUUID(),  1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, playerPos.getX(), playerPos.getY(), playerPos.getZ(), playerOrientation, (float) Math.toRadians(45.0f),15.0f,false, 0.0f);

        // Track The New Wrapper Object In The Active Area Lights List
        activeAreaLights.add(mceAreaLightData);

        // Sync All Lights
        syncAllLights();
    }

    public static void duplicateAreaLight(MCEAreaLightData mceAreaLightData) {
        // Create A Duplicate Of The Passed In MCE Area Light Data
        MCEAreaLightData duplicatedLight = new MCEAreaLightData(mceAreaLightData);

        // Track The New Light
        activeAreaLights.add(duplicatedLight);

        // Sync All Lights
        syncAllLights();
    }



    /**
     * Make a new MCE Directional Light Data object, track it, and sync it to the server.
     */
    public static void addDirectionalLight() {
        // Make A New MCE Directional Light Object
        MCEDirectionalLightData mceDirectionalLightData = new MCEDirectionalLightData("Directional Light", UUID.randomUUID(), 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, -1.0f, 0.0f, false);

        // Track The New Wrapper Object In The Active Directional Lights List
        activeDirectionalLights.add(mceDirectionalLightData);

        // Sync All Lights
        syncAllLights();
    }

    public static void duplicateDirectionalLight(MCEDirectionalLightData mceDirectionalLightData) {
        // Create A Duplicate Of The Passed In MCE Directional Light Data
        MCEDirectionalLightData duplicatedLight = new MCEDirectionalLightData(mceDirectionalLightData);

        // Track The New Light
        activeDirectionalLights.add(duplicatedLight);

        // Sync All Lights
        syncAllLights();
    }

    /**
     * Make a new MCE Point Light Data object, track it, and sync it to the server.
     * @param playerPos, the player's position to set the point light to.
     */
    public static void addPointLight(Vec3d playerPos) {
        // Make A New MCE Point Light Data Object
        MCEPointLightData mcePointLightData = new MCEPointLightData("Point Light", UUID.randomUUID(), 1.0f, 1.0f, 1.0f, 1.0f, playerPos.getX(), playerPos.getY(), playerPos.getZ(), 15.0f, false, 0.0f);

        // Track The New Wrapper Object In The Active Point Lights List
        activePointLights.add(mcePointLightData);

        // Sync All Lights
        syncAllLights();
    }

    public static void duplicatePointLight(MCEPointLightData mcePointLightData) {
        // Create A Duplicate Of The New Passed In MCE Point Light Data
        MCEPointLightData duplicatedLight = new MCEPointLightData(mcePointLightData);

        // Track The New Light
        activePointLights.add(duplicatedLight);

        // Sync All Lights
        syncAllLights();
    }

    /**
     * Make a new MCE Spot Light Data object, track it, and sync it to the server.
     * @param playerPos, the player's position to set the area light to.
     * @param playerOrientation, the player's view orientation to set the area light to.
     */
    public static void addSpotLight(Vec3d playerPos, Quaternionf playerOrientation) {
        // Make A New MCE Spot Light Data Object
        MCESpotLightData mceSpotLightData = new MCESpotLightData("Spot Light", UUID.randomUUID(), 1.0f,1.0f, 1.0f, 1.0f, (float) Math.toRadians(45), playerPos.getX(), playerPos.getY(), playerPos.getZ(), playerOrientation, (float) Math.toRadians(45.0f),15.0f,false, 0.0f);

        // Track The New Wrapper Object In The Active Spot Lights List
        activeSpotLights.add(mceSpotLightData);

        // Sync All Lights
        syncAllLights();
    }

    public static void duplicateSpotLight(MCESpotLightData mceSpotLightData) {
        // Create A Duplicate Of The passed In MCE Spot Light Data
        MCESpotLightData duplicatedLight = new MCESpotLightData(mceSpotLightData);

        // Track The New Light
        activeSpotLights.add(duplicatedLight);

        // Sync All Lights
        syncAllLights();
    }

    /**
     * Delete all lights lists and sync to the server.
     */
    public static void clearLights() {
        // Clear Light Lists
        activeAreaLights.clear();
        activePointLights.clear();
        activeDirectionalLights.clear();
        activeSpotLights.clear();

        // Sync All Lights
        syncAllLights();
    }

    /**
     * Send packets to sync all light lists with the server and other clients.
     */
    public static void syncAllLights() {
        ClientPlayNetworking.send(new ClearLightRendererPayload());
        ClientPlayNetworking.send(new SyncAreaLightsDataPayload(new ArrayList<>(activeAreaLights)));
        ClientPlayNetworking.send(new SyncPointLightsDataPayload(new ArrayList<>(activePointLights)));
        ClientPlayNetworking.send(new SyncDirectionalLightsDataPayload(new ArrayList<>(activeDirectionalLights)));
        ClientPlayNetworking.send(new SyncSpotLightsDataPayload(new ArrayList<>(activeSpotLights)));
    }
}