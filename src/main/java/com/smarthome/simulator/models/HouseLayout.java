package com.smarthome.simulator.models;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import java.util.ArrayList;

public class HouseLayout extends Id {
    private String name;
    private ArrayList<Room> rooms;

    @Override
    public String toString() {
        return "HouseLayout{" +
                "id='" + getId() + '\'' +
                ", name='" + name + '\'' +
                ", rooms=" + rooms +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }

    public HouseLayout() {
        super();
    }

    public HouseLayout(String name) {
        super();
        this.name = name;
    }

    public HouseLayout(String name, ArrayList<Room> rooms) {
        super();
        this.name = name;
        this.rooms = rooms;
    }

    public JFileChooser selectFile()
    {
        //Setting the UI of the file chooser
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (InstantiationException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
            System.exit(0);
        }

        //Creating prompt to choose file and restricting it to JSON file only
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON File", "json");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(filter);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        return fileChooser;
    }

    public void parseFile()
    {
        //Choosing a file
        JFileChooser fileChooser = selectFile();
        int result = fileChooser.showOpenDialog(new Component() {});
        File selectedFile = null;

        //Verifying if the user chose a file
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
        }
        else
        {
            System.exit(0);
        }

        Object obj = null;

        //Parsing file
        try {
            obj = new JSONParser().parse(new FileReader(selectedFile.getAbsolutePath()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        } catch (ParseException e) {
            System.out.println(e.toString());
            System.exit(0);
        }

        //Typecasting obj to JSONObject
        JSONObject house = (JSONObject) obj;

        //Getting rooms
        JSONArray rooms = (JSONArray) house.get("rooms");

        //Verifying if the user entered the rooms correctly
        if(rooms == null)
        {
            System.out.println("Could not find any rooms. Make sure that the file contains a rooms array.");
            System.exit(0);
        }

        //Verifying if the house is empty
        if(rooms.size() == 0)
        {
            System.out.println("House is empty. No rooms were defined.");
            System.exit(0);
        }

        //Creating arraylist to store all rooms in the house
        ArrayList<Room> roomsList = new ArrayList<Room>();

        //Going through each room
        for(int i=0; i < rooms.size(); i++)
        {
            //Getting the ith room
            JSONObject room = (JSONObject) rooms.get(i);

            String name = "";
            int nbOfWindows = 0;
            int nbOfDoors = 0;
            int nbOfLights = 0;
            float height = 0;
            float width = 0;
            float positionX = 0;
            float positionY = 0;

            //Getting all the information on the room
            try {
                name = room.get("name").toString();
                nbOfWindows = Integer.parseInt(room.get("nbOfWindows").toString());
                nbOfDoors = Integer.parseInt(room.get("nbOfDoors").toString());
                nbOfLights = Integer.parseInt(room.get("nbOfLights").toString());
                height = Float.parseFloat(room.get("height").toString());
                width = Float.parseFloat(room.get("width").toString());
                positionX = Float.parseFloat(room.get("position-x").toString());
                positionY = Float.parseFloat(room.get("position-y").toString());
            } catch(NumberFormatException e)
            {
                System.out.println("Invalid data type " + e.getMessage().toLowerCase() + ".");
                System.exit(0);
            }
            catch(NullPointerException e)
            {
                System.out.println("Missing fields in json file.");
                System.exit(0);
            }

            //Saving the objects of the room in an arraylist
            ArrayList<Window> windows = setRoomWindows(nbOfWindows);
            ArrayList<Door> doors = setRoomDoors(nbOfDoors);
            ArrayList<Light> lights = setRoomLights(nbOfLights);

            //Setting the dimensions of the room
            Rectangle2D.Float dimensions = new Rectangle2D.Float(positionX,positionY,width,height);

            //Creating the room with the information gathered above
            Room room1 = new Room(name,dimensions,windows,doors,lights);

            //Adding the room to the list of rooms in the house
            roomsList.add(room1);
        }

        this.setRooms(roomsList);
    }

    public ArrayList<Window> setRoomWindows(int nbOfWindows)
    {
        //Creating the number of windows specified in the layout and storing them in a list
        ArrayList<Window> window = new ArrayList<Window>();
        for(int i = 0; i < nbOfWindows; i++)
        {
            Window window1 = new Window("Close");
            window.add(window1);
        }
        return window;
    }

    public ArrayList<Door> setRoomDoors(int nbOfDoors)
    {
        //Creating the number of doors specified in the layout and storing them in a list
        ArrayList<Door> door = new ArrayList<Door>();
        for(int i = 0; i < nbOfDoors; i++)
        {
            Door door1 = new Door(false,false);
            door.add(door1);
        }
        return door;
    }

    public ArrayList<Light> setRoomLights(int nbOfLights)
    {
        //Creating the number of lights specified in the layout and storing them in a list
        ArrayList<Light> light = new ArrayList<Light>();
        for(int i = 0; i < nbOfLights; i++)
        {
            Light light1 = new Light(true);
            light.add(light1);
        }
        return light;
    }
}
