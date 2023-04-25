package org.main.datavalidator;



import org.main.jTable.Rule1.Rule1Model;
import org.main.model.ErrorModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Rule1ValidatorEngine {

    private List<ErrorModel> errors;

    private List<Rule1Model> lstRule1;

    public Rule1ValidatorEngine() {
        errors = new ArrayList<>();
        lstRule1 = new ArrayList<>();
    }

    public int getRuleListSize(){
        return lstRule1.size();
    }


    public String getErrorsList(){
        System.out.println("errors size in Rule 1 :: "+ errors.size());

        String data="";

        if(errors.size()>0){
            for (int i = 0; i < errors.size(); i++) {
                ErrorModel rule = errors.get(i);
                String x = "";
                x = x.concat(rule.getRule()).concat(",").concat(rule.getSheetName()).concat(",").concat(String.valueOf(rule.getRowNo())).concat(",")
                        .concat(rule.getColumnHeader()).concat(",").concat( rule.getInfo());
                data = data.concat(x).concat("&");
            }
        }
        else{
            data = "Rule 1 Processed successfully. No issues found. ";
        }

        return data;
    }




    // Rule4: Define Mandatory Columns.
    //Ex: Columns dob,name,nric.. values are should not be empty. must fill.
    public void validateRule4(Map<String, Map<String, Map<String, String>>> inputExcelData, List<Rule1Model> rule1ModelList ) {

        lstRule1 = rule1ModelList;

        for(int i=0;i<lstRule1.size();i++){

            Rule1Model rule = lstRule1.get(i);

            System.out.println("rule "+ rule);

            System.out.println( "rule.getIsToRun() "+ rule.getIsToRun());

            // this IF is to decide which rule to be applied
            if(rule.getIsToRun().equals("Yes")) {

                switch (rule.getRuleExecutionType()) {

                    case "All Rows":
                        System.out.println("ALL");

                        Map<String, String> map = getSpecificColumnAllValues(inputExcelData, rule.getSheet(), rule.getTargetHeader().trim());

                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            System.out.println(entry.getKey() + " : " + entry.getValue());
                            int dataLength = entry.getValue().trim().length();

                            if(dataLength==0){
                                errors.add(new ErrorModel("Rule1",rule.getSheet(),Integer.parseInt(entry.getKey().split("#")[1]),rule.getTargetHeader(),"Cell value is Empty."));
                            }
                        }
                        break;

                    case "Custom":
                        System.out.println("CUSTOM");

                        Map<String, String> mapCustom = getSpecificColumnAllValues(inputExcelData, rule.getSheet(), rule.getTargetHeader().trim());

                        boolean isToEnd = false;

                        for (Map.Entry<String, String> entry : mapCustom.entrySet()) {

                            int fromNo = Integer.parseInt(rule.getFromRow());
                            int toNo = Integer.parseInt(rule.getToRow());

                                if(Integer.parseInt(entry.getKey().split("#")[1]) >= fromNo ){
                                    System.out.println(entry.getKey() + " : " + entry.getValue());
                                    int dataLength = entry.getValue().trim().length();

                                    if(dataLength==0){

                                        errors.add(new ErrorModel("Rule1",rule.getSheet(),Integer.parseInt(entry.getKey().split("#")[1]),rule.getTargetHeader(),"Cell value is Empty."));
                                    }
                                    if(Integer.parseInt(entry.getKey().split("#")[1]) == toNo){
                                        isToEnd = true;
                                        break;
                                    }
                                }
                                if(isToEnd)
                                    break;
                        }

                        break;

                    default:

                }
            }
            else{
                System.out.println(" Skipping Rule :  "+ rule);
            }

        }



    }


    public Map<String, String> getSpecificColumnAllValues(Map<String, Map<String, Map<String, String>>> fullExcel,String sheetName,String headerName) {
        Map<String, Map<String, String>> mapOfHeaders = fullExcel.get(sheetName);
        Map<String, String> map = mapOfHeaders.get(headerName);
        return map;
    }








}
