package com.innsmap.domain.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.innsmap.data.util.CommonUtil;
import com.innsmap.data.util.SPUtils;
import com.innsmap.domain.RobotHeartbeatListener;
import com.innsmap.domain.RobotManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by long on 2018/9/25.
 */

public class LogcatHelper implements RobotHeartbeatListener {
    private static LogcatHelper INSTANCE = null;
    public static String PATH_LOGCAT;
    private LogDumper mLogDumper = null;
    public static String TAG = "LogCat";
    public static String path;
    public static String LOG = ".log";
    public static String ZIP = ".zip";
    public static File logFile;
    public static File zipFile;
    public static String FTP_SERVER = "101.201.54.173";
    public static String FTP_USERNAME = "uploadSoft";
    public static String FTP_PASSWORD = "innsmap123";
    public static int FTP_PORT = 26688;
    private File dataFile;
    private Context context;


    private boolean isStarted = false;
    private long logStopTime = Utils.getWorkTime("24:00");

    /**
     * 初始化目录
     */
    public void init(Context context) {
        this.context = context;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {// 优先保存到SD卡中
            PATH_LOGCAT = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + File.separator + "DATASHOWJI";
        } else {// 如果SD卡不存在，就保存到本应用的目录下
            PATH_LOGCAT = context.getFilesDir().getAbsolutePath()
                    + File.separator + "DATASHOWJI";
        }
        dataFile = new File(PATH_LOGCAT);
        if (!dataFile.exists()) {
            dataFile.mkdirs();
        }
    }

