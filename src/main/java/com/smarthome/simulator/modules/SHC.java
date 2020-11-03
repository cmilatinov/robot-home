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

    // SHC shc = new SHC(simulation)
    // shc.executeCommand("RemoteLightActivation", which light? on or off? , true)

    @Override
    public void executeCommand(String command, Map<String, String> payload, boolean sentByUser) {

        super.executeCommand(command, payload, sentByUser);

        switch (command) {
            case P_REMOTE_LIGHT_ACTIVATION:

        }

    }

}
