package net.scotticles.mcengine.lights;

import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.light.data.AreaLightData;
import foundry.veil.api.client.render.light.data.DirectionalLightData;
import foundry.veil.api.client.render.light.data.PointLightData;
import foundry.veil.api.client.render.light.data.SpotLightData;
import foundry.veil.api.client.render.light.renderer.LightRenderer;
import net.minecraft.util.math.Vec3d;
import net.scotticles.mcengine.lights.lightdatasaving.MCEAreaLightData;
import net.scotticles.mcengine.lights.lightdatasaving.MCEDirectionalLightData;
import net.scotticles.mcengine.lights.lightdatasaving.MCEPointLightData;
import net.scotticles.mcengine.lights.lightdatasaving.MCESpotLightData;
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

    private static final LightRenderer lightRenderer = VeilRenderSystem.renderer().getLightRenderer();

    public static void addAreaLight(Vec3d playerPos, Quaternionf playerOrientation) {
        // Create A Veil Area Light And Give It Default Attributes
        AreaLightData areaLightData = new AreaLightData().setBrightness(1.0f).setColor(1.0f, 1.0f, 1.0f).setSize(1.0f, 1.0f).setAngle((float) Math.toRadians(45.0f)).setDistance(15.0f).setOcclusionEnabled(false).setInscatteringStrength(0.0f);
        areaLightData.getPosition().set(playerPos.x, playerPos.y, playerPos.z);
        areaLightData.getOrientation().set(playerOrientation);

        // Make A New MC Engine Wrapper Area Light Data Object And Pass In Default Values (Including The New Veil Area Light)
        MCEAreaLightData mceAreaLightData = new MCEAreaLightData("Area Light", UUID.randomUUID(), areaLightData);

        // Track The New Wrapper Object In The Active Area Lights List
        activeAreaLights.add(mceAreaLightData);

        // TODO(priority:low category:Cleanup @Scott): Remove Temp Logic That Simulates Syncing
        // Temp Logic: Add The Light To The Renderer To Simulate Syncing
        lightRenderer.addLight(mceAreaLightData.getAreaLightData());

        // Sync All Lights
        syncAllLights();
    }

    public static void addDirectionalLight() {
        // Create A Veil Directional Light And Give It Default Attributes
        DirectionalLightData directionalLightData = new DirectionalLightData().setBrightness(1.0f).setColor(1.0f, 1.0f, 1.0f).setOcclusionEnabled(false).setDirection(0, -1, 0);

        // Make A New MC Engine Wrapper Directional Light Data Object And Pass In Default Values (Including The New Veil Directional Light)
        MCEDirectionalLightData mceDirectionalLightData = new MCEDirectionalLightData("Directional Light", UUID.randomUUID(), directionalLightData);
        activeDirectionalLights.add(mceDirectionalLightData);

        // Temp Logic: Add The Light To The Renderer To Simulate Syncing
        // TODO(priority:low category:Cleanup @Scott): Remove Temp Logic That Simulates Syncing
        lightRenderer.addLight(mceDirectionalLightData.getDirectionalLightData());

        // Sync All Lights
        syncAllLights();
    }

    public static void addPointLight(Vec3d playerPos) {
        // Create A Veil Point Light And Give It Default Attributes
        PointLightData pointLightData = new PointLightData().setBrightness(1.0f).setColor(1.0f, 1.0f, 1.0f).setRadius(15.0f).setOcclusionEnabled(false).setInscatteringStrength(0.0f).setPosition(playerPos.x, playerPos.y, playerPos.z);

        // Make A New MC Engine Wrapper Point Light Data Object And Pass In Default Values (Including The New Veil Point Light)
        MCEPointLightData mcePointLightData = new MCEPointLightData("Point Light", UUID.randomUUID(), pointLightData);
        activePointLights.add(mcePointLightData);

        // TODO(priority:low category:Cleanup @Scott): Remove Temp Logic That Simulates Syncing
        // Temp Logic: Add The Light To The Renderer To Simulate Syncing
        lightRenderer.addLight(mcePointLightData.getPointLightData());

        // Sync All Lights
        syncAllLights();
    }

    public static void addSpotLight() {
        SpotLightData spotLightData = new SpotLightData().setBrightness(1.0f);
    }

    public static void clearLights() {
        // Clear Light Lists
        activeAreaLights.clear();
        activePointLights.clear();
        activeDirectionalLights.clear();
        activeSpotLights.clear();

        // TODO(priority:low category:Cleanup @Scott): Remove Temp Logic That Simulates Syncing
        // Temp Logic: Clear The Light Renderer To Simulate Syncing
        VeilRenderSystem.renderer().getLightRenderer().free();

        // Sync All Lights
        syncAllLights();
    }

    public static void syncAllLights() {
        // TODO(priority:high category:Core-Functionality @Scott): Create Light Syncing Functionality, Send Packets To Sync All Client Tracked Lights To The Server And Other Clients
        // Send Area Light List Packet
        // Send Point Light List Packet
        // Send Directional Light List Packet
        // Send Spot Light List Packet
    }
}