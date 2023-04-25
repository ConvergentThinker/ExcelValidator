package org.main.jTable.Rule2;

public class Rule2Model {

    private String isToRun = "";
    private String  ruleExecutionType = ""; // Row[All/Custom]

    public Rule2Model(String isToRun, String sheet, String targetHeader,String format,String ruleExecutionType, String fromRow, String toRow ) {
        this.isToRun = isToRun;
        this.ruleExecutionType = ruleExecutionType;
        this.sheet = sheet;
        this.targetHeader = targetHeader;
        this.fromRow = fromRow;
        this.toRow = toRow;
        this.format = format;
    }

    public Rule2Model(String isToRun, String sheet, String targetHeader,String format,String ruleExecutionType ) {
        this.isToRun = isToRun;
        this.ruleExecutionType = ruleExecutionType;
        this.sheet = sheet;
        this.targetHeader = targetHeader;
        this.format = format;
    }


    private String  sheet = "";

    public String getIsToRun() {
        return isToRun;
    }

    public void setIsToRun(String isToRun) {
        this.isToRun = isToRun;
    }

    public String getRuleExecutionType() {
        return ruleExecutionType;
    }

    public void setRuleExecutionType(String ruleExecutionType) {
        this.ruleExecutionType = ruleExecutionType;
    }

    public String getSheet() {
        return sheet;
    }

    public void setSheet(String sheet) {
        this.sheet = sheet;
    }

    public String getTargetHeader() {
        return targetHeader;
    }

    public void setTargetHeader(String targetHeader) {
        this.targetHeader = targetHeader;
    }

    public String getFromRow() {
        return fromRow;
    }

    public void setFromRow(String fromRow) {
        this.fromRow = fromRow;
    }

    public String getToRow() {
        return toRow;
    }

    public void setToRow(String toRow) {
        this.toRow = toRow;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    private String  targetHeader = "";
    private String  fromRow = "";
    private String  toRow = "";
    private String format = "";

    @Override
    public String toString() {
        return
                "Rule2Row: " + "::" + isToRun + "::" +
                        sheet + "::" +
                        targetHeader + "::" +
                        format + "::"+
                        ruleExecutionType + "::" +
                        fromRow + "::" +
                        toRow;

    }





}
