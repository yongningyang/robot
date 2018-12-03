package com.innsmap.data.http.interceptor;

import android.text.TextUtils;

import com.innsmap.data.util.CommonUtil;
import com.innsmap.data.util.ListUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author yongningyang@gmail.com
 * @date 2018/11/27
 * @Description
 */
public class DomainInterceptor implements Interceptor {

    public static final String DOMAIN_BASE_URL_NAME = "DOMAIN_BASE_URL_NAME";

    private final Map<String, String> mCache = new HashMap<>();
    private final Map<String, HttpUrl> mDomainNameHub = new HashMap<>();

    private final HttpUrl defaultDomain;

    public DomainInterceptor(String mainBaseUrl, Map<String, String> otherBaseUrls) {
        defaultDomain = checkUrl(mainBaseUrl);
        if (!CommonUtil.isEmpty(otherBaseUrls)) {
            ListUtil.forEach(otherBaseUrls.keySet(), (key) -> mDomainNameHub.put(key, checkUrl(otherBaseUrls.get(key))));
        }

    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        return chain.proceed(processRequest(chain.request()));
    }

    /**
     * 对 {@link Request} 进行一些必要的加工, 执行切换 BaseUrl 的相关逻辑
     *
     * @param request {@link Request}
     * @return {@link Request}
     */
    public Request processRequest(Request request) {
        if (request == null) return request;

        Request.Builder newBuilder = request.newBuilder();
        HttpUrl httpUrl = defaultDomain;

        String domainName = obtainDomainNameFromHeaders(request);

        // 如果有 header,获取 header 中 domainName 所映射的 url,若没有,则检查全局的 BaseUrl,未找到则为null
        if (!TextUtils.isEmpty(domainName)) {
            httpUrl = fetchDomain(domainName);
            newBuilder.removeHeader(DOMAIN_BASE_URL_NAME);
        } else if (!TextUtils.isEmpty(request.url().host())) {//全路径，不替换
            return request;
        }

        if (null != httpUrl) {
            HttpUrl newUrl = parseUrl(httpUrl, request.url());

            return newBuilder
                    .url(newUrl)
                    .build();
        }

        return newBuilder.build();

    }

    /**
     * 从 {@link Request#header(String)} 中取出 DomainName
     *
     * @param request {@link Request}
     * @return DomainName
     */
    private String obtainDomainNameFromHeaders(Request request) {
        List<String> headers = request.headers(DOMAIN_BASE_URL_NAME);
        if (headers == null || headers.size() == 0)
            return null;
        if (headers.size() > 1)
            throw new IllegalArgumentException("Only one Domain_Name in the headers");
        return request.header(DOMAIN_BASE_URL_NAME);
    }

    /**
     * 取出对应 {@code domainName} 的 Url(BaseUrl)
     *
     * @param domainName
     * @return
     */
    public synchronized HttpUrl fetchDomain(String domainName) {
        checkNotNull(domainName, "domainName cannot be null");
        return mDomainNameHub.get(domainName);
    }

    private HttpUrl checkUrl(String url) {
        HttpUrl parseUrl = HttpUrl.parse(url);
        if (null == parseUrl) {
            return HttpUrl.parse("http://www.innsmap.com");
        } else {
            return parseUrl;
        }
    }

    private <T> T checkNotNull(T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }

    public HttpUrl parseUrl(HttpUrl domainUrl, HttpUrl url) {
        // 如果 HttpUrl.parse(url); 解析为 null 说明,url 格式不正确,正确的格式为 "https://github.com:443"
        // http 默认端口 80, https 默认端口 443, 如果端口号是默认端口号就可以将 ":443" 去掉
        // 只支持 http 和 https

        if (null == domainUrl) return url;

        HttpUrl.Builder builder = url.newBuilder();

        if (TextUtils.isEmpty(mCache.get(getKey(domainUrl, url)))) {
            for (int i = 0; i < url.pathSize(); i++) {
                //当删除了上一个 index, PathSegment 的 item 会自动前进一位, 所以 remove(0) 就好
                builder.removePathSegment(0);
            }

            List<String> newPathSegments = new ArrayList<>();
            newPathSegments.addAll(domainUrl.encodedPathSegments());
            newPathSegments.addAll(url.encodedPathSegments());

            for (String PathSegment : newPathSegments) {
                builder.addEncodedPathSegment(PathSegment);
            }
        } else {
            builder.encodedPath(mCache.get(getKey(domainUrl, url)));
        }

        HttpUrl httpUrl = builder
                .scheme(domainUrl.scheme())
                .host(domainUrl.host())
                .port(domainUrl.port())
                .build();

        if (TextUtils.isEmpty(mCache.get(getKey(domainUrl, url)))) {
            mCache.put(getKey(domainUrl, url), httpUrl.encodedPath());
        }
        return httpUrl;
    }

    private String getKey(HttpUrl domainUrl, HttpUrl url) {
        return domainUrl.encodedPath() + url.encodedPath();
    }

    protected HttpUrl getDefaultBaseUrl() {
        return defaultDomain;
    }
}
