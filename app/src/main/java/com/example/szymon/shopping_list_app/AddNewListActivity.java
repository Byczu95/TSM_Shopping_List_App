package com.example.szymon.shopping_list_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Szymon on 03.04.2017.
 */

public class AddNewListActivity extends AppCompatActivity {

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_list);

        editText = (EditText)findViewById(R.id.shoppingListNameAL);
        editText.setText("Shopping List 123");
    }

    public void onClickAddProduct(View v)
    {
        Intent intent = new Intent(this, AddNewProduct.class);
        intent.putExtra("ExtraListName", editText.getText().toString());
        startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
