package org.main.jTable.Rule2;

import org.main.jTable.Rule1.Rule1Model;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class Rule2TableModel extends AbstractTableModel {
    private final String[] COL_NAMES = {"Validate?", "Sheet", "Target Header", "Format", "Run[All rows/Custom]", "Row No: From", "Row No: To",};


    private static final long serialVersionUID = 1L;
    private List<Rule2Model> rule2ModelArrayList = new ArrayList<>();

    @Override
    public int getColumnCount() {
        return 7;
    }

    @Override
    public int getRowCount() {
        return rule2ModelArrayList.size();
    }



    public void loadTableRows(List<Rule2Model> list){
        for (Rule2Model item:list) {
            addRow(item);
        }
    }

    public List<Rule2Model> getRule2ModelArrayList(){
        return rule2ModelArrayList;
    }


    @Override
    public String getColumnName(int column) {
        return COL_NAMES[column];
    }

    @Override
    public Object getValueAt(int row, int column) {
        Rule2Model rule1Model = rule2ModelArrayList.get(row);
        switch (column) {
            case 0:
                return rule1Model.getIsToRun();
            case 1:
                return rule1Model.getSheet();
            case 2:
                return rule1Model.getTargetHeader();
            case 3:
                return rule1Model.getFormat();
            case 4:
                return rule1Model.getRuleExecutionType();
            case 5:
                return rule1Model.getFromRow();
            case 6:
                return rule1Model.getToRow();
        }
        return null;
    }

    @Override
  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        Rule2Model rule2Model = rule2ModelArrayList.get(rowIndex);

        switch (columnIndex) {
            case 0:
                 rule2Model.setIsToRun(aValue.toString());
            case 1:
                 rule2Model.setSheet(aValue.toString());
            case 2:
                rule2Model.setTargetHeader(aValue.toString());
            case 3:
                rule2Model.setFormat(aValue.toString());
            case 4:
                 rule2Model.setRuleExecutionType(aValue.toString());
            case 5:
                 rule2Model.setFromRow(aValue.toString());
            case 6:
                 rule2Model.setToRow(aValue.toString());

        }

        fireTableRowsUpdated(rowIndex, rowIndex);

    }





    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public void addRow(Rule2Model item) {

        Rule2Model itemWithCount = new Rule2Model(
                item.getIsToRun(),
                item.getSheet(),
                item.getTargetHeader(),
                item.getFormat(),
                item.getRuleExecutionType(),
                item.getFromRow(),
                item.getToRow());


        rule2ModelArrayList.add(itemWithCount);
        int row = rule2ModelArrayList.size() - 1;
        fireTableRowsInserted(row, row);
    }


    public void deleteRow(int rowIndex){

        rule2ModelArrayList.remove(rowIndex);
        fireTableDataChanged();

    }


    public Rule2Model getTableRow(int rowIndex){
        return rule2ModelArrayList.get(rowIndex);
    }

    public void updateTableRow(Rule2Model item,int rowIndex){
        Rule2Model updateModel =  rule2ModelArrayList.get(rowIndex);
        updateModel.setIsToRun(item.getIsToRun());
        updateModel.setSheet(item.getSheet());
        updateModel.setTargetHeader(item.getTargetHeader());
        updateModel.setFormat(item.getFormat());
        updateModel.setRuleExecutionType(item.getRuleExecutionType());
        updateModel.setFromRow(item.getFromRow());
        updateModel.setToRow(item.getToRow());
        fireTableDataChanged();
    }








}
