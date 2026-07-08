package net.scotticles.mcengine;


import net.fabricmc.api.ClientModInitializer;
import net.scotticles.mcengine.event.KeyInputHandler;
import net.scotticles.mcengine.networking.common.S2C;
import net.scotticles.mcengine.ui.UIManager;

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