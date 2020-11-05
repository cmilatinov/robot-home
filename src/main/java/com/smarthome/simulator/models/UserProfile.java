package com.smarthome.simulator.models;

import com.smarthome.simulator.modules.SHC;
import com.smarthome.simulator.modules.SHP;
import com.smarthome.simulator.utils.FileChooserUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 * This class represents a user profile with its functionalities.
 */
public class UserProfile extends IdentifiableObject {
    public static final ArrayList<String> ALL_PERMISSIONS = new ArrayList<String>() {
        {
            add(SHC.P_CONTROL_DOORS);
            add(SHC.P_CONTROL_WINDOWS);
            add(SHC.P_CONTROL_LIGHTS);
            add(SHC.P_CONTROL_AUTO_MODE);
            add(SHC.P_CLOSE_ALL_WINDOWS);
            add(SHC.P_LOCK_ALL_DOORS);
            add(SHC.P_UPDATE_ROOM);

            add(SHC.P_REMOTE_CONTROL_DOORS);
            add(SHC.P_REMOTE_CONTROL_WINDOWS);
            add(SHC.P_REMOTE_CONTROL_LIGHTS);

            add(SHP.P_SET_AWAY_LIGHTS);
            add(SHP.P_SET_AWAY_MODE);
        }
    };

    /**
     * Name of the user profile.
     */
    private String name;

    /**
     * List of {@link String} representing permissions of the user profile.
     */
    private ArrayList<String> permissions;

    // ============================ CONSTRUCTORS ============================

    /**
     * Constructor for a profile with an empty permission list.
     *
     * @param name The name of the user profile.
     */
    public UserProfile(String name) throws Exception {
        if(name != null && !name.matches("\\s+") && name.matches("[a-zA-Z0-9\\s]+") && name.length()<=16) {
            this.name = name;
            this.permissions = new ArrayList<>();
        }
        else{
            throw new Exception();
        }
    }

    /**
     * Parameterized constructor.
     *
     * @param name        The name of the user profile.
     * @param permissions The list of {@link String} representing the permissions of the user profile.
     */
    public UserProfile(String name, ArrayList<String> permissions) {
        super();
        this.name = name;
        this.permissions = permissions;
    }

    // ============================ OVERRIDES ============================

    /**
     * This function is meant to put all attributes of a user profile in a string format.
     *
     * @return String representation of all the current attributes of the user profile.
     */
    @Override
    public String toString() {
        return "UserProfile{" +
                "id='" + getId() + '\'' +
                ", name='" + name + '\'' +
                ", permissions=" + permissions +
                '}';
    }
  
    /**
     * This function compares two UserProfiles.
     * @param o The UserProfile being compared to.
     * @return Boolean that represents if the UserProfile being compared to is the same or not.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserProfile)) return false;
        UserProfile that = (UserProfile) o;
        return this.getId().equals(that.getId());
    }

    // ============================ GETTERS/SETTERS ============================

    /**
     * This function gets the name of the user profile.
     *
     * @return The name of the user profile.
     */
    public String getName() {
        return name;
    }

    /**
     * This function sets the name of the user profile.
     *
     * @param name The name of the user profile.
     */
    public void setName(String name) throws Exception {
        if(name != null && !name.matches("\\s+") && name.matches("[a-zA-Z0-9\\s]+") && name.length()<=16) {
            this.name = name;
        }
        else{
            throw new Exception();
        }
    }

    /**
     * This function gets the list of {@link String} representing the permissions of the user profile.
     *
     * @return The list of {@link String} representing the permissions of the user profile.
     */
    public ArrayList<String> getPermissions() {
        return permissions;
    }

    /**
     * This function sets the list of {@link String} representing the permissions of the user profile.
     *
     * @param permissions The list of {@link String} representing the permissions of the user profile.
     * @return Boolean representation of whether or not the setPermissions was successful.
     */
    public boolean setPermissions(ArrayList<String> permissions) {
        for (String permission: permissions) {
            if (!UserProfile.ALL_PERMISSIONS.contains(permission))
            {
                return false;
            }
        }

        this.permissions = permissions;
        return true;
    }

    public boolean setPermissions(String clearance) {
        switch (clearance) {
            case "Parent":
                return this.setPermissions(new ArrayList<String>(){{
                    add("ControlDoors");
                    add("ControlLights");
                    add("ControlWindows");
                    add("ControlTemperatureOutside");
                    add("ControlTemperatureOutside");
                    add("ControlPerson");
                    add("ControlRooms");
                    add("ControlRoomDimensions");
                    add("ControlAutoMode");

                    add("RemoteControlDoors");
                    add("RemoteControlLights");
                    add("RemoteControlWindows");
                    add("RemoteControlTemperatureOutside");
                    add("RemoteControlTemperatureOutside");
                    add("RemoteControlPerson");
                    add("RemoteControlRooms");
                    add("RemoteControlRoomDimensions");

                    add("SetAwayLights");
                    add("SetAwayMode");
                }});
            default:
                return false;
        }
    }

