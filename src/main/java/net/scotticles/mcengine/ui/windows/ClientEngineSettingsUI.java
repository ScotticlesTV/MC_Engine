package net.scotticles.mcengine.ui.windows;

import foundry.imgui.api.ImGuiMC;
import imgui.ImGui;
import net.scotticles.mcengine.settings.MCEngineClientConfig;
import net.scotticles.mcengine.ui.UIManager;

public class ClientEngineSettingsUI {
    private static final float[] uiColor = {MCEngineClientConfig.uiColorR, MCEngineClientConfig.uiColorG, MCEngineClientConfig.uiColorB, MCEngineClientConfig.uiColorA};
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
            if (ImGui.checkbox("##Automatically open the Engine Nav Bar when joining a world.", MCEngineClientConfig.openNavBarOnWorldJoin)) {
                MCEngineClientConfig.openNavBarOnWorldJoin = !MCEngineClientConfig.openNavBarOnWorldJoin;
                MCEngineClientConfig.save();
            }
            ImGui.sameLine();
            ImGui.textWrapped("Automatically open the Engine Nav Bar when joining a world.");
            ImGui.separator();
            // UI Colors
            ImGui.textWrapped("Change The Color Of The Engine UI");
            if (ImGui.checkbox("##Use Custom UI Colors", MCEngineClientConfig.useCustomUIColors)) {
                MCEngineClientConfig.useCustomUIColors = !MCEngineClientConfig.useCustomUIColors;
                MCEngineClientConfig.save();
            }
            ImGui.sameLine();
            ImGui.textWrapped("Use Custom UI Colors");
            ImGui.spacing();
            if (ImGui.colorEdit4("UI Color", uiColor)) {
                MCEngineClientConfig.uiColorR = uiColor[0];
                MCEngineClientConfig.uiColorG = uiColor[1];
                MCEngineClientConfig.uiColorB = uiColor[2];
                MCEngineClientConfig.uiColorA = uiColor[3];
                MCEngineClientConfig.save();
            }
            ImGui.end();
        }
    }
}