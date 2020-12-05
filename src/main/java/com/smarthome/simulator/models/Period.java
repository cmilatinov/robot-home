package com.smarthome.simulator.models;

public class Period {
    private int startTime;
    private int endTime;
    private int duration;
    private float desiredTemperature;

    public Period(int startTime, int endTime, float desiredTemperature) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.desiredTemperature = desiredTemperature;
        this.duration = endTime - startTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public float getDesiredTemperature() {
        return desiredTemperature;
    }

    public void setDesiredTemperature(float desiredTemperature) {
        this.desiredTemperature = desiredTemperature;
    }
}
