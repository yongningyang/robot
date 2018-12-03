package com.innsmap.domain.util;


import android.graphics.PointF;

import com.innsmap.data.http.BaseResponse;
import com.innsmap.data.util.CommonUtil;
import com.innsmap.domain.ConstantValue;
import com.innsmap.domain.RobotManager;
import com.innsmap.domain.bean.AdvData;
import com.innsmap.domain.bean.ChargeData;
import com.innsmap.domain.bean.ChargeGroupData;
import com.innsmap.domain.bean.EventData;
import com.innsmap.domain.bean.LoginBean;
import com.innsmap.domain.bean.NeedBase;
import com.innsmap.domain.bean.NetHotPoiData;
import com.innsmap.domain.bean.NetPoiData;
import com.innsmap.domain.bean.NetPoiDataList;
import com.innsmap.domain.bean.PoiAliasData;
import com.innsmap.domain.bean.PoiData;
import com.innsmap.domain.bean.RobotPointData;

import java.util.ArrayList;
import java.util.List;

public class TransformUtil {

    // 登陆接口的转换
    public static LoginBean loginTrans(BaseResponse<LoginBean> back) {
        LoginBean bean = new LoginBean();
        if (back == null) {
            bean.setSucceed(false);
            return bean;
        }
        bean.setErrerMsg(ConstantValue.HTTP_BACK_NULL);
        switch (back.getCode()) {
            case ConstantValue.NETWORK_SUCCEED_CODE:
                bean = back.getData();
                bean.setSucceed(true);
                break;
            case ConstantValue.LOGIN_FAIL_CODE_1:
                bean.setSucceed(false);
                bean.setErrerMsg(ConstantValue.LOGIN_FAIL_DESC_1);
                break;
            case ConstantValue.LOGIN_FAIL_CODE_2:
                bean.setSucceed(false);
                bean.setErrerMsg(ConstantValue.LOGIN_FAIL_DESC_2);
                break;
            case ConstantValue.LOGIN_FAIL_CODE_3:
                bean.setSucceed(false);
                bean.setErrerMsg(ConstantValue.LOGIN_FAIL_DESC_3);
                break;
            case ConstantValue.LOGIN_FAIL_CODE_4:
                bean.setSucceed(false);
                bean.setErrerMsg(ConstantValue.LOGIN_FAIL_DESC_4);
                break;
            case ConstantValue.LOGIN_FAIL_CODE_5:
                bean.setSucceed(false);
                bean.setErrerMsg(ConstantValue.LOGIN_FAIL_DESC_5);
                break;
            default:
                bean.setSucceed(false);
                bean.setErrerMsg(ConstantValue.NETWORK_BACK_NOT_MARK);
                break;
        }
        return bean;
    }

    public static NeedBase<ArrayList<PoiData>> poiTrans(
            BaseResponse<NetPoiDataList> back, String floorId) {
        NeedBase<ArrayList<PoiData>> base = new NeedBase<ArrayList<PoiData>>();
        if (back == null || back.getData() == null) {
            base.setSucceed(false);
            base.setErrerMsg(ConstantValue.HTTP_BACK_NULL);
            return base;
        }
        switch (back.getCode()) {
            case ConstantValue.NETWORK_SUCCEED_CODE:
                base.setSucceed(true);
                ArrayList<PoiData> mAdapterBuildingBeans = new ArrayList<>();
                List<NetPoiData> list = back.getData().getDatalist();
                NetPoiData mData = null;
                PoiData poi = null;
                String floorName = null;
                if (!CommonUtil.isEmpty(floorId)) {
                    for (int i = 0; i < ConstantValue.floorIds.length; i++) {
                        if (floorId.equals(ConstantValue.floorIds[i])) {
                            floorName = ConstantValue.floorName[i];
                        }
                    }
                }
                for (int i = 0; i < list.size(); i++) {
                    mData = list.get(i);
                    poi = new PoiData();
                    poi.setPoiId(mData.getPoiId());
                    poi.setPoiName(mData.getPoiName());
                    poi.setAddr(mData.getAddr());
                    poi.setTel(mData.getTel());
                    poi.setPhotoPath(mData.getPhotoPath());
                    poi.setFloorId(floorId);
                    poi.setFloorName(floorName);
                    poi.setX(mData.getX());
                    poi.setY(mData.getY());
                    poi.setPinyin(PinyinUtils.getPingYin(mData.getPoiName()));
                    poi.setFirstSpell(PinyinUtils.getFirstSpell(mData.getPoiName()));
                    mAdapterBuildingBeans.add(poi);
                }
                base.setData(mAdapterBuildingBeans);
                floorName = null;
                break;
            case ConstantValue.GET_BUILDING_FAIL_CODE_1:
                base.setSucceed(false);
                base.setErrerMsg(ConstantValue.GET_BUILDING_FAIL_DESC_1);
                break;
            case ConstantValue.GET_BUILDING_FAIL_CODE_2:
                base.setErrerMsg(ConstantValue.GET_BUILDING_FAIL_DESC_2);
                base.setSucceed(false);
                break;
            case ConstantValue.GET_BUILDING_FAIL_CODE_3:
                base.setErrerMsg(ConstantValue.GET_BUILDING_FAIL_DESC_3);
                base.setSucceed(false);
                break;
            case ConstantValue.GET_BUILDING_FAIL_CODE_4:
                base.setErrerMsg(ConstantValue.GET_BUILDING_FAIL_DESC_4);
                base.setSucceed(false);
                break;
            case ConstantValue.GET_BUILDING_FAIL_CODE_5:
                base.setErrerMsg(ConstantValue.GET_BUILDING_FAIL_DESC_5);
                base.setSucceed(false);
                break;
            default:
                base.setSucceed(false);
                base.setErrerMsg(ConstantValue.NETWORK_BACK_NOT_MARK);
                break;
        }
        return base;
    }

