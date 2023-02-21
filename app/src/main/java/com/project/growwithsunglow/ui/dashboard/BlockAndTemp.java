package com.project.growwithsunglow.ui.dashboard;

public class BlockAndTemp {
    String blockNo, plantedDate, daysAfter, threeDays;

    public BlockAndTemp(String blockNo, String plantedDate, String daysAfter, String threeDays){
        this.blockNo = blockNo;
        this.plantedDate = plantedDate;
        this.daysAfter = daysAfter;
        this.threeDays = threeDays;
    }

    public String getBlockNo() {
        return blockNo;
    }

    public void setBlockNo(String blockNo) {
        this.blockNo = blockNo;
    }

    public String getPlantedDate() {
        return plantedDate;
    }

    public void setPlantedDate(String plantedDate) {
        this.plantedDate = plantedDate;
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
