/**
 * Author: Scotticles
 * Description: A class defining a rectangular area that the player can interact with by entering and exiting.
 */
package net.scotticles.mcengine.regions.regiondatasaving;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Uuids;

import java.util.*;

public class LinearRegionData {

    // ---------- Instance Properties ----------

    private UUID uuid;
    private String name;
    private int pos1X;
    private int pos1Y;
    private int pos1Z;
    private int pos2X;
    private int pos2Y;
    private int pos2Z;
    private Set<UUID> playersInside;
    private boolean enabled;
    private List<String> enterCommands;
    private List<String> exitCommands;
    private String sound;
    private float soundVolume;
    private float soundFadeDuration;

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
                buf.writeUuid(value.uuid);
                buf.writeString(value.name);
                buf.writeInt(value.pos1X);
                buf.writeInt(value.pos1Y);
                buf.writeInt(value.pos1Z);
                buf.writeInt(value.pos2X);
                buf.writeInt(value.pos2Y);
                buf.writeInt(value.pos2Z);
                STRING_LIST_CODEC.encode(buf, value.enterCommands);
                STRING_LIST_CODEC.encode(buf, value.exitCommands);
                UUID_SET_CODEC.encode(buf, value.playersInside);
                buf.writeBoolean(value.enabled);
                buf.writeString(value.sound);
                buf.writeFloat(value.soundVolume);
                buf.writeFloat(value.soundFadeDuration);
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
     * @param uuid, the unique ID of the Linear Region.
     * @param name, the name of the Linear Region.
     * @param pos1X, the Linear Regions 1st position's x-value.
     * @param pos1Y, the Linear Regions 1st position's y-value.
     * @param pos1Z, the Linear Regions 1st position's z-value.
     * @param pos2X, the Linear Regions 2nd position's x-value.
     * @param pos2Y, the Linear Regions 2nd position's y-value.
     * @param pos2Z, the Linear Regions 2nd position's z-value.
     * @param enterCommands, the enter commands of the Linear Region.
     * @param exitCommands, the exit commands of the Linear Region.
     * @param playersInside, the player's currently inside the Linear Region's bounds.
     * @param enabled, whether or not the Linear Region is currently enabled.
     * @param sound, the sound to play when a player enters the Linear Region.
     * @param soundVolume, the volume to play the Linear Region's sound at.
     * @param soundFadeDuration, the time it should take for the Linear Region's sound to fade when a player exits the region.
     */
    public LinearRegionData(UUID uuid, String name, int pos1X, int pos1Y, int pos1Z, int pos2X, int pos2Y, int pos2Z, List<String> enterCommands, List<String> exitCommands, Set<UUID> playersInside, boolean enabled, String sound, float soundVolume, float soundFadeDuration) {
        this.setUuid(uuid);
        this.setName(name);
        this.setPos1(pos1X, pos1Y, pos1Z);
        this.setPos2(pos2X, pos2Y, pos2Z);
        this.setEnterCommands(enterCommands);
        this.setExitCommands(exitCommands);
        this.setPlayersInside(playersInside);
        this.setEnabled(enabled);
        this.setSound(sound);
        this.setSoundVolume(soundVolume);
        this.setSoundFadeDuration(soundFadeDuration);
    }

    // ---------- Getters & Setters ----------

    /**
     * Retrieve the Linear Region Data's uuid property.
     * @return the Linear Region Data's uuid.
     */
    public UUID getUuid() {
        return this.uuid;
    }

    /**
     * Assign the Linear Region Data's uuid property.
     * @param uuid, the uuid to set the Linear Region Data's uuid property to.
     */
    private void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Retrieve the Linear Region Data's name property.
     * @return the Linear Region Data's name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Assign the Linear Region Data's name property.
     * @param name, the Linear Region Data's name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieve the Linear Region Data's first position's x-value.
     * @return the Linear Region Data's first position's x-value.
     */
    public int getPos1X() {
        return this.pos1X;
    }

    /**
     * Assign the Linear Region Data's first position's x-value.
     * @param pos1X, the x-value to set the Linear Region Data's first position's x-value to.
     */
    public void setPos1X(int pos1X) {
        this.pos1X = pos1X;
    }

    /**
     * Retrieve the Linear Region Data's first position's y-value.
     * @return the Linear Region Data's first position's y-value.
     */
    public int getPos1Y() {
        return this.pos1Y;
    }

    /**
     * Assign the Linear Region Data's first position's y-value.
     * @param pos1Y, the y-value to set the Linear Region Data's first position's y-value to.
     */
    public void setPos1Y(int pos1Y) {
        this.pos1Y = pos1Y;
    }

    /**
     * Retrieve the Linear Region Data's first position's z-value.
     * @return the Linear Region Data's first position's z-value.
     */
    public int getPos1Z() {
        return this.pos1Z;
    }

    /**
     * Assign the Linear Region Data's first position's z-value.
     * @param pos1Z, the z-value to set the Linear Region Data's first position's z-value to.
     */
    public void setPos1Z(int pos1Z) {
        this.pos1Z = pos1Z;
    }

    /**
     * Retrieve the Linear Region Data's second position's z-value.
     * @return the Linear Region Data's second position's z-value.
     */
    public int getPos2X() {
        return this.pos2X;
    }

