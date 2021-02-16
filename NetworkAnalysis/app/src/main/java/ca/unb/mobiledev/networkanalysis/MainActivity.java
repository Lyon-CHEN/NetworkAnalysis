package ca.unb.mobiledev.networkanalysis;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button button_wifi, button_search, button_test, button_fixer, button_setting;
    //Scope: Wifi:0, Search:1, Test:2, Fixer:3, Setting:4, Default scope 0 wifi
    private int Scope = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize fragmentManager and add Transaction
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.layout_center, new WifiFragment()).commit();

        //Initialize buttons and assign listeners
        button_wifi = findViewById(R.id.button_wifi);
        button_search = findViewById(R.id.button_search);
        button_test = findViewById(R.id.button_test);
        button_fixer = findViewById(R.id.button_fixer);
        button_setting =findViewById(R.id.button_setting);
        button_wifi.setOnClickListener(v -> {
            if(Scope != 0){
                fragmentManager.beginTransaction().replace(R.id.layout_center, new WifiFragment()).commit();
                Scope = 0;
            }
        });
        button_search.setOnClickListener(v -> {
            if(Scope != 1){
                fragmentManager.beginTransaction().replace(R.id.layout_center, new SearchFragment()).commit();
                Scope = 1;
            }
        });
        button_test.setOnClickListener(v -> {
            if(Scope != 2){
                fragmentManager.beginTransaction().replace(R.id.layout_center, new TestFragment()).commit();
                Scope = 2;
            }
        });
        button_fixer.setOnClickListener(v -> {
            if(Scope != 3){
                fragmentManager.beginTransaction().replace(R.id.layout_center, new FixerFragment()).commit();
                Scope = 3;
            }
        });
        button_setting.setOnClickListener(v -> {
            if(Scope != 4){
                fragmentManager.beginTransaction().replace(R.id.layout_center, new SettingFragment()).commit();
                Scope = 4;
            }
        });
    }

}