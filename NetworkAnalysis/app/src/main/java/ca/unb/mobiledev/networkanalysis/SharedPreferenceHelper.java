package ca.unb.mobiledev.networkanalysis;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceHelper {

        private Context mContext;

        public SharedPreferenceHelper(Context mContext) {
            this.mContext = mContext;
        }


        //输入data和key保存
        public void save(String data, String key) {
            SharedPreferences sp = mContext.getSharedPreferences("mysp", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(data, key);
            editor.commit();
        }

        //用key读取
        public String read(String key){
            SharedPreferences sp = mContext.getSharedPreferences("mysp", Context.MODE_PRIVATE);
            return sp.getString(key, "");
        }
}
