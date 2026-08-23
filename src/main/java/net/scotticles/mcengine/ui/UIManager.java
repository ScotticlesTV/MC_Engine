package net.scotticles.mcengine.ui;

import foundry.imgui.api.ImGuiMC;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.type.ImBoolean;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.scotticles.mcengine.settings.MCEngineClientConfig;
import net.scotticles.mcengine.ui.windows.*;

public class UIManager {

    // UI States
    public static boolean showEngineUI = false;
    public static ImBoolean showClientEngineSettings = new ImBoolean(false);
    public static ImBoolean showWorldTimeUI = new ImBoolean(false);
    public static ImBoolean showWorldWeatherUI = new ImBoolean(false);
    public static ImBoolean showWorldGamerulesUI = new ImBoolean(false);
    public static ImBoolean showWorldMusicUI = new ImBoolean(false);
    public static ImBoolean showRegionEditorUI = new ImBoolean(false);
    public static ImBoolean showLightEditorUI = new ImBoolean(false);
    public static ImBoolean showInteractionEditorUI = new ImBoolean(false);
//    public static ImBoolean showDecalsEditorUI = new ImBoolean(false);



    public static void editorUIInit() {
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            try (ImGuiMC.ActiveContext ctx = ImGuiMC.withImGui()) {
                if (ctx == null) return;
                if (MCEngineClientConfig.useCustomUIColors) {
                    ImGui.getStyle().setColor(ImGuiCol.WindowBg, MCEngineClientConfig.uiColorR, MCEngineClientConfig.uiColorG, MCEngineClientConfig.uiColorB, MCEngineClientConfig.uiColorA);
                }
                else {
                    ImGui.styleColorsDark();
                }
            }

            // This runs directly on the Render Thread during frame drawing
            if (showEngineUI) {
                EngineNavBarUI.showEngineNavBar();
                if (showClientEngineSettings.get())
                {
                    ClientEngineSettingsUI.showClientEngineSettings();
                }
                if (showWorldTimeUI.get()) {
                    WorldTimeUI.showWorldTimeUI();
                }
                if (showWorldWeatherUI.get()) {
                    WorldWeatherUI.showWorldWeatherUI();
                }
                if (showWorldGamerulesUI.get()) {
                    WorldGamerulesUI.showWorldGamerulesUI();
                }
                if (showWorldMusicUI.get()) {
                    WorldMusicUI.showWorldWeatherUI();
                }
                if (showRegionEditorUI.get()) {
                    RegionsEditorUI.showRegionsEditorUI();
                }
                if (showInteractionEditorUI.get()) {
                    InteractionsEditorUI.showInteractionEditorUI();
                }
                if (showLightEditorUI.get()) {
                    LightEditorUI.showLightEditorUI();
                }
            }
        });
    }

    public static void engineUIWorldJoinInit() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (MCEngineClientConfig.openNavBarOnWorldJoin) {
                UIManager.showEngineUI = true;
            } else {
                UIManager.showEngineUI = false;
            }
        });
    }
}
