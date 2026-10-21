package net.scotticles.mcengine.networking.lights.payloads;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ClearLightRendererPayload() implements CustomPayload {
    public static final Id<ClearLightRendererPayload> TYPE =
            new Id<>(Identifier.of("mc_engine", "clear_light_renderer_packet"));

    public static final PacketCodec<RegistryByteBuf, ClearLightRendererPayload> CODEC =
            PacketCodec.unit(new ClearLightRendererPayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}
