package com.project.growwithsunglow;

import java.util.Calendar;
import java.util.Date;

public class BlockModel {
    String block;
    String variety;
    String propagator;
    String date;
    String daysAfter;
    String threeDays;
    String key;


    public BlockModel(String block, String variety, String propagator, String date, String daysAfter, String threeDays){
        this.block = block;
        this.variety = variety;
        this.propagator = propagator;
        this.date = date;
        this.daysAfter = daysAfter;
        this.threeDays = threeDays;

    }
    public BlockModel(String block, String variety, String propagator, String date){
        this.block = block;
        this.variety = variety;
        this.propagator = propagator;
        this.date = date;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
}
