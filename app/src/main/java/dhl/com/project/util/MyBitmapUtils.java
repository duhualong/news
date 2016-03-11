package dhl.com.project.util;

import android.graphics.Bitmap;
import android.widget.ImageView;

import dhl.com.project.R;
import dhl.com.project.cache.LocalCacheUtils;
import dhl.com.project.cache.MemoryCacheUtils;
import dhl.com.project.cache.NetCacheUtils;

/**
*
 * 自定义图片加载工具
 */
public class MyBitmapUtils {
    NetCacheUtils mNetCacheUtils;
    LocalCacheUtils mLocalCacheUtils;
    MemoryCacheUtils mMemoryCacheUtils;
    public MyBitmapUtils(){
        mMemoryCacheUtils=new MemoryCacheUtils();
        mLocalCacheUtils=new LocalCacheUtils();
        mNetCacheUtils=new NetCacheUtils(mLocalCacheUtils,mMemoryCacheUtils);


    }
    public void display(ImageView ivPic, String url) {
        ivPic.setImageResource(R.drawable.news_pic_default);//设置默认加载图片
        Bitmap bitmap=null;
        //从内存读
        bitmap=mMemoryCacheUtils.getBitmapFormMemory(url);
        if (bitmap!=null){
            ivPic.setImageBitmap(bitmap);
            return;
        }

        //本地读
        bitmap=mLocalCacheUtils.getBitmapFromLocal(url);
        if (bitmap!=null){
            ivPic.setImageBitmap(bitmap);
            mMemoryCacheUtils.setBitmapToMemory(url, bitmap);//将图片保存的内存
            return;
        }

        //网络缓存
        mNetCacheUtils.getBitmapFromNet(ivPic,url);



    }
}
