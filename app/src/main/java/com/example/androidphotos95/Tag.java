package com.example.androidphotos95;

import java.io.Serializable;

public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;
    public String type, value;
    public int total;

    public Tag(String type, String value) {

        this.type = type.toLowerCase();
        this.value = value.toLowerCase();
        this.total = 0;
    }

    public boolean equals(Tag tag) {

        if(type.equalsIgnoreCase(tag.type) && value.equalsIgnoreCase(tag.value)) {
            return true;
        }
        return false;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String t) {

        this.type = t;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String v) {
        this.value = v;
    }

    public String getTag() {

        return this.type;
    }

    public String toString() {
        return this.type + ": " + this.value;
    }
}
