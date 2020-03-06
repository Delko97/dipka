package com.example.tuke.converter.converter.controllers;

import com.example.tuke.converter.converter.pojos.Table;
import com.example.tuke.converter.converter.services.ExcelService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@CrossOrigin
@RequestMapping("/excel")
public class ExcelController {


    @Autowired
    ExcelService excelService;

    @GetMapping("/getExcelData")
    public ResponseEntity<Table> loadExcel(@RequestParam String destination) throws IOException, InvalidFormatException {
        return new ResponseEntity(excelService.getDataFromExcelFile(destination),HttpStatus.OK);
    }

    @GetMapping("/sheetNames")
    public ResponseEntity<List<String>> getSheetNames(@RequestParam String destination) throws IOException, InvalidFormatException {
        return new ResponseEntity(excelService.getSheetNames(destination), HttpStatus.OK);
    }

    @GetMapping("/firstRow")
    public ResponseEntity<List<String>> getFirstRow(@RequestParam String destination,
                                                    @RequestParam String sheetName) throws IOException, InvalidFormatException {
        return new ResponseEntity(excelService.getSheetFirstRow(destination,sheetName),HttpStatus.OK);
    }

    @PostMapping("/saveFile")
    public ResponseEntity saveFile(@RequestParam String destination,
                                   @RequestBody Table table) throws IOException, InvalidFormatException {
        excelService.saveTable(destination,table);
        return new ResponseEntity(HttpStatus.OK);
    }
}
