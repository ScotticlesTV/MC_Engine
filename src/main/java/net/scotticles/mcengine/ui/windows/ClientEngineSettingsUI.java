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
            // Reopen Any Engine Windows That Were Open Upon Leaving A World On World Join
            // All Previously Open Windows Should Open At Their Previous Position And Size

            // Should This Just Be A General Setting For The Windows Open When Leaving *Any* World
            // Or Should Each World Have Its Own Data For Whether Or Not A Window Was Open?
            // If All Worlds/Servers Have A Specific Set That May Require Client JSON Files, But
            // If It's A General Check Then It Can Just Be A Config File.
            if (ImGui.checkbox("##Reopen windows that were open upon leaving the last world/server..", MCEngineClientConfig.reopenWindowsOnWorldJoin)) {
                MCEngineClientConfig.reopenWindowsOnWorldJoin = !MCEngineClientConfig.reopenWindowsOnWorldJoin;
                MCEngineClientConfig.save();
            }
            ImGui.sameLine();
            ImGui.textWrapped("Reopen windows that were open upon leaving the last world/server.");
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