    public static NeedBase<ArrayList<PoiData>> poiAliasTrans(
            BaseResponse<ArrayList<PoiAliasData>> back) {
        NeedBase<ArrayList<PoiData>> base = new NeedBase<ArrayList<PoiData>>();
        if (back == null || back.getData() == null) {
            base.setSucceed(false);
            base.setErrerMsg(ConstantValue.HTTP_BACK_NULL);
            return base;
        }
        switch (back.getCode()) {
            case ConstantValue.NETWORK_SUCCEED_CODE:
                base.setSucceed(true);
                ArrayList<PoiData> mAdapterBuildingBeans = new ArrayList<>();
                List<PoiAliasData> list = back.getData();
                PoiAliasData mData = null;
                PoiData poi = null;
                for (int i = 0; i < list.size(); i++) {
                    mData = list.get(i);
                    for (int j = 0; j < ConstantValue.floorIds.length; j++) {
                        List<PoiData> pois = RobotManager.getInstance().getPoiData(ConstantValue.floorIds[j]);
                        for (int k = 0; !CommonUtil.isEmpty(pois) && k < pois.size(); k++) {
                            if (mData.getPoiId() == pois.get(k).getPoiId()) {
                                poi = pois.get(k);
                                String ali = mData.getPoiAlias();
                                String[] alias = new String[0];
                                if (!CommonUtil.isEmpty(ali)) {
                                    alias = ali.split(",");
                                    for (int n = 0; n < alias.length; n++) {
                                        alias[n] = PinyinUtils.getPingYin(alias[n]);
                                    }
                                }
                                poi.setAlias(alias);
                                mAdapterBuildingBeans.add(poi);
                                break;
                            }
                        }
                    }
                }
                base.setData(mAdapterBuildingBeans);
                break;
            case ConstantValue.GET_BUILDING_FAIL_CODE_1:
                base.setSucceed(false);
                base.setErrerMsg(ConstantValue.GET_BUILDING_FAIL_DESC_1);
                break;
            case ConstantValue.GET_BUILDING_FAIL_CODE_2:
                base.setErrerMsg(ConstantValue.GET_BUILDING_FAIL_DESC_2);
                base.setSucceed(false);
                break;
            case ConstantValue.GET_BUILDING_FAIL_CODE_3:
                base.setErrerMsg(ConstantValue.GET_BUILDING_FAIL_DESC_3);
                base.setSucceed(false);
                break;
            case ConstantValue.GET_BUILDING_FAIL_CODE_4:
                base.setErrerMsg(ConstantValue.GET_BUILDING_FAIL_DESC_4);
                base.setSucceed(false);
                break;
            case ConstantValue.GET_BUILDING_FAIL_CODE_5:
                base.setErrerMsg(ConstantValue.GET_BUILDING_FAIL_DESC_5);
                base.setSucceed(false);
                break;
            default:
                base.setSucceed(false);
                base.setErrerMsg(ConstantValue.NETWORK_BACK_NOT_MARK);
                break;
        }
        return base;
    }