    /**
     * Assign the Linear Region Data's second position's x-value.
     * @param pos2X, the x-value to set the Linear Region Data's second position's x-value to.
     */
    public void setPos2X(int pos2X) {
        this.pos2X = pos2X;
    }

    /**
     * Retrieve the Linear Region Data's second position's y-value.
     * @return the Linear Region Data's second position's y-value.
     */
    public int getPos2Y() {
        return this.pos2Y;
    }

    /**
     * Assign the Linear Region Data's second position's y-value.
     * @param pos2Y, the y-value to set the Linear Region Data's second position's y-value to.
     */
    public void setPos2Y(int pos2Y) {
        this.pos2Y = pos2Y;
    }

    /**
     * Retrieve the Linear Region Data's second position's z-value.
     * @return the Linear Region Data's second position's z-value.
     */
    public int getPos2Z() {
        return this.pos2Z;
    }

    /**
     * Assign the Linear Region Data's second position's z-value.
     * @param pos2Z, the z-value to set the Linear Region Data's second position's z-value to.
     */
    public void setPos2Z(int pos2Z) {
        this.pos2Z = pos2Z;
    }

    /**
     * Assign the full first position of the Linear Region Data.
     * @param pos1X, the x-value to assign the Linear Region Data's first position to.
     * @param pos1Y, the y-value to assign the Linear Region Data's first position to.
     * @param pos1Z, the z-value to assign the Linear Region Data's first position to.
     */
    public void setPos1(int pos1X, int pos1Y, int pos1Z) {
        this.pos1X = pos1X;
        this.pos1Y = pos1Y;
        this.pos1Z = pos1Z;
    }

    /**
     * Assign the full second position of the Linear Region Data.
     * @param pos2X, the x-value to assign the Linear Region Data's second position to.
     * @param pos2Y, the y-value to assign the Linear Region Data's second position to.
     * @param pos2Z, the z-value to assign the Linear Region Data's second position to.
     */
    public void setPos2(int pos2X, int pos2Y, int pos2Z) {
        this.pos2X = pos2X;
        this.pos2Y = pos2Y;
        this.pos2Z = pos2Z;
    }

    /**
     * Retrieve the playersInside property of the Linear Region Data.
     * @return the set of players currently inside the Linear Region Data's bounds.
     */
    public Set<UUID> getPlayersInside() {
        return this.playersInside;
    }

    /**
     * Assign the playersInside property of the Linear Region Data.
     * @param playersInside, the set of players currently inside the Linear Region Data's bounds to set the playersInside property to.
     */
    public void setPlayersInside(Set<UUID> playersInside) {
        this.playersInside = playersInside;
    }

    /**
     * Retriever whether or not the Linear Region Data is currently enabled.
     * @return the Linear Region Data's enabled status.
     */
    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * Assign whether or not the Linear Region Data is currently enabled.
     * @param enabled, the enabled value to set the Linear Region Data's enabled property to.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Retrieve the Linear Region Data's enter commands.
     * @return the list of enter commands for the Linear Region Data.
     */
    public List<String> getEnterCommands() {
        return this.enterCommands;
    }

    /**
     * Assign the enter commands list property of the Linear Region Data.
     * @param enterCommands, the list of enter commands to set the Linear Region Data's enterCommands property to.
     */
    public void setEnterCommands(List<String> enterCommands) {
        this.enterCommands = enterCommands;
    }

    /**
     * Retrieve the Linear Region Data's enter commands.
     * @return the list of enter commands for the Linear Region Data.
     */
    public List<String> getExitCommands() {
        return this.exitCommands;
    }

    /**
     * Assign the exit commands list property of the Linear Region Data.
     * @param exitCommands, the list of exit commands to set the Linear Region Data's exitCommands property to.
     */
    public void setExitCommands(List<String> exitCommands) {
        this.exitCommands = exitCommands;
    }

    /**
     * Retrieve the sound property of the Linear Region Data.
     * @return the sound property of the Linear Region Data.
     */
    public String getSound() {
        return this.sound;
    }

    /**
     * Assign the sound property of the Linear Region Data.
     * @param sound, the sound to set the sound property of the Linear Region Data to.
     */
    public void setSound(String sound) {
        this.sound = sound;
    }

    /**
     * Retrieve the sound volume property of the Linear Region Data.
     * @return the sound volume of the Linear Region Data.
     */
    public float getSoundVolume() {
        return this.soundVolume;
    }

    /**
     * Assign the sound volume property of the Linear Region Data.
     * @param soundVolume, the sound volume to set the Linear Region Data's sound property to.
     */
    public void setSoundVolume(float soundVolume) {
        this.soundVolume = soundVolume;
    }

    /**
     * Get the sound fade duration property of the Linear Region Data.
     * @return the Linear Region Data's sound fade duration property.
     */
    public float getSoundFadeDuration() {
        return this.soundFadeDuration;
    }

    /**
     * Assign the sound fade duration property of the Linear Region Data.
     * @param soundFadeDuration, the sound fade duration value to set the Linear Region Data's sound fade duration property to.
     */
    public void setSoundFadeDuration(float soundFadeDuration) {
        this.soundFadeDuration = soundFadeDuration;
    }
}
