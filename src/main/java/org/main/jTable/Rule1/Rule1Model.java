package org.main.jTable.Rule1;

public class Rule1Model {

    public void setIsToRun(String isToRun) {
        this.isToRun = isToRun;
    }

    public void setRuleExecutionType(String ruleExecutionType) {
        this.ruleExecutionType = ruleExecutionType;
    }

    public void setFromRow(String fromRow) {
        this.fromRow = fromRow;
    }

    public void setToRow(String toRow) {
        this.toRow = toRow;
    }

    public void setSheet(String sheet) {
        this.sheet = sheet;
    }

    public void setTargetHeader(String targetHeader) {
        this.targetHeader = targetHeader;
    }

    public String getIsToRun() {
        return isToRun;
    }

    public String getRuleExecutionType() {
        return ruleExecutionType;
    }

    public String getFromRow() {
        return fromRow;
    }

    public String getToRow() {
        return toRow;
    }

    public String getSheet() {
        return sheet;
    }

    public String getTargetHeader() {
        return targetHeader;
    }

    @Override
    public String toString() {
        return
                       "Rule1Row" +"::" + isToRun + "::" +
                        sheet + "::" +
                        targetHeader + "::" +
                        ruleExecutionType + "::" +
                        fromRow + "::" +
                        toRow;

    }
    private String isToRun = "";
    private String  ruleExecutionType = ""; // Row[All/Custom]
    private String  fromRow = "";
    private String  toRow = "";
    private String  sheet = "";
    private String  targetHeader = "";



    public Rule1Model(String isToRun, String sheet, String targetHeader,String ruleExecutionType, String fromRow, String toRow) {
        this.isToRun = isToRun;
        this.sheet = sheet;
        this.targetHeader = targetHeader;
        this.ruleExecutionType = ruleExecutionType;
        this.fromRow = fromRow;
        this.toRow = toRow;
    }


    public Rule1Model(String isToRun, String sheet, String targetHeader,String ruleExecutionType) {
        this.isToRun = isToRun;
        this.sheet = sheet;
        this.targetHeader = targetHeader;
        this.ruleExecutionType = ruleExecutionType;
    }






}
