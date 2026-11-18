package net.scotticles.mcengine;

import net.fabricmc.api.ClientModInitializer;
import net.scotticles.mcengine.event.KeyInputHandler;
import net.scotticles.mcengine.networking.common.ClientReceiving;
import net.scotticles.mcengine.settings.MCEngineClientSettings;
import net.scotticles.mcengine.ui.UIManager;

public class MCEngineClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Load The Client Engine Settings Config
        MCEngineClientSettings.load();

        // Register Keybinds
        KeyInputHandler.registerKeybinds();

        // Initialize Editor UI
        UIManager.editorUIInit();
        UIManager.engineUIWorldJoinInit();

        // Register Client Side Packet Receivers
        ClientReceiving.registerClientReceivers();
    }
}