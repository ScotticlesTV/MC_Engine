package net.scotticles.mcengine.lights.lightdatasaving;

import foundry.veil.api.client.render.light.data.AreaLightData;

import java.util.UUID;

public class MCEAreaLightData {
    // ---------- Instance Properties ----------

    private String name;
    private UUID uuid;
    private AreaLightData areaLightData;

    // ---------- Constructors ----------

    public MCEAreaLightData(String name, UUID uuid, AreaLightData areaLightData) {
        this.setName(name);
        this.setUuid(uuid);
        this.setAreaLightData(areaLightData);
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

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public AreaLightData getAreaLightData() {
        return areaLightData;
    }

    private void setAreaLightData(AreaLightData areaLightData) {
        this.areaLightData = areaLightData;
    }
}
