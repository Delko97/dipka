package com.example.tuke.converter.converter.services;

import com.example.tuke.converter.converter.pojos.Field;
import com.example.tuke.converter.converter.pojos.Table;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelService {

    public Table getDataFromExcelFile(String path) throws IOException, InvalidFormatException {
        Table table = new Table();

        XSSFWorkbook workbook = new XSSFWorkbook(new File(path));
        Sheet sheet = workbook.getSheetAt(0);

        // get sheetName and first row
        Iterator<Row> iterator = sheet.iterator();
        table.setTableName(sheet.getSheetName());
        Row nextRow = iterator.next();
        List<String> firstRow = new ArrayList<>();
        for (int colNum = 0; colNum < nextRow.getLastCellNum(); colNum++) {
            Cell cell = nextRow.getCell(colNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            firstRow.add(cell.getStringCellValue());
        }
        table.setTableFields(firstRow);

        //get rest of the table
        List<List<Field>> wholeTable = new ArrayList<>();
        List<Field> oneRow;
        while (iterator.hasNext()) {
            nextRow = iterator.next();
            oneRow = new ArrayList<>();
            for (int colNum = 0; colNum < firstRow.size(); colNum++) {

                Cell cell = nextRow.getCell(colNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                switch (cell.getCellTypeEnum()) {

                    case STRING:
                        oneRow.add(new Field(cell.getStringCellValue(), "string"));
                        System.out.print(cell.getStringCellValue() + "\t");
                        break;
                    case BOOLEAN:
                        oneRow.add(new Field(cell.getBooleanCellValue(), "boolean"));
                        System.out.print(cell.getBooleanCellValue() + "\t");
                        break;
                    case NUMERIC:
                        oneRow.add(new Field(cell.getNumericCellValue(), "numeric"));
                        System.out.print(cell.getNumericCellValue() + "\t");
                        break;
                    default:
                        oneRow.add(new Field("-", "blank"));
                        System.out.print("-" + "\t");
                        break;

                }
            }
            wholeTable.add(oneRow);
            System.out.println();
        }
        table.setTableValues(wholeTable);
        workbook.close();

        return table;
    }

    public List<String> getSheetNames(String path) throws IOException, InvalidFormatException {
        XSSFWorkbook workbook = new XSSFWorkbook(new File(path));
        List<String> sheetNames = new ArrayList<>();
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            sheetNames.add(workbook.getSheetName(i));
        }
        return sheetNames;
    }

    public List<String> getSheetFirstRow(String path,String sheetName) throws IOException, InvalidFormatException {
        XSSFWorkbook workbook = new XSSFWorkbook(new File(path));
        Sheet sheet = workbook.getSheet(sheetName);
        Iterator<Row> iterator = sheet.iterator();

        Row nextRow = iterator.next();
        List<String> firstRow = new ArrayList<>();
        for (int colNum = 0; colNum < nextRow.getLastCellNum(); colNum++) {
            Cell cell = nextRow.getCell(colNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            firstRow.add(cell.getStringCellValue());
        }
        return firstRow;
    }

    public void saveTable(String path,Table table) throws IOException{
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(table.getTableName());

        int rowCount = 0;
        int columnCount = 0;
        Row row = sheet.createRow(rowCount++);

        for (String field : table.getTableFields()) {
            Cell cell = row.createCell(columnCount++);
            cell.setCellValue(field);
        }
        for (List<Field> list : table.getTableValues()) {
            row = sheet.createRow(rowCount++);

            columnCount = 0;

            for (Field field : list) {
                Cell cell = row.createCell(columnCount++);
                if (field.getValueType().equals("string")) {
                    cell.setCellValue((String) field.getValue());
                } else if (field.getValueType().equals("number")) {
                    cell.setCellValue((double) field.getValue());
                } else if (field.getValueType().equals("boolean")) {
                    cell.setCellValue((boolean) field.getValue());
                } else if (field.getValueType().equals("blank")) {
                    cell.setCellValue("");
                }
            }

        }


        try (FileOutputStream outputStream = new FileOutputStream(path)) {
            workbook.write(outputStream);
        }

    }

}
