package net.scotticles.mcengine.ui.windows;

import foundry.imgui.api.ImGuiMC;
import imgui.ImGui;
import net.scotticles.mcengine.ui.UIManager;

public class WorldGamerulesUI {

    public static void showWorldGamerulesUI() {
                try (ImGuiMC.ActiveContext ctx = ImGuiMC.withImGui()) {
                    if (ctx == null) {
                        return;
                    }
                    // ImGUI Code
                    ImGui.begin("Gamerules", UIManager.showWorldGamerulesUI);
                    ImGui.textWrapped("View and set the world's gamerules.");
                    if (ImGui.checkbox("Do Daylight Cycle", false)) {

                    }
                    ImGui.setItemTooltip("Set whether or not time passes.");
                    if (ImGui.checkbox("Do Weather Cycle", false)) {

                    }
                    ImGui.setItemTooltip("Set whether or not weather cycles naturally.");
                    ImGui.end();
        }
    }
}