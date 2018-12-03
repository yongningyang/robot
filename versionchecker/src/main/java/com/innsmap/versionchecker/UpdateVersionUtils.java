package com.innsmap.versionchecker;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.widget.Toast;

import com.innsmap.data.http.BaseResponse;
import com.innsmap.data.http.RetrofitClient;
import com.innsmap.data.util.CommonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.innsmap.versionchecker.Constants.updateInfo;


public class UpdateVersionUtils {
    public static final int DOWNLOAD = 1;
    public static final int DOWNLOAD_FINISH = 2;
    public final int GETVERSIONDATA = 1000;
    public String SERVER_ADDRESS = "http://update.innsmap.com/update/app/getVersion";// 软件更新包地址
    public String SERVER_IP = "http://update.innsmap.com";
    private VersionInfoListener listener;
    int downloadCount = 0;
    /* 下载保存路径 */
    private String mSavePath;
    /* 记录进度条数量 */
    private int progress;
    /* 是否取消更新 */
    private boolean cancelUpdate = false;
    //是否暂停
    private boolean pauseDown = false;

    private VersionUpdateDialog mUpdateDialog;
    public CustomProgressDialog mCustomProgressDialog;

    private Context mCpntext;

    public NotificationUtils notificationUtils;

    public UpdateVersionUtils(Context context) {
        this.mCpntext = context;
        init();
    }

