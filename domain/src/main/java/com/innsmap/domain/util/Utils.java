package com.innsmap.domain.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.text.TextUtils;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.innsmap.InnsMap.INNSMapView3D;
import com.innsmap.InnsMap.net.http.domain.net.NetPoiPropBean;
import com.innsmap.InnsMap.net.http.listener.forout.NetMapLoadListener;
import com.innsmap.data.util.CommonUtil;
import com.innsmap.data.util.MathUtil;
import com.innsmap.domain.ConstantValue;
import com.innsmap.domain.RobotManager;
import com.innsmap.domain.bean.PoiData;
import com.robot.performlib.action.SpeakAction;
import com.robot.performlib.action.WakeupAction;
import com.robot.performlib.performs.RecognitionPerform;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by long on 2018/7/5.
 */

public class Utils {
    public static final String TAG = "Utils";

    public static void setPower(TextView textView) {
        if (RobotManager.getInstance().getBattery() != -1) {
            textView.setText("电量" + RobotManager.getInstance().getBattery() + "%");
        }
    }


    public static void loadMap(final INNSMapView3D innsMapView) {
        if (RobotManager.getInstance().isFloorInvalid() || innsMapView == null) return;

        innsMapView.loadMap(ConstantValue.buildingId, RobotManager.getInstance().getFloorId(), new NetMapLoadListener() {
            @Override
            public void onSuccess() {
                RobotManager.getInstance().goUpFloor();
            }

            @Override
            public void onFail(String msg) {

            }
        });
    }

