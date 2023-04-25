package org.main.jTable.Rule2;

import org.main.UtilsClass.ComboBox;
import org.main.UtilsClass.SpringUtilities;
import org.main.UtilsClass.TextField;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Map;
import java.util.Set;

public class Rule2FieldsWindow implements ItemListener {


    private JPanel mainPanel = new JPanel();
    ComboBox toRunDrp;
    ComboBox sheetDrp;
    ComboBox targetColumnDrp;

    ComboBox formats;

    ComboBox noOfRowsToRunDrp;
    TextField textFieldFrom;
    TextField textFieldTo ;

    JLabel l5;
    JLabel l6;
    Map<String, Map<String, Map<String, String>>> workbook;

    @SuppressWarnings("serial")
    public Rule2FieldsWindow(String headerDirection) {

        mainPanel.setLayout(new SpringLayout());

        //String[] COL_NAMES = {"Validate?:", "Sheet:", "All rows/Custom]:", "Row From:", "Row To:", "Target Column:","Format"};


        String[] COL_NAMES;

        if (headerDirection.equals("Column")) {

            COL_NAMES = new String[7];
            COL_NAMES[0] = "Validate?";
            COL_NAMES[1] =  "Sheet";
            COL_NAMES[2] = "Target Header";
            COL_NAMES[3] = "Format";
            COL_NAMES[4] = "Run[All rows/Custom]";
            COL_NAMES[5] = "Column No: From";
            COL_NAMES[6] = "Column No: To";

        } else {

            COL_NAMES = new String[7];
            COL_NAMES[0] = "Validate?";
            COL_NAMES[1] =  "Sheet";
            COL_NAMES[2] = "Target Header";
            COL_NAMES[3] = "Format";
            COL_NAMES[4] = "Run[All rows/Custom]";
            COL_NAMES[4] = "Row No: From";
            COL_NAMES[5] = "Row No: To";


        }

        // 1
        String[] toRun = { "Yes","No" };
        JLabel l = new JLabel(COL_NAMES[0], JLabel.TRAILING);
        mainPanel.add(l);
        toRunDrp = new ComboBox(toRun);
        toRunDrp.setEditable(false);
        l.setLabelFor(toRunDrp);
        mainPanel.add(toRunDrp);

        JLabel l2 = new JLabel(COL_NAMES[1], JLabel.TRAILING);
        mainPanel.add(l2);
        sheetDrp = new ComboBox();
        sheetDrp.addItemListener(this);
        sheetDrp.setEditable(false);
        l2.setLabelFor(sheetDrp);
        mainPanel.add(sheetDrp);

        JLabel l3 = new JLabel(COL_NAMES[2], JLabel.TRAILING);
        mainPanel.add(l3);
        targetColumnDrp = new ComboBox();
        targetColumnDrp.setEditable(false);
        l2.setLabelFor(targetColumnDrp);
        mainPanel.add(targetColumnDrp);

        JLabel l7 = new JLabel(COL_NAMES[3], JLabel.TRAILING);
        mainPanel.add(l7);
        String[] formatsArr = {"Number","Text",
                "D/MM/YY","DD/MM/YYYY","D/M/YY","DD/MM/YY","DD/M/YYYY","DD.M.YYYY","DD-M-YYYY","DD/M/YY",
                "MM/DD/YYYY","MM/DD/YY","MM.DD.YYYY","M.DD.YYYY","MM-DD-YY","MM-DD-YYYY",
                "MM\\DD\\YYYY", "M\\DD\\YYYY","MM/DD/YYYY",""
        };
        formats = new ComboBox(formatsArr);
        formats.setEditable(false);
        l7.setLabelFor(formats);
        mainPanel.add(formats);

        JLabel l4 = new JLabel(COL_NAMES[4], JLabel.TRAILING);
        mainPanel.add(l4);
        String[] noOfRowsToRun = {"All Rows","Custom"};
        noOfRowsToRunDrp = new ComboBox(noOfRowsToRun);
        noOfRowsToRunDrp.setEditable(false);
        noOfRowsToRunDrp.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getItem().toString().equals("Custom")){
                    textFieldFrom.setVisible(true);
                    textFieldTo.setVisible(true);
                    l5.setVisible(true);
                    l6.setVisible(true);

                } else if (e.getItem().toString().equals("All Rows")) {
                    textFieldFrom.setVisible(false);
                    textFieldTo.setVisible(false);

                    l5.setVisible(false);
                    l6.setVisible(false);

                }
            }
        });
        l4.setLabelFor(noOfRowsToRunDrp);
        mainPanel.add(noOfRowsToRunDrp);

        l5 = new JLabel(COL_NAMES[5], JLabel.TRAILING);
        mainPanel.add(l5);
        textFieldFrom = new TextField();
        textFieldFrom.setVisible(false);
        l5.setVisible(false);
        l5.setLabelFor(textFieldFrom);
        mainPanel.add(textFieldFrom);

        l6 = new JLabel(COL_NAMES[6], JLabel.TRAILING);
        mainPanel.add(l6);
        textFieldTo = new TextField();
        textFieldTo.setVisible(false);
        l6.setVisible(false);
        l6.setLabelFor(textFieldTo);
        mainPanel.add(textFieldTo);

        //Lay out the panel.
        SpringUtilities.makeCompactGrid(mainPanel,
                7, 2, //rows, cols
                10, 5,        //initX, initY
                70, 10);       //xPad, yPad


    }


    public JPanel getMainPanel(Map<String, Map<String, Map<String, String>>> workbook) {

        this.workbook = workbook;
        sheetDrp.setModel(new DefaultComboBoxModel(convert(workbook.keySet())));
        sheetDrp.setSelectedIndex(-1);
        targetColumnDrp.setSelectedIndex(-1);
        noOfRowsToRunDrp.setSelectedIndex(-1);
        return mainPanel;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }


    public Rule2Model getSelectedItem() {

        if(noOfRowsToRunDrp.getSelectedItem().toString().equals("Custom")){
            return new Rule2Model(
                    toRunDrp.getSelectedItem().toString(),
                    sheetDrp.getSelectedItem().toString(),
                    targetColumnDrp.getSelectedItem().toString(),
                    formats.getSelectedItem().toString(),
                    noOfRowsToRunDrp.getSelectedItem().toString(),
                    textFieldFrom.getText(),
                    textFieldTo.getText()
            ) ;
        }else{
            return new Rule2Model(
                    toRunDrp.getSelectedItem().toString(),
                    sheetDrp.getSelectedItem().toString(),
                    targetColumnDrp.getSelectedItem().toString(),
                    formats.getSelectedItem().toString(),
                    noOfRowsToRunDrp.getSelectedItem().toString()
            ) ;
        }

    }

    public void pushDataIntoForm(Rule2Model model,Map<String, Map<String, Map<String, String>>> workbook){
        this.workbook = workbook;
        sheetDrp.setModel(new DefaultComboBoxModel(convert(workbook.keySet())));
        toRunDrp.setSelectedItem(model.getIsToRun());
        noOfRowsToRunDrp.setSelectedItem(model.getRuleExecutionType());
        sheetDrp.setSelectedItem(model.getSheet());
        formats.setSelectedItem(model.getFormat());
        targetColumnDrp.setModel(new DefaultComboBoxModel(convert(workbook.get(model.getSheet()).keySet())));
        targetColumnDrp.setSelectedItem(model.getTargetHeader());
        noOfRowsToRunDrp.setSelectedItem(model.getRuleExecutionType());
        textFieldFrom.setText(model.getFromRow());
        textFieldTo.setText(model.getToRow());
    }

    // Function to convert Set<String> to String[]
    public static String[] convert(Set<String> setOfString) {

        // Create String[] of size of setOfString
        String[] arrayOfString = new String[setOfString.size()];

        // Copy elements from set to string array
        // using advanced for loop
        int index = 0;
        for (String str : setOfString)
            arrayOfString[index++] = str;

        // return the formed String[]
        return arrayOfString;
    }
    @Override
    public void itemStateChanged(ItemEvent e) {

        if ((e.getStateChange() == ItemEvent.SELECTED)) {
            String selection = sheetDrp.getSelectedItem().toString();
            targetColumnDrp.setModel(new DefaultComboBoxModel(convert(workbook.get(selection).keySet())));

        }

    }




}
