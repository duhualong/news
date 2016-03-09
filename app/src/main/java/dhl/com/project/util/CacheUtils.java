package dhl.com.project.util;

import android.content.Context;

/**
 * _ooOoo_
 * o8888888o
 * 88" . "88
 * (| -_- |)
 * O\  =  /O
 * ____/`---'\____
 * .'  \\|     |//  `.
 * /  \\|||  :  |||//  \
 * /  _||||| -:- |||||-  \
 * |   | \\\  -  /// |   |
 * | \_|  ''\---/''  |   |
 * \  .-\__  `-`  ___/-. /
 * ___`. .'  /--.--\  '. .'__
 * ."" '<  `.___\_<|>_/___.'  >'"".
 * | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 * \  \ `-.   \_ __\ /__ _/   .-` /  /
 * ======`-.____`-.___\_____/___.-`____.-'======
 * `=---='
 * ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 * 佛祖保佑       永无BUG
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


    }

    /**
     * 获取缓存 key:url
     */
    public static String getCache(String key,Context ctx){
       return PreUtils.getString(ctx,key,null);


    }

}
