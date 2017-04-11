package com.example.szymon.shopping_list_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Szymon on 11.04.2017.
 */

public class AddNewProduct extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        textView = (TextView)findViewById(R.id.shoppingListNameAP);

        textView.setText("Shopping List 1");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String listName = (String) extras.getSerializable("ExtraListName");
            textView.setText(listName);
        }


    }
}
