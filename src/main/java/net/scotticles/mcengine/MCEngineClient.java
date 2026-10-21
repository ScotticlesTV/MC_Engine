package net.scotticles.mcengine;

import foundry.veil.Veil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.scotticles.mcengine.event.KeyInputHandler;
import net.scotticles.mcengine.networking.common.S2C;
import net.scotticles.mcengine.ui.UIManager;

import javax.swing.text.JTextComponent;

public class MCEngineClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Register Keybinds
        KeyInputHandler.registerKeybinds();

        // Initialize Editor UI
        UIManager.editorUIInit();
        UIManager.engineUIWorldJoinInit();

        // Register Client Side Packet Receivers
        S2C.registerClientReceivers();

        // Unbind Veil Editor Key -- Logic Does Not Work
//        ClientLifecycleEvents.CLIENT_STARTED.register(minecraftClient -> {
//            for (KeyBinding key : minecraftClient.options.allKeys) {
//                if ("key.veil.editor".equals(key.getTranslationKey())) {
//                    key.setBoundKey(InputUtil.UNKNOWN_KEY);
//
//                    KeyBinding.updateKeysByCode();
//                    System.out.println("[" + MCEngine.MOD_ID + "]: Successfully Found And Unbound Veil Editor Key");
//                    break;
//                }
//            }
//        });
    }
}