package com.qerat.lotto;

import java.io.Serializable;

public class Game implements Serializable {

    private String name;
    private String code;
    private String date;

    Game(String code, String name, String date) {
        this.name = name;
        this.code = code;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
