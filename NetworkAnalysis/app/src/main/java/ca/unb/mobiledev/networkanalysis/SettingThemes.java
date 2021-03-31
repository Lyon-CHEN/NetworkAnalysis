package ca.unb.mobiledev.networkanalysis;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class SettingThemes extends AppCompatActivity {
    ListView listView;
    private String[] themes = {"Purple&Green", "Blue&White", "Orange&Black"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_setting_themes);

        listView = findViewById(R.id.theme_lv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, themes);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String lang = themes[i];
                switch (i){
                    case 0:
                        changeAppTheme();
                    case 1:
                        changeAppTheme();
                    case 2:
                        changeAppTheme();
                }
                Toast.makeText(SettingThemes.this ,lang,Toast.LENGTH_LONG).show();
            }
        });
    }

    public void changeAppTheme() {
        finish();
        return ;
    }
}
