package net.scotticles.mcengine.ui.windows;

import foundry.imgui.api.ImGuiMC;
import imgui.ImGui;
import net.scotticles.mcengine.settings.MCEngineClientSettings;
import net.scotticles.mcengine.ui.UIManager;

public class ClientEngineSettingsUI {
    private static final float[] uiColor = {MCEngineClientSettings.uiColorR, MCEngineClientSettings.uiColorG, MCEngineClientSettings.uiColorB, MCEngineClientSettings.uiColorA};
    // UI Loop
    public static void showClientEngineSettings() {
        try (ImGuiMC.ActiveContext ctx = ImGuiMC.withImGui()) {
            if (ctx == null) {
                return;
            }

            // ImGUI Code
            ImGui.begin("Client Engine Settings", UIManager.showClientEngineSettings);
            ImGui.textWrapped("Configure MC Engine to your liking.");
            // Open Nav Bar On World Join
            if (ImGui.checkbox("Automatically open the Engine Nav Bar when joining a world.", MCEngineClientSettings.openNavBarOnWorldJoin)) {
                MCEngineClientSettings.openNavBarOnWorldJoin = !MCEngineClientSettings.openNavBarOnWorldJoin;
                MCEngineClientSettings.save();
            }
            ImGui.separator();

            // Play Vanilla Menu Music
            if (ImGui.checkbox("Play vanilla menu music.", MCEngineClientSettings.shouldPlayVanillaMenuMusic)) {
                MCEngineClientSettings.shouldPlayVanillaMenuMusic = !MCEngineClientSettings.shouldPlayVanillaMenuMusic;
                MCEngineClientSettings.save();
            }
            ImGui.setItemTooltip("Set whether vanilla music should play in the main menu (requires a restart to take full effect)");
            ImGui.separator();

            // UI Colors
            ImGui.textWrapped("Change The Color Of The Engine UI");
            if (ImGui.checkbox("Use Custom UI Colors", MCEngineClientSettings.useCustomUIColors)) {
                MCEngineClientSettings.useCustomUIColors = !MCEngineClientSettings.useCustomUIColors;
                MCEngineClientSettings.save();
            }
            ImGui.spacing();
            if (ImGui.colorEdit4("UI Color", uiColor)) {
                MCEngineClientSettings.uiColorR = uiColor[0];
                MCEngineClientSettings.uiColorG = uiColor[1];
                MCEngineClientSettings.uiColorB = uiColor[2];
                MCEngineClientSettings.uiColorA = uiColor[3];
                MCEngineClientSettings.save();
            }
            ImGui.end();
        }
    }
}