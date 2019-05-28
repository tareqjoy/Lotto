package com.qerat.lotto;

import java.io.Serializable;
import java.util.List;

public class ForecastClass implements Serializable {

    private String gameName;
    private long date;
    private String banker;
    private String sure2_1, sure2_2;
    private String best5_1, best5_2, best5_3, best5_4, best5_5;

    public ForecastClass(String gameName, long date, String banker, String sure2_1, String sure2_2, String best5_1, String best5_2, String best5_3, String best5_4, String best5_5) {
        this.gameName = gameName;
        if (date > 0) {
            date *= -1;
        }
        this.date = date;
        this.banker = banker;
        this.sure2_1 = sure2_1;
        this.sure2_2 = sure2_2;
        this.best5_1 = best5_1;
        this.best5_2 = best5_2;
        this.best5_3 = best5_3;
        this.best5_4 = best5_4;
        this.best5_5 = best5_5;
    }


    ForecastClass() {

    }


    public String getBanker() {
        return banker;
    }

    public void setBanker(String banker) {
        this.banker = banker;
    }

    public String getSure2_1() {
        return sure2_1;
    }

    public void setSure2_1(String sure2_1) {
        this.sure2_1 = sure2_1;
    }

    public String getSure2_2() {
        return sure2_2;
    }

    public void setSure2_2(String sure2_2) {
        this.sure2_2 = sure2_2;
    }

    public String getBest5_2() {
        return best5_2;
    }

    public void setBest5_2(String best5_2) {
        this.best5_2 = best5_2;
    }

    public String getBest5_1() {
        return best5_1;
    }

    public void setBest5_1(String best5_1) {
        this.best5_1 = best5_1;
    }

    public String getBest5_3() {
        return best5_3;
    }

    public void setBest5_3(String best5_3) {
        this.best5_3 = best5_3;
    }

    public String getBest5_4() {
        return best5_4;
    }

    public void setBest5_4(String best5_4) {
        this.best5_4 = best5_4;
    }

    public String getBest5_5() {
        return best5_5;
    }

    public void setBest5_5(String best5_5) {
        this.best5_5 = best5_5;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        if (date > 0) {
            date *= -1;
        }
        this.date = date;
    }
}
