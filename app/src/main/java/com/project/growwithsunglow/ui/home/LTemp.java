package com.project.growwithsunglow.ui.home;

public class LTemp {
    String date, temp;

    public LTemp(String date, String temp){
     this.date = date;
     this.temp = temp;
    }
    public LTemp(String temp){
        this.temp = temp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }
}
