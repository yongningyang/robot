package me.yongning.mvvmframework.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * @author yongningyang@gmail.com
 * @date 2018/11/26
 * @Description
 */
public final class FrameworkContextEngineUtils {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private FrameworkContextEngineUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(@NonNull final Context context) {
        FrameworkContextEngineUtils.context = context.getApplicationContext();
        ScreenUtil.initScreen(context);
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) {
            return context;
        }
        throw new NullPointerException("should be initialized in application");
    }
}
