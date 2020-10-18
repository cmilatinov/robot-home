package com.smarthome.simulator.models;

import org.junit.Assert;
import org.junit.*;

/**
 * Block windows movement by putting an arbitrary object
 */
public class WindowTest {

    Window window = new Window();

    /**
     * Testing if the getter method of blocked windows returns true if we set it to true
     */
    @Test
    public void blockedWindowsShouldBeTrue () {
        window.setBlocked(true);
        Assert.assertTrue(window.isBlocked());
    }

    /**
     * Testing if the getter method of blocked windows returns false if we set it to false
     */
    @Test
    public void blockedWindowsShouldBeFalse () {
        window.setBlocked(false);
        Assert.assertFalse( window.isBlocked());
    }

    /**
     * Testing if the getter method of opened windows returns true if we set it to true
     */
    @Test
    public void openedWindowShouldBeTrue () {
        window.setOpen(true);
        Assert.assertTrue(window.isOpen());
    }

    /**
     * Testing if the getter method of blocked windows returns false if we set it to false
     */
    @Test
    public void openedWindowsShouldBeFalse () {
        window.setOpen(false);
        Assert.assertFalse(window.isOpen());
    }

}
