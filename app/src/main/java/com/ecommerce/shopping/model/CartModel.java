package com.ecommerce.shopping.model;

public class CartModel
{
    private  String Product_Name;
    private  String Product_Description;
    private  String Product_Price;
    private  String Product_Quantity;
    private  String Product_Discount;

    public CartModel()
    {
    }

    public CartModel(String product_Name, String product_Description, String product_Price, String product_Quantity, String product_Discount)
    {
        Product_Name = product_Name;
        Product_Description = product_Description;
        Product_Price = product_Price;
        Product_Quantity = product_Quantity;
        Product_Discount = product_Discount;
    }


    public String getProduct_Name() {
        return Product_Name;
    }

    public void setProduct_Name(String product_Name) {
        Product_Name = product_Name;
    }

    public String getProduct_Description() {
        return Product_Description;
    }

    public void setProduct_Description(String product_Description) {
        Product_Description = product_Description;
    }

    public String getProduct_Price() {
        return Product_Price;
    }

    public void setProduct_Price(String product_Price) {
        Product_Price = product_Price;
    }

    public String getProduct_Quantity() {
        return Product_Quantity;
    }

    public void setProduct_Quantity(String product_Quantity) {
        Product_Quantity = product_Quantity;
    }

    public String getProduct_Discount() {
        return Product_Discount;
    }

    public void setProduct_Discount(String product_Discount) {
        Product_Discount = product_Discount;
    }
}
