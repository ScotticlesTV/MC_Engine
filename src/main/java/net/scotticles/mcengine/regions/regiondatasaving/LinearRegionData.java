package net.scotticles.mcengine.regions.regiondatasaving;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Uuids;

import java.util.*;

public class LinearRegionData {

    // Region Data Variables
    public UUID regionUuid;
    public String regionName;
    public int regionX1;
    public int regionY1;
    public int regionZ1;
    public int regionX2;
    public int regionY2;
    public int regionZ2;
    public Set<UUID> playersInside;
    public boolean regionEnabled;
    public List<String> regionEnterCommands;
    public List<String> regionExitCommands;
    public String regionSound;
    public float regionSoundVolume;
    public float regionSoundFadeDuration;

    // Codecs For Region Enter, Exit, And UUID Sets
    private static final PacketCodec<RegistryByteBuf, List<String>> STRING_LIST_CODEC =
            PacketCodecs.collection(ArrayList::new, PacketCodecs.STRING);

    private static final PacketCodec<RegistryByteBuf, Set<UUID>> UUID_SET_CODEC =
            PacketCodecs.collection(HashSet::new, Uuids.PACKET_CODEC);

    // Packet Code
    public static final PacketCodec<RegistryByteBuf, LinearRegionData> PACKET_CODEC = PacketCodec.of(
            // Packet Encoder: Value comes first, then the buffer (value, buf)
            (LinearRegionData value, RegistryByteBuf buf) -> {
                buf.writeUuid(value.regionUuid);
                buf.writeString(value.regionName);
                buf.writeInt(value.regionX1);
                buf.writeInt(value.regionY1);
                buf.writeInt(value.regionZ1);
                buf.writeInt(value.regionX2);
                buf.writeInt(value.regionY2);
                buf.writeInt(value.regionZ2);
                STRING_LIST_CODEC.encode(buf, value.regionEnterCommands);
                STRING_LIST_CODEC.encode(buf, value.regionExitCommands);
                UUID_SET_CODEC.encode(buf, value.playersInside);
                buf.writeBoolean(value.regionEnabled);
                buf.writeString(value.regionSound);
                buf.writeFloat(value.regionSoundVolume);
                buf.writeFloat(value.regionSoundFadeDuration);
            },
            // Packet Decoder
            (RegistryByteBuf buf) -> {
                UUID uuid = buf.readUuid();
                String name = buf.readString();
                int x1 = buf.readInt();
                int y1 = buf.readInt();
                int z1 = buf.readInt();
                int x2 = buf.readInt();
                int y2 = buf.readInt();
                int z2 = buf.readInt();
                List<String> enterCommands = STRING_LIST_CODEC.decode(buf);
                List<String> exitCommands = STRING_LIST_CODEC.decode(buf);
                Set<UUID> players = UUID_SET_CODEC.decode(buf);
                boolean enabled = buf.readBoolean();
                String sound = buf.readString();
                float soundVolume = buf.readFloat();
                float soundFadeDuration = buf.readFloat();

                return new LinearRegionData(uuid, name, x1, y1, z1, x2, y2, z2, enterCommands, exitCommands, players, enabled, sound, soundVolume, soundFadeDuration);
            }
    );

    public LinearRegionData(UUID regionUuid, String regionName, int regionX1, int regionY1, int regionZ1, int regionX2, int regionY2, int regionZ2, List<String> regionEnterCommands, List<String> regionExitCommands, Set<UUID> playersInside, boolean regionEnabled, String regionSound, float regionSoundVolume, float regionSoundFadeDuration) {
        this.regionUuid = regionUuid;
        this.regionName = regionName;
        this.regionX1 = regionX1;
        this.regionY1 = regionY1;
        this.regionZ1 = regionZ1;
        this.regionX2 = regionX2;
        this.regionY2 = regionY2;
        this.regionZ2 = regionZ2;
        this.regionEnterCommands = new ArrayList<>(regionEnterCommands);
        this.regionExitCommands = new ArrayList<>(regionExitCommands);
        this.playersInside = playersInside;
        this.regionEnabled = regionEnabled;
        this.regionSound = regionSound;
        this.regionSoundVolume = regionSoundVolume;
        this.regionSoundFadeDuration = regionSoundFadeDuration;
    }

    public LinearRegionData toRegionsData() {
        return new LinearRegionData(regionUuid, regionName, regionX1, regionY1, regionZ1, regionX2, regionY2, regionZ2, new ArrayList<>(regionEnterCommands), new ArrayList<>(regionExitCommands), playersInside, regionEnabled, regionSound, regionSoundVolume, regionSoundFadeDuration);
    }
}
