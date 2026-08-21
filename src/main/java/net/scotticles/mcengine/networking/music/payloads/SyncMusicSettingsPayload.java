package net.scotticles.mcengine.networking.music.payloads;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SyncMusicSettingsPayload(boolean playVanillaMusic, boolean playCustomMusic) implements CustomPayload {
    public static final Id<SyncMusicSettingsPayload> TYPE =
            new Id<>(Identifier.of("mc_engine", "sync_music_settings_packet"));

    public static final PacketCodec<RegistryByteBuf, SyncMusicSettingsPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOL, SyncMusicSettingsPayload::playVanillaMusic,
            PacketCodecs.BOOL, SyncMusicSettingsPayload::playCustomMusic,
            SyncMusicSettingsPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}