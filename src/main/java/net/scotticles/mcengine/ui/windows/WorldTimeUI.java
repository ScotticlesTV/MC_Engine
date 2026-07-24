package net.scotticles.mcengine.ui.windows;

import foundry.imgui.api.ImGuiMC;
import imgui.ImGui;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.scotticles.mcengine.networking.regions.payloads.SyncRegionsDataPayload;
import net.scotticles.mcengine.networking.time.payloads.SetTimePayload;
import net.scotticles.mcengine.regions.RegionsManager;
import net.scotticles.mcengine.ui.UIManager;

import java.util.HashSet;

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
                    ClientPlayNetworking.send(new SetTimePayload("day"));
                    client.player.sendMessage(Text.of("Time Set To Day"), true);
                    client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                }
            }

            // Set Time To Noon
            if (ImGui.button("Noon")) {
                if (client.player != null) {
                    ClientPlayNetworking.send(new SetTimePayload("noon"));
                    client.player.sendMessage(Text.of("Time Set To Noon"), true);
                    client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                }
            }

            // Set Time To Night
            if (ImGui.button("Night")) {
                if (client.player != null) {
                    ClientPlayNetworking.send(new SetTimePayload("night"));
                    client.player.sendMessage(Text.of("Time Set To Night"), true);
                    client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                }
            }

            // Set Time To Midnight
            if (ImGui.button("Midnight")) {
                if (client.player != null) {
                    ClientPlayNetworking.send(new SetTimePayload("midnight"));
                    client.player.sendMessage(Text.of("Time Set To Midnight"), true);
                    client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                }
            }
            ImGui.end();
        }
    }
}
