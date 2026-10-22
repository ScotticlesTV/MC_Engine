/**
 * Author: Scotticles
 * Description: A wrapper class for Veil's AreaLightData class to allow for
 * adding name and UUID properties within the custom MC Engine Light Editor.
 */
package net.scotticles.mcengine.lights.lightdatasaving;

import foundry.veil.api.client.render.light.data.AreaLightData;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;

import java.util.*;

public class MCEAreaLightData extends AreaLightData {

    // ---------- Instance Properties ----------

    private String name;
    private UUID uuid;

    // ---------- Packet Encoding & Decoding ----------

    // Packet Code
    public static final PacketCodec<RegistryByteBuf, MCEAreaLightData> PACKET_CODEC = PacketCodec.of(
            // Packet Encoder: Value comes first, then the buffer (value, buf)
            (MCEAreaLightData value, RegistryByteBuf buf) -> {
                buf.writeString(value.name);
                buf.writeUuid(value.uuid);
                buf.writeFloat(value.brightness);
                buf.writeFloat(value.color.red());
                buf.writeFloat(value.color.green());
                buf.writeFloat(value.color.blue());
                buf.writeFloat(value.size.x);
                buf.writeFloat(value.size.y);
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
                float sizeX = buf.readFloat();
                float sizeY = buf.readFloat();
                double posX = buf.readDouble();
                double posY = buf.readDouble();
                double posZ = buf.readDouble();
                Quaternionf orientation = buf.readQuaternionf();
                float angle = buf.readFloat();
                float distance = buf.readFloat();
                boolean occlusionEnabled = buf.readBoolean();
                float inscattering = buf.readFloat();

                return new MCEAreaLightData(name, uuid, brightness, r, g, b, sizeX, sizeY, posX, posY, posZ, orientation, angle, distance, occlusionEnabled, inscattering);
            }
    );

    // ---------- Constructors ----------

    /**
     * Workhorse Constructor: Construct a new MCE Area Light Data object with all parameters specified.
     * @param name the name of the MCE Area Light Data.
     * @param uuid the unique ID of the MCE Area Light Data.
     * @param brightness the brightness of the MCE Area Light Data.
     * @param r the red-value of the MCE Area Light Data's color property.
     * @param g the green-value of the MCE Area Light Data's color property.
     * @param b the blue-value of the MCE Area Light Data's color property.
     * @param sizeX the MCE Area Light Data's x or width value.
     * @param sizeY the MCE Area Light Data's y or height value.
     * @param posX the MCE Area Light Data's position's x-value.
     * @param posY the MCE Area Light Data's position's y-value.
     * @param posZ the MCE Area Light Data's position's z-value.
     * @param orientation the orientation/ emission direction of the MCE Area Light Data.
     * @param angle the angle the MCE Area Light Data's light should emit from its emitting plane.
     * @param distance the distance the MCE Area Light Data should travel.
     * @param occlusionEnabled whether or not occlusion (shadows) is enabled on the MCE Area Light Data.
     * @param inScattering the intensity of the MCE Area Light Data's in-scattering.
     */
    public MCEAreaLightData(String name, UUID uuid, float brightness, float r, float g, float b, float sizeX, float sizeY, double posX, double posY, double posZ, Quaternionfc orientation, float angle, float distance, boolean occlusionEnabled, float inScattering) {
        this.setName(name);
        this.setUuid(uuid);
        this.setBrightness(brightness);
        this.setColor(r, g, b);
        this.setSize(sizeX, sizeY);
        this.getPosition().set(posX, posY, posZ);
        this.getOrientation().set(orientation);
        this.setAngle(angle);
        this.setDistance(distance);
        this.setOcclusionEnabled(occlusionEnabled);
        this.setInscatteringStrength(inScattering);
    }

    // ---------- Getters & Setters ----------

    /**
     * Retrieve the name parameter of the MCE Area Light Data object.
     * @return the object's name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Assign the name parameter of the MCE Area Light Data object.
     * @param name the name to set the object's name parameter to.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieve the UUID parameter of the MCE Area Light Data object.
     * @return the object's UUID.
     */
    public UUID getUuid() {
        return this.uuid;
    }

    /**
     * Assign the UUID parameter of the MCE Area Light Data object.
     * @param uuid the uuid to set the object's uuid parameter to.
     */
    private void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
