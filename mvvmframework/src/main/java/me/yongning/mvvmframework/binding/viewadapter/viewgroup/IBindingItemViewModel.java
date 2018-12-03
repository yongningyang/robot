package me.yongning.mvvmframework.binding.viewadapter.viewgroup;

/**
 * @author yongningyang@gmail.com
 * @date 2018/11/26
 * @Description
 */
import android.databinding.ViewDataBinding;

public interface IBindingItemViewModel<V extends ViewDataBinding> {
    void injecDataBinding(V binding);
}
