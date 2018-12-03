package com.innsmap.guomaorobot.app;

import android.databinding.ObservableBoolean;
import android.graphics.BitmapFactory;

import com.innsmap.domain.RobotHeartbeatListener;
import com.innsmap.domain.RobotManager;
import com.innsmap.domain.util.DomainContextEngineUtil;
import com.innsmap.guomaorobot.R;
import com.innsmap.guomaorobot.view.activity.StandByActivity;
import com.innsmap.versionchecker.Constants;
import com.innsmap.versionchecker.UpdateVersionUtils;

import me.yongning.mvvmframework.base.BaseApplication;
import me.yongning.mvvmframework.crash.CaocConfig;

/**
 * @author yongningyang@gmail.com
 * @date 2018/11/27
 * @Description
 */
public class AppApplication extends BaseApplication implements RobotHeartbeatListener {
    private int outTime = 0;
    private static final String TAG = "AppApplication";
    public static int APP_THEME;
    public ObservableBoolean isNightTheme = new ObservableBoolean(APP_THEME == R.style.LightTheme);

    @Override
    public int getThemeId() {
        if (APP_THEME != 0) {
            return APP_THEME;
        }

        APP_THEME = R.style.LightTheme;
        return APP_THEME;
    }

    @Override
    public void setThemeId(int resId) {
        APP_THEME = resId;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化全局异常崩溃
        initCrash();

        //初始化业务层
        DomainContextEngineUtil.init(this);

        RobotManager.getInstance().registerHeartbeatListener(TAG, this);

        FakeRobotHeartbeat.getInstance().start();

        checkVersion();
    }

    private void checkVersion() {
        UpdateVersionUtils mUpdateVersionUtils = new UpdateVersionUtils(this);
        mUpdateVersionUtils.checkVersionInfo("gmrobot");
        Constants.downTitle = "国贸机器人";
        Constants.downContent = "国贸机器人";
        Constants.notificationLittleImage = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);// 通知栏小图标
        Constants.notificationBigImage = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);//通知栏大图标
    }

    private void initCrash() {
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //背景模式,开启沉浸式
                .enabled(true) //是否启动全局异常捕获
                .enableNeverCrash(true) //启动异常自动镇压
                .showErrorDetails(true) //是否显示错误详细信息
                .showRestartButton(true) //是否显示重启按钮
                .trackActivities(true) //是否跟踪Activity
                .enableBreakpad(true) //启用breakpad捕获native崩溃
                .minTimeBetweenCrashesMs(2000) //崩溃的间隔时间(毫秒)
                .errorDrawable(R.mipmap.ic_launcher) //错误图标
                .restartActivity(StandByActivity.class) //重新启动后的activity
                .apply();
    }

    @Override
    public void onReceive() {
        //TODO some looper work per second

    }

    @Override
    public void onBack2StandBy() {

    }

    @Override
    public void onLowBatteryWarn(int batteryCountdown) {

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        FakeRobotHeartbeat.getInstance().stop();
        RobotManager.getInstance().destroy();
    }
}
