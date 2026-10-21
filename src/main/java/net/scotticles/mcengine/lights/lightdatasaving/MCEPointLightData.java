/**
 * Author: Scotticles
 * Description: A wrapper class for Veil's PointLightData class to allow for
 * adding name and UUID properties within the custom MC Engine Light Editor.
 */
package net.scotticles.mcengine.lights.lightdatasaving;

import foundry.veil.api.client.render.light.data.AreaLightData;
import foundry.veil.api.client.render.light.data.PointLightData;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;

import java.util.UUID;

public class MCEPointLightData extends PointLightData {
    // ---------- Instance Properties ----------

    private String name;
    private UUID uuid;

    // ---------- Packet Encoding & Decoding ----------

    // Packet Code
    public static final PacketCodec<RegistryByteBuf, MCEPointLightData> PACKET_CODEC = PacketCodec.of(
            // Packet Encoder: Value comes first, then the buffer (value, buf)
            (MCEPointLightData value, RegistryByteBuf buf) -> {
                buf.writeString(value.name);
                buf.writeUuid(value.uuid);
                buf.writeFloat(value.brightness);
                buf.writeFloat(value.color.red());
                buf.writeFloat(value.color.blue());
                buf.writeFloat(value.color.green());
                buf.writeDouble(value.position.x);
                buf.writeDouble(value.position.y);
                buf.writeDouble(value.position.z);
                buf.writeFloat(value.radius);
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
                double posX = buf.readDouble();
                double posY = buf.readDouble();
                double posZ = buf.readDouble();
                float radius = buf.readFloat();
                boolean occlusionEnabled = buf.readBoolean();
                float inscattering = buf.readFloat();

                return new MCEPointLightData(name, uuid, brightness, r, g, b, posX, posY, posZ, radius, occlusionEnabled, inscattering);
            }
    );

    // ---------- Constructors ----------

    /**
     * Workhorse Constructor: Construct a new MCE point Light Data object with all parameters specified.
     * @param name
     * @param uuid
     * @param brightness
     * @param r
     * @param g
     * @param b
     * @param posX
     * @param posY
     * @param posZ
     * @param radius
     * @param occlusionEnabled
     * @param inScattering
     */
    public MCEPointLightData(String name, UUID uuid, float brightness, float r, float g, float b, double posX, double posY, double posZ, float radius, boolean occlusionEnabled, float inScattering) {
        this.setName(name);
        this.setUuid(uuid);
        this.setBrightness(brightness);
        this.setColor(r, g, b);
        this.setPosition(posX, posY, posZ);
        this.setRadius(radius);
        this.setOcclusionEnabled(occlusionEnabled);
        this.setInscatteringStrength(inScattering);
    }

    // ---------- Getters & Setters ----------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
    }

    private void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
