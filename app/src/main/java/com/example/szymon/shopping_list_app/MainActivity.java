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
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.InterstitialAd;
import android.view.*;

public class MainActivity extends AppCompatActivity {

    private static SharedPreferences preferences;
    InterstitialAd mInterstitialAd;


    private static TableLayout table;
    private static List<Product> productList;
    private static String[] keywords;

    public static final String mainSplitSymbol = "#";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //getWindow().setSoftInputMode(       //do usuwanie focusu, nie działa
        //        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

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

        TopAd();
        BottomAd();
        VideoAd();
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

    public void VideoAd()
    {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-2288690960483253/8861617728");

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });
        requestNewInterstitial();
    }
    public void TopAd()
    {
        MobileAds.initialize(getApplicationContext(), getString(R.string.app_ad_id));

        AdView mAdViewTop = (AdView) findViewById(R.id.adViewTop);

        if (!BuildConfig.DEBUG) {
            AdRequest adRequest = new AdRequest.Builder()
                    //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice("D60FE1B7E027ED6351991CD32F5D9841")
                    .build();
            mAdViewTop.loadAd(adRequest);
        } else {
            AdRequest adRequest = new AdRequest.Builder()
                    .addKeyword("shopping").build();
            mAdViewTop.loadAd(adRequest);
        }
    }

    public void BottomAd()
    {
        MobileAds.initialize(getApplicationContext(), getString(R.string.app_ad_id));

        AdView mAdViewBottom = (AdView) findViewById(R.id.adViewBottom);

        keywords = new String[productList.size()];
        Integer j = 0;
        for (Product p: productList
                ) {
            keywords[j] = p.getNameProduct();
            j++;
        }

        Set<String> set = new HashSet<String>();
        int count = 0;
        while (count < keywords.length) {
            set.add(keywords[count]);
            count++;
        }

        if (!BuildConfig.DEBUG) {
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice("D60FE1B7E027ED6351991CD32F5D9841")
                    .build();
            mAdViewBottom.loadAd(adRequest);
        } else {
            AdRequest adRequest = new AdRequest.Builder()
                    .addKeyword(set.toString().replace('[',' ').replace(']',' ')).build();
            mAdViewBottom.loadAd(adRequest);
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
        
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .setBirthday(new GregorianCalendar(1990, 1, 1).getTime())
                .addKeyword("Shopping").addKeyword("Shop")
                .build();

        mInterstitialAd.loadAd(adRequest);
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
        //cell.clearFocus();
        //cell.setFocusable(true);
        //cell.setFocusableInTouchMode(true);


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
                    if(p.getQuantityProduct() == 0){
                        //zapisać produkt z ilością 0
                        //wywołać filltable
                        //i wewnatrz filltable, nie dodac jesli jest 0

                        productList.remove(p);
                        fillTable(productList);
                    }
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
            if(p.getQuantityProduct() != 0) {
                //stworzenie wiersza
                TableRow row = new TableRow(this);
                row.setPadding(10, 10, 10, 10);

                //dodanie 2 kolumn
                appendCellName(row, p.getNameProduct());
                appendCellQuan(row, p);

                //dolaczenie do widoku wiersza z kolumnami
                table.addView(row);
            }
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
