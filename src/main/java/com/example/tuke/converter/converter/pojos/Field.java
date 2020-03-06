package com.example.tuke.converter.converter.pojos;

public class Field {

    public Object value;
    public String valueType;

    public Field(Object value, String valueType) {
        this.value = value;
        this.valueType = valueType;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }
}
