package com.project.growwithsunglow.ui.home;

public class GdhModel {
    String date;
    String gdh;

    public GdhModel() {
    }

    public GdhModel(String date, String gdh){
            this.date = date;
            this.gdh = gdh;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getGdh() {
            return gdh;
        }

        public void setGdh(String gdh) {
            this.gdh = gdh;
        }

    }
