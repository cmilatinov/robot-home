package com.smarthome.simulator.models.SimulationTestClasses;

import com.smarthome.simulator.models.Simulation;
import org.junit.Assert;
import org.junit.Test;


/**
 * 9. Modify the temperature outside the home
 */
public class ModifyTemperatureTest {
    /**
     * test simulation object for the test
     */
    private Simulation testSimulation = new Simulation();

    /**
     * Testing if the temperature outside gets changed successfully when given an valid temperature.
     */
    @Test
    public void changeTemperatureOutsideWithValidTemperatureReturnTrue() {
        Assert.assertTrue(testSimulation.setTemperatureOutside(20));
    }

    /**
     * Testing if the temperature outside doesn't change when given an invalid temperature.
     */
    @Test
    public void changeTemperatureOutsideWithInvalidTemperatureReturnFalse() {
        Assert.assertFalse(testSimulation.setTemperatureOutside(-100));
    }

    /**
     * Testing if the temperature inside gets changed successfully when given an valid temperature.
     */
    @Test
    public void changeTemperatureInsideWithValidTemperatureReturnTrue() {
        Assert.assertTrue(testSimulation.setTemperatureInside(20));
    }

    /**
     * Testing if the temperature inside doesn't change when given an invalid temperature.
     */
    @Test
    public void changeTemperatureInsideWithInvalidTemperatureReturnFalse() {
        Assert.assertFalse(testSimulation.setTemperatureInside(-100));
    }
}
