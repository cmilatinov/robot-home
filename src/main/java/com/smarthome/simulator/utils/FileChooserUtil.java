package com.smarthome.simulator.utils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

/**
 * This class is used as a helper to create file choosers.
 */
public class FileChooserUtil {

    /**
     * Creates a prompt to choose a file and restricts it to JSON files only.
     * @param title represents the title of the file chooser
     * @return The resulting {@link JFileChooser} instance.
     */
    public static JFileChooser promptUser(String title) {
        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.setDialogTitle(title);
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON Files (.json)", "json");
        fileChooser.addChoosableFileFilter(filter);
        return fileChooser;
    }

}
