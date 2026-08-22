package net.scotticles.mcengine.interactions.interactiondatasaving;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Uuids;
import net.scotticles.mcengine.interactions.interactiondatasaving.InteractionData;

import java.util.*;

public class InteractionData {

    // Interaction Data Variables
    public UUID interactionUuid;
    public String interactionName;
    public boolean interactionEnabled;
    public int interactionX;
    public int interactionY;
    public int interactionZ;
    public List<String> interactionCommands;

    // Codecs For Region Enter, Exit, And UUID Sets
    private static final PacketCodec<RegistryByteBuf, List<String>> STRING_LIST_CODEC =
            PacketCodecs.collection(ArrayList::new, PacketCodecs.STRING);

    private static final PacketCodec<RegistryByteBuf, Set<UUID>> UUID_SET_CODEC =
            PacketCodecs.collection(HashSet::new, Uuids.PACKET_CODEC);

    // Packet Code
    public static final PacketCodec<RegistryByteBuf, InteractionData> PACKET_CODEC = PacketCodec.of(
            // Packet Encoder: Value comes first, then the buffer (value, buf)
            (InteractionData value, RegistryByteBuf buf) -> {
                buf.writeUuid(value.interactionUuid);
                buf.writeString(value.interactionName);
                buf.writeBoolean(value.interactionEnabled);
                buf.writeInt(value.interactionX);
                buf.writeInt(value.interactionY);
                buf.writeInt(value.interactionZ);
                STRING_LIST_CODEC.encode(buf, value.interactionCommands);

            },
            // Packet Decoder
            (RegistryByteBuf buf) -> {
                UUID uuid = buf.readUuid();
                String name = buf.readString();
                boolean enabled = buf.readBoolean();
                int x = buf.readInt();
                int y = buf.readInt();
                int z = buf.readInt();
                List<String> commands = STRING_LIST_CODEC.decode(buf);

                return new InteractionData(uuid, name, enabled, x, y, z, commands);
            }
    );

    public InteractionData(UUID interactionUuid, String interactionName, boolean interactionEnabled, int interactionX, int interactionY, int interactionZ, List<String> interactionCommands) {
        this.interactionUuid = interactionUuid;
        this.interactionName = interactionName;
        this.interactionEnabled = interactionEnabled;
        this.interactionX = interactionX;
        this.interactionY = interactionY;
        this.interactionZ = interactionZ;
        this.interactionCommands = new ArrayList<>(interactionCommands);
    }

    public InteractionData toRegionsData() {
        return new InteractionData(interactionUuid, interactionName, interactionEnabled, interactionX, interactionY, interactionZ, new ArrayList<>(interactionCommands));
    }
}