    public static NeedBase<ArrayList<PoiData>> hotPoiTrans(
            BaseResponse<ArrayList<NetHotPoiData>> back) {
        NeedBase<ArrayList<PoiData>> base = new NeedBase<ArrayList<PoiData>>();
        if (back == null || back.getData() == null) {
            base.setSucceed(false);
            base.setErrerMsg(ConstantValue.HTTP_BACK_NULL);
            return base;
        }
        switch (back.getCode()) {
            case ConstantValue.NETWORK_SUCCEED_CODE:
                base.setSucceed(true);
                ArrayList<PoiData> mAdapterBuildingBeans = new ArrayList<>();
                List<NetHotPoiData> list = back.getData();
                NetHotPoiData mData = null;
                PoiData poi = null;
                for (int i = 0; i < list.size(); i++) {
                    mData = list.get(i);
                    for (int j = 0; j < ConstantValue.floorIds.length; j++) {
                        List<PoiData> pois = RobotManager.getInstance().getPoiData(ConstantValue.floorIds[j]);
                        for (int k = 0; !CommonUtil.isEmpty(pois) && k < pois.size(); k++) {
                            if (mData.getPoiId() == pois.get(k).getPoiId()) {
                                poi = pois.get(k);
                                mAdapterBuildingBeans.add(poi);
                                break;
                            }
                        }
                    }
                }
                ConstantValue.hotPoi.clear();
                ConstantValue.hotPoi.addAll(mAdapterBuildingBeans);
                base.setData(mAdapterBuildingBeans);
                break;
            case ConstantValue.GET_BUILDING_FAIL_CODE_1:
                base.setSucceed(false);
                base.setErrerMsg(ConstantValue.GET_BUILDING_FAIL_DESC_1);
                break;
            case ConstantValue.GET_BUILDING_FAIL_CODE_2:
                base.setErrerMsg(ConstantValue.GET_BUILDING_FAIL_DESC_2);
                base.setSucceed(false);
                break;
            case ConstantValue.GET_BUILDING_FAIL_CODE_3:
                base.setErrerMsg(ConstantValue.GET_BUILDING_FAIL_DESC_3);
                base.setSucceed(false);
                break;
            case ConstantValue.GET_BUILDING_FAIL_CODE_4:
                base.setErrerMsg(ConstantValue.GET_BUILDING_FAIL_DESC_4);
                base.setSucceed(false);
                break;
            case ConstantValue.GET_BUILDING_FAIL_CODE_5:
                base.setErrerMsg(ConstantValue.GET_BUILDING_FAIL_DESC_5);
                base.setSucceed(false);
                break;
            default:
                base.setSucceed(false);
                base.setErrerMsg(ConstantValue.NETWORK_BACK_NOT_MARK);
                break;
        }
        return base;
    }

    public static NeedBase<ArrayList<EventData>> eventTrans(
            BaseResponse<ArrayList<EventData>> back) {
        NeedBase<ArrayList<EventData>> base = new NeedBase<ArrayList<EventData>>();
        if (back == null || back.getData() == null) {
            base.setSucceed(false);
            base.setErrerMsg(ConstantValue.HTTP_BACK_NULL);
            return base;
        }
        switch (back.getCode()) {
            case ConstantValue.NETWORK_SUCCEED_CODE:
                base.setSucceed(true);
                ArrayList<EventData> mAdapterBuildingBeans = new ArrayList<>();
                List<EventData> list = back.getData();
                for (int i = 0; i < list.size(); i++) {
                    EventData eventData = list.get(i);
                    String image = eventData.getImageAddress();
                    if (!CommonUtil.isEmpty(image)) {
                        String[] images = image.split(",");
                        eventData.setImageUrls(images);
                    }
                    String v = eventData.getVoiceCommand();
                    if (!CommonUtil.isEmpty(v)) {
                        String[] vs = v.split(",");
                        eventData.setVoiceCommands(vs);
                        for (int j = 0; j < vs.length; j++) {
                            vs[j] = PinyinUtils.getPingYin(vs[j]);
                        }
                        eventData.setVoiceCommandsPY(vs);
                    }
                    String url = eventData.getUrl();
                    if (!CommonUtil.isEmpty(url)) {
                        //https%3A%2F%2Fmp.weixin.qq.com%2Fs%2FKXWt68_Nh2SwJmnJ-ljMAQ
                        //https://mp.weixin.qq.com/s/KXWt68_Nh2SwJmnJ-ljMAQ
                        url = url.replace("http%3A%2F%2F", "http://");
                        url = url.replace("https%3A%2F%2F", "https://");
                        url = url.replaceAll("%2F", "/");
                        eventData.setUrl(url);
                    }
                }
                mAdapterBuildingBeans.addAll(list);
                ConstantValue.eventList.clear();
                ConstantValue.eventList.addAll(mAdapterBuildingBeans);
                base.setData(mAdapterBuildingBeans);
                break;
            case ConstantValue.GET_BUILDING_FAIL_CODE_1:
                base.setSucceed(false);
                base.setErrerMsg(ConstantValue.GET_BUILDING_FAIL_DESC_1);
                break;
            case ConstantValue.GET_BUILDING_FAIL_CODE_2:
                base.setErrerMsg(ConstantValue.GET_BUILDING_FAIL_DESC_2);
                base.setSucceed(false);
                break;
            case ConstantValue.GET_BUILDING_FAIL_CODE_3:
                base.setErrerMsg(ConstantValue.GET_BUILDING_FAIL_DESC_3);
                base.setSucceed(false);
                break;
            case ConstantValue.GET_BUILDING_FAIL_CODE_4:
                base.setErrerMsg(ConstantValue.GET_BUILDING_FAIL_DESC_4);
                base.setSucceed(false);
                break;
            case ConstantValue.GET_BUILDING_FAIL_CODE_5:
                base.setErrerMsg(ConstantValue.GET_BUILDING_FAIL_DESC_5);
                base.setSucceed(false);
                break;
            default:
                base.setSucceed(false);
                base.setErrerMsg(ConstantValue.NETWORK_BACK_NOT_MARK);
                break;
        }
        return base;
    }

