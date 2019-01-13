package com.example.user.task.model;

public class Costs {

    private String categoryName;
    private String sumCosts;
    //private String date;


    public Costs(String size, String categoryName, String sumCosts/*String date*/) {

        this.categoryName = categoryName;
        this.sumCosts = sumCosts;
        //this.date=date;
    }


    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSumCosts() {
        return sumCosts;
    }

    public void setSumCosts(String sumCosts) {
        this.sumCosts = sumCosts;
    }

    /*public String getDateTime() {
        return date;
    }

    public void setDateTime(String date) {
        this.date = date;
    }*/
}