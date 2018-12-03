package me.yongning.mvvmframework.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;

import me.yongning.mvvmframework.utils.FrameworkContextEngineUtils;

/**
 * @author yongningyang@gmail.com
 * @date 2018/11/26
 * @Description
 */
public class BaseApplication extends Application implements IThemeIdGetter {
    private static Application sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        setApplication(this);
    }

    @Override
    public int getThemeId() {
        return 0;
    }

    @Override
    public void setThemeId(int resId) {

    }

    /**
     * 当主工程没有继承BaseApplication时，可以使用setApplication方法初始化BaseApplication
     * 此时必须实现IThemeIdGetter
     *
     * @param application
     */
    public static <T extends Application, IThemeIdGetter> void setApplication(@NonNull T application) {
        sInstance = application;
        //初始化工具类
        FrameworkContextEngineUtils.init(application);
        //注册监听每个activity的生命周期,便于堆栈式管理
        application.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                AppManager.getAppManager().addActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                AppManager.getAppManager().removeActivity(activity);
            }
        });
    }

    /**
     * 获得当前app运行的Application
     */
    public static Application getInstance() {
        if (sInstance == null) {
            throw new NullPointerException("please inherit BaseApplication or call setApplication.");
        }
        return sInstance;
    }
}
