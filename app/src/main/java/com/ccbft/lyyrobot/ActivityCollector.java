package com.ccbft.lyyrobot;

import android.app.Activity;
import android.os.Process;

import java.util.ArrayList;

public class ActivityCollector {
    public static ArrayList<Activity> list=new ArrayList<>();
    public static void addActivity(Activity activity){
        list.add(activity);
    }
    public static void removeActivity(Activity activity){
        list.remove(activity);
    }
    public static void finishAll(){
        for (Activity activity : list) {
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
        list.clear();
        Process.killProcess(Process.myPid());//杀掉当前进程

    }
}
