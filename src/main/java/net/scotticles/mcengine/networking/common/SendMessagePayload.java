package net.scotticles.mcengine.networking.common;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SendMessagePayload(String message) implements CustomPayload {
    public static final Id<SendMessagePayload> TYPE =
            new Id<>(Identifier.of("mc_engine", "send_message_packet"));

    public static final PacketCodec<RegistryByteBuf, SendMessagePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, SendMessagePayload::message,
            SendMessagePayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() { return TYPE; }
}