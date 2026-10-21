package net.scotticles.mcengine.lights;

import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.light.data.DirectionalLightData;
import foundry.veil.api.client.render.light.data.PointLightData;
import foundry.veil.api.client.render.light.data.SpotLightData;
import foundry.veil.api.client.render.light.renderer.LightRenderer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.entity.VaultBlockEntity;
import net.minecraft.util.math.Vec3d;
import net.scotticles.mcengine.lights.lightdatasaving.MCEAreaLightData;
import net.scotticles.mcengine.lights.lightdatasaving.MCEDirectionalLightData;
import net.scotticles.mcengine.lights.lightdatasaving.MCEPointLightData;
import net.scotticles.mcengine.lights.lightdatasaving.MCESpotLightData;
import net.scotticles.mcengine.networking.lights.payloads.ClearLightRendererPayload;
import net.scotticles.mcengine.networking.lights.payloads.SyncAreaLightsDataPayload;
import net.scotticles.mcengine.networking.lights.payloads.SyncDirectionalLightsDataPayload;
import net.scotticles.mcengine.networking.lights.payloads.SyncPointLightsDataPayload;
import net.scotticles.mcengine.networking.regions.payloads.SyncLinearRegionsDataPayload;
import net.scotticles.mcengine.regions.RegionsManager;
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

    /**
     * Make a new MCE Area Light Data object, track it, and sync it to the server.
     * @param playerPos, the player's position to set the area light to.
     * @param playerOrientation, the player's view orientation to set the area light to.
     */
    public static void addAreaLight(Vec3d playerPos, Quaternionf playerOrientation) {
        // Make A New MCE Area Light Object
        MCEAreaLightData mceAreaLightData = new MCEAreaLightData("Area Light", UUID.randomUUID(), 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, playerPos.getX(), playerPos.getY(), playerPos.getZ(), playerOrientation, (float) Math.toRadians(45.0f),15.0f,false, 0.0f);

        // Track The New Wrapper Object In The Active Area Lights List
        activeAreaLights.add(mceAreaLightData);

        // Sync All Lights
        syncAllLights();
    }

    /**
     * Make a new MCE Directional Light Data object, track it, and sync it to the server.
     */
    public static void addDirectionalLight() {
        // Make A New MCE Directional Light Object
        MCEDirectionalLightData mceDirectionalLightData = new MCEDirectionalLightData("Directional Light", UUID.randomUUID(), 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, -1.0f, 0.0f);

        // Track The New Wrapper Object In The Active Directional Lights List
        activeDirectionalLights.add(mceDirectionalLightData);

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

    public static void addSpotLight() {

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
        // Send Spot Light List Packet
    }
}