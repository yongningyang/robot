package com.innsmap.domain;

import android.graphics.PointF;

import com.innsmap.data.http.interceptor.DomainInterceptor;
import com.innsmap.domain.bean.AdvData;
import com.innsmap.domain.bean.EventData;
import com.innsmap.domain.bean.PoiData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yongningyang@gmail.com
 * @date 2018/11/27
 * @Description
 */
public class ConstantValue {
    //region ========== 错误定义 ==========
    public static final String NO_NET = "网络已断开，无法获取数据，请检查网络";
    public static final String HTTP_BACK_NULL = "返回数据为空";
    public static final int PARAM_NULL_FAIL_CODE = 1010;
    public static final String PARAM_NULL_FAIL_DESC = "请求参数null";
    public static final int NETWORK_SUCCEED_CODE = 1; // 接口成功的通用标志
    public static final String NETWORK_BACK_NOT_MARK = "返回码无标记";
    public static final int LOGIN_FAIL_CODE_1 = 1003;
    public static final String LOGIN_FAIL_DESC_1 = "密码错误";
    public static final int LOGIN_FAIL_CODE_2 = 1002;
    public static final String LOGIN_FAIL_DESC_2 = "用户不存在";
    public static final int LOGIN_FAIL_CODE_3 = 180101;
    public static final String LOGIN_FAIL_DESC_3 = "用户名null";
    public static final int LOGIN_FAIL_CODE_4 = 180102;
    public static final String LOGIN_FAIL_DESC_4 = "密码null";
    public static final int LOGIN_FAIL_CODE_5 = 1004;
    public static final String LOGIN_FAIL_DESC_5 = "用户已禁用";

    public static final int GET_BUILDING_FAIL_CODE_1 = 1006;
    public static final String GET_BUILDING_FAIL_DESC_1 = "token为空";
    public static final int GET_BUILDING_FAIL_CODE_2 = 1007;
    public static final String GET_BUILDING_FAIL_DESC_2 = "token无效";

    public static final int GET_BUILDING_FAIL_CODE_3 = 1008;
    public static final String GET_BUILDING_FAIL_DESC_3 = "token过期";
    public static final int GET_BUILDING_FAIL_CODE_4 = 2001;
    public static final String GET_BUILDING_FAIL_DESC_4 = "用户无数据权限";
    public static final int GET_BUILDING_FAIL_CODE_5 = -1;
    public static final String GET_BUILDING_FAIL_DESC_5 = "服务异常";
    //endregion

    //region ========== 电量相关 ==========
    public static int batteryThreshold = 5;// 电量门限
    public static int batteryCeil = 100;// 电量上限
    public static int batteryLowWarnMax = 30;//低电量报警倒计时最大值
    //endregion

    //region ========== 机器人语录 ==========
    public static String standbyCycleText_1 = "hello,尊敬的顾客,您好,我是国贸指路小助手,我叫多米,我对国贸路线了如指掌,无论您想去哪里,我都能给您指路,请对我说\"hello\"或点击屏幕唤醒指路功能。";
    public static String standbyCycleText_2 = "唉,此处无人问路,我去下个地点试试。";
    public static String standbyCycleText_3 = "工作中,麻烦您给多米让下路。";
    public static String standbyCycleText_4 = "快放开我,我要去工作。";
    public static String standbyCycleText_5 = "抱歉,我该充电了,充完电,我一定会在回来的。";
    public static String standbyCycleText_6 = "抱歉,我在充电呢。";
    public static String standbyCycleText_7 = "我要去充电了,麻烦您给多米让下路。";
    public static String chargeText_1 = "亲爱的顾客,我电量已用尽," + batteryLowWarnMax + "秒后就要去充电了,请尽快完成您的问路。";
    public static String mainText_1 = "您好，您想去哪里？您可以跟我说。";
    public static String searchTextNone = "抱歉,您说的地点多米没听说过,再来一次吧！";
    public static String navSearchTextNone = "尊敬的客户,没有查询到您需要的路线,请再试一次吧！";
    public static String navTurn = "我将转头为您指定初始方向";
    public static String navTurnEnd = "我已转头为您指路，请沿屏幕面向的方向行走";
    public static String pathEnd = "请沿路线行走,谢谢您的使用,下次再见";
    public static String EWMPATH = "http://maoner.innsmap.com/eg/nav.html?";
    //endregion

