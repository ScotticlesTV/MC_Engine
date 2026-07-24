package net.scotticles.mcengine.ui.windows;

import foundry.imgui.api.ImGuiMC;
import imgui.ImGui;
import imgui.ImGuiStyle;
import imgui.flag.ImGuiCol;
import imgui.type.ImFloat;
import imgui.type.ImInt;
import net.scotticles.mcengine.MCEngine;
import net.scotticles.mcengine.settings.MCEngineConfig;
import net.scotticles.mcengine.ui.UIManager;

public class ClientEngineSettingsUI {
    private static final float[] uiColor = {MCEngineConfig.uiColorR, MCEngineConfig.uiColorG, MCEngineConfig.uiColorB, MCEngineConfig.uiColorA};
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
                if (ImGui.checkbox("##Automatically open the Engine Nav Bar when joining a world.", MCEngineConfig.openNavBarOnWorldJoin)) {
                    MCEngineConfig.openNavBarOnWorldJoin = !MCEngineConfig.openNavBarOnWorldJoin;
                    MCEngineConfig.save();
                }
                ImGui.sameLine();
                ImGui.textWrapped("Automatically open the Engine Nav Bar when joining a world.");
                ImGui.separator();
                // UI Colors
                ImGui.textWrapped("Change The Color Of The Engine UI");
                if (ImGui.checkbox("##Use Custom UI Colors", MCEngineConfig.useCustomUIColors)) {
                    MCEngineConfig.useCustomUIColors = !MCEngineConfig.useCustomUIColors;
                    MCEngineConfig.save();
                }
                ImGui.sameLine();
                ImGui.textWrapped("Use Custom UI Colors");
            ImGui.spacing();
                ImGui.colorEdit4("UI Color", uiColor);
                if (ImGui.isItemDeactivatedAfterEdit()) {
                    MCEngineConfig.uiColorR = uiColor[0];
                    MCEngineConfig.uiColorB = uiColor[1];
                    MCEngineConfig.uiColorG = uiColor[2];
                    MCEngineConfig.uiColorA = uiColor[3];
                    MCEngineConfig.save();
                }
            ImGui.end();
        }
    }
}