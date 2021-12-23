package github.lyl21.wanandroid.moudle.live.utils;

import android.content.Context;
import android.content.SharedPreferences;

import github.lyl21.wanandroid.moudle.live.Constants;


public class PrefManager {
    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
    }
}
