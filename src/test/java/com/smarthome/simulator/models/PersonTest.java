package com.smarthome.simulator.models;

import org.junit.Assert;
import org.junit.*;
import org.assertj.core.api.*;
import java.util.ArrayList;

public class PersonTest {

    Person testPerson = new Person("testPerson", "testRoom");

    /**
     * Use case #6
     * Tests that setRoomId() successfully changes the value of roomId.
     */
    @Test
    public void setRoomIdTest() {
        testPerson.setRoomId("newRoom");
        Assert.assertEquals("newRoom", testPerson.getRoomId());
    }

    /**
     * Use case #6
     * Tests that getRoomId() successfully returns the value of roomId.
     */
    @Test
    public void getRoomIdTest() {
        Assert.assertEquals("testRoom", testPerson.getRoomId());
    }

}
