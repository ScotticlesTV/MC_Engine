package net.scotticles.mcengine.lights;

import foundry.veil.api.client.render.light.data.LightData;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
import net.scotticles.mcengine.lights.lightdata.*;
import net.scotticles.mcengine.networking.lights.payloads.*;
import net.scotticles.mcengine.networking.lights.payloads.lights.SyncAreaLightsDataPayload;
import net.scotticles.mcengine.networking.lights.payloads.lights.SyncDirectionalLightsDataPayload;
import net.scotticles.mcengine.networking.lights.payloads.lights.SyncPointLightsDataPayload;
import net.scotticles.mcengine.networking.lights.payloads.lights.SyncSpotLightsDataPayload;
import net.scotticles.mcengine.settings.MCEngineServerSettings;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.Collection;
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
            // Load the server settings to get the last LP loaded.
            MCEngineServerSettings.load(server);

            // Clear all Light lists as a safety check before loading.
            clearLights();

            // Get all LP names from the world's MC Engine / Lights folder.
            LightProfilesManager.getLPNames(server);

            // Load all Lights from the last LP loaded.
            List<List<LightData>> lights = LightDataManager.load(server, MCEngineServerSettings.lastLPLoaded);

            // Separate each Light type into its corresponding tracked light list
            for (LightData areaLight : lights.get(0)) {
                activeAreaLights.add((MCEAreaLightData) areaLight);
            }
            for (LightData pointLight : lights.get(1)) {
                activePointLights.add((MCEPointLightData) pointLight);
            }
            for (LightData directionalLight : lights.get(2)) {
                activeDirectionalLights.add((MCEDirectionalLightData) directionalLight);
            }
            for (LightData spotLight : lights.get(3)) {
                activeSpotLights.add((MCESpotLightData) spotLight);
            }
        });

        // Save Light Data On Server Stop
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            // lastLPLoaded is the last light profile the server has registered when loading a light profile
            LightDataManager.save(activeAreaLights, activePointLights, activeDirectionalLights, activeSpotLights, server, MCEngineServerSettings.lastLPLoaded);

            // Clear all Light lists as a safety check for singleplayer
            clearLights();
        });
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
    }

    /**
     * Duplicate the passed in Area Light.
     * @param mceAreaLightData, the Area Light to duplicate.
     */
    public static void duplicateAreaLight(MCEAreaLightData mceAreaLightData) {
        // Create A Duplicate Of The Passed In MCE Area Light Data
        MCEAreaLightData duplicatedLight = new MCEAreaLightData(mceAreaLightData);

        // Track The New Light
        activeAreaLights.add(duplicatedLight);
    }



    /**
     * Make a new MCE Directional Light Data object, track it, and sync it to the server.
     */
    public static void addDirectionalLight() {
        // Make A New MCE Directional Light Object
        MCEDirectionalLightData mceDirectionalLightData = new MCEDirectionalLightData("Directional Light", UUID.randomUUID(), 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, -1.0f, 0.0f, false);

        // Track The New Wrapper Object In The Active Directional Lights List
        activeDirectionalLights.add(mceDirectionalLightData);
    }

    /**
     * Duplicate the passed in Directional Light.
     * @param mceDirectionalLightData, the Directional Light to duplicate.
     */
    public static void duplicateDirectionalLight(MCEDirectionalLightData mceDirectionalLightData) {
        // Create A Duplicate Of The Passed In MCE Directional Light Data
        MCEDirectionalLightData duplicatedLight = new MCEDirectionalLightData(mceDirectionalLightData);

        // Track The New Light
        activeDirectionalLights.add(duplicatedLight);
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
    }

    /**
     * Duplicate the passed in Point Light.
     * @param mcePointLightData, the Point Light to duplicate.
     */
    public static void duplicatePointLight(MCEPointLightData mcePointLightData) {
        // Create A Duplicate Of The New Passed In MCE Point Light Data
        MCEPointLightData duplicatedLight = new MCEPointLightData(mcePointLightData);

        // Track The New Light
        activePointLights.add(duplicatedLight);
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
    }

    /**
     * Duplicate the passed in Spot Light.
     * @param mceSpotLightData, the Spot Light to duplicate.
     */
    public static void duplicateSpotLight(MCESpotLightData mceSpotLightData) {
        // Create A Duplicate Of The passed In MCE Spot Light Data
        MCESpotLightData duplicatedLight = new MCESpotLightData(mceSpotLightData);

        // Track The New Light
        activeSpotLights.add(duplicatedLight);
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
    }

    /**
     * Send packets to sync all light lists with the server and other clients. CAN ONLY BE USED FROM THE CLIENT.
     */
    public static void syncAllLights() {
        ClientPlayNetworking.send(new ClearLightRendererPayload());
        ClientPlayNetworking.send(new SyncAreaLightsDataPayload(new ArrayList<>(activeAreaLights)));
        ClientPlayNetworking.send(new SyncPointLightsDataPayload(new ArrayList<>(activePointLights)));
        ClientPlayNetworking.send(new SyncDirectionalLightsDataPayload(new ArrayList<>(activeDirectionalLights)));
        ClientPlayNetworking.send(new SyncSpotLightsDataPayload(new ArrayList<>(activeSpotLights)));
    }
}