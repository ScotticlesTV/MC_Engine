package net.scotticles.mcengine.ui.windows;

import foundry.imgui.api.ImGuiMC;
import imgui.ImGui;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.scotticles.mcengine.networking.gamerules.SyncGamerulesEditorPayload;
import net.scotticles.mcengine.networking.regions.payloads.SyncRegionsDataPayload;
import net.scotticles.mcengine.regions.RegionsManager;
import net.scotticles.mcengine.ui.UIManager;

import java.util.HashSet;

public class WorldGamerulesUI {

    public static boolean doDaylightCycle = false;
    public static boolean doWeatherCycle = false;
    public static boolean sendCommandFeedback = false;

    public static void showWorldGamerulesUI() {
                try (ImGuiMC.ActiveContext ctx = ImGuiMC.withImGui()) {
                    if (ctx == null) {
                        return;
                    }
                    boolean shouldSyncGamerules = false;
                    ImGui.begin("Gamerules", UIManager.showWorldGamerulesUI);
                    ImGui.textWrapped("View and set the world's gamerules.");
                    if (ImGui.checkbox("Do Daylight Cycle", doDaylightCycle)) {
                        doDaylightCycle = !doDaylightCycle;
                        shouldSyncGamerules = true;
                    }
                    ImGui.setItemTooltip("Set whether or not time passes.");
                    if (ImGui.checkbox("Do Weather Cycle", doWeatherCycle)) {
                        doWeatherCycle = !doWeatherCycle;
                        shouldSyncGamerules = true;
                    }
                    ImGui.setItemTooltip("Set whether or not weather cycles naturally.");
                    if (ImGui.checkbox("Send Command Feedback", sendCommandFeedback)) {
                        sendCommandFeedback = !sendCommandFeedback;
                        shouldSyncGamerules = true;
                    }
                    ImGui.setItemTooltip("Set whether or not the result of commands is printed in chat.");
                    ImGui.end();
                    if (shouldSyncGamerules) {
                        ClientPlayNetworking.send(new SyncGamerulesEditorPayload(doDaylightCycle, doWeatherCycle, sendCommandFeedback));
                    }
        }
    }
}