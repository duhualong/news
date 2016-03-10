package dhl.com.project.util;

import android.widget.ImageView;

import dhl.com.project.cache.NetCacheUtils;

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
 * 自定义图片加载工具
 */
public class MyBitmapUtils {
    NetCacheUtils mNetCacheUtils;
    public MyBitmapUtils(){
        mNetCacheUtils=new NetCacheUtils();

    }
    public void display(ImageView ivPic, String url) {
        //从内存读

        //本地读

        //网络缓存
        mNetCacheUtils.getBitmapFromNet(ivPic,url);



    }
}
