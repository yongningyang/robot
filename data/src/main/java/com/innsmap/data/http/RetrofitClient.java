package com.innsmap.data.http;

import android.content.Context;

import com.innsmap.data.http.cookie.CookieJarImpl;
import com.innsmap.data.http.cookie.store.PersistentCookieStore;
import com.innsmap.data.http.interceptor.BaseInterceptor;
import com.innsmap.data.http.interceptor.CacheInterceptor;
import com.innsmap.data.http.interceptor.DomainInterceptor;
import com.innsmap.data.http.interceptor.logging.Level;
import com.innsmap.data.http.interceptor.logging.LoggingInterceptor;
import com.innsmap.data.util.KLog;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author yongningyang@gmail.com
 * @date 2018/11/27
 * @Description
 */
public class RetrofitClient {
    //超时时间
    private final int DEFAULT_TIMEOUT = 20;
    //缓存时间
    private final int CACHE_TIMEOUT = 10 * 1024 * 1024;

    private Context mContext = null;

    private OkHttpClient okHttpClient;
    private Retrofit retrofit;

    private Cache cache = null;
    private File httpCacheDirectory;

    /**
     * 初始化Retrofit
     *
     * @param context
     * @param baseUrl       接口的域名
     * @param headers       请求的头
     * @param otherBaseUrls 如果存在多域名，则除主域名外其他域名以map传入。Key是域名名称（如：DOMAIN_LOGIN），Value是域名（如：http://gorilla.innsmap.com/）
     */
    public static void init(Context context, String baseUrl, Map<String, String> headers, Map<String, String> otherBaseUrls) {
        if (INSTANCE == null) {
            synchronized (RetrofitClient.class) {
                if (INSTANCE == null) {
                    if (otherBaseUrls != null && !otherBaseUrls.isEmpty()) {
                        final DomainInterceptor domainInterceptor = new DomainInterceptor(baseUrl, otherBaseUrls);
                        INSTANCE = new RetrofitClient(context, baseUrl, domainInterceptor, headers);
                    }
                }
            }
        }
    }

    private static RetrofitClient INSTANCE = null;

    public static Retrofit getRetrofit() {
        return INSTANCE.retrofit;
    }


    private RetrofitClient(Context context, String baseUrl, DomainInterceptor domainInterceptor, Map<String, String> headers) {
        this.mContext = context;
        if (httpCacheDirectory == null) {
            httpCacheDirectory = new File(mContext.getCacheDir(), "http_cache");
        }

        try {
            if (cache == null) {
                cache = new Cache(httpCacheDirectory, CACHE_TIMEOUT);
            }
        } catch (Exception e) {
            KLog.e("Could not create http cache", e);
        }
        this.mContext = context;
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        okHttpClient = new OkHttpClient.Builder()
                .cookieJar(new CookieJarImpl(new PersistentCookieStore(mContext)))
                .cache(cache)
                .addInterceptor(new BaseInterceptor(headers))
                .addInterceptor(domainInterceptor)
                .addInterceptor(new CacheInterceptor(mContext))
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .addInterceptor(new LoggingInterceptor
                        .Builder()//构建者模式
                        .loggable(true) //是否开启日志打印
                        .setLevel(Level.BASIC) //打印的等级
                        .log(Platform.INFO) // 打印类型
                        .request("Request") // request的Tag
                        .response("Response")// Response的Tag
                        .addHeader("log-header", "I am the log request header.") // 添加请求头, 注意 key 和 value 都不能是中文
                        .build()
                )
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(8, 15, TimeUnit.SECONDS))
                .build();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }

    /**
     * create you ApiService
     * Create an implementation of the API endpoints defined by the {@code service} interface.
     */
    public static <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return getRetrofit().create(service);
    }

    /**
     * /**
     * execute your customer API
     * For example:
     * MyApiService service =
     * RetrofitClient.getRetrofit().create(MyApiService.class);
     * <p>
     * RetrofitClient.getRetrofit()
     * .execute(service.lgon("name", "password"), subscriber)
     * * @param subscriber
     */

    public static <T> T execute(Observable<T> observable, Observer<T> subscriber) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

        return null;
    }

    public static <T> T execute(Observable<T> observable, Consumer<? super T> onNext, Consumer<? super Throwable> onError) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, onError);

        return null;
    }
}
