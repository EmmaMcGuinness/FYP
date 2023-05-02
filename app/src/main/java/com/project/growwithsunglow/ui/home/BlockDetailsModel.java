package com.project.growwithsunglow.ui.home;


public class BlockDetailsModel {
    String block;
    String planted;
    String daysAfter;
    String threeDays;


    public BlockDetailsModel(String block, String planted, String daysAfter, String threeDays){
        this.block = block;
        this.planted = planted;
        this.daysAfter = daysAfter;
        this.threeDays = threeDays;

    }


    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getPlanted() {
        return planted;
    }

    public void setPlanted(String planted) {
        this.planted = planted;
    }

    public String getDaysAfter() {
        return daysAfter;
    }

    public void setDaysAfter(String daysAfter) {
        this.daysAfter = daysAfter;
    }

    public String getThreeDays() {
        return threeDays;
    }

    public void setThreeDays(String threeDays) {
        this.threeDays = threeDays;
    }
}
