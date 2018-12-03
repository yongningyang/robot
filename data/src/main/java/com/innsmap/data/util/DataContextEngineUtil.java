package com.innsmap.data.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * @author yongningyang@gmail.com
 * @date 2018/11/27
 * @Description
 */
public final class DataContextEngineUtil {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private DataContextEngineUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(@NonNull final Context context) {
        DataContextEngineUtil.context = context.getApplicationContext();
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
