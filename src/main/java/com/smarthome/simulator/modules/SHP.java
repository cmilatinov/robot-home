package com.smarthome.simulator.modules;

import com.smarthome.simulator.SmartHomeSimulator;
import com.smarthome.simulator.exceptions.ModuleException;
import com.smarthome.simulator.models.Light;
import com.smarthome.simulator.models.Simulation;
import com.smarthome.simulator.utils.DelayedRunnable;
import com.smarthome.simulator.utils.Logger;
import com.smarthome.simulator.utils.TimeUtil;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The SHP class represents the Smart Home Security module.
 */
public class SHP extends Module {

    /**
     * Indicates whether the away mode is enabled or not.
     */
    private boolean awayMode;

    /**
     * The delay before alerting authorities if an intruder is detected.
     */
    private float alertDelay;

    /**
     * The light configuration to be set when away mode is activated and withing set time window.
     */
    private List<Light> awayLights;

    /**
     * The start of the time window during which awayLights will be kept on.
     */
    private LocalTime awayTimeStart;

    /**
     * The end of the time window during which awayLights will be kept on
     */
    private LocalTime awayTimeEnd;

    /**
     * Format of time used in simulation.
     */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Permission identifier to set away lights.
     */
    public static final String SET_AWAY_LIGHTS = "SetAwayLights";

    /**
     * Permission identifier to set away mode.
     */
    public static final String SET_AWAY_MODE = "SetAwayMode";

    /**
     * Permission identifier to set alert delay.
     */
    public static final String SET_ALERT_DELAY = "SetAlertDelay";

    /**
     * Permission identifier to set the away time window.
     */
    public static final String SET_AWAY_TIME = "SetAwayTime";

    /**
     * Permission identifier to alert user.
     */
    public static final String ALERT_USER = "AlertUser";

    /**
     * Permission identifier to toggle away lights.
     */
    public static final String TOGGLE_AWAY_LIGHTS = "ToggleAwayLights";

    /**
     * Creates a new SHP with reference to the simulation.
     *
     * @param simulation represents the running {@link Simulation}
     */
    public SHP(Simulation simulation) {
        super("SHP", simulation);
        this.awayMode = false;
        this.alertDelay = 0;
        this.awayLights = new ArrayList<>();
        this.awayTimeEnd = LocalTime.MIDNIGHT;
        this.awayTimeStart = LocalTime.NOON;
    }

    /**
     * Send a notification to user and alerts authorities after set delay.
     */
    public void Alert() {
        System.out.println("Auth delay: " + alertDelay);
        NotifyUser();
        DelayedRunnable task = new DelayedRunnable() {

            private long delay = (long) alertDelay * 1000000000;

            @Override
            public long getDelay() {
                return delay;
            }

            @Override
            public synchronized void setDelay(long delay) {
                this.delay = delay;
            }

            @Override
            public boolean isPeriodic() {
                return false;
            }

            @Override
            public long getPeriod() {
                return 0;
            }

            @Override
            public void run() {
                NotifyAuthorities();
            }
        };
        Simulation.getDispatcher().schedule(task);
    }

    /**
     * Notifies the user of an intruder during away mode by printing to console
     */
    private void NotifyUser() {
        SmartHomeSimulator.LOGGER.log(Logger.WARN, getName(), "Notifying user!");
    }

    /**
     * Notifies authorities of an intruder during away mode.
     */
    private void NotifyAuthorities() {
        SmartHomeSimulator.LOGGER.log(Logger.WARN, getName(), "Notifying authorities!");
    }

    /**
     * Sets home lights to away configuration designated by the user.
     */
    private boolean isHouseEmpty() {
        if (simulation.getUserLocation() != null)
            return false;
        return simulation.getPeople().stream().allMatch(p -> p.getRoomId() == null);
    }

    /**
     * Sets home lights to away configuration designated by the user.
     */
    private void ExecuteAwayLights() {

        // Only do this in away mode
        if (!awayMode)
            return;

        // Set all the desired lights to on
        awayLights.forEach(light -> {
            Map<String, Object> eventMap = new HashMap<String, Object>() {{
                put("id", light.getId());
                put("on", true);
            }};
            try {
                simulation.executeCommand(SHC.REMOTE_CONTROL_LIGHT, eventMap, false);
            } catch (ModuleException e) {
            }
            ;
        });

    }

