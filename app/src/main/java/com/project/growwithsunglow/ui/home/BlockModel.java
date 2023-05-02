package com.project.growwithsunglow.ui.home;

import java.util.Calendar;
import java.util.Date;

public class BlockModel {
    String block;
    String variety;
    String propagator;
    String date;
    String daysAfter;
    String threeDays;
    String status;


    public BlockModel(String block, String variety, String propagator, String date, String status){
        this.block = block;
        this.variety = variety;
        this.propagator = propagator;
        this.date = date;
        this.status = status;
    }


    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public String getPropagator() {
        return propagator;
    }

    public void setPropagator(String propagator) {
        this.propagator = propagator;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
