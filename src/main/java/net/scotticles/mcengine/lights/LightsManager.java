package net.scotticles.mcengine.lights;

import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.light.data.AreaLightData;
import net.minecraft.util.math.Vec3d;
import net.scotticles.mcengine.lights.lightdatasaving.MCEAreaLightData;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LightsManager {

    public static final List<MCEAreaLightData> activeAreaLights = new ArrayList<>();

    public static void addAreaLight(Vec3d playerPos, Quaternionf playerOrientation) {
        // Create A Veil Light And Give It Default Attributes
        AreaLightData areaLightData = new AreaLightData().setBrightness(1.0f).setColor(1.0f, 1.0f, 1.0f).setSize(1.0f, 1.0f).setAngle((float) Math.toRadians(45.0f)).setDistance(15.0f).setOcclusionEnabled(false).setInscatteringStrength(0.0f);
        areaLightData.getPosition().set(playerPos.x, playerPos.y, playerPos.z);
        areaLightData.getOrientation().set(playerOrientation);

        // Make A New MC Engine Wrapper Area Light Data Object And Pass In Default Values (Including The New Veil Area Light)
        MCEAreaLightData mceAreaLightData = new MCEAreaLightData("Area Light", UUID.randomUUID(), areaLightData);
        VeilRenderSystem.renderer().getLightRenderer().addLight(mceAreaLightData.getAreaLightData());
        activeAreaLights.add(mceAreaLightData);
    }

    public void addDirectionalLight() {

    }

    public void addPointLight() {

    }

    public void addSpotLight() {

    }
}
