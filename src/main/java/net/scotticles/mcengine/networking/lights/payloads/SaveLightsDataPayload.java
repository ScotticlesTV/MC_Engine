package net.scotticles.mcengine.networking.lights.payloads;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SaveLightsDataPayload(String fileName) implements CustomPayload {
    public static final Id<SaveLightsDataPayload> TYPE =
            new Id<>(Identifier.of("mc_engine", "save_lights_data_packet"));

    public static final PacketCodec<RegistryByteBuf, SaveLightsDataPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, SaveLightsDataPayload::fileName,
            SaveLightsDataPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}