package com.innsmap.guomaorobot.vm;

import android.app.Application;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.innsmap.domain.ConstantValue;
import com.innsmap.domain.RobotManager;
import com.innsmap.guomaorobot.app.AppApplication;

import me.yongning.mvvmframework.base.BaseViewModel;
import me.yongning.mvvmframework.binding.command.BindingCommand;

/**
 * @author yongningyang@gmail.com
 * @date 2018/11/27
 * @Description
 */
public class TitleViewModel extends BaseViewModel {
    private static final TitleViewModel ourInstance = new TitleViewModel(AppApplication.getInstance());
    public ObservableField<String> battery = new ObservableField<>("电量获取中...");
    public ObservableBoolean voiceOpen = new ObservableBoolean(true);
    public ObservableBoolean voiceBtnVisible = new ObservableBoolean(false);
    //点击声音按钮
    public BindingCommand voiceBtnClickCommand = new BindingCommand(() -> {
        voiceOpen.set(!voiceOpen.get());
    });

    private TitleViewModel(Application application) {
        super(application);
        RobotManager.getInstance().battery.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                battery.set("电量" + RobotManager.getInstance().getBattery() + "%");
            }
        });

        battery.set("电量" + RobotManager.getInstance().getBattery() + "%");

        RobotManager.getInstance().batteryCountDown.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (RobotManager.getInstance().batteryCountDown.get() == 0) {
                    battery.set(ConstantValue.standbyCycleText_6);
                } else if (RobotManager.getInstance().batteryCountDown.get() < ConstantValue.batteryLowWarnMax) {
                    battery.set("充电倒计时：" + RobotManager.getInstance().batteryCountDown.get() + "s");
                }
            }
        });
    }

    public static TitleViewModel getInstance() {
        return ourInstance;
    }

}