    /**
     * Sets away mode on/off and closes all doors and windows.
     * Calls ExecuteAwayLights if current time is appropriate.
     *
     * @param payload The arguments for execution
     */
    private void ExecuteAwayMode(Map<String, Object> payload) throws ModuleException {

        boolean newAwayMode = ((boolean) payload.get("value"));
        if (isHouseEmpty() || !newAwayMode)
            awayMode = newAwayMode;
        else
            throw new ModuleException(Logger.ERROR, getName(), "CANNOT EXECUTE COMMAND 'SetAwayMode': House not empty");

        if (!awayMode)
            return;

        simulation.executeCommand(SHC.LOCK_ALL_DOORS, null, false);
        simulation.executeCommand(SHC.CLOSE_ALL_WINDOWS, null, false);
        simulation.executeCommand(SHC.CLOSE_ALL_LIGHTS, null, false);

        // Check if within allotted time to set the proper away lights on
        if (TimeUtil.isInRange(simulation.getTime(), awayTimeStart, awayTimeEnd))
            ExecuteAwayLights();

    }

    /**
     * Sets start and end values for away light activation period.
     *
     * @param payload The arguments for execution
     */
    private void ExecuteAwayTime(Map<String, Object> payload) {
        String startTime = (String) payload.get("startTime");
        String endTime = (String) payload.get("endTime");

        setAwayTimeStart(LocalTime.parse(startTime, formatter));
        setAwayTimeEnd(LocalTime.parse(endTime, formatter));
    }

    /**
     * Gets the list of permissions/commands that the Module is responsible for/can execute.
     *
     * @return List of Strings representing the permissions/commands pertaining to the module
     */
    public List<String> getCommandList() {
        return new ArrayList<String>() {{
            add(SET_AWAY_MODE);
            add(SET_AWAY_LIGHTS);
            add(SET_AWAY_TIME);
            add(SET_ALERT_DELAY);
            add(ALERT_USER);
            add(TOGGLE_AWAY_LIGHTS);
        }};
    }

    /**
     * Executes a command given by a user or other system module.
     *
     * @param command    The name of the command to be executed.
     * @param payload    The arguments for the command.
     * @param sentByUser Whether the command was called by a user or not. False if called by other system modules such as {@link SHC}.
     */
    public void executeCommand(String command, Map<String, Object> payload, boolean sentByUser) {
        // Switch state for all the possible commands
        switch (command) {
            case SET_AWAY_LIGHTS:
                setAwayLights();
                break;

            case SET_AWAY_MODE:
                try {
                    ExecuteAwayMode(payload);
                } catch (ModuleException e) {
                }
                ;
                break;

            case SET_ALERT_DELAY:
                alertDelay = Float.parseFloat(payload.get("value").toString());
                break;

            case SET_AWAY_TIME:
                ExecuteAwayTime(payload);
                break;

            case ALERT_USER:
                Alert();
                break;

            case TOGGLE_AWAY_LIGHTS:
                ExecuteAwayLights();
                break;
        }
    }

    /**
     * Saves current configuration of lights as configuration of lights to be kept on during away mode
     */
    private void setAwayLights() {
        awayLights = simulation.getAllLights()
                .stream()
                .filter(Light::isOn)
                .collect(Collectors.toList());
    }

    /**
     * Gets the start of the awayTime window in "HH:mm" format.
     *
     * @return {@link String}
     */
    public String getAwayTimeStart() {
        return formatter.format(awayTimeStart);
    }

    /**
     * Gets the start of the awayTime window as a {@link LocalTime} object
     *
     * @return {@link LocalTime}
     */
    public LocalTime getAwayTimeStartObj() {
        return awayTimeStart;
    }

    /**
     * Sets the start of the away time window.
     *
     * @param awayTimeStart the value to be set.
     */
    public void setAwayTimeStart(LocalTime awayTimeStart) {
        this.awayTimeStart = awayTimeStart;
    }

    /**
     * Gets the end of the awayTime window in "HH:mm" format.
     *
     * @return {@link String}
     */
    public String getAwayTimeEnd() {
        return formatter.format(awayTimeEnd);
    }

    /**
     * Gets the end of the awayTime window as a {@link LocalTime} object
     *
     * @return {@link LocalTime}
     */
    public LocalTime getAwayTimeEndObj() {
        return awayTimeEnd;
    }

    /**
     * Sets the end of the away time window.
     *
     * @param awayTimeEnd the value to be set.
     */
    public void setAwayTimeEnd(LocalTime awayTimeEnd) {
        this.awayTimeEnd = awayTimeEnd;
    }

    /**
     * Returns whether or not away mode is active.
     *
     * @return boolean
     */
    public boolean isAwayMode() {
        return awayMode;
    }

    /**
     * Returns the delay defined by the user for alerting the authorities.
     *
     * @return float representing the delay
     */
    public float getAlertDelay() {
        return alertDelay;
    }

}
