package com.smarthome.simulator.models.SimulationTestClasses;

import com.smarthome.simulator.models.Simulation;
import org.junit.Assert;
import org.junit.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SetDateAndTimeTest {

    /**
     * test simulation object for the test
     */
    Simulation tester = new Simulation();

    /**
     * 3. Set date and time
     * Testing if setting the date and time returns an exception when the date and time is null
     */
    @Test(expected = Exception.class)
    public void settingANullDateAndTimeShouldReturnAnException() throws Exception {
        tester.setDateTime(null);
    }

    /**
     * 3. Set date and time
     * Testing if setting the date and time returns an exception when the date and time is ""
     */
    @Test(expected = Exception.class)
    public void settingDateAndTimeWithEmptyStringShouldReturnAnException() throws Exception {
        tester.setDateTime("");
    }

    /**
     * 3. Set date and time
     * Testing if setting the date and time returns an exception when the date and time is " "
     */
    @Test(expected = Exception.class)
    public void settingDateAndTimeWithSpaceShouldReturnAnException() throws Exception {
        tester.setDateTime(" ");
    }

    /**
     * 3. Set date and time
     * Testing if setting the date and time returns an exception when the date and time is wrongly formated
     */
    @Test(expected = Exception.class)
    public void settingDateAndTimeWithWrongFormatShouldReturnAnException() throws Exception {
        tester.setDateTime("sadsadsa");
    }

    /**
     * 3. Set date and time
     * Testing if setting the date and time returns the date and time when it is formated correctly
     */
    @Test
    public void settingDateAndTimeWithTheRightFormatShouldReturnTheDateAndTime() {
        try {
            tester.setDateTime("2020-01-01 12:30");
        } catch (Exception e) {
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        LocalDateTime tester2 = LocalDateTime.parse("2020-01-01 12:30", formatter);

        Assert.assertEquals(tester2.format(formatter), tester.getDateTime());
    }
}