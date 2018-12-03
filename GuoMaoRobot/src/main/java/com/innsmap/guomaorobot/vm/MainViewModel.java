package com.innsmap.guomaorobot.vm;

import android.app.Application;
import android.support.annotation.NonNull;

import com.innsmap.domain.RobotManager;

import me.yongning.mvvmframework.base.BaseViewModel;

/**
 * @author yongningyang@gmail.com
 * @date 2018/11/27
 * @Description
 */
public class MainViewModel extends BaseViewModel {
    public TitleViewModel titleViewModel = TitleViewModel.getInstance();

    public RobotManager robot = RobotManager.getInstance();

    public MainViewModel(@NonNull Application application) {
        super(application);
    }
}
