

package org.main.textfilefc;

import javax.swing.*;
import javax.swing.filechooser.FileView;
import java.io.File;

public class TextFileView extends FileView {

    ImageIcon xlsxIcon = Utils.createImageIcon("images/pngIcon.png");

    public String getName(File f) {
        return null; //let the L&F FileView figure this out
    }

    public String getDescription(File f) {
        return null; //let the L&F FileView figure this out
    }

    public Boolean isTraversable(File f) {
        return null; //let the L&F FileView figure this out
    }

    public String getTypeDescription(File f) {
        String extension = Utils.getExtension(f);
        String type = null;

        if (extension != null) {
            if (extension.equals(Utils.rule) ||
                extension.equals(Utils.rule)) {
                type = "Excel";
            }
        }
        return type;
    }

    public Icon getIcon(File f) {
        String extension = Utils.getExtension(f);
        Icon icon = null;

        if (extension != null) {
            if (extension.equals(Utils.rule) ||
                extension.equals(Utils.rule)) {
                icon = xlsxIcon;
            }
        }
        return icon;
    }
}
