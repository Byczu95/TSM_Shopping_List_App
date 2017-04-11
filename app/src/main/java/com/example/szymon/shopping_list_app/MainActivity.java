package com.example.szymon.shopping_list_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_lists);

        }
    //https:developer.android.com/training/basics/firstapp/starting-activity.html

    public void onClickDoNewList(View v)
    {
        Intent intent = new Intent(this, AddNewListActivity.class);
        startActivity(intent);
    }

    public void onClickQuit(View v)
    {
        System.exit(0);
    }

}



