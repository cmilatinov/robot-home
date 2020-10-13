package com.smarthome.simulator.utils;

import javafx.stage.FileChooser;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class FileChooserUtil {

    /**
     * Creates a prompt to choose a file and restricts it to JSON files only.
     * @return The resulting {@link FileChooser} instance.
     */
    public static FileChooser createJSON() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON Files (.json)", "*.json"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        return fileChooser;
    }

}
