package net.scotticles.mcengine.regions.regiondatasaving;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Uuids;

import java.util.*;

public class LinearRegionData {

    // ---------- Instance Properties ----------

    private UUID regionUuid;
    private String regionName;
    private int regionX1;
    private int regionY1;
    private int regionZ1;
    private int regionX2;
    private int regionY2;
    private int regionZ2;
    private Set<UUID> playersInside;
    private boolean regionEnabled;
    private List<String> regionEnterCommands;
    private List<String> regionExitCommands;
    private String regionSound;
    private float regionSoundVolume;
    private float regionSoundFadeDuration;

    // ---------- Packet Encoding & Decoding ----------

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

    // ---------- Constructors ----------

    /**
     * Workhorse Constructor: Construct a new Linear Region Data object with all parameters specified.
     * @param regionUuid
     * @param regionName
     * @param regionX1
     * @param regionY1
     * @param regionZ1
     * @param regionX2
     * @param regionY2
     * @param regionZ2
     * @param regionEnterCommands
     * @param regionExitCommands
     * @param playersInside
     * @param regionEnabled
     * @param regionSound
     * @param regionSoundVolume
     * @param regionSoundFadeDuration
     */
    public LinearRegionData(UUID regionUuid, String regionName, int regionX1, int regionY1, int regionZ1, int regionX2, int regionY2, int regionZ2, List<String> regionEnterCommands, List<String> regionExitCommands, Set<UUID> playersInside, boolean regionEnabled, String regionSound, float regionSoundVolume, float regionSoundFadeDuration) {
        this.setRegionUuid(regionUuid);
        this.setRegionName(regionName);
        this.setRegionPos1(regionX1, regionY1, regionZ1);
        this.setRegionPos2(regionX2, regionY2, regionZ2);
        this.setRegionEnterCommands(regionEnterCommands);
        this.setRegionExitCommands(regionExitCommands);
        this.setPlayersInside(playersInside);
        this.setRegionEnabled(regionEnabled);
        this.setRegionSound(regionSound);
        this.setRegionSoundVolume(regionSoundVolume);
        this.setRegionSoundFadeDuration(regionSoundFadeDuration);
    }

    // ---------- Getters & Setters ----------

    public UUID getRegionUuid() {
        return this.regionUuid;
    }

    private void setRegionUuid(UUID regionUuid) {
        this.regionUuid = regionUuid;
    }

    public String getRegionName() {
        return this.regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public int getRegionX1() {
        return this.regionX1;
    }

    public void setRegionX1(int regionX1) {
        this.regionX1 = regionX1;
    }

    public int getRegionY1() {
        return this.regionY1;
    }

    public void setRegionY1(int regionY1) {
        this.regionY1 = regionY1;
    }

    public int getRegionZ1() {
        return this.regionZ1;
    }

    public void setRegionZ1(int regionZ1) {
        this.regionZ1 = regionZ1;
    }

    public int getRegionX2() {
        return this.regionX2;
    }

    public void setRegionX2(int regionX2) {
        this.regionX1 = regionX2;
    }

    public int getRegionY2() {
        return this.regionY2;
    }

    public void setRegionY2(int regionY2) {
        this.regionY1 = regionY2;
    }

    public int getRegionZ2() {
        return this.regionZ1;
    }

    public void setRegionZ2(int regionZ2) {
        this.regionZ1 = regionZ2;
    }

    public void setRegionPos1(int regionX1, int regionY1, int regionZ1) {
        this.regionX1 = regionX1;
        this.regionY1 = regionY1;
        this.regionZ1 = regionZ1;
    }

    public void setRegionPos2(int regionX2, int regionY2, int regionZ2) {
        this.regionX2 = regionX2;
        this.regionY2 = regionY2;
        this.regionZ2 = regionZ2;
    }

    public Set<UUID> getPlayersInside() {
        return this.playersInside;
    }

    public void setPlayersInside(Set<UUID> playersInside) {
        this.playersInside = playersInside;
    }

    public boolean isRegionEnabled() {
        return this.regionEnabled;
    }

    public void setRegionEnabled(boolean regionEnabled) {
        this.regionEnabled = regionEnabled;
    }

    public List<String> getRegionEnterCommands() {
        return this.regionEnterCommands;
    }

    public void setRegionEnterCommands(List<String> regionEnterCommands) {
        this.regionEnterCommands = regionEnterCommands;
    }

    public List<String> getRegionExitCommands() {
        return this.regionExitCommands;
    }

    public void setRegionExitCommands(List<String> regionExitCommands) {
        this.regionExitCommands = regionExitCommands;
    }

    public String getRegionSound() {
        return this.regionSound;
    }

    public void setRegionSound(String regionSound) {
        this.regionSound = regionSound;
    }

    public float getRegionSoundVolume() {
        return this.regionSoundVolume;
    }

    public void setRegionSoundVolume(float regionSoundVolume) {
        this.regionSoundVolume = regionSoundVolume;
    }

    public float getRegionSoundFadeDuration() {
        return this.regionSoundFadeDuration;
    }

    public void setRegionSoundFadeDuration(float regionSoundFadeDuration) {
        this.regionSoundFadeDuration = regionSoundFadeDuration;
    }


    public LinearRegionData toRegionsData() {
        return new LinearRegionData(regionUuid, regionName, regionX1, regionY1, regionZ1, regionX2, regionY2, regionZ2, new ArrayList<>(regionEnterCommands), new ArrayList<>(regionExitCommands), playersInside, regionEnabled, regionSound, regionSoundVolume, regionSoundFadeDuration);
    }
}
