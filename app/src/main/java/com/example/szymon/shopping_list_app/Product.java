package com.example.szymon.shopping_list_app;

import java.io.Serializable;

/**
 * Created by Szymon on 11.04.2017.
 */

public class Product implements Serializable {

    private String nameProduct;
    private int quantityProduct;
    private double priceProduct;

    public Product(String _nameProduct) {new Product(_nameProduct,1,0);}
    public Product(String _nameProduct, int _quantityProduct ) {new Product(_nameProduct,_quantityProduct,0);}
    public Product(String _nameProduct, int _quantityProduct, double _priceProduct)
    {
        nameProduct = _nameProduct;
        quantityProduct = _quantityProduct;
        priceProduct = _priceProduct;
    }

    public String getNameProduct(){ return nameProduct; }
    public void setNameProduct(String nameProduct){ this.nameProduct = nameProduct; }

    public int getQuantityProduct(){ return quantityProduct; }
    public void setQuantityProduct(int quantityProduct){ this.quantityProduct = quantityProduct; }

    public double getPriceProduct(){ return priceProduct; }
    public void setPriceProduct(double priceProduct){ this.priceProduct = priceProduct; }

}
