package net.scotticles.mcengine.ui.windows;

import foundry.imgui.api.ImGuiMC;
import imgui.ImGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.scotticles.mcengine.ui.UIManager;

public class WorldMusicUI {
    public static boolean playVanillaMusic = false;
    public static boolean playCustomMusic = false;

    public static void showWorldWeatherUI() {
        MinecraftClient client = MinecraftClient.getInstance();
        try (ImGuiMC.ActiveContext ctx = ImGuiMC.withImGui()) {
            if (ctx == null) {
                return;
            }

            // ImGUI Code
            ImGui.begin("World Music", UIManager.showWorldMusicUI);
            ImGui.textWrapped("Set the world's music settings.");
            // Play Vanilla World Music
            if (ImGui.checkbox("Play Vanilla World Music", playVanillaMusic)) {
                playVanillaMusic = !playVanillaMusic;
                client.player.sendMessage(Text.of("Play Vanilla World Music: " + playVanillaMusic), true);
                client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            }
            ImGui.setItemTooltip("Set whether or not Minecraft's regular world music should play.");
            // Play Custom World Music
            if (ImGui.checkbox("Play Custom World Music", playCustomMusic)) {
                playCustomMusic = !playCustomMusic;
                client.player.sendMessage(Text.of("Play Custom World Music: " + playCustomMusic), true);
                client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            }
            ImGui.setItemTooltip("Set whether or not your chosen custom world music should play.");

            ImGui.end();
        }
    }
}
