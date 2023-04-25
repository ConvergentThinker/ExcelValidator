

package org.main.loadrulefc;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class RuleFilter extends FileFilter {


    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = Utils.getExtension(f);
        if (extension != null) {
            if (extension.equals(Utils.rule)

             ) {
                    return true;
            } else {
                return false;
            }
        }

        return false;
    }

    //The description of this filter
    public String getDescription() {
        return ".rule file only";
    }
}