    public static NeedBase<ChargeGroupData> transChargeData(BaseResponse<ChargeGroupData> back) {
        NeedBase<ChargeGroupData> base = new NeedBase<ChargeGroupData>();
        base = isEmpty(base, back);
        if (!base.isSucceed()) {
            return base;
        }
        base = isSucceed(base, back);
        if (!base.isSucceed()) {
            return base;
        }

        ArrayList<ChargeData> charge = back.getData().getCharge();
        ArrayList<ChargeData> chargeDatas = new ArrayList<>();
        if (!CommonUtil.isEmpty(charge)) {
            for (int i = 0; i < charge.size(); i++) {
                ChargeData chargeData = new ChargeData();
                chargeData.setId(charge.get(i).getId());
                chargeData.setCode(charge.get(i).getCode());
                chargeData.setValue(charge.get(i).getValue());
                chargeData.setDesc(charge.get(i).getDesc());
                chargeData.setGroup(charge.get(i).getGroup());
                chargeData.setRemarkLow(charge.get(i).getRemarkLow());
                chargeData.setRemarkHigh(charge.get(i).getRemarkHigh());
                if (!CommonUtil.isEmpty(chargeData.getValue())) {
                    chargeData.setChargeValue(Integer.parseInt(chargeData.getValue()));
                    if (chargeData.getCode().equals(ConstantValue.MIN_VAl)) {//低电量充电值
                        ConstantValue.batteryThreshold = chargeData.getChargeValue();
                    } else if (chargeData.getCode().equals(ConstantValue.MIN_WORK)) {//工作时间充电量
                        ConstantValue.batteryCeil = chargeData.getChargeValue();
                    }
                }
                chargeDatas.add(chargeData);
            }
        }
        ArrayList<ChargeData> work = back.getData().getWork();
        ArrayList<ChargeData> workData = new ArrayList<>();
        if (!CommonUtil.isEmpty(work)) {
            for (int i = 0; i < work.size(); i++) {
                ChargeData chargeData = new ChargeData();
                chargeData.setId(work.get(i).getId());
                chargeData.setCode(work.get(i).getCode());
                chargeData.setValue(work.get(i).getValue());
                chargeData.setDesc(work.get(i).getDesc());
                chargeData.setGroup(work.get(i).getGroup());
                chargeData.setRemarkLow(work.get(i).getRemarkLow());
                chargeData.setRemarkHigh(work.get(i).getRemarkHigh());
                if (!CommonUtil.isEmpty(chargeData.getValue())) {
                    String[] data = chargeData.getValue().split(",");
                    String[] allTime = new String[data.length * 2];
                    for (int j = 0; j < data.length; j++) {
                        String[] time = data[j].split("-");
                        allTime[j * 2] = time[0];
                        allTime[j * 2 + 1] = time[1];
                    }
                    chargeData.setWorkValue(allTime);
                    if (chargeData.getCode().equals(ConstantValue.AM)) {//上午工作时间
                        ConstantValue.amBegin = chargeData.getWorkValue()[0];
                        ConstantValue.amEnd = chargeData.getWorkValue()[1];
                    } else if (chargeData.getCode().equals(ConstantValue.PM)) {//下午工作时间
                        ConstantValue.pmBegin = chargeData.getWorkValue()[0];
                        ConstantValue.pmEnd = chargeData.getWorkValue()[1];
                    }
                }
                workData.add(chargeData);
            }
        }
        RobotManager.getInstance().clearWork();
        RobotManager.getInstance().arrangeWorks(workData);
        ChargeGroupData data = new ChargeGroupData();
        data.setCharge(chargeDatas);
        data.setWork(workData);
        base.setData(data);
        return base;
    }

