package net.scotticles.mcengine.ui.windows;

import foundry.imgui.api.ImGuiMC;
import imgui.ImGui;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.scotticles.mcengine.networking.gamerules.payloads.SyncGamerulesDataPayload;
import net.scotticles.mcengine.ui.UIManager;

public class WorldGamerulesUI {

    public static boolean doDaylightCycle = false;
    public static boolean doWeatherCycle = false;
    public static boolean sendCommandFeedback = false;

    public static void showWorldGamerulesUI() {
        MinecraftClient client = MinecraftClient.getInstance();
                try (ImGuiMC.ActiveContext ctx = ImGuiMC.withImGui()) {
                    if (ctx == null) {
                        return;
                    }
                    boolean shouldSyncGamerules = false;

                    ImGui.begin("Gamerules", UIManager.showWorldGamerulesUI);
                    ImGui.textWrapped("View and set the world's gamerules.");

                    // Do Daylight Cycle Gamerule
                    if (ImGui.checkbox("Do Daylight Cycle", doDaylightCycle)) {
                        doDaylightCycle = !doDaylightCycle;
                        client.player.sendMessage(Text.of("DoDaylightCycle Set To" + doDaylightCycle), true);
                        client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                        shouldSyncGamerules = true;
                    }
                    ImGui.setItemTooltip("Set whether or not time passes.");

                    // Do Weather Cycle Gamerule
                    if (ImGui.checkbox("Do Weather Cycle", doWeatherCycle)) {
                        doWeatherCycle = !doWeatherCycle;
                        client.player.sendMessage(Text.of("DoWeatherCycle Set To" + doWeatherCycle), true);
                        client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                        shouldSyncGamerules = true;
                    }
                    ImGui.setItemTooltip("Set whether or not weather cycles naturally.");

                    // Send Command Feedback Gamerule
                    if (ImGui.checkbox("Send Command Feedback", sendCommandFeedback)) {
                        sendCommandFeedback = !sendCommandFeedback;
                        client.player.sendMessage(Text.of("SendCommandFeedback Set To" + sendCommandFeedback), true);
                        client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                        shouldSyncGamerules = true;
                    }
                    ImGui.setItemTooltip("Set whether or not the result of commands is printed in chat.");

                    ImGui.end();
                    if (shouldSyncGamerules) {
                        ClientPlayNetworking.send(new SyncGamerulesDataPayload(doDaylightCycle, doWeatherCycle, sendCommandFeedback));
                    }
        }
    }
}