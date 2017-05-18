package com.example.a97557.movie;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 97557 on 2017/5/17.
 */

//关闭Activity的类
public class CloseActivityClass{
    public static List<Activity> activityList = new ArrayList<Activity>();
    public static void exitClient(Context ctx) {
        for (int i = 0; i < activityList.size(); i++){
            if (null != activityList.get(i)) {
                activityList.get(i).finish();
            }
        }
    }
}
