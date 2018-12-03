package com.innsmap.domain;

import android.content.Context;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableDouble;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;

import com.hotelrobot.common.Notification;
import com.hotelrobot.common.logcat.HeartbeatService;
import com.hotelrobot.common.logcat.entity.HeartbeatBean;
import com.hotelrobot.common.nethub.entity.HotelNotification;
import com.hotelrobot.common.nethub.entity.Marker;
import com.innsmap.InnsMap.INNSMapSDK;
import com.innsmap.InnsMap.net.http.listener.forout.SDKInitListener;
import com.innsmap.data.util.CommonUtil;
import com.innsmap.data.util.KLog;
import com.innsmap.data.util.ListUtil;
import com.innsmap.domain.bean.ChargeData;
import com.innsmap.domain.bean.PoiData;
import com.innsmap.domain.util.InterfaceUtil;
import com.innsmap.domain.util.LogcatHelper;
import com.innsmap.domain.util.Utils;
import com.robot.performlib.action.ChargeAction;
import com.robot.performlib.action.RobotConnectAction;
import com.robot.performlib.action.SDKProp;
import com.robot.performlib.action.SpeakAction;
import com.robot.performlib.callback.MoveCallback;
import com.robot.performlib.callback.RobotConnectCallBack;
import com.robot.performlib.position.PositionInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yongningyang@gmail.com
 * @date 2018/11/27
 * <p>
 * 管理机器人自身所有属性的类，诸如心跳、位置、楼层、目标等等
 */
public class RobotManager {
    private final int HANDLER_WHAT_LOG = 0;
    private final int HANDLER_WHAT_INNSMAP = HANDLER_WHAT_LOG + 1;
    private final int HANDLER_WHAT_INTERFACE = HANDLER_WHAT_INNSMAP + 1;
    private final int HANDLER_WHAT_HEARTBEAT = HANDLER_WHAT_INTERFACE + 1;
    private final int HANDLER_WHAT_NO_OPERATION = HANDLER_WHAT_HEARTBEAT + 1;
    private final int HANDLER_WHAT_LOW_BATTERY = HANDLER_WHAT_NO_OPERATION + 1;

    /**
     * 是否需要更新poi数据
     */
    private boolean isUpdatePoiData = true;
    private boolean isClear = false;
    private final Map<String, List<PoiData>> allPoi = new HashMap<>();
    public final Map<String, Marker> markers = new HashMap<>();
    private final Map<String, RobotHeartbeatListener> mHeartbeatListener = new HashMap<>();
    private Context mContext;
    private RobotConnectAction mRobotConnectAction;
    private ChargeAction mChargeAction;
    private SpeakAction mSpeakAction;
    PositionInfo positionInfo;

    public ObservableInt batteryCountDown = new ObservableInt(ConstantValue.batteryLowWarnMax);// 电量倒计时

    /**
     * 机器人Id
     */
    private ObservableField<String> mRobotId = new ObservableField<>("");

    /**
     * 充电状态。1:充电状态  0:非充电状态
     */
    private ObservableInt chargeState = new ObservableInt(0);

    /**
     * 机器人在去充电的路上
     */
    private boolean isGoing2Charge = false;

    /**
     * 机器人正在退出充电状态
     */
    private boolean isCancellingCharge = false;

    /**
     * 休眠持续时间  机器人休眠开始计时 20s
     */
    private int sleepTime = 0;

    /**
     * 清空缓存间隔记时 2h
     */
    private int cacheMap;

    /**
     * 停止时间间隔记时 60s
     */
    private int stopTime = 0;

