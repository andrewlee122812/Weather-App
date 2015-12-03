package com.randomname.vlad.weathertest.Util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.HashMap;

public class Misc {
    private static HashMap<String, Integer> imageResourcesHashMap = new HashMap<>();


    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static int getImageResource(String uri, Context context) {
        uri = "drawable/d_" + uri;

        Integer result = imageResourcesHashMap.get(uri);

        if (result != null) {
            return result;
        } else {
            int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
            imageResourcesHashMap.put(uri, imageResource);
            return imageResource;
        }
    }
}
