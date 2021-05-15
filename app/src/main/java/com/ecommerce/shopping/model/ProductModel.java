package com.ecommerce.shopping.model;

public class ProductModel
{
    private  String Product_Image;
    private  String Product_Name;
    private  String Product_Description;
    private  String Product_Price;

    public ProductModel()
    {
    }

    public ProductModel(String product_Image, String product_Name, String product_Description, String product_Price)
    {
        Product_Image = product_Image;
        Product_Name = product_Name;
        Product_Description = product_Description;
        Product_Price = product_Price;
    }

    public String getProduct_Image() {
        return Product_Image;
    }

    public void setProduct_Image(String product_Image) {
        Product_Image = product_Image;
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
}
