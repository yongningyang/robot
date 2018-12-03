package com.innsmap.versionchecker;

import com.innsmap.data.http.BaseResponse;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author yongningyang@gmail.com
 * @date 2018/12/3
 * @Description
 */
public interface UpdateService {

    @POST("http://update.innsmap.com/update/app/getVersion/resource/user/login")
    Observable<BaseResponse<VersionUpdateInfo>> getVersion(@Query("appId") String appId);

}
