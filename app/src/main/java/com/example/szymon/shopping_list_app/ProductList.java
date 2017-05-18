package com.example.szymon.shopping_list_app;

import java.util.ArrayList;

/**
 * Created by Szymon on 02.05.2017.
 */

public class ProductList {

    private String _name;
    private boolean _isDelete; //0 mozna wyswietlac uzytkownikowi, 1 "usunieta"
    private String _lastEditDate;
    private ArrayList<Product> products;

    public ProductList(String stringToParse)
    {
        _lastEditDate = null;
        products = new ArrayList<Product>();

        String productListVar[] = stringToParse.split(MainActivity.mainSplitSymbol);
        _name = productListVar[0];
        _lastEditDate = productListVar[1];

        int i = 2;
        while (i < productListVar.length) {
            String productVar[] = productListVar[i].split(MainActivity.productSplitSymbol);
            products.add(new Product(productVar[0], Integer.parseInt(productVar[1]), Double.parseDouble(productVar[2])));
            i++;
        }
    }

    String getNameProductList(){ return _name; }
    void setNameProductList(String nameProductList){ this._name = nameProductList; }

    boolean getStatusProductList(){ return _isDelete; }
    void setStatusProductList(boolean statusProductList){ this._isDelete = statusProductList; }

    String getDateProductList(){ return _lastEditDate; }
    void setDateProductList(String lastEditDate){ this._lastEditDate = lastEditDate; }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void addProduct(Product p) {products.add(p);}

    public void deleteProduct(int index) {products.remove(index);}

    public int getProductIndex(Product p) {return products.indexOf(p);}

    public Product getProductByIndex(int index) {return products.get(index);}

    public Product getProductByName(String name)
    {
        int i = 0;
        while(i < products.size())
        {
            Product p = products.get(i);

            if (p.getNameProduct() == name)
                return p;
            else
                i++;
        }
        return null;
    }
}
