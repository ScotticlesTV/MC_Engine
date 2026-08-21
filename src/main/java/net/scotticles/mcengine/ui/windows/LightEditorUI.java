package net.scotticles.mcengine.ui.windows;

import foundry.imgui.api.ImGuiMC;
import imgui.ImGui;
import net.scotticles.mcengine.ui.UIManager;

public class LightEditorUI {
    public static void showLightEditorUI() {
        try (ImGuiMC.ActiveContext ctx = ImGuiMC.withImGui()) {
            if (ctx == null) {
                return;
            }

            ImGui.begin("Light Editor", UIManager.showLightEditorUI);
            ImGui.textWrapped("Manage world lights.");

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

                }
                if (ImGui.button("Remove ALl Area Lights")) {

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
            ImGui.endTabBar();
            ImGui.end();
        }
    }
}
