package net.scotticles.mcengine.regions;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.random.Random;

public class RegionSoundInstance extends MovingSoundInstance {
    private final ClientPlayerEntity player;
    private boolean stopping = false;
    private final float fadeIncrement;
//    private boolean active = true;

    public RegionSoundInstance(ClientPlayerEntity player, SoundEvent soundEvent, float fadeDurationSeconds) {
        super(soundEvent, SoundCategory.AMBIENT, Random.create());
        this.player = player;
        this.repeat = true;
        this.repeatDelay = 0;
        this.volume = 1.0F;
        this.pitch = 1.0F;

        this.fadeIncrement = 1.0f / (fadeDurationSeconds * 20.0f);

        // Match player coordinates immediately on creation
        // Potentially Needs To Be A Float?
        this.x = player.getX();
        this.y = player.getY();
        this.z = player.getZ();
    }

    @Override
    public void tick() {
        if (this.player.isRemoved()) {
            this.setDone();
        }

        if (this.stopping) {
            this.volume -= this.fadeIncrement;
            if (this.volume <= 0.0f) {
                this.volume = 0.0f;
                this.setDone();
            }
        }

        // Keep the sound locked directly on the player
        this.x = this.player.getX();
        this.y = this.player.getY();
        this.z = this.player.getZ();
    }

    public void fadeAndStop() {
        this.stopping = true;
    }
}
