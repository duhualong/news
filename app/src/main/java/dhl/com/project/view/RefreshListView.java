package dhl.com.project.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import dhl.com.project.R;

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
 * 下拉刷新的listView
 */
public class RefreshListView extends ListView {
    private static final int STATE_PULL_REFRESH = 0;// 下拉刷新
    private static final int STATE_RELEASE_REFRESH = 1;// 松开刷新
    private static final int STATE_REFRESHING = 2;// 正在刷新

    private int startY = -1;// 滑动起点的y坐标
    private View mHeaderView;
    private int mHeaderViewHeight;
    private int mCurrrentState = STATE_PULL_REFRESH;// 当前状态
    private TextView tvTitle;
    private TextView tvTime;
    private ImageView ivArrow;
    private ProgressBar pbProgress;
    private RotateAnimation animUp;
    private RotateAnimation animDown;

    public RefreshListView(Context context) {
        super(context);
        initHeaderView();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();

    }

    private void initHeaderView() {
        mHeaderView = View.inflate(getContext(), R.layout.refresh_header, null);
        this.addHeaderView(mHeaderView);
        tvTitle = (TextView) mHeaderView.findViewById(R.id.tv_title);
        tvTime = (TextView) mHeaderView.findViewById(R.id.tv_time);
        ivArrow = (ImageView) mHeaderView.findViewById(R.id.iv_arr);
        pbProgress = (ProgressBar) mHeaderView.findViewById(R.id.pb_progress);
        mHeaderView.measure(0, 0);
        mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);//隐藏头布局
        initArrowAnim();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getRawY();//起点Y坐标
                break;
            case MotionEvent.ACTION_MOVE:
                if (startY==-1){
                    startY = (int) ev.getRawY();
                }
                if (mCurrrentState==STATE_REFRESHING){
                    //正在刷新时不做刷新
                    break;
                }
                int endY= (int) ev.getRawY();
                int dy=endY-startY;//移动相对位移
                if (dy>0&& getFirstVisiblePosition()==0){
                    //下拉并且是第一个item才允许下拉刷新
                    int padding=dy-mHeaderViewHeight;//计算padding
                    mHeaderView.setPadding(0,padding,0,0);//设置当前padding
                   if (padding>0&&mCurrrentState!=STATE_RELEASE_REFRESH){
                       //状态改为松开刷新
                       mCurrrentState=STATE_RELEASE_REFRESH;
                       refreshState();
                   }else if (padding<=0&&mCurrrentState!=STATE_PULL_REFRESH){
                       //改为下拉刷新
                       mCurrrentState=STATE_PULL_REFRESH;
                       refreshState();
                   }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                startY=-1;//重置
      if (mCurrrentState==STATE_RELEASE_REFRESH){
          mCurrrentState=STATE_REFRESHING;//正在刷新
          mHeaderView.setPadding(0,0,0,0);//显示
          refreshState();

      }else if (mCurrrentState==STATE_PULL_REFRESH){
          mHeaderView.setPadding(0,-mHeaderViewHeight,0,0);//隐藏刷新
      }
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 刷新下拉控件的布局
     */
    private void refreshState() {
        switch (mCurrrentState){
            case  STATE_PULL_REFRESH:
                tvTitle.setText("下拉刷新");
                ivArrow.setVisibility(View.VISIBLE);
                pbProgress.setVisibility(View.INVISIBLE);
                ivArrow.startAnimation(animDown);
                break;

            case STATE_RELEASE_REFRESH:
                tvTitle.setText("松开刷新");
                ivArrow.setVisibility(View.VISIBLE);
                pbProgress.setVisibility(View.INVISIBLE);
                ivArrow.startAnimation(animUp);
                break;
            case STATE_REFRESHING:
                tvTitle.setText("正在刷新。。。");
                ivArrow.clearAnimation();//先清除动画再隐藏
                ivArrow.setVisibility(View.INVISIBLE);
                pbProgress.setVisibility(View.VISIBLE);
                break;

        }


    }
    private void initArrowAnim(){
        //箭头向上的动画
        animUp = new RotateAnimation(0,-180, Animation.RELATIVE_TO_SELF,0.5f);
        animUp.setDuration(200);
        animUp.setFillAfter(true);
        //箭头向下的动画
        animDown = new RotateAnimation(-180,0, Animation.RELATIVE_TO_SELF,0.5f);
        animDown.setDuration(200);
        animDown.setFillAfter(true);


    }
}
