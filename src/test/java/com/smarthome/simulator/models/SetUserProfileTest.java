package com.smarthome.simulator.models;

import org.junit.Assert;
import org.junit.*;
import org.assertj.core.api.*;
import java.util.ArrayList;


public class SetUserProfileTest {

    private Simulation testSimulation = new Simulation ();

    @Test
    public void TestDefaultProfile() {
        Assertions.assertThat(testSimulation.getUserProfiles().get(0))
                .asString()
                .isEqualTo(testSimulation.getActiveUserProfile().toString());
    }

    /**
     * Testing if setActiveProfile method sets correct profile
     */
    @Test
    public void TestSetActiveProfile() {
        testSimulation.setActiveUserProfile(testSimulation.getUserProfiles().get(1));
        Assertions.assertThat(testSimulation.getUserProfiles().get(1))
                .isEqualTo(testSimulation.getActiveUserProfile());
    }

    /**
     * Testing if setActiveUserProfile method returns false when passed an invalid UserProfile
     */
    @Test
    public void InvalidProfileShouldReturnFalse() {

        UserProfile testProfile = null;
        try {
            testProfile = new UserProfile("testProfile");
        }catch(Exception e){}

        Assert.assertFalse(testSimulation.setActiveUserProfile(testProfile));
    }
}
