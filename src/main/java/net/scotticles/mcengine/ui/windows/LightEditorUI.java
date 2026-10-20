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

    static ImString imLightName = new ImString("Light", 256);
    static ImFloat imLightBrightness = new ImFloat(0);
    static float[] lightColor = {1.0f, 1.0f, 1.0f};
    static ImDouble imLightX = new ImDouble(0);
    static ImDouble imLightY = new ImDouble(0);
    static ImDouble imLightZ = new ImDouble(0);


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
                if (ImGui.button("Remove All Point Lights")) {
                    LightsManager.activePointLights.clear();
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
                    imLightBrightness.set(LightsManager.activeAreaLights.get(i).getAreaLightData().getBrightness());
                    lightColor[0] = LightsManager.activeAreaLights.get(i).getAreaLightData().getColor().red();
                    lightColor[1] = LightsManager.activeAreaLights.get(i).getAreaLightData().getColor().green();
                    lightColor[2] = LightsManager.activeAreaLights.get(i).getAreaLightData().getColor().blue();
                    imLightX.set(LightsManager.activeAreaLights.get(i).getAreaLightData().getPosition().x);
                    imLightY.set(LightsManager.activeAreaLights.get(i).getAreaLightData().getPosition().y);
                    imLightZ.set(LightsManager.activeAreaLights.get(i).getAreaLightData().getPosition().z);

                    ImGui.pushID(i);

                    if (ImGui.collapsingHeader(LightsManager.activeAreaLights.get(i).getName() + "###arealight_" + LightsManager.activeAreaLights.get(i).getUuid())) {
                        // Light Name
                        ImGui.text("Name: "); ImGui.sameLine();
                        if (ImGui.inputText("##Light Name: ", imLightName)) {
                            LightsManager.activeAreaLights.get(i).setName(imLightName.get());
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Light Brightness
                        ImGui.text("Brightness: "); ImGui.sameLine();
                        if (ImGui.inputFloat("##Brightness: ", imLightBrightness)) {
                            LightsManager.activeAreaLights.get(i).getAreaLightData().setBrightness(imLightBrightness.get());
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Light Color
                        if (ImGui.colorEdit3("Color", lightColor)) {
                            LightsManager.activeAreaLights.get(i).getAreaLightData().setColor(lightColor[0], lightColor[1], lightColor[2]);
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Light Position
                        // X-Pos
                        ImGui.text("X: "); ImGui.sameLine();
                        if (ImGui.inputDouble("##X: ", imLightX)) {
                            LightsManager.activeAreaLights.get(i).getAreaLightData().getPosition().set(imLightX.get(), imLightY.get(), imLightZ.get());
                            // Temp Fix For Apparent Waiting To Sync Until Other Attribute Is Modified
                            LightsManager.activeAreaLights.get(i).getAreaLightData().setBrightness(imLightBrightness.get());
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Y-Pos
                        ImGui.text("Y: "); ImGui.sameLine();
                        if (ImGui.inputDouble("##Y: ", imLightY)) {
                            LightsManager.activeAreaLights.get(i).getAreaLightData().getPosition().set(imLightX.get(), imLightY.get(), imLightZ.get());
                            // Temp Fix For Apparent Waiting To Sync Until Other Attribute Is Modified
                            LightsManager.activeAreaLights.get(i).getAreaLightData().setBrightness(imLightBrightness.get());
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                        // Z-Pos
                        ImGui.text("Z: "); ImGui.sameLine();
                        if (ImGui.inputDouble("##Z: ", imLightZ)) {
                            LightsManager.activeAreaLights.get(i).getAreaLightData().getPosition().set(imLightX.get(), imLightY.get(), imLightZ.get());
                            // Temp Fix For Apparent Waiting To Sync Until Other Attribute Is Modified
                            LightsManager.activeAreaLights.get(i).getAreaLightData().setBrightness(imLightBrightness.get());
                        }
                        if (ImGui.isItemDeactivatedAfterEdit()) {
                            shouldSyncNetwork = true;
                        }

                    }
                    ImGui.popID();
                }

                if (ImGui.button("Remove All Area Lights")) {
                    LightsManager.activeAreaLights.clear();
                }
                ImGui.endTabItem();
            }

            // Directional Lights Tab
            if (ImGui.beginTabItem("Directional Lights")) {
                if (ImGui.button("Add Directional Light")) {
                    LightsManager.addDirectionalLight();
                }
                if (ImGui.button("Remove All Directional Lights")) {
                    LightsManager.activeDirectionalLights.clear();
                }
                ImGui.endTabItem();
            }

            // Spot Lights Tab
            if (ImGui.beginTabItem("Spot Lights")) {
                if (ImGui.button("Add Spot Light")) {

                }
                if (ImGui.button("Remove All Spot Lights")) {
                    LightsManager.activeSpotLights.clear();
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
                // TODO - Sync Point Lights List

                // TODO - Sync Area Lights List

                // TODO - Sync Directional Lights List

                // TODO - Sync Spot Lights List

            }
        }
    }
}
