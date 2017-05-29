package com.example.szymon.shopping_list_app;

import android.app.Activity;
import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.text.InputType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static SharedPreferences preferences;

    private static TableLayout table;
    private static List<Product> productList;

    public static final String mainSplitSymbol = "#";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        table = (TableLayout) findViewById(R.id.TableProducts);
        productList = new ArrayList<Product>();

        String FileName = "dbTestSL";
        preferences = getSharedPreferences(FileName, Activity.MODE_PRIVATE);

        //String test = "Mleko" + mainSplitSymbol + "2";
        //saveData(test);
        //test = "Chleb" + mainSplitSymbol + "1";
        //saveData(test);
        //test = "Jajka" + mainSplitSymbol + "1";
        //saveData(test);

        Integer i = 0; //Itereator po key produktach
        String resData = restoreData(i.toString());

        while (resData != null) //dopóki sa jakies produkty w pliku
        {
            String[] splitData = resData.split(mainSplitSymbol);
            productList.add(new Product(splitData[0], Integer.parseInt(splitData[1])));
            i++;
            resData = restoreData(i.toString());
        }

        fillTable(productList);

    }

    @Override
    protected void onStop(){
        super.onStop();
        setContentView(R.layout.activity_main);
        clearAllPreferences();

        for (Product p: productList
             ) {
            saveData(p.toStingWithMainSplitSymbol());
        }
    }

    public void buttonAddOnClick(View v)
    {
        EditText pName = (EditText) findViewById(R.id.editTextName);

        if(pName.getText().length() < 2) {
            Toast toast = Toast.makeText(getApplicationContext(), "Add name of product", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        Product p = new Product(pName.getText().toString(),1);
        productList.add(p);

        Toast toast = Toast.makeText(getApplicationContext(), "Product " + p.getNameProduct() + " was added", Toast.LENGTH_SHORT);
        toast.show();

        pName.setText("");

        fillTable(productList);
    }


    public void appendCellName(TableRow row, String cellText){

        TextView cell = new TextView(MainActivity.this);
        cell.setText(cellText);
        cell.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        cell.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1f));
        row.addView(cell);
    }

    public void appendCellQuan(TableRow row, final Product p){

        final EditText cell = new EditText(MainActivity.this);
        cell.setText(String.valueOf(p.getQuantityProduct()));
        cell.setInputType(InputType.TYPE_CLASS_NUMBER );
        cell.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        cell.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1f));

        cell.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if(!s.toString().equals(""))
                        p.setQuantityProduct(Integer.parseInt(s.toString()));
                }
                catch (Exception e){
                    cell.setText(p.getQuantityProduct());
                }
            }
        });
        row.addView(cell);
    }

    public void fillTable(List<Product> productList){

        //czyszczenie table
        int count = table.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = table.getChildAt(i);
                if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }
        table.removeAllViews();

        //kazdy uzytkownik to wiersz z 3 kolumnami
        for(Product p : productList){

            //stworzenie wiersza
            TableRow row = new TableRow(this);
            row.setPadding(10,10,10,10);

            //dodanie 2 kolumn
            appendCellName(row, p.getNameProduct());
            appendCellQuan(row, p);

            //dolaczenie do widoku wiersza z kolumnami
            table.addView(row);
        }
    }

    private static Integer key;
    public static void saveData(String inString) {
        key = 0;
        while(restoreData(key.toString()) != null) //dopoki sa jakies listy w pliku
            key++; // a co jak usuniesz cieciu

        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putString(key.toString(), inString);
        preferencesEditor.commit();
        key++;

    }

    public static String restoreData(String key) {
        return preferences.getString(key, null); //if null to nie ma takiej listy
    }

    public static void clearAllPreferences() {
        preferences.edit().clear().commit();
    }
}
