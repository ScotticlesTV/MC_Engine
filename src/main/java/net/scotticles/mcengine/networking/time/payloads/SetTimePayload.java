package net.scotticles.mcengine.networking.time.payloads;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SetTimePayload(String worldTime) implements CustomPayload {
    public static final CustomPayload.Id<SetTimePayload> TYPE =
            new CustomPayload.Id<>(Identifier.of("mc_engine", "set_time_packet"));

    public static final PacketCodec<RegistryByteBuf, SetTimePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, SetTimePayload::worldTime,
            SetTimePayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}