    private void init() {

        notificationUtils = new NotificationUtils(mCpntext, 1000);

        mCustomProgressDialog = new CustomProgressDialog(mCpntext,
                new VersionUpdateDialog.DialogListener() {

                    @Override
                    public void onSure() {
                        notificationUtils.sendCustomerNotification("正在下载", Constants.downTitle, Constants.downContent);
                    }

                    @Override
                    public void onCancel() {
                        setCancelUpdate(true);
                    }
                });
    }

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UpdateVersionUtils.DOWNLOAD:
                    mCustomProgressDialog.setShowInfo(msg.arg2, msg.arg1);
                    break;
                case UpdateVersionUtils.DOWNLOAD_FINISH:
                    Toast.makeText(mCpntext, "下载完成", Toast.LENGTH_SHORT).show();
                    mCustomProgressDialog.dismiss();
                    update(updateInfo.appName);
                    break;
            }
        }
    };

    public boolean isCancelUpdate() {
        return cancelUpdate;
    }

    public void setCancelUpdate(boolean cancelUpdate) {
        this.cancelUpdate = cancelUpdate;
    }


    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    public static int getVerCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 获取版本名称
     *
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

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                String data = (String) msg.obj;
                Map<Integer, VersionUpdateInfo> map = pressData(data);
                if (!CommonUtil.isEmpty(map) && map.containsKey(GETVERSIONDATA)) {
                    updateInfo = map.get(GETVERSIONDATA);
                    dealWithVersionInfo();
                }
            }
        }
    };

    void dealWithVersionInfo() {
        if (updateInfo.versionCode > UpdateVersionUtils
                .getVerCode(mCpntext)) {

            mUpdateDialog = new VersionUpdateDialog(
                    mCpntext, updateInfo.updContent,
                    updateInfo.versionName,
                    new VersionUpdateDialog.DialogListener() {

                        @Override
                        public void onSure() {
                            Constants.isUpdate = true;
                            downloadFile(
                                    updateInfo.appUrl,
                                    updateInfo.appName,
                                    mHandler);
                            mCustomProgressDialog.show();

                        }

                        @Override
                        public void onCancel() {
                            if (updateInfo.isForce == 1) {
                                ((Activity) mCpntext).finish();
                            }
                        }
                    });
            mUpdateDialog.show();
        }
    }

    public void checkVersionInfo(String appId) {
        final Map<String, String> map = new HashMap<String, String>();
        map.put("appId", appId);


        RetrofitClient.getRetrofit().create(UpdateService.class).getVersion(appId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<VersionUpdateInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResponse<VersionUpdateInfo> versionUpdateInfoBaseResponse) {
                        updateInfo = versionUpdateInfoBaseResponse.getData();
                        dealWithVersionInfo();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void downloadFile(String path, String appName, Handler mHandler) {
        new downloadApkThread(path, appName, mHandler).start();
    }

    /**
     * 下载线程
     *
     * @author coolszy
     * @date 2012-4-26
     * @blog http://blog.92coding.com
     */
    private class downloadApkThread extends Thread {
        private String path = "";
        private String appName = "";
        private Handler mHandler;

        public downloadApkThread(String path, String appName, Handler mHandler) {
            this.path = path;
            this.appName = appName;
            this.mHandler = mHandler;
        }

        @Override
        public void run() {
            try {
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    String sdpath = Environment.getExternalStorageDirectory()
                            + "/";
                    mSavePath = sdpath + "download";
                    URL url = new URL(SERVER_IP + path);
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    conn.connect();
                    int length = conn.getContentLength();
                    InputStream is = conn.getInputStream();

                    File file = new File(mSavePath);
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    File apkFile = new File(mSavePath, appName);
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    byte buf[] = new byte[1024];

                    do {
                        if (!pauseDown) {
                            int numread = is.read(buf);
                            count += numread;
                            progress = (int) (((float) count / length) * 100);
                            Message msg = new Message();
                            msg.what = DOWNLOAD;
                            msg.arg1 = length;
                            msg.arg2 = count;
                            mHandler.sendMessage(msg);
                            notificationUtils.updatePro(progress);
                            if (numread <= 0) {
                                Constants.isUpdate = false;
                                mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                                System.out.println("down==");
                                break;
                            }
                            fos.write(buf, 0, numread);
                        }
                    } while (!cancelUpdate);//
                    fos.close();
                    is.close();
                    Constants.isUpdate = false;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                Constants.isUpdate = false;
            }
        }
    }

    /**
     * 安装程序
     *
     * @param apkName
     */
    public void update(final String apkName) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(mSavePath, apkName)),
                "application/vnd.android.package-archive");
        mCpntext.startActivity(intent);
    }

    public interface VersionInfoListener {
        void onGetVersionInfo(VersionUpdateInfo updateInfo);
    }

    public String getServerAddress() {
        return SERVER_ADDRESS;
    }

    public String getServerIp() {
        return SERVER_IP;
    }

    public static int getWindowWith(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    public Map<Integer, VersionUpdateInfo> pressData(String data) {
        if (CommonUtil.isEmpty(data)) return null;
        VersionUpdateInfo versionUpdateInfo = new VersionUpdateInfo();
        Map<Integer, VersionUpdateInfo> map = new HashMap<>();
        try {
            //{"code":1000,"message":null,"data":{"appName":"bigfoot","versionCode":5,
            // "versionName":"1.0.7","appUrl":"/source/app/update/bigfoot/1/1.0.7/bigfoot_release(aliyun)_1.0.7.apk",
            // "updContent":"<p>\r\n\t更新了SDK版本。\r\n</p>\r\n<p>\r\n\t修正了界面交互，优化用户体验。\r\n</p>"}}
            JSONObject jsonObject = new JSONObject(data);
            int code = jsonObject.optInt("code", 0);
            if (code != 1000) {
                return map;
            }
            JSONObject object = jsonObject.optJSONObject("data");
            versionUpdateInfo.appName = object.optString("appName");
            versionUpdateInfo.versionCode = object.optInt("versionCode");
            versionUpdateInfo.versionName = object.optString("versionName");
            versionUpdateInfo.appUrl = object.optString("appUrl");
            versionUpdateInfo.updContent = object.optString("updContent");
            versionUpdateInfo.isForce = object.optInt("isForce", 0);
            map.put(code, versionUpdateInfo);
            return map;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public class NotificationClickReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (progress != 100) {
                if (pauseDown) {
                    pauseDown = false;

                } else {
                    pauseDown = true;

                }
            } else if (progress == 100) {
                update(updateInfo.appName);
            }
        }
    }

    public void cancel() {
        notificationUtils.cancelAll();
    }

}
