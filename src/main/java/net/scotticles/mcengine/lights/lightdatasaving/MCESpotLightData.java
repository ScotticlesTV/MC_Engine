/**
 * Author: Scotticles
 * Description: A wrapper class for Veil's SpotLightData class to allow for
 * adding name and UUID properties within the custom MC Engine Light Editor.
 */
package net.scotticles.mcengine.lights.lightdatasaving;

import foundry.veil.api.client.render.light.data.SpotLightData;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;

import java.util.UUID;

public class MCESpotLightData extends SpotLightData {

    // ---------- Packet Encoding & Decoding ----------

    // Packet Code
    public static final PacketCodec<RegistryByteBuf, MCESpotLightData> PACKET_CODEC = PacketCodec.of(
            // Packet Encoder: Value comes first, then the buffer (value, buf)
            (MCESpotLightData value, RegistryByteBuf buf) -> {
                buf.writeString(value.name);
                buf.writeUuid(value.uuid);
                buf.writeFloat(value.brightness);
                buf.writeFloat(value.color.red());
                buf.writeFloat(value.color.blue());
                buf.writeFloat(value.color.green());
                buf.writeFloat(value.size);
                buf.writeDouble(value.position.x);
                buf.writeDouble(value.position.y);
                buf.writeDouble(value.position.z);
                buf.writeQuaternionf(value.orientation);
                buf.writeFloat(value.angle);
                buf.writeFloat(value.distance);
                buf.writeBoolean(value.occlusionEnabled);
                buf.writeFloat(value.inscattering);
            },
            // Packet Decoder
            (RegistryByteBuf buf) -> {
                String name = buf.readString();
                UUID uuid = buf.readUuid();
                float brightness = buf.readFloat();
                float r = buf.readFloat();
                float g = buf.readFloat();
                float b = buf.readFloat();
                float size = buf.readFloat();
                double posX = buf.readDouble();
                double posY = buf.readDouble();
                double posZ = buf.readDouble();
                Quaternionf orientation = buf.readQuaternionf();
                float angle = buf.readFloat();
                float distance = buf.readFloat();
                boolean occlusionEnabled = buf.readBoolean();
                float inscattering = buf.readFloat();

                return new MCESpotLightData(name, uuid, brightness, r, g, b, size, posX, posY, posZ, orientation, angle, distance, occlusionEnabled, inscattering);
            }
    );

    // ---------- Instance Properties ----------

    private String name;
    private UUID uuid;

    // ---------- Constructors ----------

    /**
     * Workhorse Constructor: Construct a new MCE Spot Light Data object with all parameters specified.
     * @param name, the name of the MCE Spot Light Data object.
     * @param uuid
     * @param brightness
     * @param r
     * @param g
     * @param b
     * @param size
     * @param posX
     * @param posY
     * @param posZ
     * @param orientation
     * @param angle
     * @param distance
     * @param occlusionEnabled
     * @param inScattering
     */
    public MCESpotLightData(String name, UUID uuid, float brightness, float r, float g, float b, float size, double posX, double posY, double posZ, Quaternionfc orientation, float angle, float distance, boolean occlusionEnabled, float inScattering) {
        this.setName(name);
        this.setUuid(uuid);
        this.setBrightness(brightness);
        this.setColor(r, g, b);
        this.setSize(size);
        this.position.set(posX, posY, posZ);
        this.orientation.set(orientation);
        this.setAngle(angle);
        this.setDistance(distance);
        this.setOcclusionEnabled(occlusionEnabled);
        this.setInscatteringStrength(inScattering);
    }

    // ---------- Getters & Setters ----------

    /**
     * Retrieve the name parameter of the MCE Spot Light Data object.
     * @return, the object's name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Assign the name parameter of the MCE Spot Light Data object.
     * @param name, the name to set the object's name parameter to.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieve the UUID parameter of the MCE Spot Light Data object.
     * @return, the object's UUID.
     */
    public UUID getUuid() {
        return this.uuid;
    }

    /**
     * Assign the UUID parameter of the MCE Spot Light Data object.
     * @param uuid, the uuid to set the object's uuid parameter to.
     */
    private void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setPosition(double x, double y, double z) {
        this.position.set(x, y, z);
    }

    public void setOrientation(float x, float y, float z, float w) {
        this.orientation.set(x, y, z, w);
    }
}
