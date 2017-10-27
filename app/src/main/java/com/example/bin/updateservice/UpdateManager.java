package com.example.bin.updateservice;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by bin on 2017/10/27.
 */

public class UpdateManager {

    private static UpdateManager manager;
    private ThreadPoolExecutor threadPoolExecutor;
    private UpdateDownloadRequest request;
    private UpdateManager(){
        threadPoolExecutor= (ThreadPoolExecutor) Executors.newCachedThreadPool();

    }

    static  {
        manager=new UpdateManager();
    }

    public static UpdateManager getInstance(){
return manager;
    }

    public void startDownloads( String downloadUrl,String localPath,UpdateDownloadListener listener){
        if (request!=null){
                return;
        }
        checkLocalFilePath(localPath);

        //开始真正下载任务
        request=new UpdateDownloadRequest(downloadUrl,localPath,listener);
        Future<?> future=threadPoolExecutor.submit(request);

    }
    //用来检查文件路径是否已经存在
    private void checkLocalFilePath(String localPath) {
        File dir=new File(localPath.substring(0,localPath.lastIndexOf("/")+1));
        if (!dir.exists()){
            dir.mkdir();
        }
        File file=new File(localPath);
        if (!file.exists()){
            try {
                file.createNewFile();

            }catch (Exception e){

            }
        }
    }
}
