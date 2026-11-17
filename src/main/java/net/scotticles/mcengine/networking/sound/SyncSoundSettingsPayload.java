package net.scotticles.mcengine.networking.sound;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SyncSoundSettingsPayload(boolean shouldPlayVanillaMusic) implements CustomPayload {
    public static final Id<SyncSoundSettingsPayload> TYPE =
            new Id<>(Identifier.of("mc_engine", "sync_sound_settings_packet"));

    public static final PacketCodec<RegistryByteBuf, SyncSoundSettingsPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOL, SyncSoundSettingsPayload::shouldPlayVanillaMusic,
            SyncSoundSettingsPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() { return TYPE; }
}