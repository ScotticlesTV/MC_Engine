package net.scotticles.mcengine.networking.gamerules.payloads;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SyncGamerulesDataPayload(boolean doDaylightCycle,
                                       boolean doWeatherCycle,
                                       boolean sendCommandFeedback,
                                       boolean commandBlockOutput,
                                       boolean doFireTick,
                                       boolean doImmediateRespawn,
                                       boolean doMobSpawning,
                                       boolean fallDamage,
                                       boolean keepInventory,
                                       boolean mobGriefing,
                                       boolean naturalRegeneration,
                                       boolean showDeathMessages) implements CustomPayload {
    public static final CustomPayload.Id<SyncGamerulesDataPayload> TYPE =
            new CustomPayload.Id<>(Identifier.of("mc_engine", "sync_gamerules_data_packet"));

    public static final PacketCodec<RegistryByteBuf, SyncGamerulesDataPayload> CODEC = PacketCodec.of(
            (value, buf) -> {
                buf.writeBoolean(value.doDaylightCycle());
                buf.writeBoolean(value.doWeatherCycle());
                buf.writeBoolean(value.sendCommandFeedback());
                buf.writeBoolean(value.commandBlockOutput());
                buf.writeBoolean(value.doFireTick());
                buf.writeBoolean(value.doImmediateRespawn());
                buf.writeBoolean(value.doMobSpawning());
                buf.writeBoolean(value.fallDamage());
                buf.writeBoolean(value.keepInventory());
                buf.writeBoolean(value.mobGriefing());
                buf.writeBoolean(value.naturalRegeneration());
                buf.writeBoolean(value.showDeathMessages());
            },
            buf -> new SyncGamerulesDataPayload(
                    buf.readBoolean(),
                    buf.readBoolean(),
                    buf.readBoolean(),
                    buf.readBoolean(),
                    buf.readBoolean(),
                    buf.readBoolean(),
                    buf.readBoolean(),
                    buf.readBoolean(),
                    buf.readBoolean(),
                    buf.readBoolean(),
                    buf.readBoolean(),
                    buf.readBoolean()
            )
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }
}