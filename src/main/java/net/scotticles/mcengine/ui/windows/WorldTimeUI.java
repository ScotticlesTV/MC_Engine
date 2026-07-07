package net.scotticles.mcengine.ui.windows;

import foundry.imgui.api.ImGuiMC;
import imgui.ImGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.scotticles.mcengine.ui.UIManager;

public class WorldTimeUI {
    public static void showWorldTimeUI() {
        MinecraftClient client = MinecraftClient.getInstance();

        try (ImGuiMC.ActiveContext ctx = ImGuiMC.withImGui()) {
            if (ctx == null) {
                return;
            }
            // ImGUI Code
            ImGui.begin("World Time", UIManager.showWorldTimeUI);
            ImGui.textWrapped("Set the world time.");
            if (ImGui.button("Day")) {
                if (client.player != null) {
                    client.player.networkHandler.sendChatCommand("time set day");
                    client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                }
            }
            if (ImGui.button("Noon")) {
                if (client.player != null) {
                    client.player.networkHandler.sendChatCommand("time set noon");
                    client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                }
            }
            if (ImGui.button("Night")) {
                if (client.player != null) {
                    client.player.networkHandler.sendChatCommand("time set night");
                    client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                }
            }
            if (ImGui.button("Midnight")) {
                if (client.player != null) {
                    client.player.networkHandler.sendChatCommand("time set midnight");
                    client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                }
            }
            ImGui.end();
        }
    }
}