    /**
     * Prompts the user to choose the location where they want to save the {@link UserProfile}
     *
     * @return The resulting {@link File} instance, or null if the user has not selected a file.
     */
    public static File promptToSaveFile(Component mainComponent) {
        // Choosing a file
        JFileChooser fileChooser = FileChooserUtil.promptUser("Save user profiles");
        int returnValue = fileChooser.showSaveDialog(mainComponent);

        // Verifying if the user chose a file
        if (returnValue != JFileChooser.APPROVE_OPTION) {
            System.out.println("No file selected");
        }

        return fileChooser.getSelectedFile();
    }

    /**
     * Prompts the user to choose the location where they saved the {@link UserProfile}
     *
     * @return The resulting {@link File} instance, or null if the user has not selected a file.
     */
    public static File promptForUserProfiles(Component mainComponent) {
        // Choosing a file
        JFileChooser fileChooser = FileChooserUtil.promptUser("Select a user profiles file");
        int returnValue = fileChooser.showOpenDialog(mainComponent);

        // Verifying if the user chose a file
        if (returnValue != JFileChooser.APPROVE_OPTION) {
            System.out.println("No file selected");
            return null;
        }

        return fileChooser.getSelectedFile();
    }

    /**
     * Writing the {@link UserProfile} to a JSON File
     * @param selectedFile The file in which the {@link UserProfile} will be written into.
     * @param profiles The list of all current {@link UserProfile} in the simulation
     * @return The resulting {@link File} instance, or null if the user has not selected a file.
     */
    public static void writeUserProfile(File selectedFile, ArrayList<UserProfile> profiles) {
        //Creating json object and array to store the name and permissions
        JSONObject users = new JSONObject();
        JSONArray userProfiles = new JSONArray();

        //Adding the name and the permission to the JSON object
        profiles.forEach((profile) ->  {
            JSONObject obj = new JSONObject();
            obj.put("name", profile.getName());
            obj.put("permissions", profile.getPermissions());
            userProfiles.add(obj);
        });

        //Adding all the profiles to the JSON array
        users.put("userProfiles", userProfiles);

        //Writing the JSON file with the JSON object and array
        FileWriter file = null;
        try{
            if(selectedFile.getPath().contains(".json")) {
                file = new FileWriter(selectedFile.getPath());
            }
            else {
                file = new FileWriter(selectedFile.getPath() + ".json");
            }
            file.write(users.toJSONString());
            file.close();
        }catch(Exception e)
        {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * Loading the {@link UserProfile} from the JSON File
     * @param selectedFile The file in which the {@link UserProfile} will be fetch from.
     * @return The resulting {@link UserProfile} list, or null for any parsing errors.
     */
    public static ArrayList<UserProfile> loadUserProfiles(File selectedFile)
    {
        // Resulting obj from parsing
        Object obj;

        // Parsing file
        try {
            obj = new JSONParser().parse(new FileReader(selectedFile.getAbsolutePath()));
        } catch (Exception e) {
            return null;
        }

        // Catch ClassCastExceptions or any other runtime exceptions that may occur during parsing
        try {

            // Typecasting obj to JSONObject
            JSONObject profiles = (JSONObject) obj;

            // Getting profiles
            JSONArray userProfiles = (JSONArray) profiles.get("userProfiles");

            // Verifying if the user entered the profiles correctly
            if (userProfiles == null) {
                System.out.println("Could not find any profiles. Make sure that the file contains a userProfiles array.");
                return null;
            }

            // Verifying if the user profiles array is empty
            if (userProfiles.size() == 0) {
                System.out.println("User profiles array is empty. No profiles were defined.");
                return null;
            }

            // Creating arraylist to store all profiles
            ArrayList<UserProfile> profilesList = new ArrayList<>();

            // Going through each profile
            for (int i = 0; i < userProfiles.size(); i++) {

                // Getting the ith profile
                JSONObject profile = (JSONObject) userProfiles.get(i);

                //Setting the initial value of the name and permission
                String name = "";
                JSONArray permissions;
                ArrayList<String> permissionList = new ArrayList<String>();

                // Getting all the information on the profile
                try {
                    name = profile.get("name").toString();
                    permissions = (JSONArray) profile.get("permissions");

                    //Looping through the permission and saving them
                    for(int j = 0; j<permissions.size(); j++){
                        String temp = permissions.get(j).toString();
                        permissionList.add(temp);
                    }

                    //Adding the userProfile to the list
                    UserProfile tempProfile = new UserProfile(name,permissionList);
                    profilesList.add(tempProfile);

                } catch (NumberFormatException e) {
                    System.out.println("Invalid data type " + e.getMessage().toLowerCase() + ".");
                    return null;
                } catch (NullPointerException e) {
                    System.out.println("Missing fields in json file.");
                    return null;
                }
            }

            return profilesList;

        } catch (Exception e) {
            System.out.println("An error has occurred while parsing the house layout file.");
            return null;
        }
    }
}
