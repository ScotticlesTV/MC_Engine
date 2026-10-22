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
        if ("key.veil.editor".equals(keyBinding.getTranslationKey())) {
            // Set the key's binding to unbound.
            keyBinding.setBoundKey(InputUtil.UNKNOWN_KEY);
            // Cancel the registration, returning the keybind without adding it to the game
            cir.setReturnValue(keyBinding);
        }
    }
}
