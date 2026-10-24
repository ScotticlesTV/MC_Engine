package net.scotticles.mcengine.ui.windows;

import foundry.imgui.api.ImGuiMC;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiCond;
import net.minecraft.util.Util;
import net.scotticles.mcengine.ui.UIManager;

public class WelcomeWindowUI {

    public static void showWelcomeWindowUI() {
        try (ImGuiMC.ActiveContext ctx = ImGuiMC.withImGui()) {
            if (ctx == null) {
                return;
            }

            // ImGUI Code
            ImVec2 screenCenter = ImGui.getMainViewport().getCenter();
            ImGui.setNextWindowSize(820, 520, ImGuiCond.Once);
            ImGui.setNextWindowPos(screenCenter.x, screenCenter.y, ImGuiCond.Once, 0.5f, 0.5f);
            ImGui.begin("Welcome to MC Engine!", UIManager.showWelcomeWindowUI);
            ImGui.textWrapped("Hello there! Welcome to MC Engine!");
            ImGui.spacing();
            ImGui.textWrapped("It seems like this is your first time using the engine, so here's a couple tips to get you started:");

            ImGui.dummy(0f, 40f);

            ImGui.textWrapped("Accessing The Engine UI:");
            ImGui.separator();
            ImGui.textWrapped("The Engine Nav Bar can be opened by clicking \"O\" so long as you have operator permissions on the world/server you're currently in.");
            ImGui.textWrapped("(This key is rebindable in the keybind settings menu.)");

            ImGui.dummy(0f, 40f);

            ImGui.textWrapped("Engine Documentation:");
            ImGui.separator();
            ImGui.textWrapped("All of MC Engine's documentation, detailing all of its features, can be found on a public GitHub Wiki page.");
            ImGui.textWrapped("(The button below will open that Wiki page in your default browser.)");
            ImGui.spacing();
            if (ImGui.button("Open Engine Documentation")) {
                Util.getOperatingSystem().open("https://github.com/ScotticlesTV/MC_Engine/wiki");
            }
            ImGui.end();
        }
    }
}
