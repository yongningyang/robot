package com.innsmap.guomaorobot.view.activity;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.view.MotionEvent;

import com.innsmap.domain.RobotHeartbeatListener;
import com.innsmap.domain.RobotManager;

import me.yongning.mvvmframework.base.BaseActivity;
import me.yongning.mvvmframework.base.BaseViewModel;

/**
 * @author yongningyang@gmail.com
 * @date 2018/12/2
 * @Description
 */
public abstract class RobotAwareActivity<V extends ViewDataBinding, VM extends BaseViewModel> extends BaseActivity<V, VM> implements RobotHeartbeatListener {

    //region ========== robot callback ==========
    @Override
    public void onReceive() {

    }

    @Override
    public void onBack2StandBy() {
        Intent intent = new Intent(this, StandByActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLowBatteryWarn(int batteryCountdown) {

    }
    //endregion

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        RobotManager.getInstance().resetNoOperationTime();
        return super.onTouchEvent(event);
    }

    @Override
    public void initViewObservable() {
        RobotManager.getInstance().registerHeartbeatListener(getTAG(), this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RobotManager.getInstance().unRegisterHeartbeatListener(getTAG());
    }

    protected abstract String getTAG();
}
