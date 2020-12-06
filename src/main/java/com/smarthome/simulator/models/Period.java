package com.smarthome.simulator.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Represents a period of time in a day with its desired temperature.
 */
public class Period {

    /**
     * Time formatter used to convert strings into {@link LocalTime} instances.
     */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Start time of the period.
     */
    private final LocalTime startTime;

    /**
     * Start time of the period.
     */
    private final LocalTime endTime;

    /**
     * Desired temperature for the period.
     */
    private final float desiredTemperature;

    /**
     * Creates a new period with the given values.
     *
     * @param startTime          The period's start time in 'HH:mm' format.
     * @param endTime            The period's end time in 'HH:mm' format.
     * @param desiredTemperature The period's desired temperature.
     */
    public Period(String startTime, String endTime, float desiredTemperature) {
        this.startTime = LocalTime.parse(startTime, formatter);
        this.endTime = LocalTime.parse(endTime, formatter);
        this.desiredTemperature = desiredTemperature;
    }

    /**
     * Returns this period's start time.
     *
     * @return {@link LocalTime} The start of this period.
     */
    @JsonIgnore
    public LocalTime getStartTimeObj() {
        return startTime;
    }

    /**
     * Returns this period's end time.
     *
     * @return {@link LocalTime} The end of this period.
     */
    @JsonIgnore
    public LocalTime getEndTimeObj() {
        return endTime;
    }

    /**
     * Returns this period's start time.
     *
     * @return {@link LocalTime} The start of this period.
     */
    public String getStartTime() {
        return formatter.format(startTime);
    }

    /**
     * Returns this period's end time.
     *
     * @return {@link LocalTime} The end of this period.
     */
    public String getEndTime() {
        return formatter.format(endTime);
    }

    /**
     * Returns this period's duration.
     *
     * @param timeUnit The unit of time in which to return the duration. (ex: {@link ChronoUnit#HOURS}, {@link ChronoUnit#MINUTES})
     * @return {@link LocalTime} The duration of this period based on its start and end times.
     */
    @SuppressWarnings("unused")
    public long getDuration(ChronoUnit timeUnit) {
        return startTime.until(endTime, timeUnit);
    }

    /**
     * Returns the desired temperature for this time period.
     *
     * @return float The desired temperature for this time period.
     */
    public float getDesiredTemperature() {
        return desiredTemperature;
    }

}
