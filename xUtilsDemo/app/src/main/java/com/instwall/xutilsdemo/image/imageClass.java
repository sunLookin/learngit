package com.instwall.xutilsdemo.image;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.instwall.xutilsdemo.R;

import org.xutils.common.Callback;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;

public class imageClass {

    //没有imageOptions普通加载
    public static void loadCommonImage(ImageView imageView, String url){
        x.image().bind(imageView,url);
    }

    //有ImageOptions的加载
    public static void loadImageOptionsImage(ImageView imageView, String url, Context context){
        Animation animation =AnimationUtils.loadAnimation(context,R.anim.image_in);
        ImageOptions imageOptions = new ImageOptions.Builder().setAnimation(animation).build();
        x.image().bind(imageView,url,imageOptions);
    }

    public static void loadCommonCalbackImage(ImageView imageView,String url){
        x.image().bind(imageView, url, new Callback.CommonCallback<Drawable>() {
            @Override
            public void onSuccess(Drawable result) {
                Log.d("stcLog","[loadCommonCalbackImage onSuccess]:" + result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public static void loadDrawable(final ImageView imageView, String url){
        x.image().loadDrawable(url, null, new Callback.CommonCallback<Drawable>() {
            @Override
            public void onSuccess(Drawable result) {
                imageView.setImageDrawable(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public static void loadFile(final ImageView imageView, String url){
        x.image().loadFile(url, null, new Callback.CacheCallback<File>() {
            @Override
            public boolean onCache(File result) {
                //在这里设置缓存
                return false;
            }

            @Override
            public void onSuccess(File result) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
