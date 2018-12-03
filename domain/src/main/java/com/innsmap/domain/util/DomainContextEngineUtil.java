package com.innsmap.domain.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.innsmap.data.http.RetrofitClient;
import com.innsmap.data.util.DataContextEngineUtil;
import com.innsmap.domain.ConstantValue;
import com.innsmap.domain.R;
import com.innsmap.domain.RobotManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yongningyang@gmail.com
 * @date 2018/11/27
 * @Description
 */
public final class DomainContextEngineUtil {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private DomainContextEngineUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(@NonNull final Context context) {
        DomainContextEngineUtil.context = context.getApplicationContext();
        DataContextEngineUtil.init(context);
        Map<String, String> domainHeaders = new HashMap<>();
        domainHeaders.put(ConstantValue.HEADER_LOGIN_URL_DOMAIN_NAME, ConstantValue.LOGIN_URL);
        domainHeaders.put(ConstantValue.HEADER_DATA_URL_DOMAIN_NAME, ConstantValue.DATA_URL);
        RetrofitClient.init(context, ConstantValue.BASE_URL, null, domainHeaders);

        Resources mResources = context.getResources();
        ConstantValue.busName = mResources.getStringArray(R.array.bus_name);
        ConstantValue.busNameD = mResources.getStringArray(R.array.bus_name_d);
        ConstantValue.pid = mResources.getIntArray(R.array.bus_pid);
        ConstantValue.busNameShort = mResources.getStringArray(R.array.bus_name_short);
        ConstantValue.pidShort = mResources.getIntArray(R.array.bus_pid_short);

        RobotManager.getInstance().init(context);
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
