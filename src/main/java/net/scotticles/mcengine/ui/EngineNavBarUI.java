package net.scotticles.mcengine.ui;

import foundry.imgui.api.ImGuiMC;
import imgui.ImGui;
import net.minecraft.util.Util;

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
                ImGui.setItemTooltip("Open the Client Engine settings menu to change the engine UI's functionality and appearance.");
                if (ImGui.menuItem("Engine Documentation")) {
                    Util.getOperatingSystem().open("https://github.com/ScotticlesTV/MC_Engine/wiki");
                }
                ImGui.setItemTooltip("Open the MC Engine documentation in your web browser.");
                if (ImGui.menuItem("Reopen Welcome Window")) {
                    UIManager.showWelcomeWindowUI.set(true);
                }
                ImGui.setItemTooltip("Reopen the Welcome Window that shows on first joining a world with MC Engine installed.");
                ImGui.endMenu();
            }
            ImGui.separator();
            // World Tab
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
                ImGui.beginDisabled();
                if (ImGui.menuItem("Music")) {
                    UIManager.showWorldMusicUI.set(!UIManager.showWorldMusicUI.get());
                }
                ImGui.endDisabled();
                ImGui.endMenu();
            }
            // Region Tab
            if (ImGui.beginMenu("Regions")) {
                if (ImGui.menuItem("Regions Editor")) {
                    UIManager.showRegionEditorUI.set(!UIManager.showRegionEditorUI.get());
                }
                ImGui.endMenu();
            }

            // Lighting Tab
            if (ImGui.beginMenu("Lights")) {
                ImGui.beginDisabled();
                ImGui.menuItem("Light Profiles");
                ImGui.endDisabled();
                if (ImGui.menuItem("Light Editor")) {
                    UIManager.showLightEditorUI.set(!UIManager.showLightEditorUI.get());
                }
                ImGui.endMenu();
            }

            // Interactions Tab
            ImGui.beginDisabled();
            if (ImGui.beginMenu("Interactions")) {
                if (ImGui.menuItem("Interactions Editor")) {
                    UIManager.showInteractionEditorUI.set(!UIManager.showInteractionEditorUI.get());
                }
                ImGui.endMenu();
            }
            ImGui.endDisabled();

            // Audio Tab
            ImGui.beginDisabled();
            if (ImGui.beginMenu("Audio")) {
                ImGui.menuItem("Audio Sources");
                ImGui.endDisabled();
                ImGui.beginDisabled();
                ImGui.menuItem("Ambience");
                ImGui.endMenu();
            }
            ImGui.endDisabled();

            // Sequences Tab
            ImGui.beginDisabled();
            if (ImGui.beginMenu("Sequences")) {
                ImGui.menuItem("Sequences Editor");
                ImGui.endMenu();
            }
            ImGui.endDisabled();

            // Players Tab
            ImGui.beginDisabled();
            if (ImGui.beginMenu("Player(s)")) {
                ImGui.menuItem("Universal Attributes");
                ImGui.menuItem("Classes");
                ImGui.endMenu();
            }
            ImGui.endDisabled();

            // Decals Tab
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
