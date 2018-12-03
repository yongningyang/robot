package com.innsmap.domain;

import com.innsmap.data.http.BaseResponse;
import com.innsmap.domain.bean.AdvData;
import com.innsmap.domain.bean.ChargeGroupData;
import com.innsmap.domain.bean.EventData;
import com.innsmap.domain.bean.LoginBean;
import com.innsmap.domain.bean.NetHotPoiData;
import com.innsmap.domain.bean.NetPoiDataList;
import com.innsmap.domain.bean.PoiAliasData;
import com.innsmap.domain.bean.RobotPointData;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RequestServes {

    @Headers({ConstantValue.HEADER_LOGIN_URL_DOMAIN})
    @POST("resource/user/login")
    Observable<BaseResponse<LoginBean>> login(@Query("username") String username,
                                              @Query("password") String password,
                                              @Query("signature") String signature);

    @Headers({ConstantValue.HEADER_LOGIN_URL_DOMAIN})
    @POST("resource/info/build/{buildId}/floor/{floorId}/poiList")
    Observable<BaseResponse<NetPoiDataList>> getDataPris(@Path("buildId") String buildId,
                                                         @Path("floorId") String floorId,
                                                         @Query("currentpage") int currentpage,
                                                         @Query("pagesize") int pagesize,
                                                         @Header("X-Auth-Token") String token);

    @Headers({ConstantValue.HEADER_DATA_URL_DOMAIN})
    @GET("robot/api/getPoiAlias/{buildId}")
    Observable<BaseResponse<ArrayList<PoiAliasData>>> getPoiAlias(@Path("buildId") String buildId);

    @Headers({ConstantValue.HEADER_DATA_URL_DOMAIN})
    @GET("robot/api/getActivity/{buildId}")
    Observable<BaseResponse<ArrayList<EventData>>> getEvent(@Path("buildId") String buildId);

    @Headers({ConstantValue.HEADER_DATA_URL_DOMAIN})
    @GET("robot/api/getHotPoi/{buildId}")
    Observable<BaseResponse<ArrayList<NetHotPoiData>>> getHotPoi(@Path("buildId") String buildId);

    @Headers({ConstantValue.HEADER_DATA_URL_DOMAIN})
    @POST("robot/api/uploadRobotSite")
    Observable<BaseResponse<String>> uploadRobotLocation(@Query("robotId") String robotId,
                                                         @Query("buildId") String buildId,
                                                         @Query("floorId") String floorId,
                                                         @Query("x") float x,
                                                         @Query("y") float y);

    @Headers({ConstantValue.HEADER_DATA_URL_DOMAIN})
    @GET("robot/api/getRobotSite/{robotId}")
    Observable<BaseResponse<RobotPointData>> getRobotPoint(@Path("robotId") String robotId);

    @Headers({ConstantValue.HEADER_DATA_URL_DOMAIN})
    @GET("robot/api/getAdvert/{buildId}")
    Observable<BaseResponse<ArrayList<AdvData>>> getAdv(@Path("buildId") String buildId);

    @Headers({ConstantValue.HEADER_DATA_URL_DOMAIN})
    @GET("robot/api/conf")
    Observable<BaseResponse<ChargeGroupData>> robotChargeConf();
}