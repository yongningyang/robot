package me.yongning.mvvmframework.base;

import android.support.annotation.NonNull;

/**
 * @author yongningyang@gmail.com
 * @date 2018/11/26
 * @Description
 */
public class ItemViewModel<VM extends BaseViewModel> {
    protected VM viewModel;

    public ItemViewModel(@NonNull VM viewModel) {
        this.viewModel = viewModel;
    }
}
