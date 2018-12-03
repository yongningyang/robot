package com.innsmap.versionchecker;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by Clevo on 2016/6/14.
 */
public class NotificationUtils {

    NotificationManager manager;
    NotificationCompat.Builder builder;
    Notification notification;
    RemoteViews contentView;

    int notificationId=-1;

    Context context;

    private long time = 0;

    public NotificationUtils(Context context, int notificationId) {
        this.context=context;
        this.notificationId=notificationId;

        manager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        builder=new NotificationCompat.Builder(context);

    }

    public void sample(String ticker, String title, String content, PendingIntent intent) {
        //状态栏文字
        builder.setTicker(ticker);
        //通知栏标题
        builder.setContentTitle(title);
        //通知栏内容
//        builder.setContentText(content);
        //触发的intent
        builder.setContentIntent(intent);
        //这边设置颜色，可以给5.0及以上版本smallIcon设置背景色
//        builder.setColor(Color.RED);
        //小图标
        builder.setSmallIcon(R.drawable.download);
        //大图标(这边同时设置了小图标跟大图标，在5.0及以上版本通知栏里面的样式会有所不同)
//        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
        //设置该条通知时间
        builder.setWhen(System.currentTimeMillis());
        //设置为true，点击该条通知会自动删除，false时只能通过滑动来删除
//        builder.setAutoCancel(false);
        //设置优先级，级别高的排在前面
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        //设置是否为一个正在进行中的通知，这一类型的通知将无法删除
        builder.setOngoing(true);
    }

    /**
     * 自定义通知视图
     * @param ticker
     * @param title
     * @param content
     */
    public void sendCustomerNotification(String ticker, String title, String content) {

        Intent clickIntent = new Intent(context, UpdateVersionUtils.NotificationClickReceiver.class); //点击通知之后要发送的广播
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        contentView = new RemoteViews(context.getPackageName(), R.layout.inns_library_version_update_notification);
        contentView.setTextViewText(R.id.notifi_name, title + "·");
        time = System.currentTimeMillis();
        contentView.setTextViewText(R.id.notifi_time, getDate());
        contentView.setTextViewText(R.id.notifi_name2, content);
        contentView.setTextViewText(R.id.notifi_pro, "正在下载: 0%");
        if (Constants.notificationLittleImage != null){
            contentView.setImageViewBitmap(R.id.notifi_icon_little, Constants.notificationLittleImage);
        }
        if (Constants.notificationBigImage != null){
            contentView.setImageViewBitmap(R.id.notifi_icon_big, Constants.notificationBigImage);
        }

        sample(ticker, title, content, pendingIntent);
        notification=builder.build();
        notification.contentView=contentView;
        send(notification);
    }

    int lastPro;

    public void updatePro(int pro){
        if (notification != null){
            if (pro - lastPro > 5 && pro < 100){
                notification.contentView.setTextViewText(R.id.notifi_pro, "正在下载: " + pro + "%");
                notification.contentView.setTextViewText(R.id.notifi_time, getDate());
                lastPro = pro;
                send(notification);
            } else if (pro == 100){
                lastPro = pro;
                notification.contentView.setTextViewText(R.id.notifi_pro, "下载完成");
                notification.contentView.setTextViewText(R.id.notifi_time, getDate());
                send(notification);
            }
        }
    }


    /**
     * 带进度条的通知栏
     * @param ticker
     * @param title
     * @param content
     * @param smallIcon
     * @param intent
     * @param sound
     * @param vibrate
     * @param lights
     */
    public void sendProgressNotification(String ticker, String title, String content, int smallIcon, PendingIntent intent, boolean sound, boolean vibrate, boolean lights) {
        sample(ticker, title, content, intent);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0;i<=100;i+=1) {
                    builder.setProgress(100, i, false);
                    send(builder.build());
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //下载完成
                builder.setContentText("下载完成").setProgress(0, 0, false);
                send(builder.build());
            }
        }).start();
    }

    private void send(Notification notification) {
        manager.notify(notificationId, notification);
    }

    public static boolean isDarkNotificationTheme(Context context) {
        return !isSimilarColor(Color.BLACK, getNotificationColor(context));
    }

    /**
     * 获取通知栏颜色
     * @param context
     * @return
     */
    public static int getNotificationColor(Context context) {
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context);
        Notification notification=builder.build();
        int layoutId=notification.contentView.getLayoutId();
        ViewGroup viewGroup= (ViewGroup) LayoutInflater.from(context).inflate(layoutId, null, false);
        if (viewGroup.findViewById(android.R.id.title)!=null) {
            return ((TextView) viewGroup.findViewById(android.R.id.title)).getCurrentTextColor();
        }
        return findColor(viewGroup);
    }

    private static int findColor(ViewGroup viewGroupSource) {
        int color=Color.TRANSPARENT;
        LinkedList<ViewGroup> viewGroups=new LinkedList<>();
        viewGroups.add(viewGroupSource);
        while (viewGroups.size()>0) {
            ViewGroup viewGroup1=viewGroups.getFirst();
            for (int i = 0; i < viewGroup1.getChildCount(); i++) {
                if (viewGroup1.getChildAt(i) instanceof ViewGroup) {
                    viewGroups.add((ViewGroup) viewGroup1.getChildAt(i));
                }
                else if (viewGroup1.getChildAt(i) instanceof TextView) {
                    if (((TextView) viewGroup1.getChildAt(i)).getCurrentTextColor()!=-1) {
                        color=((TextView) viewGroup1.getChildAt(i)).getCurrentTextColor();
                    }
                }
            }
            viewGroups.remove(viewGroup1);
        }
        return color;
    }

    private static boolean isSimilarColor(int baseColor, int color) {
        int simpleBaseColor=baseColor|0xff000000;
        int simpleColor=color|0xff000000;
        int baseRed=Color.red(simpleBaseColor)-Color.red(simpleColor);
        int baseGreen=Color.green(simpleBaseColor)-Color.green(simpleColor);
        int baseBlue=Color.blue(simpleBaseColor)-Color.blue(simpleColor);
        double value=Math.sqrt(baseRed*baseRed+baseGreen*baseGreen+baseBlue*baseBlue);
        if (value<180.0) {
            return true;
        }
        return false;
    }

    public void cancel(int id){
        manager.cancel(id);
    }

    public void cancelAll(){
        manager.cancelAll();
    }

    public String getDate() {
        long t = System.currentTimeMillis();
        int c = 60 * 1000;
        if (t - time > c){
            return (t - time) % c == 0?(t - time) / c + "分钟前":(t - time) / c + 1 + "分钟前";
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            Date currentTime = new Date();
            String dateString = formatter.format(currentTime);
            return dateString;
        }

    }
}
