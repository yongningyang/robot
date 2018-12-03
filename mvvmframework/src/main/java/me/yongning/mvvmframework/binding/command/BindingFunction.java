package me.yongning.mvvmframework.binding.command;

/**
 * Represents a function with zero arguments.
 *
 * @param <T> the result type
 * @author yongningyang@gmail.com
 * @date 2018/11/26
 */
public interface BindingFunction<T> {
    T call();
}