package com.smarthome.simulator.models;

import com.smarthome.simulator.modules.*;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class SHPTest {


    Simulation testSimulation = new Simulation();
    SHP testSHP = (SHP) testSimulation.getModules().get(1);


    @Test
    public void guestsShouldNotSetAwayMode() {
        Map payload = new HashMap();
        boolean awayStateBeforeTest = testSimulation.isAway();
        payload.put("value", !awayStateBeforeTest);
        testSimulation.setActiveUserProfile(testSimulation.getUserProfiles().get(2));
        testSimulation.executeCommand(SHP.SET_AWAY_MODE, payload, true);
        Assert.assertTrue(testSimulation.isAway() == awayStateBeforeTest);
    }
}
