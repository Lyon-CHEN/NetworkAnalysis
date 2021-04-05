package ca.unb.mobiledev.networkanalysis;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.Locale;

public class SettingViewModel extends AndroidViewModel {
    private MutableLiveData<Integer> theme_num;
    private MutableLiveData<Integer> currentScope;
    private int subSelect = 0;
    Context main;
    String[] options;
    String[] languages = {"English", "简体中文"};
    String[] notifications = {"On", "Off"};
    String[] themes;

    public SettingViewModel(@NonNull Application application) {
        super(application);

        main = application.getApplicationContext();
        currentScope = new MutableLiveData<Integer>();
        theme_num = new MutableLiveData<Integer>();
        currentScope.setValue(0);
        theme_num.setValue(0);
        options = new String[]{main.getString(R.string.setting_language), main.getString(R.string.setting_notifications), main.getString(R.string.setting_theme)};
        themes = new String[]{main.getString(R.string.setting_theme_one), main.getString(R.string.setting_theme_two)};
    }

    public void changeScope(int i){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(currentScope.getValue()==0){
                    currentScope.setValue(i);
                    subSelect = i;
                }else{
                    handleSetting(i);
                }
            }
        },100);
    }

    public void resetScope(){
        currentScope.setValue(0);
    }

    public void handleSetting(int i){
        switch (subSelect){
            case 1:changeLanguages(i);break;
            case 2:changeNotifications(i);break;
            case 3:theme_num.setValue(i);break;
        }
        resetScope();
        //Toast.makeText(main, "Changed", Toast.LENGTH_SHORT).show();
    }

    public MutableLiveData<Integer> getCurrentScope(){
        return currentScope;
    }

    public MutableLiveData<Integer> getThemeNum(){
        return theme_num;
    }

    public void changeLanguages(int i){
        Resources resources = main.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        switch (i){
            case 1:
                config.locale = Locale.ENGLISH;
                resources.updateConfiguration(config, dm);

                break;
            case 2:
                config.locale = Locale.TRADITIONAL_CHINESE;
                resources.updateConfiguration(config, dm);
                break;
        }
        Intent intent = new Intent(main, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        main.startActivity(intent);
        // 杀掉进程
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);

    }

    public void changeNotifications(int i){

    }
}
