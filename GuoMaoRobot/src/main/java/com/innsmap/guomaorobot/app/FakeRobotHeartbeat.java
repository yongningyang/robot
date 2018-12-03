package com.innsmap.guomaorobot.app;

import android.os.Handler;
import android.os.Message;

import com.innsmap.domain.RobotManager;

/**
 * @author yongningyang@gmail.com
 * @date 2018/12/2
 * @Description
 */
class FakeRobotHeartbeat {
    private Handler handler;
    private boolean enable = false;

    private static final FakeRobotHeartbeat ourInstance = new FakeRobotHeartbeat();

    static FakeRobotHeartbeat getInstance() {
        return ourInstance;
    }

    private FakeRobotHeartbeat() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                handler.postDelayed(() -> {
                    handler.sendEmptyMessage(0);
                    if (enable) {
                        raiseHeartBeat();
                    }
                }, 1000);

            }
        };
    }

    private void raiseHeartBeat() {
        RobotManager.getInstance().sendFakeHeartbeat(0);
    }

    public void start() {
        enable = true;
        handler.sendEmptyMessage(0);

    }

    public void stop() {
        enable = false;
        handler.removeCallbacksAndMessages(null);
    }
}
