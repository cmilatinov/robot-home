package com.smarthome.simulator.models;

import com.smarthome.simulator.exceptions.UserProfileException;
import com.smarthome.simulator.modules.SHC;
import com.smarthome.simulator.modules.SHH;
import com.smarthome.simulator.modules.SHP;
import com.smarthome.simulator.utils.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

            add(SHH.SET_DEFAULT_ZONE);
            add(SHH.SET_ROOM_TEMPERATURE);
            add(SHH.EDIT_ZONE);
            add(SHH.ADD_ZONE);
            add(SHH.REMOVE_ZONE);
            add(SHH.SET_ROOM_OVERRIDE);
            add(SHH.SET_SEASON_TEMP);
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
    public UserProfile(String name) throws UserProfileException {
        if (name != null && !name.matches("\\s+") && name.matches("[a-zA-Z0-9\\s]+") && name.length() <= 16) {
            this.name = name;
            this.permissions = new ArrayList<>();
        } else {
            throw new UserProfileException(Logger.ERROR, "System", "Invalid profile name.");
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
     *
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
    public void setName(String name) throws UserProfileException {
        if (name != null && !name.matches("\\s+") && name.matches("[a-zA-Z0-9\\s]+") && name.length() <= 16) {
            this.name = name;
        } else {
            throw new UserProfileException(Logger.ERROR, "System", "Invalid profile name.");
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
                return this.setPermissions(new ArrayList<String>() {{
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

                    add(SHH.SET_DEFAULT_ZONE);
                    add(SHH.SET_ROOM_TEMPERATURE);
                    add(SHH.EDIT_ZONE);
                    add(SHH.ADD_ZONE);
                    add(SHH.REMOVE_ZONE);
                    add(SHH.SET_ROOM_OVERRIDE);
                    add(SHH.SET_SEASON_TEMP);
                }});
            case "Child":
                return this.setPermissions(new ArrayList<String>() {{
                    add(SHC.CONTROL_DOOR);
                    add(SHC.CONTROL_WINDOW);
                    add(SHC.CONTROL_LIGHT);

                    add(SHP.SET_AWAY_MODE);
                }});
            case "Guest":
                return this.setPermissions(new ArrayList<String>() {{
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
     *
     * @param selectedFile The file in which the {@link UserProfile} will be written into.
     * @param profiles     The list of all current {@link UserProfile} in the simulation
     * @return The resulting {@link File} instance, or null if the user has not selected a file.
     */
    public static void writeUserProfiles(File selectedFile, List<UserProfile> profiles) {
        JSONObject users = createUsers(createProfiles(profiles));

        //Writing the JSON file with the JSON object and array
        FileWriter file = null;
        try {
            file = new FileWriter(selectedFile.getPath());
            file.write(users.toJSONString());
            file.close();
        } catch (IOException e) {
            throw new UserProfileException(Logger.ERROR, "System", "Error while writing user profile json file.");
        }
    }

    /**
     * Method creates profiles with the user name and permissions.
     *
     * @param profiles UserProfile list containing all the current profiles.
     * @return JSONArray containing all the user profiles.
     */
    private static JSONArray createProfiles(List<UserProfile> profiles) {
        JSONArray userProfiles = new JSONArray();

        //Adding the name and the permission to the JSON object
        profiles.forEach((profile) -> {
            JSONObject obj = new JSONObject();
            obj.put("name", profile.getName());
            obj.put("permissions", profile.getPermissions());
            userProfiles.add(obj);
        });
        return userProfiles;
    }

    /**
     * Method creates the users with the different user profiles.
     *
     * @param userProfiles JSONArray of user profiles.
     * @return JSONObject containing all users of the application.
     */
    private static JSONObject createUsers(JSONArray userProfiles) {
        JSONObject users = new JSONObject();

        //Adding all the profiles to the JSON array
        users.put("userProfiles", userProfiles);

        return users;
    }

    /**
     * Loading the {@link UserProfile} from the JSON File
     *
     * @param selectedFile The file in which the {@link UserProfile} will be fetch from.
     * @return The resulting {@link UserProfile} list, or null for any parsing errors.
     */
    public static List<UserProfile> loadUserProfiles(File selectedFile) {
        // Resulting obj from parsing
        Object obj;

        // Parsing file
        try {
            obj = new JSONParser().parse(new FileReader(selectedFile.getAbsolutePath()));
        } catch (IOException | ParseException e) {
            throw new UserProfileException(Logger.ERROR, "System", "Error while fetching and parsing the json file.");
        }

        // Catch ClassCastExceptions or any other runtime exceptions that may occur during parsing
        try {
            return getUserProfilesList(obj);
        } catch (UserProfileException e) {
            return null;
        }
    }

    /**
     * Method to get the list of user profiles parsed.
     *
     * @param obj represents the parsed user profile file.
     * @return List of user profiles that are in the house.
     */
    private static List<UserProfile> getUserProfilesList(Object obj) throws UserProfileException {
        // Typecasting obj to JSONObject
        JSONObject profiles = (JSONObject) obj;

        // Getting profiles
        JSONArray userProfiles = (JSONArray) profiles.get("userProfiles");

        verifyUserProfiles(userProfiles);

        return saveUserProfileList(userProfiles);
    }

    /**
     * Method to verify if the user profiles are entered correctly and that the user profiles are not empty.
     *
     * @param userProfiles JSONArray object containing the parsed profiles.
     */
    private static void verifyUserProfiles(JSONArray userProfiles) throws UserProfileException {
        // Verifying if the user entered the profiles correctly
        if (userProfiles == null) {
            throw new UserProfileException(Logger.ERROR, "System", "Could not find any profiles. Make sure that the file contains a userProfiles array.");
        }

        // Verifying if the user profiles array is empty
        if (userProfiles.size() == 0) {
            throw new UserProfileException(Logger.ERROR, "System", "User profiles array is empty. No profiles were defined.");
        }
    }

    /**
     * Method to save all user profiles in an Arraylist.
     *
     * @param userProfiles JSONArray object containing the parsed profiles.
     * @return ArrayList of user profiles that are in the House.
     */
    private static ArrayList<UserProfile> saveUserProfileList(JSONArray userProfiles) throws UserProfileException {
        // Creating arraylist to store all profiles
        ArrayList<UserProfile> profilesList = new ArrayList<>();

        // Going through each profile
        for (int i = 0; i < userProfiles.size(); i++) {
            //Adding the userProfile to the list
            profilesList.add(getUserProfileInfo((JSONObject) userProfiles.get(i)));
        }

        return profilesList;
    }

    /**
     * Method to get all parsed information about the profiles.
     *
     * @param profile JSONObject containing one parsed profile.
     * @return One user profile of the house.
     */
    private static UserProfile getUserProfileInfo(JSONObject profile) throws UserProfileException {
        //Setting the initial value of the name and permission
        String name = "";
        JSONArray permissions;
        ArrayList<String> permissionList = new ArrayList<String>();

        // Getting all the information on the profile
        try {
            name = profile.get("name").toString();
            permissions = (JSONArray) profile.get("permissions");

            //Looping through the permission and saving them
            for (int j = 0; j < permissions.size(); j++) {
                String temp = permissions.get(j).toString();
                permissionList.add(temp);
            }
        } catch (NumberFormatException e) {
            throw new UserProfileException(Logger.ERROR, "System", "Invalid data type " + e.getMessage().toLowerCase() + ".");
        } catch (NullPointerException e) {
            throw new UserProfileException(Logger.ERROR, "System", "Missing fields in json file.");
        }

        return new UserProfile(name, permissionList);
    }
}
