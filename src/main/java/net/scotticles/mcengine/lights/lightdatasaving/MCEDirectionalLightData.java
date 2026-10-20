/**
 * Author: Scotticles
 * Description: A wrapper class for Veil's DirectionalLightData class to allow for
 * adding name and UUID properties within the custom MC Engine Light Editor.
 */
package net.scotticles.mcengine.lights.lightdatasaving;

import foundry.veil.api.client.render.light.data.DirectionalLightData;
import foundry.veil.api.client.render.light.data.PointLightData;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.UUID;

public class MCEDirectionalLightData {
    // ---------- Instance Properties ----------

    private String name;
    private UUID uuid;
    private DirectionalLightData directionalLightData;

    // ---------- Constructors ----------

    /**
     * Workhorse Constructor: Construct a new MCE Directional Light Data object with all parameters specified.
     * @param name, the name of the MCE Directional Light Data object.
     * @param uuid, the unique id of the MCE Directional Light Data object.
     * @param directionalLightData, the veil light of the MCE Directional Light Data.
     */
    public MCEDirectionalLightData(String name, UUID uuid, DirectionalLightData directionalLightData) {
        this.setName(name);
        this.setUuid(uuid);
        this.setDirectionalLightData(directionalLightData);
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

    public DirectionalLightData getDirectionalLightData() {
        return directionalLightData;
    }

    private void setDirectionalLightData(DirectionalLightData directionalLightData) {
        this.directionalLightData = directionalLightData;
    }
}
