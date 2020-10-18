package com.smarthome.simulator.models.SimulationTestClasses;

import com.smarthome.simulator.models.Simulation;
import org.junit.*;
import org.junit.Assert;
import org.junit.*;
import org.assertj.core.api.*;

/**
 * Start/Stop simulation
 */
public class StartStopSimulationTest {

    Simulation simulation = new Simulation ();

    /**
     * 5. Start/stop Simulation
     * Test if the starting state of the simulation is false
     */
    @Test
    public void startStateOfRunningShouldBeFalse () {
        Assert.assertFalse(simulation.isRunning());
    }

    /**
     * 5. Start/stop Simulation
     * Test if the simulation is running if we set it to true
     */
    @Test
    public void runningSimulationShouldBeTrue () {
        simulation.setRunning(true);
        Assert.assertTrue(simulation.isRunning());
    }
    /**
     * 5. Start/stop Simulation
     * Test if the simulation is running if we set to to false
     */
    @Test
    public void runningSimulationShouldBeFalse () {
        simulation.setRunning(false);
        Assert.assertFalse(simulation.isRunning());
    }
}
