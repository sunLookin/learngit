package com.instwall.xutilsdemo.net;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.math.BigDecimal;
import java.util.Map;

public class netGet {



    public static void get(String Url, Map<String,Object> map, final BackCallNet backCallNet){
        RequestParams requestParams = new RequestParams(Url);
        for (String key:map.keySet()){
            requestParams.addParameter(key,map.get(key));
        }
        //CommomCallback是通用的一个回调接口，参数String使我们需要返回的值
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                backCallNet.successful(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.getMessage();
                backCallNet.failed(ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    public static void post(String url, Map<String,Object> map, final BackCallNet backCallNet){
        RequestParams requestParams = new RequestParams(url);
        for (String key:map.keySet()){
            requestParams.addParameter(key,map.get(key));
        }

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                backCallNet.successful(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                backCallNet.failed(ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    //有缓存的时候，先走onCache方法，再走onSuccess方法，此时onCache方法中缓存内容返回，到onSuccess方法中，返回的是null，因为没有访问网络
    public static void cacheGet(String url, Map<String,Object> map, final BackCallNet  backCallNet){
        RequestParams requestParams = new RequestParams(url);
        requestParams.setCacheMaxAge(10*1000); //添加缓存的时间
        x.http().get(requestParams, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    backCallNet.successful(result);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                backCallNet.failed(ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public boolean onCache(String result) {
                //TODO 在setCacheMaxAge设置范围，如果再次调用Get请求
                //TODO 返回true：缓存内容返回，相信本地缓存。
                //TODO 返回false，缓存内容被返回，不相信本地缓存，任然请求网络
                backCallNet.cache("缓存" + result);
                return true;
            }
        });
    }

    public static void cachePost(String url, Map<String,Object> map, final BackCallNet backCallNet){
        RequestParams requestParams = new RequestParams(url);
        requestParams.setCacheMaxAge(10*1000);
        x.http().post(requestParams, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //在缓存期间，会调用这个方法，但是result为空
                if (result != null) {
                    backCallNet.successful(result);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                backCallNet.failed(ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public boolean onCache(String result) {
                backCallNet.cache(result);
                return true;
            }
        });
    }

    public static void uploadFile(String url, final BackCallNet backCallNet){
        String path = Environment.getExternalStorageDirectory().getPath();
        File file = new File(path + "/Document/test");
        final RequestParams requestParams = new RequestParams(url);
        requestParams.setMultipart(true);
        requestParams.addBodyParameter("file",file);
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    backCallNet.successful(result);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                backCallNet.failed(ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }


    public static void downFile(final Context context, String url, BackCallNet backCallNet){
        String path = Environment.getExternalStorageDirectory().getPath();
        final File file = new File(path + "/Document/qq");
        RequestParams requestParams = new RequestParams(url);
        requestParams.setSaveFilePath(file.getPath());

        x.http().post(requestParams, new Callback.ProgressCallback<File>() {
            @Override
            public void onSuccess(File result) {
                Log.d("stcLog","下载成功:" + result);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(result),"application/vnd.android.package-archive");
                context.startActivity(intent);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("stcLog","下载失败:" + ex.getMessage());

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                Log.d("stcLog","成功");

            }

            //网络请求之前回调
            @Override
            public void onWaiting() {
                Log.d("stcLog","准备开始下载");
            }

            //网络请求开始的时候回调
            @Override
            public void onStarted() {
                Log.d("stcLog","开始下载");
            }

            //下载的时候不间断的回调
            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                Log.d("stcLog","总大小：" + getTwo(total) + "M,已下载大小：" + getTwo(current) + "M" + ",进度：" + getProgress(total,current) + "%");
            }
        });
    }

    private static double getTwo(long data){
        double tranData = data/1024.0/1024.0;
        BigDecimal bigDecimal = new BigDecimal(tranData);
        return bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private static double getProgress(long t,long c){
        double tM = t/1024.0/1024.0;
        double cM = c/1024.0/1024.0;
        double tc = (cM / tM)*100;
        BigDecimal bigDecimal = new BigDecimal(tc);
        return bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    public interface BackCallNet{
        void successful(String stream);
        void failed(Throwable ex);
        void cache(String stream);
    }
}
