package com.innsmap.domain;

/**
 * @author yongningyang@gmail.com
 * @date 2018/11/30
 * @Description
 */
public interface RobotHeartbeatListener {
    void onReceive();

    void onBack2StandBy();

    void onLowBatteryWarn(int batteryCountdown);
}
