package com.smarthome.simulator.utils;

import com.smarthome.simulator.models.Simulation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class that handles the scheduling of tasks on simulation time
 */
public class TaskDispatcher extends Thread{

    /**
     * Reference to the simulation
     */
    private Simulation simulation;

    /**
     * The current time of the system
     */
    private long currentTime;

    /**
     * The time of the system when this thread last had control
     */
    private long previousTime;

    /**
     * The list of tasks
     */
    private List<DelayedRunnable> taskList;

    /**
     * Constructor that takes a reference to the simulation
     * @param _simulation
     */
    public TaskDispatcher(Simulation _simulation) {
        this.simulation = _simulation;
        this.currentTime = System.currentTimeMillis();
        this.previousTime = System.currentTimeMillis();
    }

    /**
     * The run method of the thread
     * Always looping to keep previous time up to date.
     * Inner loop for simulation time
     */
    public void run() {
        this.taskList = new ArrayList<DelayedRunnable>();
        this.currentTime = System.nanoTime();
        while (true) {
            this.previousTime = System.nanoTime();
            while (simulation.isRunning()) {
                System.out.println("currentTime: " + currentTime);
                this.currentTime = System.nanoTime();
                float simSpeed = simulation.getSimulationSpeed();
                long timeDelta = currentTime - previousTime;
                for (Iterator<DelayedRunnable> iterator = taskList.iterator(); iterator.hasNext();) {
                    DelayedRunnable dr = iterator.next();
                    dr.setDelay(dr.getDelay() - (long)simSpeed*timeDelta);
                    //System.out.println(dr);
                    //System.out.println(dr.getDelay());
                    if (dr.getDelay() <= 0) {
                        dr.run();
                        if (dr.isPeriodic()) {
                            dr.setDelay(dr.getPeriod());
                        } else {
                            iterator.remove();
                        }
                    }
                }
                this.previousTime = System.nanoTime();
                yield();
            }
            yield();
        }
    }

    /**
     * Schedules a task
     * @param task
     */
    public synchronized void schedule (DelayedRunnable task) {
        taskList.add(task);
    }

}
