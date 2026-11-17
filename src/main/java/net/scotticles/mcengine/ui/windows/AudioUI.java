package net.scotticles.mcengine.ui.windows;

import foundry.imgui.api.ImGuiMC;
import imgui.ImGui;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.scotticles.mcengine.networking.sound.SyncSoundSettingsPayload;
import net.scotticles.mcengine.sound.SoundManager;
import net.scotticles.mcengine.ui.UIManager;

public class AudioUI {

    public static void showSoundUI() {
        try (ImGuiMC.ActiveContext ctx = ImGuiMC.withImGui()) {
            if (ctx == null) {
                return;
            }

            // ImGUI Code
            ImGui.begin("Audio Settings", UIManager.showAudioUI);
            ImGui.textWrapped("Set the world's audio settings.");
            if (ImGui.checkbox("Play Vanilla Music", SoundManager.shouldPlayVanillaMusic)) {
                SoundManager.shouldPlayVanillaMusic = !SoundManager.shouldPlayVanillaMusic;
                ClientPlayNetworking.send(new SyncSoundSettingsPayload(SoundManager.shouldPlayVanillaMusic));
            }
            ImGui.setItemTooltip("Whether vanilla music should play for players connected to the server.");
            ImGui.end();
        }
    }
}
