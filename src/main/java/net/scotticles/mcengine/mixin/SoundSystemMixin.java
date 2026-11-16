package net.scotticles.mcengine.mixin;

import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.scotticles.mcengine.sound.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundSystem.class)
public class SoundSystemMixin {

    @Inject(
            method = "play(Lnet/minecraft/client/sound/SoundInstance;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onPlaySound(SoundInstance sound, CallbackInfo ci) {
        if (sound == null) return;

        Identifier id = sound.getId();
        SoundCategory category = sound.getCategory();

        if (category == SoundCategory.MUSIC && id.getNamespace().equals("minecraft") && !SoundManager.shouldPlayVanillaMusic) {
             ci.cancel();
        }
    }
}