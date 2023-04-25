package org.main.jTable;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class CustomRenderer implements TableCellRenderer{


    TableCellRenderer render;
    Border b;
    public CustomRenderer(TableCellRenderer r, Color top, Color left, Color bottom, Color right){
        render = r;

        //It looks funky to have a different color on each side - but this is what you asked
        //You can comment out borders if you want too. (example try commenting out top and left borders)
        b = BorderFactory.createCompoundBorder();
        b = BorderFactory.createCompoundBorder(b, BorderFactory.createMatteBorder(0,0,0,0,top));
        b = BorderFactory.createCompoundBorder(b, BorderFactory.createMatteBorder(0,0,0,0,left));
        b = BorderFactory.createCompoundBorder(b, BorderFactory.createMatteBorder(0,0,1,0,bottom));
        b = BorderFactory.createCompoundBorder(b, BorderFactory.createMatteBorder(0,0,0,0,right));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value, boolean isSelected, boolean hasFocus, int row,
                                                   int column) {
        JComponent result = (JComponent)render.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        result.setBorder(b);
        return result;
    }







}
