package me.yongning.mvvmframework.base;

/**
 * @author yongningyang@gmail.com
 * @date 2018/11/26
 * @Description
 */
public interface IBaseActivity{
    /**
     * 初始化界面传递参数
     */
    void initParam();
    /**
     * 初始化数据
     */
    void initData();

    /**
     * 初始化界面观察者的监听
     */
    void initViewObservable();
}
