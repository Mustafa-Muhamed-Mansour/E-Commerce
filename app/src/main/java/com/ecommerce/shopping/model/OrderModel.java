package com.ecommerce.shopping.model;

public class OrderModel
{
    String Order_Full_Name;
    String Order_Phone_Number;
    String Order_Home_Address;
    String Order_City;
    String Order_Total_Amount;
    String Order_Date;
    String Order_Time;


    public OrderModel()
    {
    }

    public OrderModel(String order_Full_Name, String order_Phone_Number, String order_Home_Address, String order_City, String order_Total_Amount, String order_Date, String order_Time)
    {
        Order_Full_Name = order_Full_Name;
        Order_Phone_Number = order_Phone_Number;
        Order_Home_Address = order_Home_Address;
        Order_City = order_City;
        Order_Total_Amount = order_Total_Amount;
        Order_Date = order_Date;
        Order_Time = order_Time;
    }

    public String getOrder_Full_Name() {
        return Order_Full_Name;
    }

    public void setOrder_Full_Name(String order_Full_Name) {
        Order_Full_Name = order_Full_Name;
    }

    public String getOrder_Phone_Number() {
        return Order_Phone_Number;
    }

    public void setOrder_Phone_Number(String order_Phone_Number) {
        Order_Phone_Number = order_Phone_Number;
    }

    public String getOrder_Home_Address() {
        return Order_Home_Address;
    }

    public void setOrder_Home_Address(String order_Home_Address) {
        Order_Home_Address = order_Home_Address;
    }

    public String getOrder_City() {
        return Order_City;
    }

    public void setOrder_City(String order_City) {
        Order_City = order_City;
    }

    public String getOrder_Total_Amount() {
        return Order_Total_Amount;
    }

    public void setOrder_Total_Amount(String order_Total_Amount) {
        Order_Total_Amount = order_Total_Amount;
    }

    public String getOrder_Date() {
        return Order_Date;
    }

    public void setOrder_Date(String order_Date) {
        Order_Date = order_Date;
    }

    public String getOrder_Time() {
        return Order_Time;
    }

    public void setOrder_Time(String order_Time) {
        Order_Time = order_Time;
    }
}