    public static LogcatHelper getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LogcatHelper(context);
        }
        return INSTANCE;
    }

    private LogcatHelper(Context context) {
        init(context);
    }

    public void start() {
        if (isStarted) {
            return;
        }

        isStarted = true;

        if (mLogDumper == null) {
            mLogDumper = new LogDumper(TAG, PATH_LOGCAT);
        }
        mLogDumper.start();

        RobotManager.getInstance().registerHeartbeatListener(TAG, this);
    }

    public void stop() {
        if (mLogDumper != null) {
            mLogDumper.stopLogs();
            mLogDumper = null;
        }

        isStarted = false;
    }

    @Override
    public void onReceive() {
        if (System.currentTimeMillis() > logStopTime) {
            LogcatHelper.getInstance(context).stop();
            logStopTime = Utils.getWorkTime("24:00");
            //压缩
            LogcatHelper.getInstance(context).filesToZip(LogcatHelper.path);
            //上传
            LogcatHelper.getInstance(context).upload(() -> {
                LogcatHelper.getInstance(context).delete();
                //开启
                LogcatHelper.getInstance(context).start();
                //更新距离数据
                InterfaceUtil.getPath(false);

                List<String> fileNames = LogcatHelper.getInstance(context).getFileName(false);
                List<String> fileNamesNoUp = new ArrayList<>();
                for (int i = 0; i < fileNames.size(); i++) {
                    String name = fileNames.get(i);
                    boolean b = (boolean) SPUtils.getInstance().getBoolean(name, false);
                    if (!b) {
                        fileNamesNoUp.add(name);
                    }
                }
                updateLog(fileNamesNoUp, 0);
            }, LogcatHelper.path);
        }
    }

    @Override
    public void onBack2StandBy() {

    }

    @Override
    public void onLowBatteryWarn(int batteryCountdown) {

    }

    private void updateLog(final List<String> fileNamesNoUp, final int index) {
        if (CommonUtil.isEmpty(fileNamesNoUp) || index >= fileNamesNoUp.size()) return;
        String name = fileNamesNoUp.get(index);
        //压缩
        LogcatHelper.getInstance(context).filesToZip(name);
        //上传
        LogcatHelper.getInstance(context).upload(() -> {
            if ((index + 1) >= fileNamesNoUp.size()) {

            } else {
                updateLog(fileNamesNoUp, index + 1);
            }
        }, name);
    }

    private class LogDumper extends Thread {

        private Process logcatProc;
        private BufferedReader mReader = null;
        private boolean mRunning = true;
        String cmds = null;
        private String TAG;
        //        private FileOutputStream out = null;
        private RandomAccessFile raf = null;

        public LogDumper(String Tag, String dir) {
            TAG = Tag;
            try {
                path = RobotManager.getInstance().getRobotId() + "@" + Utils.getData() + "@" + Utils.getVerName(context);
                logFile = new File(dir, path + LOG);
                raf = new RandomAccessFile(logFile, "rw");
                raf.seek(logFile.length());
            } catch (IOException e) {
                e.printStackTrace();
            }

            /**
             *
             * 日志等级：*:v , *:d , *:w , *:e , *:f , *:s
             *
             * 显示当前mPID程序的 E和W等级的日志.
             *
             * */

            // cmds = "logcat *:e *:w | grep \"(" + TAG + ")\"";
            // cmds = "logcat  | gre \"(" + TAG + ")\"";//打印所有日志信息
            cmds = "logcat -s " + TAG;//打印标签过滤信息
            // cmds = "logcat *:v *:d *:w *:e *:f *:s | grep \"(" + TAG + ")\"";

        }

        public void stopLogs() {
            mRunning = false;
        }

        @Override
        public void run() {
            try {
                logcatProc = Runtime.getRuntime().exec(cmds);
                mReader = new BufferedReader(new InputStreamReader(
                        logcatProc.getInputStream()), 1024);
                String line = null;
                while (mRunning && (line = mReader.readLine()) != null) {
                    if (!mRunning) {
                        break;
                    }
                    if (line.length() == 0) {
                        continue;
                    }
//                    if (out != null && line.contains(TAG)) {
//                        out.write((Utils.getLogDate() + " id: "+ ConstantValue.robotId +"  " + line + "\n").getBytes());
//                    }
                    if (raf != null && line.contains(TAG)) {
                        line = line.substring(line.indexOf(":"), line.length());
                        raf.write((Utils.getLogDate() + line + "\n").getBytes());
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (logcatProc != null) {
                    logcatProc.destroy();
                    logcatProc = null;
                }
                if (mReader != null) {
                    try {
                        mReader.close();
                        mReader = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (raf != null) {
                    try {
                        raf.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    raf = null;
                }
//                if (out != null) {
//                    try {
//                        out.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    out = null;
//                }

            }

        }
    }

    /**
     * 将文件打zip包
     *
     * @throws IOException
     */
    public void filesToZip(String path) {
        String targetFileName = PATH_LOGCAT + File.separator + path + ZIP;
        String logFileName = PATH_LOGCAT + File.separator + path + LOG;
        File logFile = new File(logFileName);
        File zipFile = new File(targetFileName);
        if (zipFile.exists()) {
            zipFile.delete();
        } else {
            File parent = zipFile.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
        }
        String fileOutName = targetFileName;
        byte[] buf = new byte[1024];
        FileInputStream in = null;
        FileOutputStream fos = null;
        ZipOutputStream out = null;
        try {
            fos = new FileOutputStream(fileOutName);
            out = new ZipOutputStream(fos);
            in = new FileInputStream(logFile);
            out.putNextEntry(new ZipEntry(logFile.getName()));
            int len;
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.closeEntry();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void upload(final OnUploadListener listener, final String path) {
        // 网络操作，但开一个线程进行处理
        final String sourceFilePath = PATH_LOGCAT + File.separator + path + ZIP;
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO 可以首先去判断一下网络
                FTPClientFunctions ftpClient = new FTPClientFunctions();
                boolean connectResult = ftpClient.ftpConnect(FTP_SERVER, FTP_USERNAME, FTP_PASSWORD, FTP_PORT);
                if (connectResult) {
                    boolean changeDirResult = ftpClient.ftpChangeDir("/guomao");
                    if (changeDirResult) {
                        boolean uploadResult = ftpClient.ftpUpload(sourceFilePath, path + ZIP, "");
                        if (listener != null) {
                            listener.onUploadEnd();
                        }
                        if (uploadResult) {
                            Log.w(TAG, "上传成功");
                            SPUtils.getInstance().put(path, true);
                            boolean disConnectResult = ftpClient.ftpDisconnect();
                            if (disConnectResult) {
                                Log.e(TAG, "关闭ftp连接成功");
                            } else {
                                Log.e(TAG, "关闭ftp连接失败");
                            }
                        } else {
                            Log.w(TAG, "上传失败");
                        }
                    } else {
                        Log.w(TAG, "切换ftp目录失败");
                    }
                } else {
                    Log.w(TAG, "连接ftp服务器失败");
                }
            }
        }).start();
    }

    public void delete() {
        if (dataFile == null || !dataFile.exists()) return;
        if (getFolderSize() < (500 * 1024 * 1024)) return;
        try {
            if (dataFile.isDirectory()) {// 处理目录
                File files[] = dataFile.listFiles();
                for (int i = 0; i < files.length; i++) {
                    File file = files[i];
                    if (!file.isDirectory()) {// 如果是文件，删除
                        String fileName = file.getName();
                        if (fileName.endsWith(LOG)) {
                            String s = fileName.substring(0, fileName.lastIndexOf("."));
                            if (!s.equals(Utils.getData())) {
                                boolean delete = file.delete();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnUploadListener {

        void onUploadEnd();

    }

    //读取指定目录下的所有TXT文件的文件名
    public List<String> getFileName(boolean isWithToDay) {
        File[] files = dataFile.listFiles();// 读取文件夹下文件
        List<String> list = new ArrayList<>();
        if (files != null) {  // 先判断目录是否为空，否则会报空指针
            for (File file : files) {
                if (!file.isDirectory()) {//检查此路径名的文件是否是一个目录(文件夹)
                    String fileName = file.getName();
                    if (fileName.endsWith(LOG)) {
                        String s = fileName.substring(0, fileName.lastIndexOf("."));
                        if (!isWithToDay) {
                            if (!s.equals(path)) {
                                list.add(s);
                            }
                        } else {
                            list.add(s);
                        }
                    }
                }
            }

        }
        return list;
    }

    public static void logD(String content) {
        Log.d(TAG, content);
    }

    /**
     * 2      * 获取文件夹大小
     * 3      * @param file File实例
     * 4      * @return long
     * 5
     */
    public long getFolderSize() {
        long size = 0;
        try {
            File[] fileList = dataFile.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (!fileList[i].isDirectory()) {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return size;
    }

    public static void logd(String code, String result, String desc) {
        Log.d(TAG, String.format("%s:%s:%s", code, result, desc));
    }

}
