package dhl.com.project.cache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * 作者：` on 2016/3/11 10:28
 * 邮箱2383335125@qq.com
 */
public class MemoryCacheUtils {
    //private HashMap<String,SoftReference<Bitmap>>mMemoryCache=new HashMap<>();
    private LruCache<String, Bitmap> mMemoryCache;

    public MemoryCacheUtils() {
        long maxMemory = Runtime.getRuntime().maxMemory();//获取默认最大内存
        mMemoryCache = new LruCache<String, Bitmap>((int) (maxMemory / 8)) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                int byteCount = value.getByteCount();
                return byteCount;
            }
        };
    }

    /**
     * 从内存读
     *
     * @param url
     */
    public Bitmap getBitmapFormMemory(String url) {
//        SoftReference<Bitmap> softReference=mMemoryCache.get(url);
//        if (softReference!=null) {
//            Bitmap bitmap = softReference.get();
//            return bitmap;
//        }
//        return null;
        return mMemoryCache.get(url);




    }

    /**
     * 写到内存
     *
     * @param url
     * @param bitmap
     */
    public void setBitmapToMemory(String url, Bitmap bitmap) {

//        SoftReference<Bitmap> softReference= new SoftReference<Bitmap>(bitmap);
//        mMemoryCache.put(url,softReference);
//    }
        mMemoryCache.put(url, bitmap);
    }
}