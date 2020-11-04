package com.smarthome.simulator.modules;

import com.smarthome.simulator.models.Simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SHC extends Module{

    public static final String P_REMOTE_LIGHT_ACTIVATION = "RemoteLightActivation";










    public SHC(Simulation simulation) {
        super("SHC", simulation);
    }

    @Override
    public List<String> getPermissions() {
        return new ArrayList<String>() {{
            add(P_REMOTE_LIGHT_ACTIVATION);





        }};
    }

    @Override
    public ArrayList<String> getCommands() {
        return new ArrayList<String>() {{
            add("exampleCommand");
        }};
    }

    // SHC shc = new SHC(simulation)
    // shc.executeCommand("RemoteLightActivation", which light? on or off? , true)
    // shc.executeCommand("RemoteLightActivation", new Hashmap() , true)

    @Override
    public void executeCommand(String command, Map<String, Object> payload, boolean sentByUser) {

        if (!checkPermission(command, sentByUser)) {
            return;
        }

        switch (command) {
            case P_REMOTE_LIGHT_ACTIVATION:


        }

    }

}
