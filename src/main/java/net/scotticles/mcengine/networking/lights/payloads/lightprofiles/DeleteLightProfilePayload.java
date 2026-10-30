package net.scotticles.mcengine.networking.lights.payloads.lightprofiles;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record DeleteLightProfilePayload(String fileName) implements CustomPayload {
    public static final Id<DeleteLightProfilePayload> TYPE =
            new Id<>(Identifier.of("mc_engine", "delete_light_profile_packet"));

    public static final PacketCodec<RegistryByteBuf, DeleteLightProfilePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, DeleteLightProfilePayload::fileName,
            DeleteLightProfilePayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}