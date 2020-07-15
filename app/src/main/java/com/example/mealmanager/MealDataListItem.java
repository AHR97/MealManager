package com.example.mealmanager;

public class MealDataListItem {

    private  String UserName;
    private  String Date;
    private String Cost;

    public MealDataListItem(String userName, String date, String cost) {
        UserName = userName;
        Date = date;
        Cost = cost;
    }

    public MealDataListItem() {
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getCost() {
        return Cost;
    }

    public void setCost(String cost) {
        Cost = cost;
    }
}
