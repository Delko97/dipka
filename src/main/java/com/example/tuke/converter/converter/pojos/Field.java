package com.example.tuke.converter.converter.pojos;

public class Field {

    public Object value;

    public Field(Object value, String valueType) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
