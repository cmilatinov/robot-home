package com.smarthome.simulator.models;

import com.smarthome.simulator.models.SimulationTestClasses.SetDateAndTimeTest;
import com.smarthome.simulator.models.SimulationTestClasses.SetUserProfileTest;
import com.smarthome.simulator.models.SimulationTestClasses.StartStopSimulationTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        SetUserProfileTest.class,
        StartStopSimulationTest.class,
        SetDateAndTimeTest.class
})

public class SimulationTest {

}
