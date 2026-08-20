package net.scotticles.mcengine.networking.regions.payloads;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record RegionSoundStatePayload(String soundID, boolean play) implements CustomPayload {
    public static final CustomPayload.Id<RegionSoundStatePayload> TYPE =
            new CustomPayload.Id<>(Identifier.of("mc_engine", "region_sound_state_packet"));

    public static final PacketCodec<RegistryByteBuf, RegionSoundStatePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, RegionSoundStatePayload::soundID,
            PacketCodecs.BOOL, RegionSoundStatePayload::play,
            RegionSoundStatePayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}