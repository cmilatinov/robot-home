package com.smarthome.simulator.modules;

import com.smarthome.simulator.models.Light;
import com.smarthome.simulator.models.Person;
import com.smarthome.simulator.models.Simulation;
import org.panda_lang.panda.framework.language.parser.bootstrap.annotations.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class SHP extends Module{

    /**
     * Whether or not the system is in away mode.
     */
    private boolean away;

    /**
     * The delay before alerting authorities if an intruder is detected.
     */
    private float alertDelay;

    /**
     * The light configuration to be set when away mode is activated and withing set time window.
     */
    private HashMap<String, Boolean> awayLights;

    /**
     * The start of the time window during which awayLights will be kept on.
     */
    private LocalTime awayTimeStart;

    /**
     * The end of the time window during which awayLights will be kept on
     */
    private LocalTime awayTimeEnd;

    /**
     * Whether or not the current time of the simulation is within the time window for awayLights to be kept on.
     */
    private boolean withinAwayTime;

    /**
     * Format of time used in simulation.
     */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Permission identifier to set away lights.
     */
    public static final String P_SET_AWAY_LIGHTS = "SetAwayLights";

    /**
     * Permission identifier to set away mode.
     */
    public static final String P_SET_AWAY_MODE = "SetAwayMode";

    /**
     * Permission identifier to set alert delay.
     */
    public static final String P_SET_ALERT_DELAY = "SetAlertDelay";

    /**
     * Permission identifier to set the away time window.
     */
    public static final String P_SET_AWAY_TIME = "SetAwayTime";

    /**
     * Permission identifier to alert user.
     */
    public static final String P_ALERT_USER = "AlertUser";

    /**
     * Permission identifier to toggle away lights.
     */
    public static final String P_TOGGLE_AWAY_LIGHTS = "ToggleAwayLights";

    /**
     * Creates a new SHP with reference to the simulation.
     * @param _simulation
     */
    public SHP (Simulation _simulation) {
        super("SHP", _simulation);
        this.away = false;
        this.alertDelay = 0;
        this.awayLights = new HashMap<String, Boolean>();
        this.awayTimeEnd = LocalTime.MIDNIGHT;
        this.awayTimeStart = LocalTime.NOON;
        this.withinAwayTime = false;
    }

    /**
     * Send a notification to user and alerts authorities after set delay.
     */
    public void Alert () {
        NotifyUser();
        Thread alertThread = new Thread() {
            public void run() {
                try {
                    Thread.sleep((long) alertDelay);
                    NotifyAuthorities();
                } catch (InterruptedException ie)
                {
                    Thread.currentThread().interrupt();
                }
            }
        };
        alertThread.run();
    }

    /**
     * Notifies the user of an intruder during away mode by printing to console
     */
    private void NotifyUser () {
        System.out.println("Notifying user!");
        //TODO print to console
    }

    /**
     * Notifies authorities of an intruder during away mode.
     */
    private void NotifyAuthorities () {
        System.out.println("Notifying authorities!");
        //TODO print to console
    }

    /**
     * Sets home lights to away configuration designated by the user.
     */
    private void ExecuteAwayLights() {
        Module shc = simulation.getModule("ControlLights");

        if (!awayLights.isEmpty()) {
            awayLights.forEach((k, v) -> {
                HashMap lightPayLoad = new HashMap<String, Object>() {{
                    put("id", k);
                    put("on", v);
                }};
                System.out.println(lightPayLoad);
                shc.executeCommand("RemoteControlLights", lightPayLoad, false);
            });
        }
    }

    /**
     * Sets away mode on/off and closes all doors and windows.
     * Calls ExecuteAwayLights if current time is appropriate.
     * @param payload The arguments for execution
     */
    private void ExecuteAwayMode(Map<String, Object> payload) { 
        setAway((boolean) payload.get("value"));

        System.out.println("away: " + away);

        if (!away) {return;}

        Module shc = simulation.getModule("RemoteControlDoors");

        shc.executeCommand("LockAllDoors", null, false);
        shc.executeCommand("CloseAllWindows", null, false);

        //TODO check if within allotted time to set away lights on or off
        if (true)
            ExecuteAwayLights();

    }

    /**
     * Sets start and end values for away light activation period.
     * @param payload The arguments for execution
     */
    private void ExecuteAwayTime(Map<String, Object> payload) {
        Pattern time = Pattern.compile("\\d{1,2}:\\d{2}");
        String startTime = (String) payload.get("startTime");
        String endTime = (String) payload.get("endTime");

        startTime = startTime.substring(time.matcher(startTime).start());
        setAwayTimeStart(LocalTime.parse(startTime, formatter));
        endTime = endTime.substring(time.matcher(endTime).start());
        setAwayTimeEnd(LocalTime.parse(endTime, formatter));
    }

    /**
     * Gets the list of permissions/commands that the Module is responsible for/can execute.
     * @return List of Strings representing the permissions/commands pertaining to the module
     */
    @Override
    public List<String> getPermissions() {
        return new ArrayList<String> () {{
            add(P_SET_AWAY_MODE);
            add(P_SET_AWAY_LIGHTS);
            add(P_SET_AWAY_TIME);
            add(P_SET_ALERT_DELAY);
            add(P_ALERT_USER);
            add(P_TOGGLE_AWAY_LIGHTS);
        }};
    }

    /**
     * Executes a command given by a user or other system module.
     * @param command The name of the command to be executed.
     * @param payload The arguments for the command.
     * @param sentByUser Whether the command was called by a user or not. False if called by other system modules such as {@link SHC}.
     */
    @Override
    public void executeCommand(String command, Map<String, Object> payload, boolean sentByUser) {
        if (!checkPermission(command, sentByUser)) {
            return;
        }
        switch (command) {
            case P_SET_AWAY_LIGHTS:
                setAwayLights();
                break;

            case P_SET_AWAY_MODE:
                ExecuteAwayMode(payload);
                break;

            case P_SET_ALERT_DELAY:
                setAlertDelay((float) payload.get("delay"));
                break;

            case P_SET_AWAY_TIME:
                ExecuteAwayTime(payload);
                break;

            case P_ALERT_USER:
                Alert();
                break;

            case P_TOGGLE_AWAY_LIGHTS:
                ExecuteAwayLights();
                break;
        }
    }

    /**
     * Checks if the house is empty or not by looping through all rooms.
     * @return boolean
     */
    private boolean isHouseEmpty () {
        ArrayList<Person> people = simulation.getPeople();

        for (int i=0; i<people.size(); i++) {
            if (people.get(i).getRoomId() == null);
            return false;
        }
        return true;
    }

    /**
     * Checks if simulation is in away mode.
     * @return boolean
     */
    public boolean isAway() {
        return away;
    }

    /**
     * Sets away state to true or false after checking if house is empty.
     * @param _away The state-to-be of the simulation.
     */
    public void setAway(boolean _away) {
        if (_away && isHouseEmpty()) {
            this.away = true;
        }
        else if (!_away)
            this.away = false;
    }

    /**
     * Gets the amount of time that will delay alerting authorities if an intruder is detected.
     * @return float
     */
    public float getAlertDelay() {
        return alertDelay;
    }

    /**
     * Sets the amount of time that will delay alerting the authorities if an intruder is detected.
     * @param alertDelay the value to be set.
     */
    public void setAlertDelay(float alertDelay) {
        this.alertDelay = alertDelay;
    }

    /**
     * Saves current configuration of lights as configuration of lights to be kept on during away mode
     */
    private void setAwayLights() {
        ArrayList<Light> lights = simulation.getAllLights();
        System.out.println("setting away lights");
        awayLights = new HashMap<String, Boolean>();
        for(int i = 0; i < lights.size(); i++) {
            Light l = lights.get(i);
            awayLights.put(l.getId(), l.isOn());
        }
    }

    /**
     * Gets the start of the awayTime window.
     * @return LocalTime
     */
    public LocalTime getAwayTimeStart() {
        return awayTimeStart;
    }

    /**
     * Sets the start of the away time window.
     * @param awayTimeStart the value to be set.
     */
    public void setAwayTimeStart(LocalTime awayTimeStart) {
        this.awayTimeStart = awayTimeStart;
    }

    /**
     * Gets the end of the awayTime window.
     * @return LocalTime
     */
    public LocalTime getAwayTimeEnd() {
        return awayTimeEnd;
    }

    /**
     * Sets the end of the away time window.
     * @param awayTimeEnd the value to be set.
     */
    public void setAwayTimeEnd(LocalTime awayTimeEnd) {
        this.awayTimeEnd = awayTimeEnd;
    }

    /**
     * Returns the value of withinAwayTime.
     * @return boolean
     */
    public boolean isWithinAwayTime() {
        return withinAwayTime;
    }

    /**
     * Sets the value of withinAwayTime.
     * @param withinAwayTime the value to be set.
     */
    public void setWithinAwayTime(boolean withinAwayTime) {
        this.withinAwayTime = withinAwayTime;
    }

}
