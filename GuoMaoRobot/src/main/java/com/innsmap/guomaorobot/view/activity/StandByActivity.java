package com.innsmap.guomaorobot.view.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;

import com.innsmap.domain.ConstantValue;
import com.innsmap.domain.RobotManager;
import com.innsmap.domain.StatisticsConstant;
import com.innsmap.domain.util.LogcatHelper;
import com.innsmap.guomaorobot.BR;
import com.innsmap.guomaorobot.R;
import com.innsmap.guomaorobot.databinding.ActivityStandByBinding;
import com.innsmap.guomaorobot.util.AnimationUtils;
import com.innsmap.guomaorobot.vm.StandByViewModel;

/**
 * @author yongningyang@gmail.com
 * @date 2018/11/27
 * @Description
 */
public class StandByActivity extends RobotAwareActivity<ActivityStandByBinding, StandByViewModel> {
    private static final String TAG = "StandByActivity";
    private ObjectAnimator translateAnimator;

    @Override
    public String getTAG() {
        return TAG;
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_stand_by;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        translateAnimator = AnimationUtils.standbyRotation(binding.rotatingImg);
        translateAnimator.start();
    }

    //region ========== robot callback ==========

    @Override
    public void onReceive() {
        //下班充电状态
        boolean isRobotWorking = RobotManager.getInstance().isRobotWorking();
        int robotStatus = RobotManager.getInstance().getRobotStatus();
        boolean isCancellingCharge = RobotManager.getInstance().getIsCancellingCharge();
        boolean isGoing2Charge = RobotManager.getInstance().getIsGoing2Charge();
        int chargeState = RobotManager.getInstance().getChargeState();

        if (robotStatus == 4) {
            if (isRobotWorking) {
                translateAnimator.start();
                return;
            }
        }

        if (!isRobotWorking && robotStatus != 4 && !isCancellingCharge) {
            if (chargeState == 0) {
                translateAnimator.cancel();
                choseImage(4);
                return;
            }
        }

        if (robotStatus != 2 && RobotManager.getInstance().battery.get() != -1 && RobotManager.getInstance().battery.get() <= ConstantValue.batteryThreshold && chargeState == 0) {
            translateAnimator.cancel();
            choseImage(4);
            return;
        } else if ((robotStatus == 2) && isRobotWorking && RobotManager.getInstance().battery.get() >= ConstantValue.batteryCeil) {
            //充电完成开始巡游  //上班时间
            LogcatHelper.logd(StatisticsConstant.STARTMOVE, "", StatisticsConstant.WORKENDCHARGE);
            RobotManager.getInstance().setRobotStatus(0);
            translateAnimator.start();
            return;
        } else if (robotStatus == 2 && chargeState == 0) {
            RobotManager.getInstance().setRobotStatus(2);
            translateAnimator.cancel();
            choseImage(4);
            return;
        } else if (robotStatus == 3 && chargeState == 0) {
            translateAnimator.cancel();
            choseImage(4);
            return;
        }
    }

    @Override
    public void onBack2StandBy() {
        //nothing to do
    }

    //endregion

    private void choseImage(int index) {
        switch (index) {
            case 0:
                viewModel.star1Res.set(R.mipmap.standby_star_dark_1);
                viewModel.star2Res.set(R.mipmap.standby_star_dark_2);
                viewModel.star3Res.set(R.mipmap.standby_star_light_3);
                break;
            case 1:
                viewModel.star1Res.set(R.mipmap.standby_star_light_1);
                viewModel.star2Res.set(R.mipmap.standby_star_dark_2);
                viewModel.star3Res.set(R.mipmap.standby_star_dark_3);
                break;
            case 2:
                viewModel.star1Res.set(R.mipmap.standby_star_dark_1);
                viewModel.star2Res.set(R.mipmap.standby_star_light_2);
                viewModel.star3Res.set(R.mipmap.standby_star_dark_3);
                break;
            case 4:
                viewModel.star1Res.set(R.mipmap.standby_star_dark_1);
                viewModel.star2Res.set(R.mipmap.standby_star_dark_2);
                viewModel.star3Res.set(R.mipmap.standby_star_dark_3);
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }
}
