package com.example.szymon.shopping_list_app;

/**
 * Created by Szymon on 11.04.2017.
 */

public class Product {

    private String nameProduct;
    private int quantityProduct;
    private int priceProduct;
    private String[] tagsProduct;
    private int tagsProductSize;

    public Product(String _nameProduct, int _quantityProduct, int _priceProduct, String[] tags, int _tagsSize)
    {
        tagsProductSize = _tagsSize;
        nameProduct = _nameProduct;
        quantityProduct = _quantityProduct;
        priceProduct = _priceProduct;
        tags = new String[tagsProductSize];
    }

    public Product(String _nameProduct)
    {
        int n = 20;
        String[] tags = new String[n];
        new Product(_nameProduct,1,0, tags, tags.length);
    }

    String getNameProduct(){ return nameProduct; }
    void setNameProduct(String nameProduct){ this.nameProduct = nameProduct; }

    int getQuantityProduct(){ return quantityProduct; }
    void setQuantityProduct(int quantityProduct){ this.quantityProduct = quantityProduct; }

    int getPriceProduct(){ return priceProduct; }
    void setPriceProduct(int priceProduct){ this.priceProduct = priceProduct; }

    String[] getTagsProduct(){ return tagsProduct; }
    void setTagsProduct(String[] tagsProduct){
        int j = 0;
        for(int i = 0; i <  this.tagsProduct.length; i++)
            if(i < tagsProduct.length)
            {
                this.tagsProduct[i] = tagsProduct[i];
                j++;
            }
            else
                this.tagsProduct[i] = null;
        setTagsProductSize(j);
    }

    int getTagsProductSize(){ return tagsProductSize; }
    void setTagsProductSize(int tagsProductSize){ this.priceProduct = priceProduct; }
}
