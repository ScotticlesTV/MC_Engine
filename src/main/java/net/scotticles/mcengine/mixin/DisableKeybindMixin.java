package net.scotticles.mcengine.mixin;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = KeyBindingHelper.class, remap = false)
public class DisableKeybindMixin {

    @Inject(method = "registerKeyBinding", at = @At("HEAD"), cancellable = true)
    private static void onRegisterKeyBinding(KeyBinding keyBinding, CallbackInfoReturnable<KeyBinding> cir) {
        // Replace with the exact translation key of the dependency's keybind
        if ("key.veil.editor".equals(keyBinding.getTranslationKey())) {
            // Cancel the registration, returning the keybind without adding it to the game
            keyBinding.setBoundKey(InputUtil.UNKNOWN_KEY);
            cir.setReturnValue(keyBinding);
        }
    }
}
