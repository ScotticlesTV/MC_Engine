package net.scotticles.mcengine;


import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.scotticles.mcengine.event.KeyInputHandler;
import net.scotticles.mcengine.networking.common.S2C;
import net.scotticles.mcengine.networking.editor.payloads.OpenEditorUIPayload;
import net.scotticles.mcengine.networking.regions.payloads.sendRegionsDataPayload;
import net.scotticles.mcengine.regions.RegionsManager;
import net.scotticles.mcengine.regions.regiondatasaving.RegionData;
import net.scotticles.mcengine.settings.MCEngineConfig;
import net.scotticles.mcengine.ui.UIManager;

import java.util.Set;

public class MCEngineClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Register Keybinds
        KeyInputHandler.registerKeybinds();

        // Initialize Editor UI
        UIManager.editorUIInit();
        UIManager.engineUIWorldJoinInit();

        // Register Client Side Packet Recievers
        S2C.registerClientReceivers();
    }
}