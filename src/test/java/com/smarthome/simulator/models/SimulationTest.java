package com.smarthome.simulator.models;

import com.smarthome.simulator.models.SimulationTestClasses.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        SetUserProfileTest.class,
        StartStopSimulationTest.class,
        SetDateAndTimeTest.class,
        MoveLoggedUserToAnotherRoomTest.class,
        ModifyTemperatureTest.class
})

public class SimulationTest {

}
