package ca.unb.mobiledev.networkanalysis;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SettingFragment extends Fragment {
    private final static String TAG = "SettingFragment";

    private CheckBox auto_update_checkbox;
    private TextView auto_update_tv, language_tv, theme_tv, notification_tv;
    private RelativeLayout auto_update_rl, language_rl, theme_rl, notification_rl;
    private String[] languages = {"English", "简体中文", "日本語"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        auto_update_checkbox = view.findViewById(R.id.auto_update_checkbox);
        auto_update_rl = view.findViewById(R.id.setting_auto_update);
        language_rl = view.findViewById(R.id.setting_language);
        theme_rl = view.findViewById(R.id.setting_theme);
        notification_rl = view.findViewById(R.id.setting_notification);


//        FragmentManager fragmentManager = getChildFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();



        language_rl.setOnClickListener(v -> {
            Toast.makeText(view.getContext(), "YOSSDSDA",Toast.LENGTH_SHORT).show();
            Intent languageIntent = new Intent( getContext(), SettingLanguages.class);
            startActivity(languageIntent);
        });



        return view;
    }
}