package com.example.tuke.converter.converter.services;

import com.example.tuke.converter.converter.builders.SqlBuilder;
import com.example.tuke.converter.converter.pojos.Customer;
import com.example.tuke.converter.converter.pojos.Field;
import com.example.tuke.converter.converter.pojos.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DatabaseService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    SqlBuilder sqlBuilder = new SqlBuilder();

    public void executeInsert() {
        System.out.println("Creating tables");

        jdbcTemplate.execute("CREATE TABLE customers(" +
                "id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))");



        // Split up the array of whole names into an array of first/last names
        List<Object[]> splitUpNames = Arrays.asList("John Woo", "Jeff Dean", "Josh Bloch", "Josh Long").stream()
                .map(name -> name.split(" "))
                .collect(Collectors.toList());

        // Use a Java 8 stream to print out each tuple of the list
        splitUpNames.forEach(name -> System.out.println(String.format("Inserting customer record for %s %s", name[0], name[1])));

        // Uses JdbcTemplate's batchUpdate operation to bulk load data
        jdbcTemplate.batchUpdate("INSERT INTO customers(first_name, last_name) VALUES (?,?)", splitUpNames);

        System.out.println("Querying for customer records where first_name = 'Josh':");
        jdbcTemplate.query(
                "SELECT id, first_name, last_name FROM customers WHERE first_name = ?", new Object[] { "Josh" },
                (rs, rowNum) -> new Customer(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name"))
        ).forEach(customer -> System.out.println(customer.toString()));
    }

    public void createTable(Table table) {
        String string = sqlBuilder.createTable(table);
        List<String> insert = sqlBuilder.insertToTable(table);
        String dropTable = "DROP TABLE IF EXISTS " + table.getTableName();
        jdbcTemplate.execute(dropTable);
        jdbcTemplate.execute(string);
        insert.forEach(query -> {
            System.out.println(query);
            jdbcTemplate.execute(query);
        });
    }

    public void createFile(Table table) {
        String string = sqlBuilder.createTable(table);
        List<String> insert = sqlBuilder.insertToTable(table);
        String home = System.getProperty("user.home") + "/Desktop/" + table.getTableName() + ".txt";

        try {
            FileWriter myWriter = new FileWriter(home);
            myWriter.write(string + '\n');
            insert.forEach(item -> {
                try {
                    myWriter.write(item);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public List<String> getTableSchemas() {
        List<String> tableNames = new ArrayList<>();
        jdbcTemplate.query(
                "SELECT table_name, table_schema,TABLE_TYPE\n" +
                        "FROM INFORMATION_SCHEMA.TABLES\n" +
                        "WHERE TABLE_SCHEMA != 'mysql' AND TABLE_SCHEMA != 'performance_schema'" +
                        "    AND TABLE_SCHEMA != 'information_schema' AND TABLE_SCHEMA != 'compiler';",
                (rs,rowNum) -> rs.getString("TABLE_NAME")
        ).forEach(field -> tableNames.add(field));
        return tableNames;
    }

    public Table getData (String sheetName) {
        Table table = new Table();
        table.setTableName(sheetName);
        List<List<Field>> fieldValues = new ArrayList<>();
        List<String> firstRow = new ArrayList<>();
        String selectString = "SELECT * from " + sheetName;
         jdbcTemplate.query(selectString, (ResultSetExtractor) rs -> {


             ResultSetMetaData rsmd = rs.getMetaData();
             int count = rsmd.getColumnCount();
             for(int i = 1 ; i <= count ; i++){
                 firstRow.add(rsmd.getColumnName(i));
             }
             table.setTableFields(firstRow);


             while (rs.next()) {
                 List<Field> row = new ArrayList<>();
                 for (int i = 1;i <= firstRow.size();i++) {
                     row.add(new Field(rs.getString(i) == null ? "-" : rs.getString(i),"string"));
                 }
                 fieldValues.add(row);
             }
             return count;
         });
         table.setTableValues(fieldValues);
         firstRow.forEach(item -> System.out.println(item));
         System.out.println(firstRow.size());

         return table;

    }
}
