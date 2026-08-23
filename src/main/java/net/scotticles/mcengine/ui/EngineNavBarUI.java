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
                if (ImGui.menuItem("Music")) {
                    UIManager.showWorldMusicUI.set(!UIManager.showWorldMusicUI.get());
                }
                ImGui.endMenu();
            }
            if (ImGui.beginMenu("Regions")) {
                if (ImGui.menuItem("Regions Editor")) {
                    UIManager.showRegionEditorUI.set(!UIManager.showRegionEditorUI.get());
                }
                ImGui.endMenu();
            }
            if (ImGui.beginMenu("Interactions")) {
                if (ImGui.menuItem("Interactions Editor")) {
                    UIManager.showInteractionEditorUI.set(!UIManager.showInteractionEditorUI.get());
                }
                ImGui.endMenu();
            }

            ImGui.beginDisabled();
            if (ImGui.beginMenu("Lighting")) {
                ImGui.menuItem("Light Profiles");
                ImGui.endDisabled();
                ImGui.beginDisabled();
                if (ImGui.menuItem("Light Editor")) {
                    UIManager.showLightEditorUI.set(!UIManager.showLightEditorUI.get());
                }
                ImGui.endMenu();
            }
            ImGui.endDisabled();

            ImGui.beginDisabled();
            if (ImGui.beginMenu("Audio")) {
                ImGui.menuItem("Audio Sources");
                ImGui.endDisabled();
                ImGui.beginDisabled();
                ImGui.menuItem("Ambience");
                ImGui.endMenu();
            }
            ImGui.endDisabled();

            ImGui.beginDisabled();
            if (ImGui.beginMenu("Sequences")) {
                ImGui.menuItem("Sequences Editor");
                ImGui.endMenu();
            }
            ImGui.endDisabled();

            ImGui.beginDisabled();
            if (ImGui.beginMenu("Player(s)")) {
                ImGui.menuItem("Universal Attributes");
                ImGui.menuItem("Classes");
                ImGui.endMenu();
            }
            ImGui.endDisabled();

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
