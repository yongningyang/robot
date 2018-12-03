package com.innsmap.guomaorobot.view.activity;

import android.os.Bundle;

import com.innsmap.guomaorobot.BR;
import com.innsmap.guomaorobot.R;
import com.innsmap.guomaorobot.databinding.ActivityMainBinding;
import com.innsmap.guomaorobot.vm.MainViewModel;

/**
 * @author yongningyang@gmail.com
 * @date 2018/11/27
 * @Description
 */
public class MainActivity extends RobotAwareActivity<ActivityMainBinding, MainViewModel> {
    private static final String TAG = "MainActivity";

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_main;
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
    }

    @Override
    protected String getTAG() {
        return TAG;
    }
}
