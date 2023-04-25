package org.main.engine;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// auther:  Sakthivel.I

// This class is to read input excel and store it in Map

public class ReaderEngine {
    String exception= "";
        public ReaderEngine(){
            System.out.println( "ReaderEngine Obj Created ...");
        }

    public String getException(){
        return exception;
    }
        // This is backback code, but working initial version, may need to update as per below function changes
    public Map<String, Map<Integer, String>> readCompleteFullSheet(String sheetName) throws IOException {

        // This is to store header as key +  values as map. in the map store row no as key and cell value as value
        Map<String, Map<Integer, String>> excelFileMap = new HashMap<String, Map<Integer, String>>();

        try {

            // XSSFWorkbook  = XML SpreadSheet Format
            // is for Excel 2007 or above

            String path = "D:\\ExcelDataValidator\\cloned\\excel-data-validator\\src\\main\\resources\\data\\TestDataSheet.xlsx";
            FileInputStream fis = new FileInputStream(path);
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheet(sheetName);

            int lastRow = sheet.getLastRowNum();
            int lastCol = sheet.getRow(0).getPhysicalNumberOfCells();

            //System.out.println("lastRow " + lastRow); // 0 based index
            //System.out.println("lastCol " + lastCol); // starting from 1

            for (int i = 0; i < lastCol; i++) {

                //System.out.println("Col " + i);
                String keyHeader = sheet.getRow(0).getCell(i).getStringCellValue();
               // System.out.println("keyHeader " + keyHeader);

                Map<Integer, String> dataMap = new HashMap<Integer, String>();

                for (int j = 1; j <= lastRow; j++) {

                    //System.out.println("Row " + j);
                    Row row = sheet.getRow(j);
                    //System.out.println("Full row : "+ row);

                    if(row != null){
                        Cell valueCell = row.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        //System.out.println("Full Cell "+ valueCell);

                        if(valueCell != null) {
                            String value = returnCellValue(valueCell);
                            //System.out.println("value " + value);
                            //Putting key & value in dataMap
                            dataMap.put(j, value);
                            //Putting dataMap to excelFileMap

                        }
                    }
                }

                excelFileMap.put(keyHeader.trim(), dataMap);
            }

            workbook.close();
        } catch (FileNotFoundException e) {

            System.out.println("File is not available.");
            e.printStackTrace();
            exception = e.getMessage();
        } catch (IOException e) {

            System.out.println("Problem reading file from directory.");
            e.printStackTrace();
            exception = e.getMessage();
        } catch (NullPointerException e) {
            exception = e.getMessage();
            e.printStackTrace();
        }


        return excelFileMap;
    }



    public static String returnCellValue(Cell cell) {

        String cellData = "";

        CellType cellType = cell.getCellType().equals(CellType.FORMULA) ? cell.getCachedFormulaResultType() : cell.getCellType();

        if (cellType.equals(CellType.STRING)) {
            cellData = cell.getStringCellValue();
        }
        if (cellType.equals(CellType.NUMERIC)) {
            if (DateUtil.isCellDateFormatted(cell)) {
                cellData = cell.getDateCellValue().toString();
            } else {
                Double d = new Double( cell.getNumericCellValue());
                cellData = String.valueOf(d);
            }
        }
        if (cellType.equals(CellType.BOOLEAN)) {
            Boolean b= new Boolean(cell.getBooleanCellValue());
            cellData = String.valueOf(b);

        }
        return cellData;
    }

    public Map<String, Map<String, Map<String, String>>  > readCompleteExcel(String inputFile) throws IOException {

        Map<String, Map<String, Map<String, String>>  > mapOfAllSheets = new HashMap<>();

        try {

            // XSSFWorkbook  = XML SpreadSheet Format
            // is for Excel 2007 or above

            String path = "D:\\ExcelDataValidator\\cloned\\excel-data-validator\\src\\main\\resources\\data\\TestDataSheet.xlsx";
            FileInputStream fis = new FileInputStream(path);
            Workbook workbook = new XSSFWorkbook(fis);

            Integer sheets = workbook.getNumberOfSheets();



            workbook.close();

        } catch (FileNotFoundException e) {

            System.out.println("File is not available.");
            e.printStackTrace();

        } catch (IOException e) {

            System.out.println("Problem reading file from directory.");
            e.printStackTrace();

        } catch (NullPointerException e) {

            e.printStackTrace();
        }


        return mapOfAllSheets;
    }

