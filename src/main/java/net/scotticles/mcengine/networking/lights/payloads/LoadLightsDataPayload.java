package net.scotticles.mcengine.networking.lights.payloads;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record LoadLightsDataPayload(String fileName) implements CustomPayload {
    public static final Id<LoadLightsDataPayload> TYPE =
            new Id<>(Identifier.of("mc_engine", "load_lights_data_packet"));

    public static final PacketCodec<RegistryByteBuf, LoadLightsDataPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, LoadLightsDataPayload::fileName,
            LoadLightsDataPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}