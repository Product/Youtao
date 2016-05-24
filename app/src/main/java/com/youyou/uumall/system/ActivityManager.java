package com.youyou.uumall.system;

import android.app.Activity;


import com.youyou.uumall.utils.MyLogger;

import java.util.Stack;


public class ActivityManager {
    private MyLogger log = MyLogger.getLogger("ActivityManager");
    private static Stack<Activity> activityStack;
    private static ActivityManager instance;

    private ActivityManager() {
    }

    public static ActivityManager getActivityManager() {
        if (instance == null) {
            instance = new ActivityManager();
        }
        return instance;
    }

    //获取栈顶Activity 
    public void popActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
        }
    }

    //获得当前栈顶Activity 
    public Activity currentActivity() {
        Activity activity = null;
        if (!activityStack.empty())
            activity = activityStack.lastElement();
        return activity;
    }

    //将当前Activity推入栈中 
    public void pushActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        for (int i = 0; i < activityStack.size(); i++) {
            Activity ac = activityStack.get(i);
            String className = ac.getClass().getName();
            if (activity.getClass().getName().equals(className)) {
                activityStack.remove(i);
                ac = null;
                break;
            }
        }
        activityStack.add(activity);
    }

    //从栈中移除所有Activity除开指定的activity 
    public void popAllActivityExceptOne(Class<Object> cls) {

        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            if (activity.getClass().equals(cls)) {
                break;
            }
            popActivity(activity);
        }
    }

    public void finishAllActivity() {

        for (int i = 0; i < activityStack.size(); i++) {
            Activity ac = activityStack.get(i);
            if (!ac.isFinishing()) {
                ac.finish();
            }

        }
        activityStack.clear();
    }

    //移除所有的
    public void popAllActivity() {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            popActivity(activity);
        }
    }

    public void logout(){
        for (int i = 0; i < activityStack.size(); i++) {
            Activity ac = activityStack.get(i);
            String className = ac.getClass().getName();
            if (!className.equals("com.youyoumob.xp.ui.SceneActivity_")) {
                activityStack.remove(i);
                if (!ac.isFinishing()) {
                    ac.finish();
                }
            }
        }
    }

    public Activity backToLastActivity() {
        Activity activity = currentActivity();
        if (activity != null) {
            activityStack.remove(activity);
        }
        return currentActivity();
    }

    public void read() {
        for (int i = 0; i < activityStack.size(); i++) {
            log.i(activityStack.get(i).getClass().getName());
        }
    }
}
