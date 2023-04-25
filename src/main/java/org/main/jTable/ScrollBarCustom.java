package org.main.jTable;

import javax.swing.*;
import java.awt.*;

public class ScrollBarCustom extends JScrollBar {

    public ScrollBarCustom() {
        setUI(new ModernScrollBarUI());
        setPreferredSize(new Dimension(10, 100));
        //setForeground(new Color(255,0,255));
        setForeground(new Color(103, 103, 103));
        setBackground(new Color(255,255,255));
    }
}
