package net.scotticles.mcengine.ui.windows;

import foundry.imgui.api.ImGuiMC;
import imgui.ImGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.scotticles.mcengine.ui.UIManager;

public class WorldWeatherUI {

    public static void showWorldWeatherUI() {
        MinecraftClient client = MinecraftClient.getInstance();
        try (ImGuiMC.ActiveContext ctx = ImGuiMC.withImGui()) {
            if (ctx == null) {
                return;
            }


            // ImGUI Code
            ImGui.begin("World Weather", UIManager.showWorldWeatherUI);
            ImGui.textWrapped("Set the world's weather.");
            if (ImGui.button("Clear")) {
                if (client.player != null) {
                    client.player.networkHandler.sendChatCommand("weather clear");
                    client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                }
            }
            if (ImGui.button("Rain")) {
                if (client.player != null) {
                    client.player.networkHandler.sendChatCommand("weather rain");
                    client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                }
            }
            if (ImGui.button("Thunder")) {
                if (client.player != null) {
                    client.player.networkHandler.sendChatCommand("weather thunder");
                    client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                }
            }
            ImGui.end();
        }
    }
}
