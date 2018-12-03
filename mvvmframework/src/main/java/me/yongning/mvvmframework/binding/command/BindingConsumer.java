package me.yongning.mvvmframework.binding.command;

/**
 * @param <T> the first argument type
 * @author yongningyang@gmail.com
 * @date 2018/11/26
 * <p>
 * A one-argument action.
 */
public interface BindingConsumer<T> {
    void call(T t);
}