package net.scotticles.mcengine.ui.windows;

import foundry.imgui.api.ImGuiMC;
import imgui.ImGui;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.scotticles.mcengine.networking.weather.payloads.ChangeWeatherPayload;
import net.scotticles.mcengine.ui.UIManager;

import java.util.HashSet;

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
                    ClientPlayNetworking.send(new ChangeWeatherPayload("clear"));
                    client.player.sendMessage(Text.of("Weather Set To Clear"), true);
                    client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                }
            }
            if (ImGui.button("Rain")) {
                if (client.player != null) {
                    ClientPlayNetworking.send(new ChangeWeatherPayload("rain"));
                    client.player.sendMessage(Text.of("Weather Set To Rain"), true);
                    client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                }
            }
            if (ImGui.button("Thunder")) {
                if (client.player != null) {
                    ClientPlayNetworking.send(new ChangeWeatherPayload("thunder"));
                    client.player.sendMessage(Text.of("Weather Set To Thunder"), true);
                    client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                }
            }
            ImGui.end();
        }
    }
}
