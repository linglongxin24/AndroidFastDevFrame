package cn.bluemobi.dylan.base.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import java.util.ArrayList;
import java.util.Stack;

/**
 * activity 管理类
 *
 * @author dylan
 */
public class AppManager {

    private static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager() {
        activityStack = new Stack<Activity>();
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    public Stack<Activity> getAllActivitys() {
        return activityStack;
    }

    /**
     * 栈中是否为空
     *
     * @return
     */
    public boolean isEmpty() {
        return activityStack.isEmpty();
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        if (activityStack.isEmpty()) {
            return null;
        }
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 获取前一个activity，便于返回
     *
     * @return
     */
    public Activity lastActivity() {
        if (activityStack.size() < 2) {
            return null;
        }
        return activityStack.get(activityStack.size() - 1);
    }

    /**
     * 获取前一个activity，便于返回
     *
     * @return
     */
    public Activity previousActivity() {
        if (activityStack.size() < 2) {
            return null;
        }
        return activityStack.get(activityStack.size() - 2);
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        if (activityStack.empty()) {
            return;
        }
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 从栈中移除指定的Activity
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            if (activityStack.remove(activity)) {
                activity.finish();
            }
        }
        // 隐藏上一个页面的键盘
        /*
         * if(activityStack.size() > 0){
		 * UIUtils.alwaysHideSoftKeyboard(activityStack.peek()); }
		 */
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public <T extends Activity> T getActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                return (T) activity;
            }
        }
        return null;
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        ArrayList<Activity> activityList = new ArrayList<Activity>(
                activityStack);
        for (int i = 0, size = activityList.size(); i < size; i++) {
            if (null != activityList.get(i)) {
                activityList.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 结束所有Activity保留主界面
     */
    public void finishAllActivityExcludeMain() {

        int stackSize = activityStack.size();

        if (stackSize >= 1) {

            ArrayList<Activity> activityList = new ArrayList<Activity>(
                    activityStack.subList(1, stackSize));

            for (int i = 0, size = activityList.size(); i < size; i++) {

                Activity activity = activityList.get(i);

                if (activity != null) {

                    activity.finish();

                    activityStack.remove(activity);
                }
            }
        }
    }

    /**
     * 结束所有Activity保留登录界面
     */
    public void finishAllActivityExcludeLogin() {
        ArrayList<Activity> activityList = new ArrayList<Activity>(
                activityStack);
        for (int i = 0, size = activityList.size(); i < size; i++) {
            Activity activity = activityList.get(i);
            if (null != activityList.get(i)) {
                if (!activityList.get(i).getClass().getSimpleName().equals("LoginActivity")) {
                    activityList.get(i).finish();
                    activityStack.remove(activity);
                }
            }
        }
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
//            MyApplication.getInstance().unregisterReceiver();
            /*
             * ActivityManager activityMgr= (ActivityManager)
			 * context.getSystemService(Context.ACTIVITY_SERVICE);
			 * activityMgr.restartPackage(context.getPackageName());
			 */
            // System.exit(0);

        } catch (Exception e) {
        }
    }

    /**
     * 退出应用程序并停止服务
     */
    public void AppExit(Context context, Intent intent) {
        try {
            context.stopService(intent);
            finishAllActivity();
        } catch (Exception e) {
        }
    }

    public void dataReset(Context context) {
//        UserInfo.getInstance(context).clear();
//        MyApplication.getInstance().getLockPatternUtils().clearLock();
//        GesturesPassword.getInstance(context).clear();
////                    ajax(RequestURl.logoutURl, null);
//        DbUtils db = DbUtils.create(context);
//        try {
//            db.deleteAll(Menu_Top.class);
//            db.deleteAll(Menu_Center.class);
//            db.deleteAll(News.class);
//            db.deleteAll(Notice.class);
//            db.deleteAll(PhoneList.class);
//            db.deleteAll(Project.class);
//            db.deleteAll(ProjectPersons.class);
//            db.deleteAll(ProjectPlan.class);
//            db.deleteAll(Leave.class);
//            db.deleteAll(WeekPlans.class);
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
    }
}