    private static NeedBase isEmpty(NeedBase base, BaseResponse back) {
        if (back == null || back.getData() == null) {
            base.setSucceed(false);
            base.setErrerMsg(ConstantValue.HTTP_BACK_NULL);
            return base;
        }
        base.setSucceed(true);
        return base;
    }

    public static NeedBase<ArrayList<AdvData>> advTrans(
            BaseResponse<ArrayList<AdvData>> back) {
        NeedBase<ArrayList<AdvData>> base = new NeedBase<ArrayList<AdvData>>();

        base = isEmpty(base, back);
        if (!base.isSucceed()) {
            return base;
        }
        base = isSucceed(base, back);
        if (!base.isSucceed()) {
            return base;
        }
        base.setSucceed(true);
        ArrayList<AdvData> beans = new ArrayList<>();
        List<AdvData> list = back.getData();
        for (int i = 0; i < list.size(); i++) {
            AdvData advData = list.get(i);
            String image = advData.getImagePath();
            if (!CommonUtil.isEmpty(image)) {
                String[] images = image.split(",");
                advData.setImagePaths(images);
            }
            String url = advData.getUrl();
            if (!CommonUtil.isEmpty(url)) {
                //https%3A%2F%2Fmp.weixin.qq.com%2Fs%2FKXWt68_Nh2SwJmnJ-ljMAQ
                //https://mp.weixin.qq.com/s/KXWt68_Nh2SwJmnJ-ljMAQ
                url = url.replace("http%3A%2F%2F", "http://");
                url = url.replace("https%3A%2F%2F", "https://");
                url = url.replaceAll("%2F", "/");
                advData.setUrl(url);
            }
        }
        beans.addAll(list);
        ConstantValue.advList.clear();
        ConstantValue.advList.addAll(beans);
        base.setData(beans);

        return base;
    }

    public static NeedBase<RobotPointData> robotPointTrans(
            BaseResponse<RobotPointData> back) {
        NeedBase<RobotPointData> base = new NeedBase<RobotPointData>();

        base = isEmpty(base, back);
        if (!base.isSucceed()) {
            return base;
        }
        base = isSucceed(base, back);
        if (!base.isSucceed()) {
            return base;
        }
        base.setSucceed(true);
        RobotPointData bean = new RobotPointData();
        RobotPointData robotPointData = back.getData();

        bean.setBuildId(robotPointData.getBuildId());
        bean.setFloorId(robotPointData.getFloorId());
        bean.setX(robotPointData.getX());
        bean.setY(robotPointData.getY());

        RobotManager.getInstance().setRobotPosition(new PointF(bean.getX(), bean.getY()));
        base.setData(bean);

        return base;
    }

    private static NeedBase isSucceed(NeedBase base, BaseResponse back) {
        switch (back.getCode()) {
            case ConstantValue.NETWORK_SUCCEED_CODE:
                base.setSucceed(true);
                break;
            case ConstantValue.GET_BUILDING_FAIL_CODE_1:
                base.setSucceed(false);
                base.setErrerMsg(ConstantValue.GET_BUILDING_FAIL_DESC_1);
                break;
            case ConstantValue.GET_BUILDING_FAIL_CODE_2:
                base.setErrerMsg(ConstantValue.GET_BUILDING_FAIL_DESC_2);
                base.setSucceed(false);
                break;
            case ConstantValue.GET_BUILDING_FAIL_CODE_3:
                base.setErrerMsg(ConstantValue.GET_BUILDING_FAIL_DESC_3);
                base.setSucceed(false);
                break;
            case ConstantValue.GET_BUILDING_FAIL_CODE_4:
                base.setErrerMsg(ConstantValue.GET_BUILDING_FAIL_DESC_4);
                base.setSucceed(false);
                break;
            case ConstantValue.GET_BUILDING_FAIL_CODE_5:
                base.setErrerMsg(ConstantValue.GET_BUILDING_FAIL_DESC_5);
                base.setSucceed(false);
                break;
            default:
                base.setSucceed(false);
                base.setErrerMsg(ConstantValue.NETWORK_BACK_NOT_MARK);
                break;
        }
        return base;
    }

}
