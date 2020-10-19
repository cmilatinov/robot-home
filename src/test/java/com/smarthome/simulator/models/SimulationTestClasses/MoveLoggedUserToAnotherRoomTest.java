package com.smarthome.simulator.models.SimulationTestClasses;

import com.smarthome.simulator.models.HouseLayout;
import com.smarthome.simulator.models.Simulation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * 8. Move the logged user to different room
 */
public class MoveLoggedUserToAnotherRoomTest {

    /**
     * test simulation object for the test
     */
    private Simulation testSimulation = new Simulation();

    /**
     * house layout object for the test
     */
    private HouseLayout testHouseLayout;

    /**
     * This is just to set up the required environment for the following tests to run in it
     */
    @Before
    public void settingUp() {
        //has 2 rooms 'Kitchen' and 'Bathroom'
        File selectedFile = new File("src/test/resources/working-sample-with-2-rooms-house-layout.json");
        testHouseLayout = new HouseLayout("testHouseLayout", HouseLayout.parseJSONFile(selectedFile));
        testSimulation.setHouseLayout(testHouseLayout);
        testSimulation.setUserLocation("Kitchen");
    }

    /**
     * Testing if the active user's location gets changed successfully when given an existing room name
     */
    @Test
    public void moveActiveUserToAnotherRoomShouldReturnTrue() {
        Assert.assertTrue(testSimulation.setUserLocation("Bathroom"));
    }

    /**
     * Testing if the active user's location returns false when given a none existing room name
     */
    @Test
    public void moveActiveUserToNoneExistingRoomShouldReturnFalse() {
        Assert.assertFalse(testSimulation.setUserLocation("Bathroom2323423424"));
    }

}
