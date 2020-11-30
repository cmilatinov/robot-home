package com.smarthome.simulator.models;

import com.smarthome.simulator.modules.SHP;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class SHPTest {

    Simulation testSimulation = new Simulation();
    //Simulation object that contains modules and data they manipulate./


    @Test
    public void guestsShouldNotSetAwayMode() {
        Map payload = new HashMap();
        boolean awayStateBeforeTest = testSimulation.isAway();
        payload.put("value", !awayStateBeforeTest);
        testSimulation.setActiveUserProfile(testSimulation.getUserProfiles().get(2));
        testSimulation.executeCommand(SHP.SET_AWAY_MODE, payload, true);
        Assert.assertTrue(testSimulation.isAway()==awayStateBeforeTest);
    }
}
