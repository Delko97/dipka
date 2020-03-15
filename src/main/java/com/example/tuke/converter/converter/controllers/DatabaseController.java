package com.example.tuke.converter.converter.controllers;

import com.example.tuke.converter.converter.pojos.Table;
import com.example.tuke.converter.converter.pojos.TableName;
import com.example.tuke.converter.converter.services.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin
@RequestMapping("/database")
public class DatabaseController {


    @Autowired
    DatabaseService databaseService;

    @PostMapping("/insert")
    public ResponseEntity insert() {
        databaseService.executeInsert();
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity createDatabase(@RequestBody Table table){
        databaseService.createTable(table);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/createFile")
    public ResponseEntity createFile(@RequestBody Table table){
        databaseService.createFile(table);
        return new ResponseEntity(HttpStatus.OK);
    }


    @GetMapping("/tables")
    public ResponseEntity<List<TableName>> getTables() {
        return new ResponseEntity(databaseService.getTableSchemas(),HttpStatus.OK);
    }

    @GetMapping("/getDatabaseData")
    public ResponseEntity<Table> getData(@RequestParam String sheetName) {
        return new ResponseEntity(databaseService.getData(sheetName),HttpStatus.OK);
    }

 }
