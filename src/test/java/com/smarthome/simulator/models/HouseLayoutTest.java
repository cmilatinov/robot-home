package com.smarthome.simulator.models;

import com.smarthome.simulator.exceptions.HouseLayoutException;
import org.junit.Assert;
import org.junit.*;
import java.io.File;
import java.util.ArrayList;
import org.assertj.core.api.*;

/**
 * 1. Read and load house-layout file
 */
public class HouseLayoutTest {

    /**
     * Testing if the parser method returns exception when the JSON file is empty
     */
    @Test(expected= NullPointerException.class)
    public void parsingEmptyFileShouldReturnException()
    {
        File selectedFile = new File("./src/test/resources/empty-sample-house-layout.json");

        HouseLayout.parseJSONFile(selectedFile);
    }

    /**
     * Testing if the parser method returns exception when there is a missing field in the JSON file
     */
    @Test(expected= NullPointerException.class)
    public void missingFieldInFileShouldReturnException()
    {
        File selectedFile = new File("./src/test/resources/missing-field-sample-house-layout.json");

        HouseLayout.parseJSONFile(selectedFile);
    }

    /**
     * Testing if the parser method returns exception when the JSON file does not follow the right format (JSON format)
     */
    @Test(expected= NullPointerException.class)
    public void wrongFormatShouldReturnException()
    {
        File selectedFile = new File("./src/test/resources/wrong-format-sample-house-layout.json");

        HouseLayout.parseJSONFile(selectedFile);
    }

    /**
     * Testing if the parser method returns exception when a JSON field contains the wrong data type
     */
    @Test(expected= NullPointerException.class)
    public void wrongFieldDataTypeShouldReturnException()
    {
        File selectedFile = new File("./src/test/resources/wrong-field-data-type-sample-house-layout.json");

        HouseLayout.parseJSONFile(selectedFile);
    }

    /**
     * Testing if the parser method returns exception when no file is chosen
     */
    @Test(expected= NullPointerException.class)
    public void noFileChosenShouldReturnException()
    {
        File selectedFile = new File("");

        HouseLayout.parseJSONFile(selectedFile);
    }

    /**
     * Testing if the parser method returns an ArrayList of Room when the JSON file is written correctly
     */
    @Test
    public void rightFormatShouldReturnArrayListOfRoom()
    {
        File selectedFile = new File("./src/test/resources/working-sample-house-layout.json");

        ArrayList<Room> roomList = new ArrayList<Room>();

        String name = "Kitchen";

        ArrayList<Window> windows = HouseLayout.createWindowList(2);
        ArrayList<Door> doors = HouseLayout.createDoorList(1);
        ArrayList<Light> lights = HouseLayout.createLightList(1);

        RoomDimensions dimensions = new RoomDimensions(0,0,200,300);

        Room room1 = new Room(name, dimensions, windows, doors, lights);

        roomList.add(room1);

        Assertions.assertThat(roomList)
                .usingRecursiveComparison()
                .ignoringFieldsMatchingRegexes(".*id")
                .isEqualTo(HouseLayout.parseJSONFile(selectedFile));
    }
}