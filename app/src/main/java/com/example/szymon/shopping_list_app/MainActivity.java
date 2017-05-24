package com.example.szymon.shopping_list_app;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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
        index = 0;

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

        for (Product p: productList) {
            addProductToTable(p);
        }

    }

    //Dodawanie produktu do tabeli
    public static int x = 0;
    public static int index;
    public void addProductToTable(final Product p)
    {
        TableRow row = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams
                (TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT);
        row.setLayoutParams(lp);

        TextView name= new TextView(this);
        EditText quantity = new EditText(this);

        name.setText(p.getNameProduct());
        name.setTextSize(20);
        name.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        quantity.setText(String.valueOf(p.getQuantityProduct()));
        quantity.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


        quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("")) {
                    x = Integer.parseInt(s.toString());
                    p.setQuantityProduct(x);
                }
                Toast toast = Toast.makeText(getApplicationContext(),
                        "p.name = " + p.getNameProduct() + ", p.q= " + p.getQuantityProduct(), Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        row.addView(name);
        row.addView(quantity);
        row.setMinimumHeight(15);
        row.setMinimumWidth(15);
        row.setPadding(5,5,5,5);;
        table.addView(row,index);
        index++;
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
}
