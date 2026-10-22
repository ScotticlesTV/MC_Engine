package net.scotticles.mcengine.ui.windows;

import foundry.imgui.api.ImGuiMC;
import imgui.ImGui;
import imgui.type.ImDouble;
import imgui.type.ImFloat;
import imgui.type.ImString;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.scotticles.mcengine.lights.LightsManager;
import net.scotticles.mcengine.ui.UIManager;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class LightEditorUI {

    // General Light Variables
    static ImString imLightName = new ImString("Light", 256);
    static ImFloat imLightBrightness = new ImFloat(0);
    static float[] lightColor = {1.0f, 1.0f, 1.0f};

    // All But Directional Light Variables
    static ImDouble imLightX = new ImDouble(0);
    static ImDouble imLightY = new ImDouble(0);
    static ImDouble imLightZ = new ImDouble(0);
    static ImFloat imInScattering = new ImFloat(0);

    // Area Light Variables
    static ImFloat imLightSizeX = new ImFloat(0);
    static ImFloat imLightSizeY = new ImFloat(0);

    // Area & Spot Light Variables
    static ImFloat imLightOrientationX = new ImFloat(0);
    static ImFloat imLightOrientationY = new ImFloat(0);
    static ImFloat imLightOrientationZ = new ImFloat(0);
    static ImFloat imLightOrientationW = new ImFloat(0);
    static ImFloat imAngle = new ImFloat(0);
    static ImFloat imDistance = new ImFloat(0);

    // Point Light Variables
    static ImFloat imRadius = new ImFloat(0);

    // Directional Light Variables
    static ImFloat imLightDirectionX = new ImFloat(0);
    static ImFloat imLightDirectionY = new ImFloat(0);
    static ImFloat imLightDirectionZ = new ImFloat(0);

    // Spot Light Variables
    static ImFloat imSpotLightSize = new ImFloat(0);



    public static void showLightEditorUI() {
        try (ImGuiMC.ActiveContext ctx = ImGuiMC.withImGui()) {
            if (ctx == null) {
                return;
            }

            boolean shouldSyncNetwork = false;

            ImGui.begin("Light Editor", UIManager.showLightEditorUI);
            ImGui.textWrapped("Manage the world's lights.");

            ImGui.beginTabBar("Light Types");

            // Point Lights Tab
            if (ImGui.beginTabItem("Point Lights")) {
                if (ImGui.button("Add Point Light")) {
                    ClientPlayerEntity player = MinecraftClient.getInstance().player;
                    if (player != null) {
                        Vec3d playerPos = player.getPos();
                        LightsManager.addPointLight(playerPos);
                        shouldSyncNetwork = true;
                    }
                }

                for (int i = 0; i < LightsManager.activePointLights.size(); i++) {
                    imLightName.set(LightsManager.activePointLights.get(i).getName());
                    imLightBrightness.set(LightsManager.activePointLights.get(i).getBrightness());
                    lightColor[0] = LightsManager.activePointLights.get(i).getColor().red();
                    lightColor[1] = LightsManager.activePointLights.get(i).getColor().green();
                    lightColor[2] = LightsManager.activePointLights.get(i).getColor().blue();
                    imLightX.set(LightsManager.activePointLights.get(i).getPosition().x());
                    imLightY.set(LightsManager.activePointLights.get(i).getPosition().y());
                    imLightZ.set(LightsManager.activePointLights.get(i).getPosition().z());
                    imRadius.set(LightsManager.activePointLights.get(i).getRadius());
                    imInScattering.set(LightsManager.activePointLights.get(i).getInscatteringStrength());


                    ImGui.pushID(i);

                    if (ImGui.collapsingHeader(LightsManager.activePointLights.get(i).getName() + "###pointlight_" + LightsManager.activePointLights.get(i).getUuid())) {
                        // Light Name
                        if (ImGui.inputText("Name", imLightName)) {
                            LightsManager.activePointLights.get(i).setName(imLightName.get());
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Light Brightness
                        if (ImGui.inputFloat("Brightness", imLightBrightness)) {
                            LightsManager.activePointLights.get(i).setBrightness(imLightBrightness.get());
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Light Color
                        if (ImGui.colorEdit3("Color", lightColor)) {
                            LightsManager.activePointLights.get(i).setColor(lightColor[0], lightColor[1], lightColor[2]);
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Light Position
                        ImGui.spacing();
                        ImGui.text("Light Position: ");
                        // X-Pos
                        if (ImGui.inputDouble("X", imLightX)) {
                            LightsManager.activePointLights.get(i).setPosition(imLightX.get(), imLightY.get(), imLightZ.get());
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Y-Pos
                        if (ImGui.inputDouble("Y", imLightY)) {
                            LightsManager.activePointLights.get(i).setPosition(imLightX.get(), imLightY.get(), imLightZ.get());
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Z-Pos
                        if (ImGui.inputDouble("Z", imLightZ)) {
                            LightsManager.activePointLights.get(i).setPosition(imLightX.get(), imLightY.get(), imLightZ.get());
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Set Light To View
                        if (ImGui.button("Set Position To View")) {
                            LightsManager.activePointLights.get(i).setTo(MinecraftClient.getInstance().gameRenderer.getCamera());
                            shouldSyncNetwork = true;
                        }


                        // Light Position
                        ImGui.spacing();
                        ImGui.text("Light Spread Properties: ");
                        // Light Radius
                        if (ImGui.inputFloat("Radius", imRadius)) {
                            LightsManager.activePointLights.get(i).setRadius(imRadius.get());
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Light Occlusion Enabled
                        if (ImGui.checkbox("Occlusion Enabled", LightsManager.activePointLights.get(i).isOcclusionEnabled())) {
                            LightsManager.activePointLights.get(i).setOcclusionEnabled(!LightsManager.activePointLights.get(i).isOcclusionEnabled());
                            shouldSyncNetwork = true;
                        }

                        // Light In-Scattering
                        if (ImGui.inputFloat("In-Scattering", imInScattering)) {
                            LightsManager.activePointLights.get(i).setInscatteringStrength(imInScattering.get());
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        ImGui.spacing();
                        if (ImGui.button("Duplicate Light")) {
                            LightsManager.duplicatePointLight(LightsManager.activePointLights.get(i));
                            shouldSyncNetwork = true;
                        }

                        ImGui.spacing();
                        if (ImGui.button("Remove Light")) {
                            LightsManager.activePointLights.remove(i);
                            shouldSyncNetwork = true;
                        }
                    }
                    ImGui.popID();
                }

                ImGui.spacing();
                ImGui.separator();
                ImGui.spacing();

                if (ImGui.button("Remove All Point Lights")) {
                    LightsManager.activePointLights.clear();
                    shouldSyncNetwork = true;
                }
                ImGui.endTabItem();
            }

            // Area Lights Tab
            if (ImGui.beginTabItem("Area Lights")) {
                if (ImGui.button("Add Area Light")) {
                    // Get The Player
                    ClientPlayerEntity player = MinecraftClient.getInstance().player;
                    if (player != null) {
                        Vec3d playerPos = player.getPos();
                        Vec3d look = player.getRotationVector();
                        Quaternionf playerOrientation = new Quaternionf().lookAlong(
                                new Vector3f((float) -look.x, (float) -look.y, (float) -look.z),
                                new Vector3f(0, 1, 0)
                        );
                        LightsManager.addAreaLight(playerPos, playerOrientation);
                    }
                }

                for (int i = 0; i < LightsManager.activeAreaLights.size(); i++) {
                    imLightName.set(LightsManager.activeAreaLights.get(i).getName());
                    imLightBrightness.set(LightsManager.activeAreaLights.get(i).getBrightness());
                    lightColor[0] = LightsManager.activeAreaLights.get(i).getColor().red();
                    lightColor[1] = LightsManager.activeAreaLights.get(i).getColor().green();
                    lightColor[2] = LightsManager.activeAreaLights.get(i).getColor().blue();
                    imLightX.set(LightsManager.activeAreaLights.get(i).getPosition().x);
                    imLightY.set(LightsManager.activeAreaLights.get(i).getPosition().y);
                    imLightZ.set(LightsManager.activeAreaLights.get(i).getPosition().z);
                    imLightSizeX.set(LightsManager.activeAreaLights.get(i).getSize().x);
                    imLightSizeY.set(LightsManager.activeAreaLights.get(i).getSize().y);
                    imLightOrientationX.set(LightsManager.activeAreaLights.get(i).getOrientation().x);
                    imLightOrientationY.set(LightsManager.activeAreaLights.get(i).getOrientation().y);
                    imLightOrientationZ.set(LightsManager.activeAreaLights.get(i).getOrientation().z);
                    imLightOrientationW.set(LightsManager.activeAreaLights.get(i).getOrientation().w);
                    imAngle.set((float) Math.toDegrees(LightsManager.activeAreaLights.get(i).getAngle()));
                    imDistance.set(LightsManager.activeAreaLights.get(i).getDistance());
                    imInScattering.set(LightsManager.activeAreaLights.get(i).getInscatteringStrength());


                    ImGui.pushID(i);

                    if (ImGui.collapsingHeader(LightsManager.activeAreaLights.get(i).getName() + "###arealight_" + LightsManager.activeAreaLights.get(i).getUuid())) {
                        // Light Name
                        if (ImGui.inputText("Name", imLightName)) {
                            LightsManager.activeAreaLights.get(i).setName(imLightName.get());
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Light Brightness
                        if (ImGui.inputFloat("Brightness", imLightBrightness)) {
                            LightsManager.activeAreaLights.get(i).setBrightness(imLightBrightness.get());
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Light Color
                        if (ImGui.colorEdit3("Color", lightColor)) {
                            LightsManager.activeAreaLights.get(i).setColor(lightColor[0], lightColor[1], lightColor[2]);
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Light Position
                        ImGui.spacing();
                        ImGui.text("Light Position: ");
                        // X-Pos
                        if (ImGui.inputDouble("X", imLightX)) {
                            LightsManager.activeAreaLights.get(i).getPosition().set(imLightX.get(), imLightY.get(), imLightZ.get());
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Y-Pos
                        if (ImGui.inputDouble("Y", imLightY)) {
                            LightsManager.activeAreaLights.get(i).getPosition().set(imLightX.get(), imLightY.get(), imLightZ.get());
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Z-Pos
                        if (ImGui.inputDouble("Z", imLightZ)) {
                            LightsManager.activeAreaLights.get(i).getPosition().set(imLightX.get(), imLightY.get(), imLightZ.get());
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Light Size
                        ImGui.spacing();
                        ImGui.text("Light Size: ");
                        // Light Size X
                        if (ImGui.inputFloat("X" + "##size", imLightSizeX)) {
                            LightsManager.activeAreaLights.get(i).setSize(imLightSizeX.get(), imLightSizeY.get());
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Light Size Y
                        if (ImGui.inputFloat("Y" + "##size", imLightSizeX)) {
                            LightsManager.activeAreaLights.get(i).setSize(imLightSizeX.get(), imLightSizeY.get());
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }


                        // Light Orientation
                        ImGui.spacing();
                        ImGui.text("Light Orientation: ");

                        // Light Orientation X
                        if (ImGui.inputFloat("X" + "##orientation", imLightOrientationX)) {
                            LightsManager.activeAreaLights.get(i).getOrientation().set(imLightOrientationX.get(), imLightOrientationY.get(), imLightOrientationZ.get(), imLightOrientationW.get());
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Light Orientation Y
                        if (ImGui.inputFloat("Y" + "##orientation", imLightOrientationY)) {
                            LightsManager.activeAreaLights.get(i).getOrientation().set(imLightOrientationX.get(), imLightOrientationY.get(), imLightOrientationZ.get(), imLightOrientationW.get());
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Light Orientation Z
                        if (ImGui.inputFloat("Z" + "##orientation", imLightOrientationZ)) {
                            LightsManager.activeAreaLights.get(i).getOrientation().set(imLightOrientationX.get(), imLightOrientationY.get(), imLightOrientationZ.get(), imLightOrientationW.get());
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Light Orientation W
                        if (ImGui.inputFloat("W" + "##orientation", imLightOrientationW)) {
                            LightsManager.activeAreaLights.get(i).getOrientation().set(imLightOrientationX.get(), imLightOrientationY.get(), imLightOrientationZ.get(), imLightOrientationW.get());
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Set Light To View
                        if (ImGui.button("Set Orientation And Position To View")) {
                            LightsManager.activeAreaLights.get(i).setTo(MinecraftClient.getInstance().gameRenderer.getCamera());
                            shouldSyncNetwork = true;
                        }


                        ImGui.spacing();
                        ImGui.text("Light Spread Properties: ");
                        // Light Angle
                        if (ImGui.inputFloat("Angle", imAngle)) {
                            LightsManager.activeAreaLights.get(i).setAngle((float) Math.toRadians(imAngle.get()));
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Light Distance
                        if (ImGui.inputFloat("Distance", imDistance)) {
                            LightsManager.activeAreaLights.get(i).setDistance(imDistance.get());
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Light Occlusion Enabled
                        if (ImGui.checkbox("Occlusion Enabled", LightsManager.activeAreaLights.get(i).isOcclusionEnabled())) {
                            LightsManager.activeAreaLights.get(i).setOcclusionEnabled(!LightsManager.activeAreaLights.get(i).isOcclusionEnabled());
                            shouldSyncNetwork = true;
                        }

                        // Light In-Scattering
                        if (ImGui.inputFloat("In-Scattering", imInScattering)) {
                            LightsManager.activeAreaLights.get(i).setInscatteringStrength(imInScattering.get());
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        ImGui.spacing();
                        if (ImGui.button("Duplicate Light")) {
                            LightsManager.duplicateAreaLight(LightsManager.activeAreaLights.get(i));
                            shouldSyncNetwork = true;
                        }

                        ImGui.spacing();
                        if (ImGui.button("Remove Light")) {
                            LightsManager.activeAreaLights.remove(i);
                            shouldSyncNetwork = true;
                        }
                    }
                    ImGui.popID();
                }

                ImGui.spacing();
                ImGui.separator();
                ImGui.spacing();

                if (ImGui.button("Remove All Area Lights")) {
                    LightsManager.activeAreaLights.clear();
                    shouldSyncNetwork = true;
                }
                ImGui.endTabItem();
            }

            // Directional Lights Tab
            if (ImGui.beginTabItem("Directional Lights")) {
                if (ImGui.button("Add Directional Light")) {
                    LightsManager.addDirectionalLight();
                }

                for (int i = 0; i < LightsManager.activeDirectionalLights.size(); i++) {
                    imLightName.set(LightsManager.activeDirectionalLights.get(i).getName());
                    imLightBrightness.set(LightsManager.activeDirectionalLights.get(i).getBrightness());
                    lightColor[0] = LightsManager.activeDirectionalLights.get(i).getColor().red();
                    lightColor[1] = LightsManager.activeDirectionalLights.get(i).getColor().green();
                    lightColor[2] = LightsManager.activeDirectionalLights.get(i).getColor().blue();
                    imLightDirectionX.set(LightsManager.activeDirectionalLights.get(i).getDirection().x());
                    imLightDirectionY.set(LightsManager.activeDirectionalLights.get(i).getDirection().y());
                    imLightDirectionY.set(LightsManager.activeDirectionalLights.get(i).getDirection().z());


                    ImGui.pushID(i);

                    if (ImGui.collapsingHeader(LightsManager.activeDirectionalLights.get(i).getName() + "###directionallight_" + LightsManager.activeDirectionalLights.get(i).getUuid())) {
                        // Light Name
                        if (ImGui.inputText("Name", imLightName)) {
                            LightsManager.activeDirectionalLights.get(i).setName(imLightName.get());
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Light Brightness
                        if (ImGui.inputFloat("Brightness", imLightBrightness)) {
                            LightsManager.activeDirectionalLights.get(i).setBrightness(imLightBrightness.get());
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Light Color
                        if (ImGui.colorEdit3("Color", lightColor)) {
                            LightsManager.activeDirectionalLights.get(i).setColor(lightColor[0], lightColor[1], lightColor[2]);
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Light Position
                        ImGui.spacing();
                        ImGui.text("Light Direction: ");
                        // X-Pos
                        if (ImGui.inputFloat("X", imLightDirectionX)) {
                            LightsManager.activeDirectionalLights.get(i).setDirection(imLightDirectionX.get(), imLightDirectionY.get(), imLightDirectionZ.get());
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Y-Pos
                        if (ImGui.inputFloat("Y", imLightDirectionY)) {
                            LightsManager.activeDirectionalLights.get(i).setDirection(imLightDirectionX.get(), imLightDirectionY.get(), imLightDirectionZ.get());
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Z-Pos
                        if (ImGui.inputFloat("Z", imLightDirectionZ)) {
                            LightsManager.activeDirectionalLights.get(i).setDirection(imLightDirectionX.get(), imLightDirectionY.get(), imLightDirectionZ.get());
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        ImGui.spacing();
                        ImGui.text("Light Spread Properties: ");

                        // Light Occlusion Enabled
                        if (ImGui.checkbox("Occlusion Enabled", LightsManager.activeDirectionalLights.get(i).isOcclusionEnabled())) {
                            LightsManager.activeDirectionalLights.get(i).setOcclusionEnabled(!LightsManager.activeDirectionalLights.get(i).isOcclusionEnabled());
                            shouldSyncNetwork = true;
                        }

                        ImGui.spacing();
                        if (ImGui.button("Duplicate Light")) {
                            LightsManager.duplicateDirectionalLight(LightsManager.activeDirectionalLights.get(i));
                            shouldSyncNetwork = true;
                        }

                        ImGui.spacing();

                        if (ImGui.button("Remove Light")) {
                            LightsManager.activeDirectionalLights.remove(i);
                            shouldSyncNetwork = true;
                        }
                    }
                    ImGui.popID();
                }

                ImGui.spacing();
                ImGui.separator();
                ImGui.spacing();

                if (ImGui.button("Remove All Directional Lights")) {
                    LightsManager.activeDirectionalLights.clear();
                    shouldSyncNetwork = true;
                }
                ImGui.endTabItem();
            }

            // Spot Lights Tab
            if (ImGui.beginTabItem("Spot Lights")) {
                if (ImGui.button("Add Spot Light")) {
                    // Get The Player
                    ClientPlayerEntity player = MinecraftClient.getInstance().player;
                    if (player != null) {
                        Vec3d playerPos = player.getPos();
                        Vec3d look = player.getRotationVector();
                        Quaternionf playerOrientation = new Quaternionf().lookAlong(
                                new Vector3f((float) -look.x, (float) -look.y, (float) -look.z),
                                new Vector3f(0, 1, 0)
                        );
                        LightsManager.addSpotLight(playerPos, playerOrientation);
                    }
                }

                for (int i = 0; i < LightsManager.activeSpotLights.size(); i++) {
                    imLightName.set(LightsManager.activeSpotLights.get(i).getName());
                    imLightBrightness.set(LightsManager.activeSpotLights.get(i).getBrightness());
                    lightColor[0] = LightsManager.activeSpotLights.get(i).getColor().red();
                    lightColor[1] = LightsManager.activeSpotLights.get(i).getColor().green();
                    lightColor[2] = LightsManager.activeSpotLights.get(i).getColor().blue();
                    imLightX.set(LightsManager.activeSpotLights.get(i).getPosition().x());
                    imLightY.set(LightsManager.activeSpotLights.get(i).getPosition().y());
                    imLightZ.set(LightsManager.activeSpotLights.get(i).getPosition().z());
                    imSpotLightSize.set((float) Math.toDegrees(LightsManager.activeSpotLights.get(i).getSize()));
                    imLightOrientationX.set(LightsManager.activeSpotLights.get(i).getOrientation().x());
                    imLightOrientationY.set(LightsManager.activeSpotLights.get(i).getOrientation().y());
                    imLightOrientationZ.set(LightsManager.activeSpotLights.get(i).getOrientation().z());
                    imLightOrientationW.set(LightsManager.activeSpotLights.get(i).getOrientation().w());
                    imAngle.set((float) Math.toDegrees(LightsManager.activeSpotLights.get(i).getAngle()));
                    imDistance.set(LightsManager.activeSpotLights.get(i).getDistance());
                    imInScattering.set(LightsManager.activeSpotLights.get(i).getInscatteringStrength());

                    ImGui.pushID(i);

                    if (ImGui.collapsingHeader(LightsManager.activeSpotLights.get(i).getName() + "###spotlight_" + LightsManager.activeSpotLights.get(i).getUuid())) {
                        // Light Name
                        if (ImGui.inputText("Name", imLightName)) {
                            LightsManager.activeSpotLights.get(i).setName(imLightName.get());
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Light Brightness
                        if (ImGui.inputFloat("Brightness", imLightBrightness)) {
                            LightsManager.activeSpotLights.get(i).setBrightness(imLightBrightness.get());
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Light Color
                        if (ImGui.colorEdit3("Color", lightColor)) {
                            LightsManager.activeSpotLights.get(i).setColor(lightColor[0], lightColor[1], lightColor[2]);
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Light Position
                        ImGui.spacing();
                        ImGui.text("Light Position: ");
                        // X-Pos
                        if (ImGui.inputDouble("X", imLightX)) {
                            LightsManager.activeSpotLights.get(i).setPosition(imLightX.get(), imLightY.get(), imLightZ.get());
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Y-Pos
                        if (ImGui.inputDouble("Y", imLightY)) {
                            LightsManager.activeSpotLights.get(i).setPosition(imLightX.get(), imLightY.get(), imLightZ.get());
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Z-Pos
                        if (ImGui.inputDouble("Z", imLightZ)) {
                            LightsManager.activeSpotLights.get(i).setPosition(imLightX.get(), imLightY.get(), imLightZ.get());
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        ImGui.spacing();
                        ImGui.text("Light Orientation: ");
                        // Light Orientation X
                        if (ImGui.inputFloat("X" + "##orientation", imLightOrientationX)) {
                            LightsManager.activeSpotLights.get(i).setOrientation(imLightOrientationX.get(), imLightOrientationY.get(), imLightOrientationZ.get(), imLightOrientationW.get());
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Light Orientation Y
                        if (ImGui.inputFloat("Y" + "##orientation", imLightOrientationY)) {
                            LightsManager.activeSpotLights.get(i).setOrientation(imLightOrientationX.get(), imLightOrientationY.get(), imLightOrientationZ.get(), imLightOrientationW.get());
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Light Orientation Z
                        if (ImGui.inputFloat("Z" + "##orientation", imLightOrientationZ)) {
                            LightsManager.activeSpotLights.get(i).setOrientation(imLightOrientationX.get(), imLightOrientationY.get(), imLightOrientationZ.get(), imLightOrientationW.get());
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Light Orientation W
                        if (ImGui.inputFloat("W" + "##orientation", imLightOrientationW)) {
                            LightsManager.activeSpotLights.get(i).setOrientation(imLightOrientationX.get(), imLightOrientationY.get(), imLightOrientationZ.get(), imLightOrientationW.get());
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Set Light To View
                        if (ImGui.button("Set Orientation And Position To View")) {
                            LightsManager.activeSpotLights.get(i).setTo(MinecraftClient.getInstance().gameRenderer.getCamera());
                            shouldSyncNetwork = true;
                        }

                        ImGui.spacing();
                        ImGui.text("Light Spread Properties: ");
                        // Light Size
                        if (ImGui.inputFloat("Size", imSpotLightSize)) {
                            LightsManager.activeSpotLights.get(i).setSize((float) Math.toRadians(imSpotLightSize.get()));
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Light Angle
                        if (ImGui.inputFloat("Angle", imAngle)) {
                            LightsManager.activeSpotLights.get(i).setAngle((float) Math.toRadians(imAngle.get()));
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Light Distance
                        if (ImGui.inputFloat("Distance", imDistance)) {
                            LightsManager.activeSpotLights.get(i).setDistance(imDistance.get());
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Light Occlusion Enabled
                        if (ImGui.checkbox("Occlusion Enabled", LightsManager.activeSpotLights.get(i).isOcclusionEnabled())) {
                            LightsManager.activeSpotLights.get(i).setOcclusionEnabled(!LightsManager.activeSpotLights.get(i).isOcclusionEnabled());
                            shouldSyncNetwork = true;
                        }

                        // Light In-Scattering
                        if (ImGui.inputFloat("In-Scattering", imInScattering)) {
                            LightsManager.activeSpotLights.get(i).setInscatteringStrength(imInScattering.get());
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        ImGui.spacing();
                        if (ImGui.button("Duplicate Light")) {
                            LightsManager.duplicateSpotLight(LightsManager.activeSpotLights.get(i));
                            shouldSyncNetwork = true;
                        }

                        ImGui.spacing();
                        if (ImGui.button("Remove Light")) {
                            LightsManager.activeSpotLights.remove(i);
                            shouldSyncNetwork = true;
                        }
                    }
                    ImGui.popID();
                }

                ImGui.spacing();
                ImGui.separator();
                ImGui.spacing();

                if (ImGui.button("Remove All Spot Lights")) {
                    LightsManager.activeSpotLights.clear();
                    shouldSyncNetwork = true;
                }
                ImGui.endTabItem();
            }

            // Mass Light Editing Tab
            if (ImGui.beginTabItem("Mass Light Editing")) {
                if (ImGui.button("Delete All Lights")) {
                    LightsManager.clearLights();
                }
                ImGui.endTabItem();
            }
            ImGui.endTabBar();
            ImGui.end();

            if (shouldSyncNetwork) {
                LightsManager.syncAllLights();
            }
        }
    }
}