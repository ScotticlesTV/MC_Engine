package net.scotticles.mcengine.networking.weather.payloads;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ChangeWeatherPayload(String weatherType) implements CustomPayload {
    public static final CustomPayload.Id<ChangeWeatherPayload> TYPE =
            new CustomPayload.Id<>(Identifier.of("mc_engine", "change_weather_packet"));

    public static final PacketCodec<RegistryByteBuf, ChangeWeatherPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, ChangeWeatherPayload::weatherType,
            ChangeWeatherPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() { return TYPE; }
}