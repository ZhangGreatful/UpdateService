package com.example.bin.updateservice;

/**
 * Created by bin on 2017/10/27.
 */

public interface UpdateDownloadListener {

    /**
     * 下载请求开始
     */
    public void onStarted();

    /**
     * 进度更新回掉
     * @param progress
     * @param downloadUrl
     */
    public void onProgressChanged(int progress,String downloadUrl);


    /**
     * 下载完成回掉
     * @param completeSize
     * @param downloadUrl
     */
    public void onFinished(int completeSize,String downloadUrl);


    /**
     * 下载失败回掉
     */
    public void onFailur();



}
