package com.feidian.sidebarTree.domain;

import com.feidian.common.core.domain.BaseEntity;

public class Data extends BaseEntity {

    private float ambientTemperature;
    private float ambientHumidity;
    private float CO2;
    private float pow;
    private Integer RSSI;
    private Integer lightIntensity;
    private String detectedTime;
    private String clientId;
    private String dewTemp;

    public Data() {
    }

    public String getDewTemp() {
        return dewTemp;
    }

    public void setDewTemp(String dewTemp) {
        this.dewTemp = dewTemp;
    }

    public Data(float ambientTemperature, float ambientHumidity, float CO2, float pow, Integer RSSI, Integer lightIntensity, String detectedTime, String clientId,String dewTemp) {
        this.ambientTemperature = ambientTemperature;
        this.ambientHumidity = ambientHumidity;
        this.CO2 = CO2;
        this.pow = pow;
        this.RSSI = RSSI;
        this.lightIntensity = lightIntensity;
        this.detectedTime = detectedTime;
        this.clientId = clientId;
        this.dewTemp = dewTemp;
    }



    public float getAmbientTemperature() {
        return ambientTemperature;
    }

    public void setAmbientTemperature(float ambientTemperature) {
        this.ambientTemperature = ambientTemperature;
    }

    public float getAmbientHumidity() {
        return ambientHumidity;
    }

    public void setAmbientHumidity(float ambientHumidity) {
        this.ambientHumidity = ambientHumidity;
    }

    public float getCO2() {
        return CO2;
    }

    public void setCO2(float CO2) {
        this.CO2 = CO2;
    }

    public float getPow() {
        return pow;
    }

    public void setPow(float pow) {
        this.pow = pow;
    }

    public Integer getLightIntensity() {
        return lightIntensity;
    }

    public void setLightIntensity(Integer lightIntensity) {
        this.lightIntensity = lightIntensity;
    }

    public String getDetectedTime() {
        return detectedTime;
    }

    public void setDetectedTime(String detectedTime) {
        this.detectedTime = detectedTime;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Integer getRSSI() {
        return RSSI;
    }

    public void setRSSI(Integer RSSI) {
        this.RSSI = RSSI;
    }
}

