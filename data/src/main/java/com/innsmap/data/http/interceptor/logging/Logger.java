package com.innsmap.data.http.interceptor.logging;

import okhttp3.internal.platform.Platform;

/**
 * @author yongningyang@gmail.com
 * @date 2018/11/27
 * @Description
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public interface Logger {
    void log(int level, String tag, String msg);

    Logger DEFAULT = new Logger() {
        @Override
        public void log(int level, String tag, String message) {
            Platform.get().log(level, message, null);
        }
    };
}
