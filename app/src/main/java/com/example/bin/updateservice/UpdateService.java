package com.example.bin.updateservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import java.io.File;

/**
 * Created by bin on 2017/10/27.
 */

public class UpdateService extends Service{

    private String apkUrl;
    private String filePath;
    private NotificationManager notificationManager;
    private Notification notification;





    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
         filePath= Environment.getExternalStorageDirectory()+"/haha/CoachAssistant.apk";

    }


    @Override
    public int onStartCommand(Intent intent, @IntDef(value = {Service.START_FLAG_REDELIVERY, Service.START_FLAG_RETRY}, flag = true) int flags, int startId) {
        if (intent!=null){
            notifyUser(getString(R.string.update_download_failed),
                    getString(R.string.update_download_failed_msg),0);
            stopSelf();
        }
        apkUrl=intent.getStringExtra("apkUrl");
        notifyUser(getString(R.string.update_download_start),getString(R.string.update_download_start),0);
        startDownload();
        return super.onStartCommand(intent, flags, startId);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startDownload(){
        UpdateManager.getInstance().startDownloads(apkUrl, filePath, new UpdateDownloadListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgressChanged(int progress, String downloadUrl) {
                notifyUser(getString(R.string.update_download_processing)
                        ,getString(R.string.update_download_processing),progress);
            }

            @Override
            public void onFinished(int completeSize, String downloadUrl) {
                notifyUser(getString(R.string.update_download_finish)
                        ,getString(R.string.update_download_finish),100);
            }

            @Override
            public void onFailur() {
notifyUser(getString(R.string.update_download_failed),getString(R.string.update_download_failed),0);
            }
        });
    }

    //更新我们的notification来告知用户当前下载的进度
    private void notifyUser(String result,String reason,int progress){
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher))
                .setContentTitle(R.string.app_name);
        if (progress>0&&progress<100){
            builder.setProgress(100,progress,false);
        }else {
            builder.setProgress(0,0,false);
        }
        builder.setAutoCancel(true);
        builder.setWhen(System.currentTimeMillis());
        builder.setTicker(result);
        builder.setContentIntent(progress>=100,getContentIntent():PendingIntent.getActivity(this,0,new Intent(),PendingIntent.FLAG_UPDATE_CURRENT));
notification=builder.build();
        notificationManager.notify(0,notification);

    }

    private PendingIntent getContentIntent(){
        File apkFile=new File(filePath);
        Intent intent=new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://"+apkFile.getAbsolutePath()),"application/vnd.android.package-archive");
        PendingIntent pendingIntent=PendingIntent.
                getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }
}
