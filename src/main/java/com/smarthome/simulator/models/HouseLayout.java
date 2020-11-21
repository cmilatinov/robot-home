package com.smarthome.simulator.models;

import com.smarthome.simulator.exceptions.HouseLayoutException;
import com.smarthome.simulator.utils.FileChooserUtil;
import com.smarthome.simulator.utils.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class describing a specific house layout.
 */
public class HouseLayout {

    /**
     * The name of the house layout.
     */
    private String name;

    /**
     * The list of {@link Room} making up this house layout.
     */
    private ArrayList<Room> rooms;

    // ============================ CONSTRUCTORS ============================

    /**
     * Parameterized constructor.
     *
     * @param name  The name of the house layout.
     * @param rooms The list of {@link Room} inside the house layout.
     */
    public HouseLayout(String name, ArrayList<Room> rooms) {
        this.name = name;
        this.rooms = rooms;
    }

    // ============================ OVERRIDES ============================

    /**
     * This function is meant to put all attributes of a house layout in a string format.
     *
     * @return String representation of all the current attributes of the house layout.
     */
    @Override
    public String toString() {
        return "HouseLayout{" +
                ", name='" + name + '\'' +
                ", rooms=" + rooms +
                '}';
    }

    // ============================ GETTERS/SETTERS ============================

    /**
     * This function gets the name of the house layout.
     *
     * @return Name of the house layout.
     */
    public String getName() {
        return name;
    }

    /**
     * This function sets the name of the house layout.
     *
     * @param name Name of the house layout.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This function gets the list of {@link Room} of the house layout.
     *
     * @return The list of {@link Room} of the house layout.
     */
    public ArrayList<Room> getRooms() {
        return rooms;
    }

