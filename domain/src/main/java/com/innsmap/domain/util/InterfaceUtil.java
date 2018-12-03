package com.innsmap.domain.util;

import android.content.Context;
import android.graphics.PointF;

import com.innsmap.InnsMap.INNSMapSDKResource;
import com.innsmap.InnsMap.net.http.listener.forout.NetPathSearchListener;
import com.innsmap.data.http.NetworkUtil;
import com.innsmap.data.http.RetrofitClient;
import com.innsmap.data.util.CommonUtil;
import com.innsmap.data.util.KLog;
import com.innsmap.data.util.SPUtils;
import com.innsmap.data.util.ToastUtils;
import com.innsmap.domain.ConstantValue;
import com.innsmap.domain.RequestServes;
import com.innsmap.domain.RobotManager;
import com.innsmap.domain.bean.AdvData;
import com.innsmap.domain.bean.ChargeGroupData;
import com.innsmap.domain.bean.EventData;
import com.innsmap.domain.bean.LoginBean;
import com.innsmap.domain.bean.NeedBase;
import com.innsmap.domain.bean.PoiData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;


/**
 * Created by long on 2018/5/16.
 */

public class InterfaceUtil {

    private static InterfaceUtil mInstance;
    private static Context mContext;
    private final RequestServes apiServices = RetrofitClient.create(RequestServes.class);

    private InterfaceUtil() {
        super();
    }

    public static InterfaceUtil getInstance(Context context) {
        if (mInstance == null) {
            synchronized (InterfaceUtil.class) {
                if (mInstance == null) {
                    mInstance = new InterfaceUtil();
                    mContext = context;
                }
            }
        }
        return mInstance;
    }

