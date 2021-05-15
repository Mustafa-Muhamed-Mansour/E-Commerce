package com.ecommerce.shopping.model;

public class SellerModel
{
    private  String Product_Seller_Image;
    private  String Product_Seller_Name;
    private  String Product_Seller_Description;
    private  String Product_Seller_Price;

    public SellerModel()
    {
    }

    public SellerModel(String product_Seller_Image, String product_Seller_Name, String product_Seller_Description, String product_Seller_Price)
    {
        Product_Seller_Image = product_Seller_Image;
        Product_Seller_Name = product_Seller_Name;
        Product_Seller_Description = product_Seller_Description;
        Product_Seller_Price = product_Seller_Price;
    }

    public String getProduct_Seller_Image() {
        return Product_Seller_Image;
    }

    public void setProduct_Seller_Image(String product_Seller_Image) {
        Product_Seller_Image = product_Seller_Image;
    }

    public String getProduct_Seller_Name() {
        return Product_Seller_Name;
    }

    public void setProduct_Seller_Name(String product_Seller_Name) {
        Product_Seller_Name = product_Seller_Name;
    }

    public String getProduct_Seller_Description() {
        return Product_Seller_Description;
    }

    public void setProduct_Seller_Description(String product_Seller_Description) {
        Product_Seller_Description = product_Seller_Description;
    }

    public String getProduct_Seller_Price() {
        return Product_Seller_Price;
    }

    public void setProduct_Seller_Price(String product_Seller_Price) {
        Product_Seller_Price = product_Seller_Price;
    }
}
