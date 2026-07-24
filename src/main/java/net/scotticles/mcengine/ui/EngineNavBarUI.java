package net.scotticles.mcengine.ui;

import foundry.imgui.api.ImGuiMC;
import imgui.ImGui;

public class EngineNavBarUI {

    public static void showEngineNavBar() {
        try (ImGuiMC.ActiveContext ctx = ImGuiMC.withImGui()) {
            if (ctx == null) {
                return;
            }

            // Menu Bar
            ImGui.beginMainMenuBar();
            if (ImGui.beginMenu("MCEngine")) {
                if (ImGui.menuItem("Client Engine Settings")) {
                    UIManager.showClientEngineSettings.set(!UIManager.showClientEngineSettings.get());
                }
                ImGui.beginDisabled();
                if (ImGui.menuItem("Engine Documentation")) {

                }
                ImGui.endDisabled();
                ImGui.endMenu();
            }
            ImGui.separator();
            if (ImGui.beginMenu("World")) {
                if (ImGui.menuItem("Time")) {
                    UIManager.showWorldTimeUI.set(!UIManager.showWorldTimeUI.get());
                }
                if (ImGui.menuItem("Weather")) {
                    UIManager.showWorldWeatherUI.set(!UIManager.showWorldWeatherUI.get());
                }
                if (ImGui.menuItem("Gamerules")) {
                    UIManager.showWorldGamerulesUI.set(!UIManager.showWorldGamerulesUI.get());
                }
                ImGui.endMenu();
            }
            if (ImGui.beginMenu("Lighting")) {
                ImGui.beginDisabled();
                ImGui.menuItem("Profiles");
                ImGui.endDisabled();
                ImGui.beginDisabled();
                if (ImGui.menuItem("Light Editor")) {
                    UIManager.showLightEditorUI.set(!UIManager.showLightEditorUI.get());
                }
                ImGui.endDisabled();
                ImGui.endMenu();
            }
            if (ImGui.beginMenu("Audio")) {
                ImGui.beginDisabled();
                ImGui.menuItem("Audio Sources");
                ImGui.endDisabled();
                ImGui.beginDisabled();
                ImGui.menuItem("Ambience");
                ImGui.endDisabled();
                ImGui.endMenu();
            }
            if (ImGui.beginMenu("Events")) {
                ImGui.beginDisabled();
                ImGui.menuItem("Event Editor");
                ImGui.endDisabled();
                ImGui.endMenu();
            }
            if (ImGui.beginMenu("Player(s)")) {
                ImGui.beginDisabled();
                ImGui.menuItem("Universal Attributes");
                ImGui.endDisabled();
                ImGui.beginDisabled();
                ImGui.menuItem("Classes");
                ImGui.endDisabled();
                ImGui.endMenu();
            }
            if (ImGui.beginMenu("Regions")) {
                if (ImGui.menuItem("Regions Editor")) {
                    UIManager.showRegionEditorUI.set(!UIManager.showRegionEditorUI.get());
                }
                ImGui.endMenu();
            }

            ImGui.beginDisabled();
            if (ImGui.beginMenu("Decals")) {
                if (ImGui.menuItem("Decals Editor")) {
                    // Open Decals Editor
                }
                ImGui.endMenu();
            }
            ImGui.endDisabled();



            ImGui.endMainMenuBar();
        }
    }
}