    public static List<NetPoiPropBean> poiList2beanList(List<PoiData> list) {
        List<NetPoiPropBean> beans = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            beans.add(poi2poi(list.get(i)));
        }
        return beans;
    }

    public static List<PoiData> beanList2poiList(List<NetPoiPropBean> list) {
        List<PoiData> beans = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            beans.add(poi2poi(list.get(i)));
        }
        return beans;
    }

    public static List<PoiData> poiList2poiList(List<NetPoiPropBean> list) {
        List<PoiData> beans = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            beans.add(poi2poi(list.get(i)));
        }
        return beans;
    }

    public static NetPoiPropBean poi2poi(PoiData poiData) {
        NetPoiPropBean bean = new NetPoiPropBean();
        bean.setPoiId(poiData.getPoiId());
        bean.setBuildingId(poiData.getBuildingId());
        bean.setFloorId(poiData.getFloorId());
        bean.setBuildingName(poiData.getBuildingName());
        bean.setFloorName(poiData.getFloorName());
        bean.setFloorNum(poiData.getFloorNum());
        bean.setPhotoPath(poiData.getPhotoPath());
        bean.setPoiName(poiData.getPoiName());
        bean.setPoint(new PointF(poiData.getX(), poiData.getY()));
        return bean;
    }

    public static PoiData poi2poi(NetPoiPropBean poiBean) {
        PoiData poiData = new PoiData();
        poiData.setPoiId(poiBean.getPoiId());
        poiData.setBuildingId(poiBean.getBuildingId());
        poiData.setFloorId(poiBean.getFloorId());
        poiData.setBuildingName(poiBean.getBuildingName());
        poiData.setFloorName(poiBean.getFloorName());
        poiData.setFloorNum(poiBean.getFloorNum());
        poiData.setPhotoPath(poiBean.getPhotoPath());
        poiData.setPoiName(poiBean.getPoiName());
        poiData.setX(poiBean.getPoint().x);
        poiData.setY(poiBean.getPoint().y);
        return poiData;
    }

    public static String countTime(float distance) {
        int time = (int) (distance / 1.5);
        int minute = time / 60;
        int second = time % 60;
        return minute + "分" + second + "秒";

    }

    public static String countTime2(float distance) {
        int time = (int) (distance / 1.5);
        int minute = time / 60;
        int second = time % 60;
        if (second > 0) {
            minute++;
        }
        return minute + "分钟";

    }

    public static String exact(double totalPrice) {
        java.text.DecimalFormat myformat = new java.text.DecimalFormat("0.00");
        String str = myformat.format(totalPrice);
        return str;
    }

    public static float countDistance(Map<String, List<List<PointF>>> map) {
        Iterator<Map.Entry<String, List<List<PointF>>>> it = map.entrySet()
                .iterator();
        float distance = 0;
        while (it.hasNext()) {
            Map.Entry<String, List<List<PointF>>> entry = it.next();
            List<List<PointF>> mLists = entry.getValue();
            distance += distance(mLists);
        }
        return formatDouble(distance);
    }

    private static float distance(List<List<PointF>> mLists) {
        float distance = 0;
        List<PointF> mList = null;
        for (int i = 0; i < mLists.size(); i++) {
            mList = mLists.get(i);
            for (int j = 0; j < mList.size() - 1; j++) {
                distance += dist(mList.get(j).x, mList.get(j).y,
                        mList.get(j + 1).x, mList.get(j + 1).y);
            }
        }
        return distance;
    }

    public static float dist(float x1, float y1, float x2, float y2) {
        float d = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
        return (float) Math.sqrt(d);
    }

    private static float formatDouble(float d) {
        return (float) Math.round(d * 100) / 100;
    }

    /**
     * 返回每个按钮应该出现的角度(弧度单位)
     *
     * @param index
     * @return double 角度(弧度单位)
     */
    public static double getAngle(int total, int index) {

        return Math.toRadians(90 / (total - 1) * index + 90);
    }

    public static Bitmap generateBitmap(String content) {
        if (TextUtils.isEmpty(content)) return null;
        int width = 400;
        int height = 400;
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 1);
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height);
            encode = deleteWhite(encode);//删除白边
            width = encode.getWidth();
            height = encode.getHeight();
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static BitMatrix deleteWhite(BitMatrix matrix) {
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] + 1;
        int resHeight = rec[3] + 1;

        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i + rec[0], j + rec[1]))
                    resMatrix.set(i, j);
            }
        }
        return resMatrix;
    }

    /**
     * 根据一条折线获取箭头的3点线集合
     *
     * @param step     步长
     * @param map
     * @param floorIds
     * @return
     */
    public static Map<String, List<PointF>> getArrowList(float step, Map<String, List<List<PointF>>> map, List<String> floorIds) {

        if (step <= 0 || CommonUtil.isEmpty(map))
            return null;

        Map<String, List<PointF>> arrowMap = new HashMap<>();
        Set<String> key = map.keySet();
        floorIds.clear();
//        for (String s : key) {
        String s = ConstantValue.floorIdB1;
        floorIds.add(s);
        List<List<PointF>> list = map.get(s);
        List<PointF> arrowList = new ArrayList<PointF>();

        int stepNum = 0; // 步数
        float stepHead = step; // 步头，计算时每次需要加上步头
        for (List<PointF> pointList : list) {
            for (int j = 0; j < pointList.size() - 1; j++) {
                PointF A = pointList.get(j);
                PointF B = pointList.get(j + 1);
                if (A == null || B == null) // 有错误的空点出现
                    continue;
                float spacing = MathUtil.spacing(A, B);

                List<PointF> separate = MathUtil.separateLine(stepHead, step, A, B);
                for (PointF f : separate) {
                    if (f == null)
                        continue;
                    float x = f.x;
                    float y = f.y;
                    arrowList.add(new PointF(x, y));
                    stepNum++;
                }
                // 超过AB两点总长后
                if (stepNum == 0) {
                    stepHead = stepHead - spacing;
                } else {
                    stepHead = stepHead + step * stepNum - spacing;
                }
                stepNum = 0;
            }
            arrowList.add(pointList.get(pointList.size() - 1));
        }
        arrowMap.put(s, arrowList);
//        }
        return arrowMap;
    }

    public static boolean lowBattery(Context context, boolean isTalk) {
        int battery = RobotManager.getInstance().getBattery();
        if (!isTalk && RobotManager.getInstance().getRobotStatus() != 2 && battery != -1 && battery <= ConstantValue.batteryThreshold) {
            SpeakAction.getInstance().speak(context, ConstantValue.chargeText_1);
            RobotManager.getInstance().setRobotStatus(2);
            return true;
        }
        return false;
    }


    public static double angle(List<List<PointF>> mLists) {
        PointF f1 = null, f2 = null;
        List<PointF> list = mLists.get(0);
        for (int j = 0; j < list.size() - 1; j++) {
            f1 = list.get(j);
            f2 = list.get(j + 1);
            if ((Math.abs(f1.x - f2.x) > 0.1 || Math.abs(f2.y - f1.y) > 0.1)) {
                break;
            }
        }
        if (f1 == null) return 0;
        PointF f3 = new PointF(f1.x, f1.y - 1);
        double a = angle(f1, f2, f3);
        if (f2.x > f1.x) {
            a = -a;
        } else if (f2.x < f1.x) {

        } else {
            if (f2.y > f1.y) {
                a = 180;
            } else {
                a = 0;
            }
        }

        double robot = 0;
        robot = RobotManager.getInstance().angle.get() - ConstantValue.angleN;
        a = a - robot;
        if (a > 180) {
            a = a - 360;
        } else if (a < -180) {
            a = 360 + a;
        }
        return a;
    }

    public static void turn(double a, Context context) {
        double c = 60;
        if (a < 0) {
            c = -c;
        }
        int num = (int) (a / c) * 2;
        for (int i = 0; i < num; i++) {
            RecognitionPerform.create().turnAround(context, (float) Math.toRadians(c));
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (a % c != 0) {
            RecognitionPerform.create().turnAround(context, (float) Math.toRadians(a % c));
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            RecognitionPerform.create().turnAround(context, (float) Math.toRadians(a % c));
        }
    }


    /**
     * @param o 中间的点
     * @param s 起点
     * @param e 终点
     * @return
     */
    public static double angle(PointF o, PointF s, PointF e) {
        double cosfi = 0, fi = 0, norm = 0;
        double dsx = s.x - o.x;
        double dsy = s.y - o.y;
        double dex = e.x - o.x;
        double dey = e.y - o.y;

        cosfi = dsx * dex + dsy * dey;
        norm = (dsx * dsx + dsy * dsy) * (dex * dex + dey * dey);
        cosfi /= Math.sqrt(norm);

        if (cosfi >= 1.0) return 0;
        if (cosfi <= -1.0) return 180;
        fi = Math.acos(cosfi);

        if (180 * fi / Math.PI < 180) {
            return 180 * fi / Math.PI;
        } else {
            return 360 - 180 * fi / Math.PI;
        }

    }

    /**
     * 检测当前机器人是否处于唤醒状态 没有唤醒则每隔20 秒唤醒一次
     *
     * @param sleepTime
     * @param context
     */
    public static int checkWakeUp(int sleepTime, Context context) {
        if (!RobotManager.getInstance().isRobotWakeUp()) {
            if (sleepTime >= ConstantValue.wakeUpTime) {
                sleepTime = 0;
                WakeupAction.AIUIWakeUp(context, 0);
            } else {
                sleepTime += 1;
            }
        }
        return sleepTime;
    }

    /**
     * 每隔2小时清空地图缓存
     *
     * @param cacheMap
     */
    public static int clearMapCache(int cacheMap) {
        if (cacheMap >= ConstantValue.CLEARMAPCACHE) {
            RobotManager.getInstance().setClearMapCache(true);
            RobotManager.getInstance().setIsUpdatePoiData(true);
            return cacheMap;
        } else {
            return cacheMap + 1;
        }
    }

    /**
     * 获取今天的日期 yyyy-MM-dd
     *
     * @return
     */
    public static String getTimeShort() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date currentTime = new Date();
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 指定时间转为long
     *
     * @return
     */
    public static long getWorkTime(String time) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm");
        Date date;
        try {
            date = dateFormat.parse(getTimeShort() + time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static List<PoiData> searchPoi(String poiName) {
        if (CommonUtil.isEmpty(poiName)) return null;
        poiName = poiName.toLowerCase();
        List<PoiData> showPoi = new ArrayList<>();
        for (int i = 0; i < ConstantValue.floorIds.length; i++) {
            List<PoiData> list = RobotManager.getInstance().getPoiData(ConstantValue.floorIds[i]);
            for (int j = 0; !CommonUtil.isEmpty(list) && j < list.size(); j++) {
                String name = list.get(j).getPoiName();
                if (!CommonUtil.isEmpty(name)) name = name.toLowerCase();
                String pinyin = list.get(j).getPinyin();
                if (!CommonUtil.isEmpty(pinyin)) pinyin = pinyin.toLowerCase();
                String firstSpell = list.get(j).getFirstSpell();
                if (!CommonUtil.isEmpty(firstSpell)) firstSpell = firstSpell.toLowerCase();

                if (!CommonUtil.isEmpty(name)
                        && name.contains(poiName)) {
                    showPoi.add(list.get(j));
                } else if (!CommonUtil.isEmpty(pinyin)
                        && pinyin.contains(poiName)) {
                    showPoi.add(list.get(j));
                } else if (!CommonUtil.isEmpty(firstSpell)
                        && firstSpell.contains(poiName)) {
                    showPoi.add(list.get(j));
                } else {
                    String[] alias = list.get(j).getAlias();
                    if (alias == null || alias.length <= 0) continue;
                    for (String alia : alias) {
                        name = alia;
                        if (CommonUtil.isEmpty(name)) continue;
                        name = name.toLowerCase();
                        if (name.contains(poiName)) {
                            showPoi.add(list.get(j));
                            break;
                        }
                    }
                }
            }
        }
        return showPoi;
    }

    public static String getData() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    public static String getLogDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd HH:mm:ss.SSS");
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    public static PoiData updatePoi(PoiData poiData) {
        if (poiData != null && poiData.getPoiId() != 0) {
            switch (poiData.getPoiId()) {
                case 24390:
                    poiData.setPoiId(997260);
                    poiData.setFloorId(ConstantValue.floorIdF1);
                    break;
                case 24400:
                    poiData.setPoiId(997612);
                    poiData.setFloorId(ConstantValue.floorIdF1);
                    break;
                case 24191:
                    poiData.setPoiId(997262);
                    poiData.setFloorId(ConstantValue.floorIdF1);
                    break;
                case 24201:
                    poiData.setPoiId(997261);
                    poiData.setFloorId(ConstantValue.floorIdF1);
                    break;
                case 23845:
                    poiData.setPoiId(997601);
                    poiData.setFloorId(ConstantValue.floorIdF1);
                    break;
                case 23767:
                    poiData.setPoiId(997922);
                    poiData.setFloorId(ConstantValue.floorIdF1);
                    break;
                case 24714:
                    poiData.setPoiId(12190);
                    poiData.setFloorId(ConstantValue.floorIdF1);
                    break;
                case 999939:
                    poiData.setPoiId(999825);
                    poiData.setFloorId(ConstantValue.floorIdF1);
                    break;
                case 999938:
                    poiData.setPoiId(999824);
                    poiData.setFloorId(ConstantValue.floorIdF1);
                    break;
                case 10213:
                    poiData.setPoiId(9275);
                    poiData.setFloorId(ConstantValue.floorIdF1);
                    break;
                case 10223:
                    poiData.setPoiId(9275);
                    poiData.setFloorId(ConstantValue.floorIdF1);
                    break;
                case 15485://国贸大厦A座
                    poiData.setPoiId(18304);
                    poiData.setFloorId(ConstantValue.floorIdF1);
                    break;
                case 997803://国贸大酒店
                    poiData.setPoiId(999818);
                    poiData.setFloorId(ConstantValue.floorIdF1);
                    break;
                case 16047://新国贸饭店
                    poiData.setPoiId(999817);
                    poiData.setFloorId(ConstantValue.floorIdF1);
                    break;
                case 16315://国贸大厦B座
                    poiData.setPoiId(18441);
                    poiData.setFloorId(ConstantValue.floorIdF1);
                    break;
                case 14917://写字楼2座
                    poiData.setPoiId(28143);
                    poiData.setFloorId(ConstantValue.floorIdF1);
                    break;
                case 21120://写字楼1座
                    poiData.setPoiId(14571);
                    poiData.setFloorId(ConstantValue.floorIdF1);
                    break;
                case 15236://中国大饭店
                    poiData.setPoiId(999836);
                    poiData.setFloorId(ConstantValue.floorIdF2);
                    break;
            }
        }
        return poiData;
    }

    /**
     * 获取版本名称
     * @param context
     * @return
     */
    public static String getVerName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (Exception e) {
        }
        return "";
    }
}
