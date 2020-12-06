package com.smarthome.simulator.utils;

/**
 * Interface class for delaying tasks
 */
public interface DelayedRunnable extends Runnable {

    /**
     * Get the delay time
     *
     * @return The value of the delay
     */
    long getDelay();

    /**
     * Set the delay time
     *
     * @param delay The delay time for the runnable
     */
    void setDelay(long delay);

    /**
     * Returns true if the task is periodic
     *
     * @return Indicates if the task is periodic
     */
    boolean isPeriodic();

    /**
     * Returns the value of the period that the task recurs over
     *
     * @return The value of the period that the task recurs over
     */
    long getPeriod();

}
