package net.scotticles.mcengine.ui.windows;

import foundry.imgui.api.ImGuiMC;
import imgui.ImGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.scotticles.mcengine.lights.LightsManager;
import net.scotticles.mcengine.ui.UIManager;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class LightEditorUI {
    public static void showLightEditorUI() {
        try (ImGuiMC.ActiveContext ctx = ImGuiMC.withImGui()) {
            if (ctx == null) {
                return;
            }

            ImGui.begin("Light Editor", UIManager.showLightEditorUI);
            ImGui.textWrapped("Manage the world's lights.");

            ImGui.beginTabBar("Light Types");
            if (ImGui.beginTabItem("Point Lights")) {
                if (ImGui.button("Add Point Light")) {

                }
                if (ImGui.button("Remove All Point Lights")) {

                }
                ImGui.endTabItem();
            }
            if (ImGui.beginTabItem("Area Lights")) {
                if (ImGui.button("Add Area Light")) {
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
                if (ImGui.button("Remove All Area Lights")) {

                }
                ImGui.endTabItem();
            }
            if (ImGui.beginTabItem("Directional Lights")) {
                if (ImGui.button("Add Directional Light")) {

                }
                if (ImGui.button("Remove All Directional Lights")) {

                }
                ImGui.endTabItem();
            }
            if (ImGui.beginTabItem("Spot Lights")) {
                if (ImGui.button("Add Spot Light")) {

                }
                if (ImGui.button("Remove All Spot Lights")) {

                }
                ImGui.endTabItem();
            }
            if (ImGui.beginTabItem("Mass Light Editing")) {
                if (ImGui.button("Delete All Lights")) {

                }
                ImGui.endTabItem();
            }
            ImGui.endTabBar();
            ImGui.end();
        }
    }
}
