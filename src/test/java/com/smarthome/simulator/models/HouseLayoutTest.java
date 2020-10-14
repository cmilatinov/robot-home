package com.smarthome.simulator.models;

import org.junit.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.ArrayList;
import org.assertj.core.api.*;
import static org.hamcrest.CoreMatchers.is;


public class HouseLayoutTest {

    @Test
    public void parsingEmptyFileShouldReturnNull()
    {
        HouseLayout tester = new HouseLayout("test", null);

        File selectedFile = new File("src/test/resources/empty-sample-house-layout.json");

        org.junit.Assert.assertNull(tester.parseJSONFile(selectedFile));
    }

    @Test
    public void missingFieldInFileShouldReturnNull()
    {
        HouseLayout tester = new HouseLayout("test", null);

        File selectedFile = new File("src/test/resources/missing-field-sample-house-layout.json");

        org.junit.Assert.assertNull(tester.parseJSONFile(selectedFile));
    }

    @Test
    public void wrongFormatShouldReturnNull()
    {
        HouseLayout tester = new HouseLayout("test", null);

        File selectedFile = new File("src/test/resources/wrong-format-sample-house-layout.json");

        org.junit.Assert.assertNull(tester.parseJSONFile(selectedFile));
    }

   /* @Test
    public void rightFormatShouldReturnRoomList()
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

        org.assertj.core.api.Assertions.assertThat(roomList).usingElementComparatorIgnoringFields("id").isEqualTo(tester.parseJSONFile(selectedFile));

    }*/
}