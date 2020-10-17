package com.smarthome.simulator.utils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class FileChooserUtil {

    /**
     * Creates a prompt to choose a file and restricts it to JSON files only.
     * @return The resulting {@link JFileChooser} instance.
     */
    public static JFileChooser createJSON() {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setDialogTitle("Select a house layout");
        jfc.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON Files (.json)", "json");
        jfc.addChoosableFileFilter(filter);
        return jfc;
    }

}
