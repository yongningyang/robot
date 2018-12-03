package com.innsmap.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by long on 2018/10/10.
 */

public class StatisticsConstant {

    public static String CLICKWAKEUP = "1001"; //点击唤醒
    public static String VOICEWAKEUP = "1002"; //语音唤醒
    public static String STARTWORK = "1003"; //上班时间开始巡游
    public static String ENDWORK_NOCHARGE = "1004"; //下班状态未充电
    public static String ENDWORK_STATAE = "1005"; //下班时间到改为下班状态
    public static String BATTERYTHRESHOLDCHARGE = "1006"; //到达电量阈值去充电
    public static String WORKENDCHARGE = "1007"; //上班时间充电完成开始巡游
    public static String VOICECHARGE = "1008"; //语音回冲
    public static String CHARGE = "1009"; //调用充电API
    public static String HOMECLICK_VOICE = "1010"; //首页点击功能或使用语音代替点击功能
    public static String HOMECLICK_ADV = "1011"; //点击广告位
    public static String HOMEVOICE_POI = "1012"; //语音搜索到商铺
    public static String HOMEVOICE_EVENT = "1013"; //语音搜索到活动
    public static String MAP_QUICKSEARCH = "1014"; //快捷搜索
    public static String PARKMAP_QUICKSEARCH = "1015"; //找车位快捷搜索
    public static String MAP_NAV = "1016"; //地图页面进入路径规划页面
    public static String PARKMAP_NAV = "1017"; //找车位页面进入路径规划页面
    public static String MAP_SEARCH = "1018"; //地图页面进入搜索页面
    public static String TEXT_RESUTLT = "1019"; //进入测试结果页面
    public static String LOADMAPSUCCESS = "1020"; //地图页面加载地图成功
    public static String LOADMAPFAIL = "1021"; //地图页面加载地图失败
    public static String LOADPARKMAPSUCCESS = "1022"; //找车位页面加载地图成功
    public static String LOADPARKMAPFAIL = "1023"; //找车位页面加载地图失败
    public static String NAVLOADMAPSUCCESS = "1024"; //路径规划页面加载地图成功
    public static String NAVLOADMAPFAIL = "1025"; //路径规划页面加载地图失败
    public static String NAVPATHSUCCESS = "1026"; //路径规划页面寻径成功
    public static String NAVPATHFAIL = "1027"; //路径规划页面寻径失败
    public static String OPENHOME = "1028"; //打开首页
    public static String OPENMAP = "1029"; //打开地图页面
    public static String OPENPARKMAP = "1030"; //打开找车位页面
    public static String OPENSEARCH = "1031"; //打开搜索页面
    public static String OPENNAV = "1032"; //打开路径规划页面
    public static String OPENTEST = "1033"; //打开测运气页面
    public static String BACKCHARGEFAIL = "1034"; //回冲失败
    public static String BACKCHARGESUCCESS = "1035"; //回冲成功
    public static String STARTMOVE = "1036"; //开始巡游
    public static String ENDMOVE = "1037"; //巡游结束
    public static String MOVEFAIL = "1038"; //巡游失败
    public static String STARTTEST = "1039"; //开始运气测试
    public static String MOVECANCEL = "1040"; //巡游取消
    public static String FINDPERSON = "1041"; //识别到人
    public static String HOMEVOICE = "1042"; //听到说话
    public static String HOMECLICK_POI = "1043"; //首页点击商铺
    public static String MAPCLICK_POI = "1044"; //地图点击商铺
    public static String PARKMAPCLICK_POI = "1045"; //找车位点击商铺
    public static String SEARCHCLICK_POI = "1046"; //搜索点击商铺
    public static String PARKMAPSEARCH = "1047"; //找车位搜索内容
    public static String SEARCH_SEARCH = "1048"; //搜索页面搜索内容

    public static String HOMECLICK_FINDPOI = "1012"; //点击首页找商铺
    public static String HOMECLICK_FINDFOOD = "1013"; //点击首页找美食
    public static String HOMECLICK_FINDBUS = "1014"; //点击首页找公交
    public static String HOMECLICK_FINDSUBWAY = "1015"; //点击首页找地铁
    public static String HOMECLICK_LUCK = "1016"; //点击首页测运气
    public static String HOMEVOICE_FINDPOI = "1017"; //首页语音找商铺
    public static String HOMEVOICE_FINDFOOD = "1018"; //首页语音找美食
    public static String HOMEVOICE_FINDBUS = "1019"; //首页语音找公交
    public static String HOMEVOICE_FINDSUBWAY = "1020"; //首页语音找地铁
    public static String HOMEVOICE_LUCK = "1021"; //首页语音测运气
    public static String HOME_NAV = "1025"; //首页进入路径规划页面
    public static String SEARCH_NAV = "1029"; //搜索页面进入路径规划页面

    public static Map<String, String> statisticsMap = new HashMap<>();

    public static void init() {
        statisticsMap.put("robot", RobotManager.getInstance().getRobotId());
    }

}
