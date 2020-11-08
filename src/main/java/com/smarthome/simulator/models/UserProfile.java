package com.smarthome.simulator.models;

import com.smarthome.simulator.SmartHomeSimulator;
import com.smarthome.simulator.modules.SHC;
import com.smarthome.simulator.modules.SHP;
import com.smarthome.simulator.utils.FileChooserUtil;
import com.smarthome.simulator.utils.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.ArrayList;

/**
 * This class represents a user profile with its functionalities.
 */
public class UserProfile extends IdentifiableObject {

    /**
     * The file in which to save the user profile information.
     */
    public static final String USER_PROFILE_FILEPATH = "./user_profiles.json";

    /**
     * List of all possible permission identifiers a user could have.
     */
    private static final ArrayList<String> ALL_PERMISSIONS = new ArrayList<String>() {
        {
            add(SHC.CONTROL_DOOR);
            add(SHC.CONTROL_WINDOW);
            add(SHC.CONTROL_LIGHT);
            add(SHC.SET_AUTO_MODE);
            add(SHC.CLOSE_ALL_WINDOWS);
            add(SHC.LOCK_ALL_DOORS);
            add(SHC.CLOSE_ALL_LIGHTS);
            add(SHC.UPDATE_ROOM_LIGHTS);

            add(SHC.REMOTE_CONTROL_DOOR);
            add(SHC.REMOTE_CONTROL_WINDOW);
            add(SHC.REMOTE_CONTROL_LIGHT);

            add(SHP.SET_AWAY_LIGHTS);
            add(SHP.SET_AWAY_MODE);
            add(SHP.SET_ALERT_DELAY);
            add(SHP.SET_AWAY_TIME);

            add(SHP.ALERT_USER);
            add(SHP.TOGGLE_AWAY_LIGHTS);
        }
    };

    /**
     * Name of the user profile.
     */
    private String name;

    /**
     * List of {@link String} representing permissions of the user profile.
     */
    private List<String> permissions;

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
    public List<String> getPermissions() {
        return permissions;
    }

    /**
     * This function sets the list of {@link String} representing the permissions of the user profile.
     *
     * @param permissions The list of {@link String} representing the permissions of the user profile.
     * @return Boolean representation of whether or not the setPermissions was successful.
     */
    public boolean setPermissions(List<String> permissions) {

        // If any permission is not in the list of permissions, return
        if (permissions.stream()
                .anyMatch(p -> !UserProfile.ALL_PERMISSIONS.contains(p)))
            return false;

        this.permissions = new ArrayList<>(permissions);
        return true;
    }

    /**
     * This function sets the list of {@link String} representing the permissions of the user profile, based on preset profile permission levels.
     *
     * @param clearance The type of profile permission level.
     * @return Boolean representation of whether or not the setPermissions was successful.
     */
    public boolean setPermissions(String clearance) {
        switch (clearance) {
            case "Parent":
                return this.setPermissions(new ArrayList<String>(){{
                    add(SHC.CONTROL_DOOR);
                    add(SHC.CONTROL_WINDOW);
                    add(SHC.CONTROL_LIGHT);
                    add(SHC.SET_AUTO_MODE);
                    add(SHC.CLOSE_ALL_WINDOWS);
                    add(SHC.LOCK_ALL_DOORS);

                    add(SHC.REMOTE_CONTROL_DOOR);
                    add(SHC.REMOTE_CONTROL_WINDOW);
                    add(SHC.REMOTE_CONTROL_LIGHT);

                    add(SHP.SET_AWAY_LIGHTS);
                    add(SHP.SET_AWAY_MODE);
                    add(SHP.SET_ALERT_DELAY);
                    add(SHP.SET_AWAY_TIME);
                }});
            case "Child":
                return this.setPermissions(new ArrayList<String>(){{
                    add(SHC.CONTROL_DOOR);
                    add(SHC.CONTROL_WINDOW);
                    add(SHC.CONTROL_LIGHT);

                    add(SHP.SET_AWAY_MODE);
                }});
            case "Guest":
                return this.setPermissions(new ArrayList<String>(){{
                    add(SHC.CONTROL_DOOR);
                    add(SHC.CONTROL_LIGHT);
                    add(SHC.CONTROL_WINDOW);
                }});

            case "Stranger":
            default:
                return this.setPermissions(new ArrayList<>());
        }
    }

    /**
     * Writing the {@link UserProfile} to a JSON File
     * @param selectedFile The file in which the {@link UserProfile} will be written into.
     * @param profiles The list of all current {@link UserProfile} in the simulation
     * @return The resulting {@link File} instance, or null if the user has not selected a file.
     */
    public static void writeUserProfiles(File selectedFile, List<UserProfile> profiles) {
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
    public static List<UserProfile> loadUserProfiles(File selectedFile)
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
                SmartHomeSimulator.LOGGER.log(Logger.ERROR, "System", "Could not find any profiles. Make sure that the file contains a userProfiles array.");
                return null;
            }

            // Verifying if the user profiles array is empty
            if (userProfiles.size() == 0) {
                SmartHomeSimulator.LOGGER.log(Logger.ERROR, "System", "User profiles array is empty. No profiles were defined.");
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
                    SmartHomeSimulator.LOGGER.log(Logger.ERROR, "System", "Invalid data type " + e.getMessage().toLowerCase() + ".");
                    return null;
                } catch (NullPointerException e) {
                    SmartHomeSimulator.LOGGER.log(Logger.ERROR, "System", "Missing fields in json file.");
                    return null;
                }
            }

            return profilesList;

        } catch (Exception e) {
            SmartHomeSimulator.LOGGER.log(Logger.ERROR, "System", "An error has occurred while parsing the house layout file.");
            return null;
        }
    }
}
