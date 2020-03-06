package com.example.tuke.converter.converter.builders;

import com.example.tuke.converter.converter.pojos.Field;
import com.example.tuke.converter.converter.pojos.Table;

import java.util.ArrayList;
import java.util.List;

public class SqlBuilder {

    public String createTable(Table table) {
        StringBuilder builder = new StringBuilder("CREATE TABLE " + table.getTableName() +"\n(");
        builder.append("id INT(11) NOT NULL AUTO_INCREMENT,\n");
        for (int i = 0; i < table.getTableFields().size(); i++ ) {
            builder.append(table.getTableFields().get(i) + " " + getType(table.getTableValues().get(0).get(i)) + ",");
            builder.append("\n");
        }
        builder.append("CONSTRAINT " + table.getTableName() + "_id PRIMARY KEY (id));");

        return builder.toString();
    }

    public List<String>  insertToTable(Table table) {
        StringBuilder builder = new StringBuilder();
        Object valueToInsert;
        List<String> queries = new ArrayList<>();
        for (List<Field> values : table.getTableValues()) {
            builder.append("INSERT INTO " + table.getTableName() + "(");
            for (int i = 0; i < table.getTableFields().size(); i++) {
                if (!values.get(i).getValue().equals("-"))
                    builder.append(table.getTableFields().get(i) + ",");
            }
            builder.deleteCharAt(builder.toString().length() - 1);
            builder.append(") VALUES (");
            for (Field value : values) {
                valueToInsert = value.getValue().equals("-") ? "" :
                        (value.getValueType().equals("string") ? "\'" + value.getValue()+ "\'" + "," : value.getValue() + ",");
                builder.append(valueToInsert);
            }
            builder.deleteCharAt(builder.toString().length()-1);
            builder.append(")\n");
            queries.add(builder.toString());
            builder = new StringBuilder();


        }
        return queries;
    }

    private String getType(Field field) {
        switch (field.getValueType()) {
            case "string":
                return "VARCHAR(255)";
            case "boolean":
                return "BOOLEAN";
            case "numeric":
                return "double";
            default:
                return "VARCHAR(255)";
        }
    }
}
