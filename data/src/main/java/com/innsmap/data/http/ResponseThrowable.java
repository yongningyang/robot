package com.innsmap.data.http;

/**
 * @author yongningyang@gmail.com
 * @date 2018/11/27
 * @Description
 */

public class ResponseThrowable extends Exception {
    public int code;
    public String message;

    public ResponseThrowable(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
    }
}
