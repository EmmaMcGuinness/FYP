package com.project.growwithsunglow.ui.home;

public class AvgTemp {
    //int blockNo;
    String date, temp;

    public AvgTemp(String date, String temp){
        this.date = date;
        this.temp = temp;
        //this.blockNo = blockNo;

    }
    public AvgTemp(String temp){
        this.temp = temp;


    }

  /*  public int getBlockNo() {
        return blockNo;
    }

    public void setBlockNo(int blockNo) {
        this.blockNo = blockNo;
    }*/

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
