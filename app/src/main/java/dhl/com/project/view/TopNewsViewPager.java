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
 * 头条新闻的viewpager
 */
public class TopNewsViewPager extends ViewPager{

    private int startX;
    private int startY;

    public TopNewsViewPager(Context context) {
        super(context);
    }

    public TopNewsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    //事件分发,请求父控件及祖宗是否要拦截事件

    /**
     * 1当右滑，处于第一个页面，需要父控件拦截
     *2 当左滑，处于最后一个页面，需要父控件拦截
     * 3当上下滑动listView，需要父控件拦截
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);//不拦截,才能保证ACTION_MOVE执行
                startX = (int) ev.getRawX();
                startY = (int) ev.getRawY();

                break;
            case MotionEvent.ACTION_MOVE:
                int endX= (int) ev.getRawX();
                int endY= (int) ev.getRawY();
                if (Math.abs(endX-startX)>Math.abs(endY-startY)){
                    //左右滑动
                    if (endX>startX){
                        //向右滑
                      if (getCurrentItem()==0){
                          //处于第一个页面需要父控件拦截
                          getParent().requestDisallowInterceptTouchEvent(false);//拦截
                      }

                    }else {
                        if (getCurrentItem()==getAdapter().getCount()-1){
                            //最后一个
                            getParent().requestDisallowInterceptTouchEvent(false);//拦截

                        }
                    }
                    // this.requestDisallowInterceptTouchEvent(true);//不拦截
                }else {
                    //上下滑
                    getParent().requestDisallowInterceptTouchEvent(false);//拦截
                }


                break;
        }
        return super.dispatchTouchEvent(ev);

    }
}
