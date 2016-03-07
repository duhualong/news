package dhl.com.project.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

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
 * 水平11个页签水平滑动的viewpager,暂时用不到这个类
 */
public class HorizontalViewPager  extends ViewPager{
    public HorizontalViewPager(Context context) {
        super(context);
    }

    public HorizontalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    //事件分发,请求父控件及祖宗是否要拦截事件
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentItem()!=0) {
            this.requestDisallowInterceptTouchEvent(true);//不拦截
        }else {
            this.requestDisallowInterceptTouchEvent(false);//拦截
        }
        return super.dispatchTouchEvent(ev);

    }
}