    public Map<String, Map<String, Map<String, String>>  > readCompleteExcelUpdated(String inputFile,String headDir) throws IOException {

        Map<String, Map<String, Map<String, String>>  > mapOfAllSheets = new HashMap<>();

        try {

            // XSSFWorkbook  = XML SpreadSheet Format
            // is for Excel 2007 or above

            String path = inputFile;
            FileInputStream fis = new FileInputStream(path);
            Workbook workbook = new XSSFWorkbook(fis);

            Integer sheets = workbook.getNumberOfSheets();
//

            switch (headDir) {

                case "Row":
                    for (int s = 0; s < sheets; s++) {

                        // This is to store header as key +  values as map. in the map store row no as key and cell value as value
                        Map<String, Map<String, String>> excelFileMap = new HashMap<String, Map<String, String>>();

                        Sheet sheet = workbook.getSheetAt(s);
                        System.out.println("sheet >>> "+ sheet.getSheetName());

                        // start sheet
                        int lastRow = sheet.getLastRowNum();
                        int lastCol = sheet.getRow(0).getPhysicalNumberOfCells();

                        //System.out.println("lastRow " + lastRow); // 0 based index
                        //System.out.println("lastCol " + lastCol); // starting from 1

                        for (int i = 0; i < lastCol; i++) {

                          //  System.out.println("Col " + i);
                            String keyHeader = sheet.getRow(0).getCell(i).getStringCellValue();
                           // System.out.println("keyHeader " + keyHeader);

                            Map<String, String> dataMap = new HashMap<String, String>();

                            for (int j = 1; j <= lastRow; j++) {

                               // System.out.println("Row " + j);
                                Row row = sheet.getRow(j);
                                //System.out.println("Full row : "+ row);

                                if(row != null){
                                    Cell valueCell = row.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

                                    if(valueCell != null) {

                                        String value = returnCellValue(valueCell);
                                       // System.out.println("value " + value);
                                        //Putting key & value in dataMap
                                        dataMap.put(valueCell.getAddress().toString() +"#"+ (j + 1), value);

                                    }
                            else{
                                //System.out.println("Cell value is Empty ");
                                dataMap.put("#"+ String.valueOf(j + 1), "");
                            }

                                }

                            }
                            excelFileMap.put(keyHeader.trim(), dataMap);
                        }

                        mapOfAllSheets.put(sheet.getSheetName(),excelFileMap);
                    }


                    break;
                case "Column":
                    for (int s = 0; s < sheets; s++) {

                        // This is to store header as key +  values as map. in the map store row no as key and cell value as value
                        Map<String, Map<String, String>> excelFileMap = new HashMap<String, Map<String, String>>();

                        Sheet sheet = workbook.getSheetAt(s);
                        System.out.println("sheet >>> "+ sheet.getSheetName());

                        // start sheet
                        int lastRow = sheet.getLastRowNum();
                        int lastCol = sheet.getRow(0).getPhysicalNumberOfCells();

                        System.out.println("lastRow " + lastRow); // 0 based index
                        System.out.println("lastCol " + lastCol); // starting from 1

                        for (int i = 0; i <= lastRow; i++) {

                            System.out.println("Row " + i);

                            String keyHeader = sheet.getRow(i).getCell(0).getStringCellValue();

                            System.out.println("keyHeader " + keyHeader);

                            Map<String, String> dataMap = new HashMap<String, String>();

                            for (int j = 1; j <= lastCol; j++) {

                                // System.out.println("Co " + i);
                                Row row = sheet.getRow(i);

                                if(row != null){
                                    Cell valueCell = row.getCell(j, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);


                                    //  System.out.println("valueCell.getAddress(); "+ valueCell.getAddress());
                                    //  System.out.println("valueCell.getColumnIndex(); "+ valueCell.getColumnIndex() );

                                    if(valueCell != null) {

                                        String value = returnCellValue(valueCell);
                                        //System.out.println("value " + value);
                                        //Putting key & value in dataMap
                                        dataMap.put(valueCell.getAddress().toString() +"#"+ (j + 1), value);

                                    }
                            else{
                                //System.out.println("Cell value is Empty ");
                                dataMap.put( "#"+String.valueOf(j+1), "");
                            }

                                }

                            }
                            excelFileMap.put(keyHeader.trim(), dataMap);
                        }

                        mapOfAllSheets.put(sheet.getSheetName(),excelFileMap);
                    }
                    break;

                default:


            }

            //


            workbook.close();

        } catch (FileNotFoundException e) {
            exception = e.getMessage();
            System.out.println("File is not available.");
            e.printStackTrace();

        } catch (IOException e) {
            exception = e.getMessage();
            System.out.println("Problem reading file from directory.");
            e.printStackTrace();

        } catch (NullPointerException e) {
            exception = e.getMessage();
            e.printStackTrace();
        }


        return mapOfAllSheets;
    }









}
