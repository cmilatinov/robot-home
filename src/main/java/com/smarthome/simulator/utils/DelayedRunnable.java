package com.smarthome.simulator.utils;

/**
 * Interface class for delaying tasks
 */
public interface DelayedRunnable extends Runnable {

    /**
     * Get the delay time
     * @return the value of the delay
     */
    public long getDelay();

    /**
     * Set the delay time
     * @param delay
     */
    public void setDelay(long delay);

    /**
     * Returns true if the task is periodic
     * @return
     */
    public boolean isPeriodic();

    /**
     * Returns the value of the period that the task recurs over
     * @return
     */
    public long getPeriod();

}
