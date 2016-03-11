package dhl.com.project.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
 * 网络缓存
 */
public class NetCacheUtils {
    private LocalCacheUtils mLocalCacheUtils;
    private MemoryCacheUtils mMemoryCacheUtils;
    public NetCacheUtils(LocalCacheUtils localCacheUtils, MemoryCacheUtils memoryCacheUtils) {
        mLocalCacheUtils=localCacheUtils;
        mMemoryCacheUtils=memoryCacheUtils;
    }

    /**
     * 从网络下载图片
     * @param ivPic
     * @param url
     */
    public void getBitmapFromNet(ImageView ivPic, String url) {
new BitmapTask().execute(ivPic,url);//启动AsyncTask,在doInBackground获取

    }

    /**
     * 第一个泛型 参数类型
     * 第二个泛型 更新进度的泛型
     * 第三个泛型是onPostExecute返回结果
     */
    class  BitmapTask extends AsyncTask<Object,Void,Bitmap>{

        private ImageView ivPic;
        private String url;

        /**
         * 后台耗时方法在此执行
         * 在子线程运行
         * @param params
         * @return
         */
        @Override
        protected Bitmap doInBackground(Object... params) {
            ivPic = (ImageView) params[0];
            url = (String) params[1];
            ivPic.setTag(url);//绑定url和imageView
            return downLoadBitmap(url);
        }

        /**
         * 更新进度
         * 在主线程运行
         * @param values
         */
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        /**
         * 耗时方法结束后执行该方法
         * 在主线程运行
         * @param result
         */
        @Override
        protected void onPostExecute(Bitmap result) {
            if (result!=null){
               String bindUrl= (String) ivPic.getTag();
                if (url.equals(bindUrl)) {
                    //确保图片设定给了imageView
                    ivPic.setImageBitmap(result);
                    mLocalCacheUtils.setBitmapToLocal(url, result);
                    mMemoryCacheUtils.setBitmapToMemory(url, result);
                }
            }
        }
     }
    private  Bitmap downLoadBitmap(String url){
        HttpURLConnection conn=null;
        try {
             conn= (HttpURLConnection) new URL(url).openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");
            conn.connect();
            int responseCode=conn.getResponseCode();
            if (responseCode==200){
               InputStream inputStream= conn.getInputStream();
                //图片压缩处理
                BitmapFactory.Options option=new BitmapFactory.Options();
                option.inSampleSize=2;//宽高都压缩为原来的1/2
                option.inPreferredConfig=Bitmap.Config.RGB_565;//设置图片格式
                Bitmap bitmap=BitmapFactory.decodeStream(inputStream,null,option);
                return bitmap;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            conn.disconnect();

        }
return null;

    }
}
