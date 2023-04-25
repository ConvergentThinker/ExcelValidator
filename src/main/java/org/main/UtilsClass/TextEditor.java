package org.main.UtilsClass;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TextEditor extends JPanel implements ActionListener {

    private static String[] fontOptions = {"Serif", "Agency FB", "Arial", "Calibri", "Cambrian", "Century Gothic", "Comic Sans MS", "Courier New", "Forte", "Garamond", "Monospaced", "Segoe UI", "Times New Roman", "Trebuchet MS", "Serif"};
    private static String[] sizeOptions = {"8", "10", "12", "14", "16", "18", "20", "22", "24", "26", "28"};
    ImageIcon fontIcon = new ImageIcon("res/FontIcon.png");
    JButton changeFont = new JButton("ChangeFont");

    JLabel fontLabel = new JLabel(fontIcon);
    JLabel fontLabelText = new JLabel("Font: ");
    JLabel fontSizeLabel = new JLabel("Size: ");

    JComboBox <String> fontName = new JComboBox<>(fontOptions);
    JComboBox <String> fontSize = new JComboBox<>(sizeOptions);
    JToolBar tool = new JToolBar();
    JTextArea texty = new JTextArea();
    JScrollPane scroll = new JScrollPane(texty);

    private static final int WIDTH = 1366;
    private static final int HEIGHT = WIDTH / 16 * 9;

    private static String name = "Text Editor";


    public void TextEditor() {

        changeFont.addActionListener(this);
        changeFont.setToolTipText("Change the Font");
        fontLabel.setToolTipText("Font");
        fontLabelText.setToolTipText("Set the kind of Font");
        fontSizeLabel.setToolTipText("Set the size of the Font");
        tool.add(fontLabel);
        tool.add(fontLabelText);
        tool.add(fontName);
        tool.addSeparator();
        tool.add(fontSizeLabel);
        tool.add(fontSize);
        tool.addSeparator();
        tool.add(changeFont);
        setLayout(new BorderLayout());
        add(tool, "North");
        add(scroll, "Center");
    }


    public void actionPerformed(ActionEvent evt) {

        String fontNameSet;
        String fontSizeSetTemp;
        int fontSizeSet;
        Object source = evt.getSource();
       if(source == changeFont) {
            fontNameSet = (String) fontName.getSelectedItem();
            fontSizeSetTemp = (String) fontSize.getSelectedItem();
            fontSizeSet = Integer.parseInt(fontSizeSetTemp);
            System.out.println(fontNameSet + fontSizeSet);
            texty.setFont(new Font(fontNameSet, Font.PLAIN, fontSizeSet));
        }

    }


}