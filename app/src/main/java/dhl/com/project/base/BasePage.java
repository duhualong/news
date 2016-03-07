package dhl.com.project.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import dhl.com.project.R;
import dhl.com.project.activity.MainActivity;

/**
 * 主页下五个子页面的基类
 */
public class BasePage {
    public  Activity mActivity;
    public View mRootView;
    public TextView tvTitle;
    public FrameLayout flContent;
    public ImageButton btnMenu;//菜单按钮


    public BasePage(Activity activity) {
        mActivity=activity;
    initViews();
    }

    /**
     * 初始化布局
     */
    public  void initViews(){
        mRootView = View.inflate(mActivity, R.layout.base_page, null);
        tvTitle= (TextView) mRootView.findViewById(R.id.tv_title);
        flContent= (FrameLayout) mRootView.findViewById(R.id.fl_content);
        btnMenu= (ImageButton) mRootView.findViewById(R.id.btn_menu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        toggleSlidingMenu();
            }
        });


    }
    //切换SlidingMenu的状态
    private void toggleSlidingMenu( ) {
        MainActivity mainUi = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUi.getSlidingMenu();
        slidingMenu.toggle();//切换状态。显示，隐藏
    }

    /**
     * 初始化数据
     */
    public void initData(){

    }

    /**
     * 设置侧边栏开启或关闭
     * @param enable
     */
    public void setSlidingMenuEnable(boolean enable){
        MainActivity mainUi= (MainActivity) mActivity;
        SlidingMenu slidingMenu= mainUi.getSlidingMenu();
        if (enable){
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }
}