    //region ========== 建筑楼层信息 ==========
    public static String buildingId = "5a6b9e59634f4c6e82c9d843694ddc14";//北京国贸商城
    public static String floorIdB1 = "4830d5a28b684be1be2d35a2b16362a4";
    public static String floorIdB2 = "490702a9b0224db6ab47edef40ecce4e";
    public static String floorIdF1 = "debed3d2c37d4758bdcf1b0233ab6b8f";
    public static String floorIdF2 = "02b1b970f5894b3a8928d26aaeb61cf9";
    public static String floorIdF3 = "0d5f37178a10451d85e5c6ab3bf03959";
    public static String floorIdF4 = "024ef01dead34272ad0288d201dd67dd";
    public static String floorIdF5 = "0560e78fab324fbd943e390c2803b2a2";
    public static String floorIdF6 = "3d28b9970c2a40a3a08940c595c8ba9a";
    public static String floorIdF7 = "9f5cb08cc77e45e288036cf4787e4c52";

    public static String[] floorIds = {floorIdF7, floorIdF6, floorIdF5, floorIdF4, floorIdF3, floorIdF2, floorIdF1, floorIdB1, floorIdB2};
    public static String[] floorName = {"F7", "F6", "F5", "F4", "F3", "F2", "F1", "B1", "B2"};
    //endregion

    //region ========== 公交信息 ==========
    public static String[] busName = new String[0];
    public static int[] pid = new int[0];
    public static String[] busNameD = new String[0];
    public static String[] busNameShort = new String[0];
    public static int[] pidShort = new int[0];
    public static List<PoiData> busPOI = new ArrayList<>();
    //endregion

    //region ========== 网络定义 ==========
    public static final String BASE_URL = "http://gorilla.innsmap.com/";
    public static final String LOGIN_URL = "http://gorilla.innsmap.com/";
    public static final String DATA_URL = "http://106.3.148.20:9025/"; //外网

    public static final String HEADER_LOGIN_URL_DOMAIN_NAME = DomainInterceptor.DOMAIN_BASE_URL_NAME + "_LOGIN";
    public static final String HEADER_LOGIN_URL_DOMAIN = DomainInterceptor.DOMAIN_BASE_URL_NAME + ":" + HEADER_LOGIN_URL_DOMAIN_NAME;
    public static final String HEADER_DATA_URL_DOMAIN_NAME = DomainInterceptor.DOMAIN_BASE_URL_NAME + "_DATA";
    public static final String HEADER_DATA_URL_DOMAIN = DomainInterceptor.DOMAIN_BASE_URL_NAME + ":" + HEADER_DATA_URL_DOMAIN_NAME;
    //endregion

    //region ========== 机器人默认坐标 ==========
    /**
     * 机器人默认坐标
     */
    public static PointF robotPointData = new PointF(292.30f, 109.71f);
    //endregion

    //region ========== 机器人方位角补偿 ==========
    public static double angleN = -34;
    public static double angleS = 146;
    //endregion

    //region ========== 机器人唤醒间隔 ==========
    public static int wakeUpTime = 20;
    //endregion

    //region ========== 机器人工作时间 ==========
    public static String amBegin = "09:30:00";
    public static String amEnd = "12:00:00";
    public static String pmBegin = "12:00:00";
    public static String pmEnd = "21:30:00";
    public static String MIN_VAl = "MIN_VAl";
    public static String MIN_WORK = "MIN_WORK";
    public static String AM = "AM";
    public static String PM = "PM";
    public static String[] chargeCode = {"MIN_VAl", "MIN_WORK", "AM", "PM"};
    //endregion

    //region ========== 机器人地图缓存清理时间 ==========
    public static int minutes = 60;
    public static int OPTIME = 60;
    public static float STOPALLTIME = 60;
    public static float CLEARMAPCACHE = 2 * minutes * minutes;
    //endregion

    //region ========== 机器人路线坐标 ==========
    public static String[] patrolMarker = {"李盼盼", "李莹", "李祖龙"};
//    public static String[] patrolMarker = {"点位1", "点位2", "点位3"};
    //endregion

    public static List<PoiData> hotPoi = new ArrayList<>();
    public static List<EventData> eventList = new ArrayList<>();
    public static List<AdvData> advList = new ArrayList<>();
}
