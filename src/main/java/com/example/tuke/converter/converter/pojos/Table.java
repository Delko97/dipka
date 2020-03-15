package com.example.tuke.converter.converter.pojos;

import java.util.ArrayList;
import java.util.List;

public class Table {

    private String tableName;
    private List<String> tableFields;
    private List<List<String>> tableValues;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getTableFields() {
        return tableFields;
    }

    public void setTableFields(List<String> tableFields) {
        this.tableFields = tableFields;
    }

    public List<List<String>> getTableValues() {
        return tableValues;
    }

    public void setTableValues(List<List<String>> tableValues) {
        this.tableValues = tableValues;
    }

}
