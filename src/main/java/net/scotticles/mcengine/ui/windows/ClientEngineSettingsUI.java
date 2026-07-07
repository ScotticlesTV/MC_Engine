package net.scotticles.mcengine.ui.windows;

import foundry.imgui.api.ImGuiMC;
import imgui.ImGui;
import net.scotticles.mcengine.settings.MCEngineConfig;
import net.scotticles.mcengine.ui.UIManager;

public class ClientEngineSettingsUI {
    public static void showClientEngineSettings() {
        try (ImGuiMC.ActiveContext ctx = ImGuiMC.withImGui()) {
            if (ctx == null) {
                return;
            }
            // ImGUI Code
                ImGui.begin("Client Engine Settings", UIManager.showClientEngineSettings);
                ImGui.textWrapped("Configure MC Engine to your liking.");
                if (ImGui.checkbox("Automatically open the Engine Nav Bar when joining a world.", MCEngineConfig.openNavBarOnWorldJoin)) {
                    MCEngineConfig.openNavBarOnWorldJoin = !MCEngineConfig.openNavBarOnWorldJoin;
                    MCEngineConfig.save();
                }
            ImGui.end();
        }
    }
}