    /**
     * This function sets the list of {@link Room} of the house layout.
     *
     * @param rooms The list of {@link Room} of the house layout.
     */
    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }

    // ============================ STATIC METHODS ============================

    /**
     * This static function creates a new list of {@link Window} and sets them all as closed.
     *
     * @param nbOfWindows Number of {@link Window} to be created.
     * @return The new list of {@link Window} created.
     */
    public static ArrayList<Window> createWindowList(int nbOfWindows) {

        // Creating the number of windows specified in the layout and storing them in a list
        return new ArrayList<Window>() {{
            for (int i = 0; i < nbOfWindows; i++)
                add(new Window());
        }};
    }

    /**
     * This static function creates a new list of {@link Door} and sets them all as closed and unlocked.
     *
     * @param nbOfDoors Number of {@link Door} to be created.
     * @return The new list of {@link Door} created.
     */
    public static ArrayList<Door> createDoorList(int nbOfDoors) {

        // Creating the number of doors specified in the layout and storing them in a list
        return new ArrayList<Door>() {{
            for (int i = 0; i < nbOfDoors; i++)
                add(new Door());
        }};
    }

    /**
     * This static function creates a new list of {@link Light} and sets them all as turned on.
     *
     * @param nbOfLights Number of {@link Light} to be created.
     * @return The new list of {@link Light} created.
     */
    public static ArrayList<Light> createLightList(int nbOfLights) {

        // Creating the number of lights specified in the layout and storing them in a list
        return new ArrayList<Light>() {{
            for (int i = 0; i < nbOfLights; i++)
                add(new Light());
        }};
    }

    /**
     * Method to parse a JSON file selected
     *
     * @param selectedFile File object that contains the JSON file that will be parsed
     * @return ArrayList of rooms that are in the HouseLayout
     */
    public static ArrayList<Room> parseJSONFile(File selectedFile) {

        // Resulting obj from parsing
        Object obj;

        // Parsing file
        try {
            obj = new JSONParser().parse(new FileReader(selectedFile.getAbsolutePath()));
        } catch (IOException|ParseException e) {
            throw new HouseLayoutException(Logger.ERROR, "System", "Error while fetching and parsing the json file.");
        }

        // Catch ClassCastExceptions or any other runtime exceptions that may occur during parsing
        try {
            // Typecasting obj to JSONObject
            JSONObject house = (JSONObject) obj;

            // Getting rooms
            JSONArray rooms = (JSONArray) house.get("rooms");

            // Verifying if the user entered the rooms correctly
            if (rooms == null) {
                throw new HouseLayoutException(Logger.ERROR, "System", "Could not find any rooms. Make sure that the file contains a rooms array.");
            }

            // Verifying if the house is empty
            if (rooms.size() == 0) {
                throw new HouseLayoutException(Logger.ERROR, "System", "House is empty. No rooms were defined.");
            }

            // Creating arraylist to store all rooms in the house
            ArrayList<Room> roomsList = new ArrayList<>();

            // Going through each room
            for (int i = 0; i < rooms.size(); i++) {

                // Getting the ith room
                JSONObject room = (JSONObject) rooms.get(i);

                String name = "";
                int nbOfWindows = 0;
                int nbOfDoors = 0;
                int nbOfLights = 0;
                float height = 0;
                float width = 0;
                float positionX = 0;
                float positionY = 0;
                boolean isHouseEntrance = false;

                // Getting all the information on the room
                try {
                    name = room.get("name").toString();
                    nbOfWindows = Integer.parseInt(room.get("nbOfWindows").toString());
                    nbOfDoors = Integer.parseInt(room.get("nbOfDoors").toString());
                    nbOfLights = Integer.parseInt(room.get("nbOfLights").toString());
                    height = Float.parseFloat(room.get("height").toString());
                    width = Float.parseFloat(room.get("width").toString());
                    positionX = Float.parseFloat(room.get("position-x").toString());
                    positionY = Float.parseFloat(room.get("position-y").toString());
                    isHouseEntrance = Boolean.parseBoolean(room.get("isHouseEntrance").toString());
                } catch (NumberFormatException e) {
                    throw new HouseLayoutException(Logger.ERROR, "System", "Invalid data type " + e.getMessage().toLowerCase() + ".");
                } catch (NullPointerException e) {
                    throw new HouseLayoutException(Logger.ERROR, "System", "Missing fields in json file.");
                }

                // Saving the objects of the room in an arraylist
                ArrayList<Window> windows = createWindowList(nbOfWindows);
                ArrayList<Door> doors = createDoorList(nbOfDoors);
                ArrayList<Light> lights = createLightList(nbOfLights);

                // Setting the dimensions of the room
                RoomDimensions dimensions = new RoomDimensions(positionX, positionY, width, height);

                // Creating the room with the information gathered above
                Room room1 = new Room(name, dimensions, windows, doors, lights);

                // if the room is of type houseEntrance
                if (isHouseEntrance)
                    room1.getDoors().stream().forEach(door -> door.setHouseEntrance(true));

                // Adding the room to the list of rooms in the house
                roomsList.add(room1);
            }

            return roomsList;

        } catch (HouseLayoutException e) {
            return null;
        }
    }

    /**
     * Prompts the user for a file to load a house layout from. Reads the selected file and loads its information in a corresponding {@link HouseLayout} instance.
     *
     * @return The resulting {@link HouseLayout} instance, or null if the user has not selected a file or any other error occurs during parsing.
     */
    public static HouseLayout promptForLayout(Component mainComponent) {

        // Choosing a file
        JFileChooser fileChooser = FileChooserUtil.promptUser("Select a house layout");
        int returnValue = fileChooser.showOpenDialog(mainComponent);

        File selectedFile;
        ArrayList<Room> roomsList;

        try {

            // Verifying if the user chose a file
            if (returnValue != JFileChooser.APPROVE_OPTION) {
                throw new HouseLayoutException(Logger.WARN, "System", "No file selected.");
            }

            selectedFile = fileChooser.getSelectedFile();
            roomsList = parseJSONFile(selectedFile);

            if (roomsList == null) {
                throw new HouseLayoutException(Logger.WARN, "System", "No rooms were detected");
            }
        }catch (HouseLayoutException e)
        {
            return null;
        }

        // Return a new HouseLayout instance
        return new HouseLayout(selectedFile.getName().replace("\\..*$", ""), roomsList);
    }

}