    /**
     *
     */
    public void login() {
        final String userName = "guomao";
        final String password = "123456";
        final String signature = "ACCA46D623BC9F770E2B06FCD8859DE32F37F4EA";
        if (!NetworkUtil.isNetworkAvailable(mContext)) {
            ToastUtils.showToast(ConstantValue.NO_NET);
            return;
        }

        RetrofitClient.execute(apiServices.login(userName, MD5Util.getMD5(password), signature).map(back -> TransformUtil.loginTrans(back)),
                new DisposableObserver<LoginBean>() {
                    @Override
                    public void onNext(LoginBean loginBeanBaseResponse) {
                        getDataPris(loginBeanBaseResponse.getToken());
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getDataPris(final String token) {
        if (RobotManager.getInstance().isFloorInvalid()) return;

        String buildId = RobotManager.getInstance().getBuildingId();
        final String floorId = RobotManager.getInstance().getFloorId();

        getDataPris(token, buildId, floorId, arrayListNeedBase -> {
            if (!CommonUtil.isEmpty(arrayListNeedBase.getData())) {
                RobotManager.getInstance().putPoiData(floorId, arrayListNeedBase.getData());

                if (RobotManager.getInstance().isB1Floor(floorId)) {
                    for (int i = 0; i < arrayListNeedBase.getData().size(); i++) {
                        PoiData poi = arrayListNeedBase.getData().get(i);
                        for (int j = 0; j < ConstantValue.pidShort.length; j++) {
                            if (poi.getPoiId() == ConstantValue.pidShort[j]) {
                                PoiData p = new PoiData();
                                p.setFloorId(ConstantValue.floorIdB1);
                                p.setFloorName("B1");
                                p.setPoiId(poi.getPoiId());
                                p.setX(poi.getX());
                                p.setY(poi.getY());
                                ConstantValue.busPOI.add(p);
                            }

                        }
                    }
                }
            }


            if (RobotManager.getInstance().goUpFloor()) {
                getDataPris(token);
            } else {
                RobotManager.getInstance().setIsUpdatePoiData(false);
                RobotManager.getInstance().floorIndex.set(0);
                try {
                    getData();
                } catch (Exception e) {
                    KLog.d("accept: " + e.toString());
                }
            }
        }, throwable -> {
            if (RobotManager.getInstance().goUpFloor()) {
                getDataPris(token);
            }
        });
    }

    /**
     * 获取用户权限
     *
     * @param token
     * @param onNext
     * @param onError
     */
    private void getDataPris(String token, String buildId, final String floorId,
                             Consumer<NeedBase<ArrayList<PoiData>>> onNext,
                             Consumer<Throwable> onError) {
        if (!NetworkUtil.isNetworkAvailable(mContext)) {
            try {
                onError.accept(new Throwable(ConstantValue.NO_NET));
            } catch (Exception e) {
                e.printStackTrace();
            }
            ToastUtils.showToast(ConstantValue.NO_NET);
            return;
        }

        RetrofitClient.execute(apiServices.getDataPris(buildId, floorId, 1, 500, token).map(arg0 -> TransformUtil.poiTrans(arg0, floorId)),
                onNext,
                onError);

    }

    public void getData() {
        getPoiAlias(arrayListNeedBase -> {
        }, throwable -> {
        });
        getHotPoi(arrayListNeedBase -> {
        }, throwable -> {
        });
        getEventData(arrayListNeedBase -> {
        }, throwable -> {
        });
        getAdvData(arrayListNeedBase -> {
        }, throwable -> {
        });
        getChargeData(chargeGroupDataNeedBase -> {
        }, throwable -> {
        });
    }

    /**
     * 获取poi别名
     *
     * @param onNext
     * @param onError
     */
    public void getPoiAlias(Consumer<NeedBase<ArrayList<PoiData>>> onNext,
                            Consumer<Throwable> onError) {
        if (!NetworkUtil.isNetworkAvailable(mContext)) {
            try {
                onError.accept(new Throwable(ConstantValue.NO_NET));
            } catch (Exception e) {
                e.printStackTrace();
            }
            ToastUtils.showToast(ConstantValue.NO_NET);
            return;
        }

        RetrofitClient.execute(apiServices.getPoiAlias(ConstantValue.buildingId).map(poiAliasDataBaseBack -> TransformUtil.poiAliasTrans(poiAliasDataBaseBack)),
                onNext,
                onError);

    }

    public void getEventData(Consumer<NeedBase<ArrayList<EventData>>> onNext,
                             Consumer<Throwable> onError) {
        if (!NetworkUtil.isNetworkConnected(mContext)) {
            try {
                onError.accept(new Throwable(ConstantValue.NO_NET));
            } catch (Exception e) {
                e.printStackTrace();
            }
            ToastUtils.showToast(ConstantValue.NO_NET);
            return;
        }
        RetrofitClient.execute(apiServices.getEvent(ConstantValue.buildingId)
                .map(eventDataBaseBack -> TransformUtil.eventTrans(eventDataBaseBack)), onNext, onError);
    }

    public void getHotPoi(Consumer<NeedBase<ArrayList<PoiData>>> onNext,
                          Consumer<Throwable> onError) {
        if (!NetworkUtil.isNetworkAvailable(mContext)) {
            try {
                onError.accept(new Throwable(ConstantValue.NO_NET));
            } catch (Exception e) {
                e.printStackTrace();
            }
            ToastUtils.showToast(ConstantValue.NO_NET);
            return;
        }

        RetrofitClient.execute(apiServices.getHotPoi(ConstantValue.buildingId)
                .map(hotPoiDataBaseBack -> TransformUtil.hotPoiTrans(hotPoiDataBaseBack)), onNext, onError);
    }

    public void uploadLocation(Consumer<NeedBase<String>> onNext,
                               Consumer<Throwable> onError) {
        if (!NetworkUtil.isNetworkConnected(mContext)) {
            try {
                onError.accept(new Throwable(ConstantValue.NO_NET));
            } catch (Exception e) {
                e.printStackTrace();
            }
            ToastUtils.showToast(ConstantValue.NO_NET);
            return;
        }
        RetrofitClient.execute(apiServices.uploadRobotLocation(RobotManager.getInstance().getRobotId(), ConstantValue.buildingId,
                RobotManager.getInstance().getFloorId(), RobotManager.getInstance().getRobotPointX(), RobotManager.getInstance().getRobotPointY())
                .map(stringBaseBack -> {
                    NeedBase<String> need = new NeedBase<>();
                    if (stringBaseBack.getCode() == 1) {
                        need.setSucceed(true);
                    } else {
                        need.setSucceed(false);
                    }
                    need.setData(stringBaseBack.getData());
                    need.setErrerMsg(stringBaseBack.getMessage());
                    return need;
                }), onNext, onError);
    }

    public static void getChargeData(Consumer<NeedBase<ChargeGroupData>> onNext,
                                     Consumer<Throwable> onError) {
        if (!NetworkUtil.isNetworkConnected(mContext)) {
            try {
                onError.accept(new Throwable(ConstantValue.NO_NET));
            } catch (Exception e) {
                e.printStackTrace();
            }
            ToastUtils.showToast(ConstantValue.NO_NET);
            return;
        }
        RetrofitClient.execute(mInstance.apiServices.robotChargeConf()
                .map(back -> TransformUtil.transChargeData(back)), onNext, onError);

    }

    public static void getPath(boolean isCache) {
        if (!NetworkUtil.isNetworkConnected(mContext)) {
            ToastUtils.showToast(ConstantValue.NO_NET);
            return;
        }
        if (RobotManager.getInstance().isPoiAndPositionValid()) {
            int mapIndex = 0;
            int listIndex = 0;
            try {
                search(mapIndex, listIndex, isCache);
            } catch (Exception e) {
                KLog.d("accept: " + e.toString());
            }
        }
    }

    public static void search(final int mapIndex, final int listIndex, final boolean isUseCache) {
        if (mapIndex < ConstantValue.floorIds.length) {
            String floorId = ConstantValue.floorIds[mapIndex];
            final List<PoiData> list = RobotManager.getInstance().getPoiData(floorId);
            if (CommonUtil.isEmpty(list) || listIndex >= list.size()) {
                search(mapIndex + 1, 0, isUseCache);
                return;
            }
            final PoiData poi = list.get(listIndex);
            float d = SPUtils.getInstance().getFloat(poi.getPoiId() + "", 0.0f);
            if (isUseCache && d > 0) {
                int i = listIndex + 1;
                int m = mapIndex + 1;
                poi.setDistance(d);
                if (i < list.size()) {
                    search(mapIndex, i, isUseCache);
                } else {
                    if (m < ConstantValue.floorIds.length) {
                        search(m, 0, isUseCache);
                    }
                }

            } else {
                INNSMapSDKResource.searchPath(ConstantValue.buildingId, RobotManager.getInstance().getFloorId(), 0,
                        RobotManager.getInstance().getRobotPosition(), poi.getFloorId(), poi.getPoiId(), new PointF(poi.getX(), poi.getY()),
                        new NetPathSearchListener() {
                            @Override
                            public void onSuccess(Map<String, List<List<PointF>>> map) {
                                int i = listIndex + 1;
                                int m = mapIndex + 1;
                                poi.setDistance(Utils.countDistance(map));
                                SPUtils.getInstance().put(poi.getPoiId() + "", poi.getDistance());
                                if (i < list.size()) {
                                    search(mapIndex, i, isUseCache);
                                } else {
                                    if (m < ConstantValue.floorIds.length) {
                                        search(m, 0, isUseCache);
                                    }
                                }
                            }

                            @Override
                            public void onFail(String msg) {
                                int i = listIndex + 1;
                                int m = mapIndex + 1;
                                poi.setDistance(0);
                                SPUtils.getInstance().put(poi.getPoiId() + "", 0.0f);
                                if (i < list.size()) {
                                    search(mapIndex, i, isUseCache);
                                } else {
                                    if (m < ConstantValue.floorIds.length) {
                                        search(m, 0, isUseCache);
                                    }
                                }
                            }
                        });
            }
        }
    }

    /**
     * 获取广告
     *
     * @param onNext
     * @param onError
     */
    public void getAdvData(Consumer<NeedBase<ArrayList<AdvData>>> onNext,
                           Consumer<Throwable> onError) {
        if (!NetworkUtil.isNetworkConnected(mContext)) {
            try {
                onError.accept(new Throwable(ConstantValue.NO_NET));
            } catch (Exception e) {
                e.printStackTrace();
            }
            ToastUtils.showToast(ConstantValue.NO_NET);
            return;
        }
        RetrofitClient.execute(apiServices.getAdv(ConstantValue.buildingId)
                .map(advDataBaseBack -> TransformUtil.advTrans(advDataBaseBack)), onNext, onError);
    }

    public void getRobotPoint() {
        if (!NetworkUtil.isNetworkConnected(mContext)) {
            try {
                upload();
                getPath(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ToastUtils.showToast(ConstantValue.NO_NET);
            return;
        }
        RetrofitClient.execute(apiServices.getRobotPoint(RobotManager.getInstance().getRobotId())
                .map(robotPointDataBaseBack -> TransformUtil.robotPointTrans(robotPointDataBaseBack)), needBase -> getPath(true), throwable -> {
            upload();
            getPath(true);
        });
    }

    public void upload() {
        if (RobotManager.getInstance().getRobotPosition() != null) {
            uploadLocation(arrayListNeedBase -> {
            }, throwable -> {
            });
        }
    }

}
