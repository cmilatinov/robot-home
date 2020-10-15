package com.smarthome.simulator.models;

import org.junit.Assert;
import org.junit.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.ArrayList;
import org.assertj.core.api.*;

/**
 * 1. Read and load house-layout file
 */
public class HouseLayoutTest {

    /**
     * Testing if the parser method returns null when the JSON file is empty
     */
    @Test
    public void parsingEmptyFileShouldReturnNull()
    {
        HouseLayout tester = new HouseLayout("test", null);

        File selectedFile = new File("src/test/resources/empty-sample-house-layout.json");

        Assert.assertNull(tester.parseJSONFile(selectedFile));
    }

    /**
     * Testing if the parser method returns null when there is a missing field in the JSON file
     */
    @Test
    public void missingFieldInFileShouldReturnNull()
    {
        HouseLayout tester = new HouseLayout("test", null);

        File selectedFile = new File("src/test/resources/missing-field-sample-house-layout.json");

        Assert.assertNull(tester.parseJSONFile(selectedFile));
    }

    /**
     * Testing if the parser method returns null when the JSON file does not follow the right format (JSON format)
     */
    @Test
    public void wrongFormatShouldReturnNull()
    {
        HouseLayout tester = new HouseLayout("test", null);

        File selectedFile = new File("src/test/resources/wrong-format-sample-house-layout.json");

        Assert.assertNull(tester.parseJSONFile(selectedFile));
    }

    /**
     * Testing if the parser method returns null when a JSON field contains the wrong data type
     */
    @Test
    public void wrongFieldDataTypeShouldReturnNull()
    {
        HouseLayout tester= new HouseLayout("test",null);

        File selectedFile = new File("src/test/resources/wrong-field-data-type-sample-house-layout.json");

        Assert.assertNull(tester.parseJSONFile(selectedFile));
    }

    /**
     * Testing if the parser method returns null when no file is chosen
     */
    @Test
    public void noFileChosenShouldReturnNull()
    {
        HouseLayout tester= new HouseLayout("test",null);

        File selectedFile = new File("");

        Assert.assertNull(tester.parseJSONFile(selectedFile));
    }

    /**
     * Testing if the parser method returns an ArrayList of Room when the JSON file is written correctly
     */
    @Test
    public void rightFormatShouldReturnArrayListOfRoom()
    {
        HouseLayout tester = new HouseLayout("test", null);

        File selectedFile = new File("src/test/resources/working-sample-house-layout.json");

        ArrayList<Room> roomList = new ArrayList<Room>();

        String name = "Kitchen";

        ArrayList<Window> windows = tester.createWindowList(2);
        ArrayList<Door> doors = tester.createDoorList(1);
        ArrayList<Light> lights = tester.createLightList(1);

        Rectangle2D.Float dimensions = new Rectangle2D.Float(0,0,200,300);

        Room room1 = new Room(name, dimensions, windows, doors, lights);

        roomList.add(room1);

        Assertions.assertThat(roomList)
                .usingRecursiveComparison()
                .ignoringFieldsMatchingRegexes(".*id")
                .isEqualTo(HouseLayout.parseJSONFile(selectedFile));
    }
}