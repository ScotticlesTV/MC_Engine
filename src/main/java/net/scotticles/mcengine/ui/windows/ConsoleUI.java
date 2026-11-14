package net.scotticles.mcengine.ui.windows;

import foundry.imgui.api.ImGuiMC;
import imgui.ImGui;
import net.scotticles.mcengine.console.ConsoleManager;
import net.scotticles.mcengine.ui.UIManager;

public class ConsoleUI {

    public static void showConsoleUI() {
        try (ImGuiMC.ActiveContext ctx = ImGuiMC.withImGui()) {
            if (ctx == null) {
                return;
            }

            // ImGUI Code
            ImGui.begin("Console", UIManager.showConsoleUI);

            for (String message : ConsoleManager.consoleMessages) {
                ImGui.textWrapped(message);
                ImGui.separator();
            }
            ImGui.end();
        }
    }
}
