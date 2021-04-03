package ca.unb.mobiledev.networkanalysis;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SettingFragment extends Fragment {
    private final static String TAG = "SettingFragment";

    private RelativeLayout setting_rl;
    private ListView setting_lv;
    private SettingViewModel mSettingViewModel;
    ArrayAdapter<String> adapter;
    Activity currentActivity;
    Window currentWindow;
    LinearLayout main_lv;
    ImageButton button_wifi, button_search, button_test, button_fixer, button_setting;
    SharedPreferenceHelper sharedPreferenceHelper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        setting_lv = view.findViewById(R.id.setting_lv);
        setting_rl = view.findViewById(R.id.setting_rl);
        currentActivity = getActivity();
        mSettingViewModel = new ViewModelProvider(requireActivity()).get(SettingViewModel.class);
        sharedPreferenceHelper = new SharedPreferenceHelper(getContext());

        mSettingViewModel.getCurrentScope().observe(getViewLifecycleOwner(), num->{
            String[] list = null;
            switch (num){
                case 0: list = mSettingViewModel.options;break;
                case 1: list = mSettingViewModel.languages;break;
                case 2: list = mSettingViewModel.notifications;break;
                case 3: list = mSettingViewModel.themes;break;
            }
            adapter = new ArrayAdapter<String>(
                    getContext(), android.R.layout.simple_list_item_1, list
            );
            setting_lv.setAdapter(adapter);


            setClickListener();
//            switch (num){
//                case 0: Toast.makeText(getContext(),"0", Toast.LENGTH_LONG).show();break;
//                case 1: Toast.makeText(getContext(),"1", Toast.LENGTH_LONG).show();break;
//                case 2: Toast.makeText(getContext(),"2", Toast.LENGTH_LONG).show();break;
//                case 3: Toast.makeText(getContext(),"3", Toast.LENGTH_LONG).show();break;
//            }
         //   Toast.makeText(getContext(),"changed", Toast.LENGTH_LONG).show();


        });

        main_lv = getActivity().findViewById(R.id.main_content);
        currentWindow = currentActivity.getWindow();
        button_wifi = getActivity().findViewById(R.id.button_wifi);
        button_search = getActivity().findViewById(R.id.button_search);
        button_test = getActivity().findViewById(R.id.button_test);
        button_setting = getActivity().findViewById(R.id.button_setting);
        button_fixer = getActivity().findViewById(R.id.button_fixer);



        mSettingViewModel.getThemeNum().observe(getViewLifecycleOwner(), num->{
            switch (num){
                case 1: Theme_One();break;
                case 2: Theme_Two();break;
            }
        });

        return view;
    }

    public void setClickListener(){
        setting_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //    Toast.makeText(getContext(), "111", Toast.LENGTH_LONG).show();
                mSettingViewModel.changeScope(position+1);
            }
        });
    }

    public void Theme_One(){
        button_wifi.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.menu_button_bg1));
        button_fixer.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.menu_button_bg1));
        button_setting.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.menu_button_bg1));
        button_test.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.menu_button_bg1));
        button_search.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.menu_button_bg1));
        main_lv.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.main_bg1));
        getActivity().setTheme(R.style.Theme1);
        currentWindow.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.theme_purple));
        sharedPreferenceHelper.save("theme1", "theme");
    }

    public void Theme_Two(){
        button_wifi.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.menu_button_bg2));
        button_fixer.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.menu_button_bg2));
        button_setting.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.menu_button_bg2));
        button_test.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.menu_button_bg2));
        button_search.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.menu_button_bg2));
        main_lv.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.main_bg2));
        getActivity().setTheme(R.style.Theme2);
        currentWindow.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.theme_blue));

        sharedPreferenceHelper.save("theme2", "theme");
    }

}