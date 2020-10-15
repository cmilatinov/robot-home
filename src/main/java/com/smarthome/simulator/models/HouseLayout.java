package com.smarthome.simulator.models;

import com.smarthome.simulator.utils.FileChooserUtil;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Class to set up the house layout
 */
public class HouseLayout extends IdentifiableObject {

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
     * @param name The name of the house layout.
     * @param rooms The list of {@link Room} inside the house layout.
     */
    public HouseLayout(String name, ArrayList<Room> rooms) {
        this.name = name;
        this.rooms = rooms;
    }

    // ============================ OVERRIDES ============================

    /**
     * This function is meant to put all attributes of a house layout in a string format.
     * @return String representation of all the current attributes of the house layout.
     */
    @Override
    public String toString() {
        return "HouseLayout{" +
                "id='" + getId() + '\'' +
                ", name='" + name + '\'' +
                ", rooms=" + rooms +
                '}';
    }

    /**
     * This function is meant to compare two HouseLayout objects and to verify if they are the same.
     * @param other HouseLayout object that represents the house layout being compared too.
     * @return Boolean value confirming or not if the two house layouts are the same.
     */
    public boolean equals(HouseLayout other)
    {
        if(this.name.equalsIgnoreCase(other.name))
        {
            if(this.rooms.equals(other.rooms))
            {
                return true;
            }
        }

        return false;
    }

    // ============================ GETTERS/SETTERS ============================

    /**
     * This function gets the name of the house layout.
     * @return Name of the house layout.
     */
    public String getName() {
        return name;
    }

    /**
     * This function sets the name of the house layout.
     * @param name Name of the house layout.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This function gets the list of {@link Room} of the house layout.
     * @return The list of {@link Room} of the house layout.
     */
    public ArrayList<Room> getRooms() {
        return rooms;
    }

    /**
     * This function sets the list of {@link Room} of the house layout.
     * @param rooms The list of {@link Room} of the house layout.
     */
    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }

    // ============================ STATIC METHODS ============================

    /**
     * This static function creates a new list of {@link Window} and sets them all as closed.
     * @param nbOfWindows Number of {@link Window} to be created.
     * @return The new list of {@link Window} created.
     */
    public static ArrayList<Window> createWindowList(int nbOfWindows) {
        // Creating the number of windows specified in the layout and storing them in a list
        ArrayList<Window> window = new ArrayList<Window>();
        for (int i = 0; i < nbOfWindows; i++) {
            Window window1 = new Window("Close");
            window.add(window1);
        }
        return window;
    }

    /**
     * This static function creates a new list of {@link Door} and sets them all as closed and unlocked.
     * @param nbOfDoors Number of {@link Door} to be created.
     * @return The new list of {@link Door} created.
     */
    public static ArrayList<Door> createDoorList(int nbOfDoors) {
        // Creating the number of doors specified in the layout and storing them in a list
        ArrayList<Door> door = new ArrayList<Door>();
        for (int i = 0; i < nbOfDoors; i++) {
            Door door1 = new Door(false, false);
            door.add(door1);
        }
        return door;
    }

    /**
     * This static function creates a new list of {@link Light} and sets them all as turned on.
     * @param nbOfLights Number of {@link Light} to be created.
     * @return The new list of {@link Light} created.
     */
    public static ArrayList<Light> createLightList(int nbOfLights) {
        // Creating the number of lights specified in the layout and storing them in a list
        ArrayList<Light> light = new ArrayList<Light>();
        for (int i = 0; i < nbOfLights; i++) {
            Light light1 = new Light(true);
            light.add(light1);
        }
        return light;
    }

    /**
     * Method to parse a JSON file selected
     * @param selectedFile File object that contains the JSON file that will be parsed
     * @return ArrayList of rooms that are in the HouseLayout
     */
    public static ArrayList<Room> parseJSONFile(File selectedFile)
    {
        Object obj = null;

        // Parsing file
        try {
            obj = new JSONParser().parse(new FileReader(selectedFile.getAbsolutePath()));
        } catch (Exception e) {
            return null;
        }

        // Catch ClassCastExceptions or any other runtime exceptions that may occur during parsing
        try {

            // Typecasting obj to JSONObject
            JSONObject house = (JSONObject) obj;

            // Getting rooms
            JSONArray rooms = (JSONArray) house.get("rooms");

            // Verifying if the user entered the rooms correctly
            if (rooms == null) {
                System.out.println("Could not find any rooms. Make sure that the file contains a rooms array.");
                return null;
            }

            // Verifying if the house is empty
            if (rooms.size() == 0) {
                System.out.println("House is empty. No rooms were defined.");
                return null;
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
                } catch (NumberFormatException e) {
                    System.out.println("Invalid data type " + e.getMessage().toLowerCase() + ".");
                    return null;
                } catch (NullPointerException e) {
                    System.out.println("Missing fields in json file.");
                    return null;
                }

                // Saving the objects of the room in an arraylist
                ArrayList<Window> windows = createWindowList(nbOfWindows);
                ArrayList<Door> doors = createDoorList(nbOfDoors);
                ArrayList<Light> lights = createLightList(nbOfLights);

                // Setting the dimensions of the room
                Rectangle2D.Float dimensions = new Rectangle2D.Float(positionX, positionY, width, height);

                // Creating the room with the information gathered above
                Room room1 = new Room(name, dimensions, windows, doors, lights);

                // Adding the room to the list of rooms in the house
                roomsList.add(room1);
            }

            return roomsList;

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Prompts the user for a file to load a house layout from. Reads the selected file and loads its information in a corresponding {@link HouseLayout} instance.
     *
     * @return The resulting {@link HouseLayout} instance, or null if the user has not selected a file or any other error occurs during parsing.
     */
    public static HouseLayout promptForLayout(Stage stage) {

        // Choosing a file
        FileChooser fileChooser = FileChooserUtil.createJSON();
        File selectedFile = fileChooser.showOpenDialog(stage);

        // Verifying if the user chose a file
        if (selectedFile == null) {
            System.out.println("No file selected");
            return null;
        }

        ArrayList<Room> roomsList = parseJSONFile(selectedFile);

        if(roomsList == null)
        {
            return null;
        }

        // Return a new HouseLayout instance
        return new HouseLayout(selectedFile.getName(), roomsList);

    }

}