    /**
     * 记录心跳次数。
     */
    private long heartbeatSeconds = 0;
    private final long ONEDAYSECONDS = 24 * 60 * 60;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == HANDLER_WHAT_LOG) {
                LogcatHelper.getInstance(mContext).start();
            } else if (msg.what == HANDLER_WHAT_INNSMAP) {
                INNSMapSDK.init(mContext);
            } else if (msg.what == HANDLER_WHAT_INTERFACE) {
                //获取网络数据
                InterfaceUtil.getInstance(mContext).login();
            } else if (msg.what == HANDLER_WHAT_HEARTBEAT) {
                if (!CommonUtil.isEmpty(mHeartbeatListener)) {
                    try {
                        ListUtil.forEach(mHeartbeatListener.values(), (item) -> item.onReceive());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (msg.what == HANDLER_WHAT_NO_OPERATION) {
                if (!CommonUtil.isEmpty(mHeartbeatListener)) {
                    try {
                        ListUtil.forEach(mHeartbeatListener.values(), (item) -> item.onBack2StandBy());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (msg.what == HANDLER_WHAT_LOW_BATTERY) {
                if (!CommonUtil.isEmpty(mHeartbeatListener)) {
                    try {
                        ListUtil.forEach(mHeartbeatListener.values(), (item) -> item.onLowBatteryWarn(batteryCountDown.get()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    private static RobotManager ourInstance = new RobotManager();

    /**
     * 机器人无交互待机时间，60s后返回待机页
     */
    private int outTime = 0;

    public static RobotManager getInstance() {
        return ourInstance;
    }

    private RobotManager() {
        mRobotId.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                StatisticsConstant.init();

                /**
                 * 启动log统计
                 */
                LogcatHelper.getInstance(mContext).start();

                /**
                 * 获取机器人坐标信息
                 */
                InterfaceUtil.getInstance(mContext).getRobotPoint();
            }
        });
    }

    HeartbeatService.HeartDataListener heartDataListener = new HeartbeatService.HeartDataListener() {
        @Override
        public void heartData(HeartbeatBean heartbeatBean) {
            if (CommonUtil.isEmpty(allPoi) && heartbeatSeconds % 120 == 0) {
                handler.sendEmptyMessage(HANDLER_WHAT_INTERFACE);
            }

            if (!INNSMapSDK.isInit() && heartbeatSeconds % 50 == 0) {
                handler.sendEmptyMessage(HANDLER_WHAT_INNSMAP);
            }

            robotHeartBeat.set(heartbeatBean);
            hardwareStop.set(heartbeatBean.hardwareStop);
            battery.set(heartbeatBean.powerPercent);
            angle.set(Math.toDegrees((float) heartbeatBean.angle));
            KLog.d("heartData-angle: " + angle.get());

            checkSelf();

            checkLowBattery();

            checkNoOperation();

            chargeState.set(heartbeatBean.chargeState);
            handler.sendEmptyMessage(HANDLER_WHAT_HEARTBEAT);

            heartbeatSeconds = (heartbeatSeconds + 1) % ONEDAYSECONDS;
        }

        @Override
        public void softEStopStateChange(boolean b) {
            KLog.e("软急停状态切换为:" + b);
        }

        @Override
        public void hardStopStateChange(boolean b) {
            KLog.e("硬急停状态切换为:" + b);
            hardwareStop.set(b);
            if (isSpeak.get() && b) {
                isSpeak.set(false);
            }
        }
    };

    /**
     * 初始化
     *
     * @param context
     */
    public synchronized void init(Context context) {
        this.mContext = context;

        SDKProp.aiuiInit(mContext, heartDataListener);

        INNSMapSDK.init(mContext, new SDKInitListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFail(String s) {

            }
        });

        positionInfo = PositionInfo.init(mContext);
        if (!CommonUtil.isEmpty(positionInfo.getMarkers()))
            markers.putAll(positionInfo.getMarkers());
        mRobotConnectAction = RobotConnectAction.init(mContext).setConnectCallback(connectCallback);
        mChargeAction = ChargeAction.init(mContext, chargeCallback);
        mSpeakAction = SpeakAction.getInstance();
    }

    private void checkLowBattery() {
        boolean isCountDown = Utils.lowBattery(mContext, false);

        if (isCountDown || status.get() == 2) {
            if (batteryCountDown.get() > 0) {
                batteryCountDown.set(batteryCountDown.get() - 1);
                handler.sendEmptyMessage(HANDLER_WHAT_LOW_BATTERY);
            } else {
                outTime = ConstantValue.OPTIME;
            }
        }
    }

    private void checkNoOperation() {
        if (outTime >= ConstantValue.OPTIME) {
            outTime = 0;
            handler.sendEmptyMessage(HANDLER_WHAT_NO_OPERATION);
        } else {
            outTime++;
        }
    }

    /**
     * 机器人状态刷新
     */
    private void checkSelf() {
        //下班充电状态
        boolean isRobotWorking = RobotManager.getInstance().isRobotWorking();
        int robotStatus = RobotManager.getInstance().getRobotStatus();
        if (robotStatus == 4) {
            if (isRobotWorking) {
                //上班时间 开始巡游
                LogcatHelper.logd(StatisticsConstant.STARTMOVE, "", StatisticsConstant.STARTWORK);
                isCancellingCharge = false;
                RobotManager.getInstance().setRobotStatus(0);
                walk();
                return;
            } else {
                if (chargeState.get() == 0) {
                    LogcatHelper.logd(StatisticsConstant.ENDWORK_NOCHARGE, "", "");
                    charge();
                    return;
                }
            }
        }

        if (!isRobotWorking && robotStatus != 4 && !isCancellingCharge) {
            //下班时间到 改为下班状态
            LogcatHelper.logd(StatisticsConstant.ENDWORK_STATAE, "", "");
            RobotManager.getInstance().setRobotStatus(4);
            //未充电去充电
            if (chargeState.get() == 0) {
                //如果不是在充电状态回去充电
                isGoing2Charge = false;
                charge();
                return;
            }
        }

        if (robotStatus != 2 && RobotManager.getInstance().battery.get() != -1 && RobotManager.getInstance().battery.get() <= ConstantValue.batteryThreshold && chargeState.get() == 0) {
            //到达电量阈值去充电
            LogcatHelper.logd(StatisticsConstant.BATTERYTHRESHOLDCHARGE, "", "");
            speak(ConstantValue.standbyCycleText_5);
            RobotManager.getInstance().setRobotStatus(2);
            isGoing2Charge = false;
            charge();
            return;
        } else if ((robotStatus == 2) && isRobotWorking && RobotManager.getInstance().battery.get() >= ConstantValue.batteryCeil) {
            //充电完成开始巡游  //上班时间
            LogcatHelper.logd(StatisticsConstant.STARTMOVE, "", StatisticsConstant.WORKENDCHARGE);
            RobotManager.getInstance().setRobotStatus(0);
            walk();
            return;
        } else if (robotStatus == 2 && chargeState.get() == 0) {
            charge();
            RobotManager.getInstance().setRobotStatus(2);
            return;
        } else if (robotStatus == 3 && chargeState.get() == 0) {
            charge();
            RobotManager.getInstance().setRobotStatus(3);
            return;
        }

        sleepTime = Utils.checkWakeUp(sleepTime, mContext);
        cacheMap = Utils.clearMapCache(cacheMap);


        if (robotStatus == 0) {
            if (stopTime >= ConstantValue.STOPALLTIME) {
                RobotManager.getInstance().setRobotStatus(1);
                speak(ConstantValue.standbyCycleText_2);
                walk();
            } else {
                stopTime++;
            }
        }

        if (chargeState.get() == 1) {
            RobotManager.getInstance().isSpeak.set(false);
            if ((robotStatus != 1 || robotStatus != 0) && isGoing2Charge) {
                isGoing2Charge = false;
                RobotManager.getInstance().getRobotConnectAction().sendCancel();
            }
        } else if (chargeState.get() == 0 && !RobotManager.getInstance().hardwareStop.get()) {
            RobotManager.getInstance().isSpeak.set(true);
        } else if (RobotManager.getInstance().hardwareStop.get()) {
            RobotManager.getInstance().isSpeak.set(false);
        }
    }


    /**
     * 链接状态的回调
     */
    private RobotConnectCallBack connectCallback = new RobotConnectCallBack() {
        @Override
        public void robotNotificationResult(HotelNotification info) {
            super.robotNotificationResult(info);
            switch (info.code) {
                case Notification.MOVE_START:
                    break;
                case Notification.MOVE_FAILED:
                    break;
                case Notification.CRUISE_STARTED:
                    LogcatHelper.logd(StatisticsConstant.STARTMOVE, "start", "");
                    break;
                case Notification.CRUISE_FINISHED:
                    LogcatHelper.logd(StatisticsConstant.STARTMOVE, "success", StatisticsConstant.ENDMOVE);
                    break;
                case Notification.CRUISE_FAILED:
                    LogcatHelper.logd(StatisticsConstant.STARTMOVE, "fail", StatisticsConstant.MOVEFAIL);
                    break;
                case Notification.CRUISE_CANCELED:
                    break;
                case Notification.MOVE_RETRY:
                    break;
                case Notification.TRAPPED:
                    break;
                case Notification.ATTITUDE_CORRECTION:
                    break;
                case Notification.ESTOP_ON:
                    isSpeak.set(false);
                    break;
                case Notification.ESTOP_OFF:
                    isSpeak.set(true);
                    break;
            }
        }

        @Override
        public void robotMarkersListResult(HashMap<String, Marker> mapMarker) {
            super.robotMarkersListResult(mapMarker);
            markers.clear();
            markers.putAll(mapMarker);
        }
    };

    /**
     * 充电状态的回调
     */
    private MoveCallback chargeCallback = new MoveCallback() {
        @Override
        public void MoveCompleteHandler() { //回充成功
            super.MoveCompleteHandler();
            isGoing2Charge = false;
            LogcatHelper.logd(StatisticsConstant.BACKCHARGESUCCESS, "", "");
        }

        @Override
        public void MoveFailedHandler() { // 移动失败，也可代表上桩失败
            super.MoveFailedHandler();
            LogcatHelper.logd(StatisticsConstant.BACKCHARGEFAIL, "", "");
        }

        @Override
        public void MoveRetryHandler() { // 移动重试（1.0.6 新增）
            super.MoveRetryHandler();
            if (isSpeak.get()) speak(ConstantValue.standbyCycleText_7);
        }

        @Override
        public void successLeaveDock() { // 离开充电桩成功（1.0.6 新增）
            super.successLeaveDock();
        }

        @Override
        public void estopPrompt(boolean estop) {
            super.estopPrompt(estop);
            isSpeak.set(!estop);

            //急停状态，只在开始执行任务的时候检查一次（1.0.6 新增）
            if (isSpeak.get()) {
                speak(ConstantValue.standbyCycleText_7);
            }
        }

        @Override
        public void trapped() { // 地图偏移或被困住（1.1.1 新增）
            super.trapped();
            if (isSpeak.get()) {
                speak(ConstantValue.standbyCycleText_7);
            }
        }

        @Override
        public void attitudeCorrection() { // 机器人被搬动（1.1.1 新增）
            super.attitudeCorrection();
            if (isSpeak.get()) {
                speak(ConstantValue.standbyCycleText_7);
            }
        }
    };

    /**
     * 释放
     */
    public void destroy() {
        SDKProp.destroy(mContext);
        mHeartbeatListener.clear();
        LogcatHelper.getInstance(mContext).stop();
        ourInstance = null;
    }

    /**
     * 待机页面是否说话
     */
    public ObservableBoolean isSpeak = new ObservableBoolean(false);

    /**
     * 机器人方向角
     */
    public ObservableDouble angle = new ObservableDouble(0d);

    /**
     * 机器人心跳
     */
    public ObservableField<HeartbeatBean> robotHeartBeat = new ObservableField<>();

    /**
     * 机器人电量
     */
    public ObservableInt battery = new ObservableInt(-1);

    /**
     * 机器人硬件异常停止
     */
    public ObservableBoolean hardwareStop = new ObservableBoolean(false);

    /**
     * 当前楼层索引
     */
    public ObservableInt floorIndex = new ObservableInt(0);

    /**
     * 机器人位置
     */
    public ObservableField<PointF> robotPoint = new ObservableField<>(ConstantValue.robotPointData);

    /**
     * 机器人状态
     */
    public ObservableInt status = new ObservableInt(-1);// 0 停留状态 1 移动状态  2 充电状态  3 语音缓冲 4 时间缓冲

    public int index = 0;// 三个点的下标

    /**
     * 机器人唤醒状态
     */
    public ObservableBoolean wakeUpState = new ObservableBoolean(false);

    /**
     * 机器人工作清单
     */
    public static List<ChargeData> work = new ArrayList<ChargeData>();

    /**
     * 当前楼层是否无效
     *
     * @return
     */
    public boolean isFloorInvalid() {
        return floorIndex.get() >= ConstantValue.floorIds.length || floorIndex.get() < 0;
    }

    /**
     * 判断当前是否在负一层
     *
     * @param floorId
     * @return
     */
    public boolean isB1Floor(String floorId) {
        return floorId.equalsIgnoreCase(ConstantValue.floorIdB1);
    }

    /**
     * 获取建筑Id
     *
     * @return
     */
    public String getBuildingId() {
        return ConstantValue.buildingId;
    }

    /**
     * 获取楼层Id
     *
     * @return
     */
    public String getFloorId() {
        return ConstantValue.floorIds[floorIndex.get()];
    }

    /**
     * 存储楼层Poi坐标
     *
     * @param floorId
     * @param sets
     */
    public void putPoiData(String floorId, List<PoiData> sets) {
        allPoi.put(floorId, sets);
    }

    /**
     * 上楼
     */
    public synchronized boolean goUpFloor() {
        if (floorIndex.get() < ConstantValue.floorIds.length - 1) {
            floorIndex.set(floorIndex.get() + 1);
            return true;
        }

        return false;
    }

    /**
     * 下楼
     */
    public synchronized boolean goDownFloor() {
        if (floorIndex.get() > 0 && ConstantValue.floorIds.length > 1) {
            floorIndex.set(floorIndex.get() - 1);
            return true;
        }
        return false;
    }

    /**
     * 是否需要更新楼层Poi数据
     *
     * @return
     */
    public boolean isNeedUpdatePoiData() {
        return isUpdatePoiData || CommonUtil.isEmpty(allPoi);
    }

    /**
     * 清除Poi缓存
     */
    public void clearPoiData() {
        isUpdatePoiData = true;
        allPoi.clear();
    }

    /**
     * 获取机器人编号
     *
     * @return
     */
    public String getRobotId() {
        return mRobotId.get();
    }

    public void setIsUpdatePoiData(boolean flag) {
        isUpdatePoiData = flag;
    }

    public List<PoiData> getPoiData(String floorId) {
        return allPoi.get(floorId);
    }

    /**
     * 机器人所在位置横坐标
     *
     * @return
     */
    public float getRobotPointX() {
        return robotPoint.get().x;
    }

    /**
     * 机器人坐在位置Y坐标
     *
     * @return
     */
    public float getRobotPointY() {
        return robotPoint.get().y;
    }

    /**
     * 机器人的位置坐标对象
     *
     * @return
     */
    public PointF getRobotPosition() {
        return robotPoint.get();
    }

    /**
     * 设置机器人坐标
     *
     * @param position
     */
    public void setRobotPosition(PointF position) {
        this.robotPoint.set(position);
    }

    /**
     * 获取电量
     *
     * @return
     */
    public int getBattery() {
        return battery.get();
    }

    /**
     * 位置信息有效
     *
     * @return
     */
    public boolean isPoiAndPositionValid() {
        return !CommonUtil.isEmpty(allPoi) && robotPoint != null && robotPoint.get() != null;
    }

    /**
     * 获取机器人状态
     *
     * @return
     */
    public int getRobotStatus() {
        return status.get();
    }

    /**
     * 设置机器人状态
     *
     * @param status
     */
    public void setRobotStatus(int status) {
        this.status.set(status);
    }

    /**
     * 机器人是否已唤醒
     *
     * @return
     */
    public boolean isRobotWakeUp() {
        return wakeUpState.get();
    }

    /**
     * 唤醒机器人
     */
    public void wakeUpTheRobot(boolean flag) {
        this.wakeUpState.set(flag);
    }

    public boolean isClearMapCache() {
        return isClear;
    }

    public void setClearMapCache(boolean clear) {
        isClear = clear;
    }

    /**
     * 机器人是否在工作
     *
     * @return
     */
    public boolean isRobotWorking() {
        Date date = new Date();
        if (!CommonUtil.isEmpty(work)) {
            boolean isWorking = false;
            for (int i = 0; i < work.size(); i++) {
                String[] time = work.get(i).getWorkValue();
                for (int j = 0; j < time.length / 2; j++) {
                    if (date.getTime() - Utils.getWorkTime(time[j * 2]) > 0
                            && date.getTime() - Utils.getWorkTime(time[j * 2 + 1]) < 0) {
                        isWorking = true;
                    }
                }
            }
            return isWorking;
        } else if ((date.getTime() - Utils.getWorkTime(ConstantValue.amBegin) > 0
                && date.getTime() - Utils.getWorkTime(ConstantValue.amEnd) < 0)  // 上午上班时间
                || (date.getTime() - Utils.getWorkTime(ConstantValue.pmBegin) > 0
                && date.getTime() - Utils.getWorkTime(ConstantValue.pmEnd) < 0)) {// 下午上班时间
            return true;
        }
        return false;
    }

    /**
     * 清除机器人所有工作
     */
    public void clearWork() {
        work.clear();
    }

    /**
     * 为机器人安排工作
     *
     * @param works
     */
    public void arrangeWorks(List<ChargeData> works) {
        work.addAll(works);
    }

    /**
     * 注册机器人心跳监听
     *
     * @param name
     * @param listener
     */
    public void registerHeartbeatListener(String name, RobotHeartbeatListener listener) {
        mHeartbeatListener.put(name, listener);
    }

    /**
     * 取消机器人心跳监听
     *
     * @param name
     */
    public void unRegisterHeartbeatListener(String name) {
        mHeartbeatListener.remove(name);
    }

    public RobotConnectAction getRobotConnectAction() {
        return mRobotConnectAction;
    }

    public void setRobotConnectAction(RobotConnectAction mRobotConnectAction) {
        this.mRobotConnectAction = mRobotConnectAction;
    }

    /**
     * 机器人说话
     *
     * @param words
     */
    private void speak(String words) {
        mSpeakAction.speak(mContext, words);
    }

    private void walk() {
        isGoing2Charge = false;
        if (getRobotStatus() == 2 || getRobotStatus() == 3 || getRobotStatus() == 4) {
            charge();
            return;
        }
        if (CommonUtil.isEmpty(markers)) {
            if (!CommonUtil.isEmpty(positionInfo.getMarkers()))
                markers.putAll(positionInfo.getMarkers());
            return;
        }

        ArrayList<Marker> markerList = new ArrayList<>();
        for (String markerName : ConstantValue.patrolMarker) {
            if (markers.containsKey(markerName)) {
                markerList.add(markers.get(markerName));
            }
        }
        if (markers.containsKey(ConstantValue.patrolMarker[0])) {
            markerList.add(markers.get(ConstantValue.patrolMarker[0]));
        }

        if (!CommonUtil.isEmpty(markerList)) {
            mRobotConnectAction.sendMoveMarkers(markerList);
            stopTime = 0;
            status.set(1);
        }
    }

    private void charge() {
        if (!isGoing2Charge && chargeState.get() == 0) {
            isSpeak.set(false);
            isCancellingCharge = false;
            isGoing2Charge = true;
            LogcatHelper.logd(StatisticsConstant.CHARGE, "", "");
            mChargeAction.action(mContext);
        }
    }

    public boolean getIsGoing2Charge() {
        return isGoing2Charge;

    }

    public boolean getIsCancellingCharge() {
        return isCancellingCharge;
    }

    public int getChargeState() {
        return chargeState.get();
    }

    /**
     * 重置无操作计时
     */
    public void resetNoOperationTime() {
        outTime = 0;
    }

    /**
     * 发送模拟心跳，发版时应注释
     *
     * @param type
     */
    public void sendFakeHeartbeat(int type) {
        HeartbeatBean heartbeatBean = new HeartbeatBean();
        heartDataListener.heartData(heartbeatBean);
    }
}
