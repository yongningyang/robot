package com.innsmap.guomaorobot.vm;

import android.app.Application;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;

import com.innsmap.guomaorobot.R;
import com.innsmap.guomaorobot.view.activity.MainActivity;

import me.yongning.mvvmframework.base.BaseViewModel;
import me.yongning.mvvmframework.binding.command.BindingCommand;

/**
 * @author yongningyang@gmail.com
 * @date 2018/11/27
 * @Description
 */
public class StandByViewModel extends BaseViewModel {
    public ObservableInt star1Res = new ObservableInt(R.mipmap.standby_star_dark_1);
    public ObservableInt star2Res = new ObservableInt(R.mipmap.standby_star_dark_2);
    public ObservableInt star3Res = new ObservableInt(R.mipmap.standby_star_dark_3);

    public TitleViewModel titleViewModel = TitleViewModel.getInstance();

    public BindingCommand awakeMeCommand = new BindingCommand(() -> startActivity(MainActivity.class));

    public BindingCommand helloCommand = new BindingCommand(() -> {
        String x = null;
        x.toString();
    });

    public StandByViewModel(@NonNull Application application) {
        super(application);
    }
}
