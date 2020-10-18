package com.smarthome.simulator.models;

import com.smarthome.simulator.models.SimulationTestClasses.SetUserProfileTest;
import com.smarthome.simulator.models.SimulationTestClasses.StartStopSimulationTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        SetUserProfileTest.class,
        StartStopSimulationTest.class
})

public class SimulationTest {
}
