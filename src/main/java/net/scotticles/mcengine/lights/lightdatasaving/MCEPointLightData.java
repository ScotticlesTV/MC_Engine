/**
 * Author: Scotticles
 * Description: A wrapper class for Veil's PointLightData class to allow for
 * adding name and UUID properties within the custom MC Engine Light Editor.
 */
package net.scotticles.mcengine.lights.lightdatasaving;

import foundry.veil.api.client.render.light.data.AreaLightData;
import foundry.veil.api.client.render.light.data.PointLightData;

import java.util.UUID;

public class MCEPointLightData {
    // ---------- Instance Properties ----------

    private String name;
    private UUID uuid;
    private PointLightData pointLightData;

    // ---------- Constructors ----------

    /**
     * Workhorse Constructor: Construct a new MCE point Light Data object with all parameters specified.
     * @param name, the name of the MCE Point Light Data object.
     * @param uuid, the unique id of the MCE Point Light Data object.
     * @param pointLightData, the veil light of the MCE Point Light Data.
     */
    public MCEPointLightData(String name, UUID uuid, PointLightData pointLightData) {
        this.setName(name);
        this.setUuid(uuid);
        this.setPointLightData(pointLightData);
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

    public PointLightData getPointLightData() {
        return pointLightData;
    }

    private void setPointLightData(PointLightData pointLightData) {
        this.pointLightData = pointLightData;
    }
}
