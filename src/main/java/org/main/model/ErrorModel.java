package org.main.model;

import lombok.Data;

@Data

public class ErrorModel {

    String rule;
    String sheetName;
    int rowNo;
    String columnHeader;
    String info;

    public ErrorModel(String rule, String sheetName, int rowNo, String columnHeader, String info) {
        this.rule = rule;
        this.sheetName = sheetName;
        this.rowNo = rowNo;
        this.columnHeader = columnHeader;
        this.info = info;
    }






}
