package com.example.szymon.shopping_list_app;

import android.app.Activity;
import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    static ArrayList<ProductList> productList;

    private static SharedPreferences preferences;

    public static final String mainSplitSymbol = "#";
    public static final String productSplitSymbol = "@";

    TableLayout table;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_lists);

        String FileName = "dbTest";
        preferences = getSharedPreferences(FileName, Activity.MODE_PRIVATE);
        productList = new ArrayList<>();

        table = (TableLayout)findViewById(R.id.tableALOL);

        Integer i = 0;
        while(restoreData(i.toString()) != null) //dopoki sa jakies listy w pliku
        {
            productList.add(new ProductList(restoreData(i.toString())));
            i++;
        }

        i = 0;
        for(int j = 0; j < productList.size(); j++)
        {
            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            row.setLayoutParams(lp);
            TextView textViewName = new TextView(this);
            TextView textViewDate = new TextView(this);
            Button buttonEdit = new Button(this);

            textViewName.setText(productList.get(j).getNameProductList());
            textViewName.setPadding(5,5,5,5);
            textViewName.setLayoutParams(lp);

            textViewDate.setText(productList.get(j).getDateProductList());
            textViewDate.setPadding(5,5,5,5);
            textViewDate.setLayoutParams(lp);

            buttonEdit.setText("Edit/Delete");
            buttonEdit.setPadding(5,5,5,5);
            buttonEdit.setLayoutParams(lp);

            row.addView(textViewName);
            row.addView(textViewDate);
            row.addView(buttonEdit);
            table.addView(row,i);
            i++;
        }

        //String test = "Nazwa4#02.05.2017 13:47#Chleb@1@2.5#Mleko@1@3.2";
        //saveData(test);


    }

    public void onClickDoNewList(View v)
    {
        Intent intent = new Intent(this, AddNewListActivity.class);
        startActivity(intent);
    }

    public void onClickQuit(View v)
    {
        System.exit(0);
    }

    private static Integer key = 0;
    public static void saveData(String inString) {
        key = 0;
        while(restoreData(key.toString()) != null) //dopoki sa jakies listy w pliku
            key++;

        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putString(key.toString(), inString);
        preferencesEditor.commit();
        key++;

    }

    public static String restoreData(String key) {
        return preferences.getString(key, null); //if null to nie ma takiej listy
    }
}



