package dhl.com.project.cache;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import dhl.com.project.util.MD5Encoder;

public class LocalCacheUtils {
    public static final  String CACHE_PATH= Environment.getExternalStorageDirectory().getAbsolutePath()+"/progress";
    /**
     * 从本地读图片
     * @param url
     */
    public  Bitmap getBitmapFromLocal(String url){
        try {
            String fileName= MD5Encoder.encode(url);
            File file=new File(CACHE_PATH,fileName);
            if (file.exists()){
                Bitmap bitmap= BitmapFactory.decodeStream(new FileInputStream(file));
                return bitmap;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;


    }

    /**
     * 向sdcard 写图片
     * @param url
     * @param bitmap
     */

    public  void setBitmapToLocal(String url, Bitmap bitmap){
        try {
            String fileName= MD5Encoder.encode(url);
            File file=new File(CACHE_PATH,fileName);
          File parentFile=file.getParentFile();
            if (!parentFile.exists()){
                parentFile.mkdirs();//文件夹不存在创建文件夹
            }
            //将图片保存在本地
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
