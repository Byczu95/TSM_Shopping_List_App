package com.example.szymon.shopping_list_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Szymon on 11.04.2017.
 */

public class AddNewProduct extends AppCompatActivity {

    TextView shoppingListNameAP;
    TextView newProductNameAP;
    TextView newProductQuantityAP;
    TextView newProductPriceAP;
    String listName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        shoppingListNameAP = (TextView)findViewById(R.id.shoppingListNameAP);

        shoppingListNameAP.setText("Shopping List 1");
        listName = (String) getIntent().getSerializableExtra("ExtraListName");
        if(listName != null) {
            shoppingListNameAP.setText(listName);
        }
    }

    public void onClickAddNewProduct(View v)
    {
        newProductNameAP = (TextView)findViewById(R.id.newProductNameAP);
        newProductQuantityAP = (TextView)findViewById(R.id.newProductQuantityAP);
        newProductPriceAP = (TextView)findViewById(R.id.newProductPriceAP);

        Intent intent = new Intent(this, AddNewListActivity.class);

        if(validationNewProductNameAP()) {

            Product addItTolist;

            if(validationNewProductPriceAP() == 0)
                if(validationNewProductQuantityAP() == 1)
                    addItTolist = new Product(newProductNameAP.getText().toString());
                else
                    addItTolist = new Product(newProductNameAP.getText().toString(), validationNewProductQuantityAP() );
            else
                addItTolist = new Product(newProductNameAP.getText().toString(), validationNewProductQuantityAP(), validationNewProductPriceAP());

            Bundle bundle = new Bundle();
            bundle.putSerializable("addItToList", addItTolist);
            intent.putExtra("bundle", bundle);

            startActivity(intent);
        }
        else {
            //wyswietlenie ze podaj nazwe produktu
        }


    }



    public boolean validationNewProductNameAP()
    {
        if(newProductNameAP.getText().length() == 0)
            return false;
        return true;
    }

    public int validationNewProductQuantityAP()
    {
        if(newProductQuantityAP.getText().length() == 0)
            return 1;
        else
            try {
                return Integer.parseInt(newProductQuantityAP.getText().toString());
            }
            catch(NumberFormatException ex)
            {
                //obsluga
                return 1;
            }

    }

    public float validationNewProductPriceAP()
    {
        if(newProductQuantityAP.getText().length() == 0)
            return 0;
        else {
            try {
                return Float.parseFloat(newProductQuantityAP.getText().toString());
            }
            catch(NumberFormatException ex)
            {
                //obsluga
                return 0;
            }

        }
    }
}
