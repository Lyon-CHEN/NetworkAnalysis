package ca.unb.mobiledev.networkanalysis;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{
    private ImageButton button_wifi, button_search, button_test, button_fixer, button_setting;
    enum Scope
    {
        WIFI, SEARCH, TEST, FIXER, SETTING;
    }
    //Scope: Wifi:0, Search:1, Test:2, Fixer:3, Setting:4, Default scope 0 wifi
    private static Scope currentScope = Scope.WIFI;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //Initialize fragmentManager and add Transaction
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.layout_center, new WifiFragment()).commit();

        //Action bar hide
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //apply settings saved before
        SharedPreferenceHelper sharedPreferenceHelper = new SharedPreferenceHelper(getApplicationContext());

        switch (sharedPreferenceHelper.read("theme")){
            case "theme1":
                setTheme(R.style.Theme1);
                break;
            case "theme2":
                setTheme(R.style.Theme2);
                break;
        }

        //Initialize buttons and assign listeners
        button_wifi = findViewById(R.id.button_wifi);
        button_search = findViewById(R.id.button_search);
        button_test = findViewById(R.id.button_test);
        button_fixer = findViewById(R.id.button_fixer);
        button_setting =findViewById(R.id.button_setting);
        button_wifi.setImageResource(R.drawable.icon_wifi72_inv);
        button_wifi.setOnClickListener(v -> {
            if(currentScope != Scope.WIFI){
                refreshImageButtons();
                button_wifi.setImageResource(R.drawable.icon_wifi72_inv);
                fragmentManager.beginTransaction().replace(R.id.layout_center, new WifiFragment()).commit();
                currentScope = Scope.WIFI;
            }
        });
        button_search.setOnClickListener(v -> {
            if(currentScope != Scope.SEARCH){
                refreshImageButtons();
                button_search.setImageResource(R.drawable.icon_search72_inv);
                fragmentManager.beginTransaction().replace(R.id.layout_center, new SearchFragment()).commit();
                currentScope = Scope.SEARCH;
            }
        });
        button_test.setOnClickListener(v -> {
            if(currentScope != Scope.TEST){
                refreshImageButtons();
                button_test.setImageResource(R.drawable.icon_timer72_inv);
                fragmentManager.beginTransaction().replace(R.id.layout_center, new TestFragment()).commit();
                currentScope = Scope.TEST;
            }
        });
        button_fixer.setOnClickListener(v -> {
            if(currentScope != Scope.FIXER){
                refreshImageButtons();
                button_fixer.setImageResource(R.drawable.icon_repairkit72_inv);
                fragmentManager.beginTransaction().replace(R.id.layout_center, new FixerFragment()).commit();
                currentScope = Scope.FIXER;
            }
        });
        button_setting.setOnClickListener(v -> {
            if(currentScope != Scope.SETTING){
                refreshImageButtons();
                button_setting.setImageResource(R.drawable.icon_setting72_inv);
                fragmentManager.beginTransaction().replace(R.id.layout_center, new SettingFragment()).commit();
                currentScope = Scope.SETTING;
            }
        });

        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 100);
    }

    private void refreshImageButtons(){
        if (currentScope == Scope.FIXER){
            button_fixer.setImageResource(R.drawable.icon_repairkit72);
        }
        if (currentScope == Scope.WIFI){
            button_wifi.setImageResource(R.drawable.icon_wifi72);
        }
        if (currentScope == Scope.SETTING){
            button_setting.setImageResource(R.drawable.icon_setting72);
        }
        if (currentScope == Scope.SEARCH){
            button_search.setImageResource(R.drawable.icon_search72);
        }
        if (currentScope == Scope.TEST){
            button_test.setImageResource(R.drawable.icon_timer72);
        }
    }

}