package com.example.szymon.shopping_list_app;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    //Zmienne
    private static SharedPreferences preferences; //Miejsce zapisu danych o produktach
    private static Integer key; //Klucz do pliku z produktami
    InterstitialAd mInterstitialAd; //Zmienna dla VideoAd
    private static String[] keywords; //Zmiannna dla BottomAd
    private static TableLayout table; //W layoucie miejsce na produkty
    private static List<Product> productList; //Kolekcja obiektów
    public static final String mainSplitSymbol = "#"; //Zmienna do zapisu produktu w pliku

    /////////////////////////////////////////////////////////////////////////////////////////////

    //Deklaracja onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inicjalizacja zmiennych
        table = (TableLayout) findViewById(R.id.TableProducts);
        productList = new ArrayList<Product>();

        //Inicjalizacja pliku z produktami
        String FileName = "dbTestSL";
        preferences = getSharedPreferences(FileName, Activity.MODE_PRIVATE);

        //Pobranie całej zawartości
        restoreAllPreferences();

        //Wyświetlenie produktów
        fillTable();

        //Utworzenie reklam - główne zadanie projektu
        TopAd(); //Reklama górna - zależna od ostatnio dodanego produktu
        BottomAd(); //Rekalma dolna - zależna od wszystkich dodanych na liste produktów
        VideoAd(); //Reklama wideo - zależna od ostatnio dodanego produktu
    }

    //Deklaracja onRestart
    @Override
    protected void onRestart() {
        super.onRestart();
        setContentView(R.layout.activity_main);

        //Pobranie całej zawartości
        restoreAllPreferences();

        //Wyświetlenie produktów
        fillTable();

        TopAd(); //Reklama górna - zależna od ostatnio dodanego produktu
        BottomAd(); //Rekalma dolna - zależna od wszystkich dodanych na liste produktów
        VideoAd(); //Reklama wideo - zależna od ostatnio dodanego produktu
    }

    //Deklaracja onStop
    @Override
    protected void onStop(){
        super.onStop();
        setContentView(R.layout.activity_main);
        saveAllPreferences();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////

    //Deklaracja VideoAd
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

    //Deklaracja TopAd
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
                    .addKeyword(getKeyWord()) //Ustalenie zawartości reklamy
                    .build();
            mAdViewTop.loadAd(adRequest);
        }
    }

    //Deklaracja BottomAd
    public void BottomAd()
    {
        MobileAds.initialize(getApplicationContext(), getString(R.string.app_ad_id));

        AdView mAdViewBottom = (AdView) findViewById(R.id.adViewBottom);

        //Pobranie nazw produtków
        keywords = new String[productList.size()];
        Integer j = 0;
        for (Product p: productList
                ) {
            keywords[j] = p.getNameProduct();
            j++;
        }

        //Ustelenie zawartości obiektu typu Set dla rekalmy
        Set<String> set = new HashSet<String>();
        int count = 0;
        while (count < keywords.length) {
            set.add(keywords[count]);
            count++;
        }

        if (!BuildConfig.DEBUG) {
            AdRequest adRequest = new AdRequest.Builder()
                    //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice("D60FE1B7E027ED6351991CD32F5D9841")
                    .build();
            mAdViewBottom.loadAd(adRequest);
        } else {
            AdRequest adRequest = new AdRequest.Builder()
                    .addKeyword(set.toString().replace('[',' ').replace(']',' ')).build(); //Ustalenie zawartości reklamy
            mAdViewBottom.loadAd(adRequest);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////

    //Deklaracja requestNewInterstitial - wykorzystywane przez VideoAd
    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .setBirthday(new GregorianCalendar(1990, 1, 1).getTime())
                .addKeyword(getKeyWord())
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    //Deklaracja buttonAddOnClick - po kliknięciu w przycisk
    public void buttonAddOnClick(View v)
    {
        //przypisanie zmiennej w celu pobrania zawasrtości
        EditText pName = (EditText) findViewById(R.id.editTextName);

        //Nazwa nie może być krótsza niż 2
        if(pName.getText().length() < 2) {
            showToast("Add correct name of product");
            return;
        }
        //inicjalizacja nowego produktu i dodanie go na liste
        Product p = new Product();

        if(typeOfFormat(pName.getText().toString())) {
            if(!isOnList(pName.getText().toString().split("x",2)[1].toUpperCase())){
                p.setNameProduct(pName.getText().toString().split("x",2)[1]);
                p.setQuantityProduct(Integer.parseInt(pName.getText().toString().split("x",2)[0]));
            }else {
                showToast("Product " + pName.getText().toString().split("x",2)[1].toUpperCase() + " exist on the list");
                pName.setText("");
                return;
            }
        }else {
            if(!isOnList(pName.getText().toString().toUpperCase())){
                p.setNameProduct(pName.getText().toString());
                p.setQuantityProduct(1);
            }else {
                showToast("Product " + pName.getText().toString().toUpperCase() + " exist on the list");
                pName.setText("");
                return;
            }
        }

        productList.add(p);

        //czyszczenie zaswartosci editTextName
        pName.setText("");

        //Aktualizacja zawasrości wyświetlanej listy
        fillTable();

        showToast("Product " + p.getNameProduct() + " was added");

        //Rekalama wideo
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }
    }

    //Deklaracja buttonClearAllOnClick - czyszczenie calej listy
    public void buttonClearAllOnClick(View v)
    {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        productList.clear();
                        fillTable();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage("Clear a list?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    //Deklaracja typeOfFormat - 5xChleb czy Chleb
    public boolean typeOfFormat(String s) {
        try {
            String[] sSplit = s.split("x",2);
            Integer.parseInt(sSplit[0]);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    //Deklaracja showToast - wiadomość dla uzytkownika
    public void showToast(String msg)
    {
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    //Deklaracja getKeyWord - pobieranie słowa kluczowego dla rekalam
    public String getKeyWord()
    {
        if(productList.size() > 1)
            return productList.get(productList.size()-1).getNameProduct();
        else
            return "shopping,food";
    }

    /////////////////////////////////////////////////////////////////////////////////////////////

    //Deklaracja appendCellName - wykorzystywane przez fillTable, część: Nazwa produktów
    public void appendCellName(TableRow row, String cellText){

        TextView cell = new TextView(MainActivity.this);
        cell.setText(cellText);
        cell.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        cell.setLayoutParams(new TableRow.LayoutParams(1, TableRow.LayoutParams.MATCH_PARENT, 0.6f));
        cell.setTextSize(20);
        cell.setFocusableInTouchMode(true);
        row.addView(cell);
    }

    //Deklaracja appendCellQuan - wykorzystywane przez fillTable, część: Ilość produktów
    public void appendCellQuan(TableRow row, final Product p){

        //Tworzenie obiektu do edycji ilości produktu
        final EditText cell = new EditText(MainActivity.this);
        cell.setText(String.valueOf(p.getQuantityProduct()));
        cell.setInputType(InputType.TYPE_CLASS_NUMBER );
        cell.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        cell.setLayoutParams(new TableRow.LayoutParams(1, TableRow.LayoutParams.MATCH_PARENT, 0.4f));
        cell.setTextSize(20);
        cell.setFocusableInTouchMode(true);

        //Dodanie listenera do obsługi zmiany ilości produktu
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
                        p.setQuantityProduct(Integer.parseInt(s.toString())); //ustawienie ilości produktu

                    if(p.getQuantityProduct() == 0){
                        productList.remove(p); //usunięcie produktu z powodu ilości równej 0
                        fillTable(); //aktualizacja tablicy produktów
                    }
                }
                catch (Exception e){
                    cell.setText(p.getQuantityProduct());
                }
            }
        });
        //Dodanie wiersza z produktem
        row.addView(cell);
    }

    //Deklaracja fillTable - ustawienie wyświtetlanej zawartości
    public void fillTable(){

        //czyszczenie table
        int count = table.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = table.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }
        table.removeAllViews();

        //kazdy produkt to wiersz z 2 kolumnami
        for(Product p : productList){
            if(p.getQuantityProduct() != 0) {
                //stworzenie wiersza
                TableRow row = new TableRow(this);
                row.setPadding(5,5,5,5);

                //dodanie 2 kolumn
                appendCellName(row, p.getNameProduct());
                appendCellQuan(row, p);

                row.setFocusableInTouchMode(true);
                //dolaczenie do widoku wiersza z kolumnami
                table.addView(row);
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////

    //Deklaracaja saveData - zapis produktu na koniec pliku
    public void saveData(String inString) {
        key = 0;
        while(restoreData(key.toString()) != null) //dopoki sa jakies listy w pliku
            key++;

        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putString(key.toString(), inString);
        preferencesEditor.commit();
    }

    //Deklaracja restoreData - odczyt produktu z pliku na podstawie klucza
    public String restoreData(String key) {
        return preferences.getString(key, null); //if null to nie ma takiego produktu
    }

    //Deklaracja clearAllPreferences - usunięcie zawartości pliku
    public void clearAllPreferences() {
        preferences.edit().clear().commit();
    }

    //Deklaracja saveAllPrederences - zapisanie zawartości do pliku
    public void saveAllPreferences() {
        clearAllPreferences();

        //Zapis nowej zawartości do pliku
        for (Product p: productList) {
            saveData(p.toStingWithMainSplitSymbol());
        }
        showToast("List was saved");
    }

    //Deklaracja restoreAllPreferences - pobranie wszsytkich produktów z pliku i dodanie ich na czystą liste
    public void restoreAllPreferences() {
        productList.clear();

        //Rozpoczęcie pobierania z pliku
        Integer i = 0; //Itereator po key produktach
        String resData = restoreData(i.toString());

        while (resData != null){ //dopóki sa jakies produkty w pliku
            String[] splitData = resData.split(mainSplitSymbol);
            productList.add(new Product(splitData[0], Integer.parseInt(splitData[1])));
            i++;
            resData = restoreData(i.toString());
        }
    }

    //Deklaracja isOnList - false gdy nie ma takiego produktu na liscie
    public boolean isOnList(String s) {
        for (Product p: productList) {
            if(p.getNameProduct().equals(s))
                return true;
        }
        return false;
    }
}
