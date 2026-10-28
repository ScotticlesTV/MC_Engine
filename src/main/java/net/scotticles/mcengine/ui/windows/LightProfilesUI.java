package net.scotticles.mcengine.ui.windows;

import foundry.imgui.api.ImGuiMC;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiCond;
import net.minecraft.util.Util;
import net.scotticles.mcengine.ui.UIManager;

public class LightProfilesUI {

    public static void showLightProfilesUI() {
        try (ImGuiMC.ActiveContext ctx = ImGuiMC.withImGui()) {
            if (ctx == null) {
                return;
            }

            // ImGUI Code
            ImVec2 screenCenter = ImGui.getMainViewport().getCenter();
            ImGui.setNextWindowPos(screenCenter.x, screenCenter.y, ImGuiCond.Once, 0.5f, 0.5f);
//            ImGui.setNextWindowSize(820, 520, ImGuiCond.Once);

            ImGui.begin("Light Profiles", UIManager.showLightsProfilesUI);
            ImGui.textWrapped("Edit the world's light profiles.");
            ImGui.beginTabBar("Light Profiles");
            if (ImGui.beginTabItem("Active Profile")) {
                // Text - Currently Active LP Name
                // Text Input - LP Name For Action

                // Save
                // Save To File Based On LP Name

                // Load
                // Load From File Based On LP Name
                // Dropdown of Detected Options

                ImGui.endTabItem();
            }
            if (ImGui.beginTabItem("Mass Light Profile Editing")) {
                // Clear All Profiles

                ImGui.endTabItem();
            }
            ImGui.endTabBar();

            ImGui.end();
        }
    }
}