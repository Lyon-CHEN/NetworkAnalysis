package ca.unb.mobiledev.networkanalysis;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button button_wifi_list, button_search, button_speed_test, button_fixer, button_setting;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_main);
        button_wifi_list.findViewById(R.id.button_wifi_list);
        button_search.findViewById(R.id.button_search);
        button_speed_test.findViewById(R.id.button_speed_test);
        button_fixer.findViewById(R.id.button_fixer);
        button_setting.findViewById(R.id.button_setting);

       // button_wifi_list.setOnClickListener(v -> {
       //     Intent switch_wifi_list = new Intent(this ,WifiActivity.class);
        // });
    }

}