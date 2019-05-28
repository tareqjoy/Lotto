package com.qerat.lotto;

import java.io.Serializable;
import java.util.List;

public class ResultClass implements Serializable {
    private List<String> ffn;
    private List<String> xtra;
    private List<String> lfn;
    private String code;
    private long  date;


    ResultClass(String code, long date, List<String> ffn, List<String> xtra, List<String> lfn) {
        this.ffn = ffn;
        this.code = code;
        this.xtra = xtra;
        if(date>0){
            date*=-1;
        }
        this.date = date;
        this.lfn = lfn;

    }

    ResultClass(){

    }


    public List<String> getFfn() {
        return ffn;
    }

    public void setFfn(List<String> ffn) {
        this.ffn = ffn;
    }

    public List<String> getXtra() {
        return xtra;
    }

    public void setXtra(List<String> xtra) {
        this.xtra = xtra;
    }

    public List<String> getLfn() {
        return lfn;
    }

    public void setLfn(List<String> lfn) {
        this.lfn = lfn;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        if(date>0){
            date*=-1;
        }
        this.date = date;
    }
}
