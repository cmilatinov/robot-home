package com.smarthome.simulator.models;

import java.awt.geom.Rectangle2D;
import java.io.FileReader;
import java.io.IOException;
import java.awt.geom.Rectangle2D;

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

    public HouseLayout(String name, ArrayList<Room> rooms) {
        super();
        this.name = name;
        this.rooms = rooms;
    }

    public void parseFile()
    {

        Object obj = null;

        //Parsing House-layout.json
        try {
            obj = new JSONParser().parse(new FileReader("house-layout.json"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (ParseException e) {
            e.printStackTrace();
            System.exit(0);
        }

        //Typecasting obj to JSONObject
        JSONObject house = (JSONObject) obj;

        //Getting rooms
        JSONArray rooms = (JSONArray) house.get("rooms");

        for(int i=0; i < rooms.size(); i++)
        {
            JSONObject room = (JSONObject) rooms.get(i);
            String name = room.get("name").toString();
            int nbOfWindows  = Integer.parseInt(room.get("nbOfWindows").toString());
            int nbOfDoors  = Integer.parseInt(room.get("nbOfDoors").toString());
            int nbOfLights  = Integer.parseInt(room.get("nbOfLights").toString());
            double height  = Double.parseDouble(room.get("height").toString());
            double width  = Double.parseDouble(room.get("width").toString());
            int positionX  = Integer.parseInt(room.get("position-x").toString());
            int positionY  = Integer.parseInt(room.get("position-y").toString());

        }

    }
}
