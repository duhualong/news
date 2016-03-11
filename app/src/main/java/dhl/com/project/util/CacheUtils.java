package dhl.com.project.util;

import android.content.Context;

/**
 * 缓存工具类
 */
public class CacheUtils {
    /**
     * 设置缓存
     * @param key url
     * @param value jspn
     */

    public static void setCache(Context ctx,String key,String value){
        PreUtils.setString(ctx,key,value);
//可以将缓存放在文件中，文件名是Md5(url),文件内容是json

    }

    /**
     * 获取缓存 key:url
     */
    public static String getCache(String key,Context ctx){
       return PreUtils.getString(ctx,key,null);


    }

}
