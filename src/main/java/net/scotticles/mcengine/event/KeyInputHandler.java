package net.scotticles.mcengine.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.scotticles.mcengine.networking.editor.payloads.RequestEditorUIPermsPayload;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    public static final String KEY_CATEGORY_MCENGINE = "key.category.mc-engine";

    // Key Binding Names
    public static final String TOGGLE_ENGINE_UI = "key.mc-engine.toggle-engine-ui_key";


    //Key Bindings
    public static KeyBinding toggleEngineUIKey;





    public static void registerKeyInputs() {

        ClientTickEvents.END_CLIENT_TICK.register(minecraftClient -> {

            if (toggleEngineUIKey.wasPressed()) {
//                UIManager.showEngineUI = !UIManager.showEngineUI;
                ClientPlayNetworking.send(new RequestEditorUIPermsPayload());
            }

        });
    }

    public static void registerKeybinds() {


        // Toggle Engine UI Key
        toggleEngineUIKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                TOGGLE_ENGINE_UI,
                InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_O,
                KEY_CATEGORY_MCENGINE));
        registerKeyInputs();

    }
}
