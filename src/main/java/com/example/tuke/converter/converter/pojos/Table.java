package com.example.tuke.converter.converter.pojos;

import java.util.ArrayList;
import java.util.List;

public class Table {

    private String tableName;
    private List<String> tableFields;
    private List<List<Field>> tableValues;

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

    public List<List<Field>> getTableValues() {
        return tableValues;
    }

    public void setTableValues(List<List<Field>> tableValues) {
        this.tableValues = tableValues;
    }
}
