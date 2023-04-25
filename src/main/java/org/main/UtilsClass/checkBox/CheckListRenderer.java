package org.main.UtilsClass.checkBox;

import javax.swing.*;
import java.awt.*;

public class CheckListRenderer extends JCheckBox implements ListCellRenderer {
    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean hasFocus) {
        setEnabled(list.isEnabled());
        setSelected(((CheckListItem) value).isSelected());
        setFont(list.getFont());
        //setBackground(list.getBackground());
        setBackground(new java.awt.Color(255,255,240));
        setForeground(list.getForeground());
        setText(value.toString());
        return this;
    }
}