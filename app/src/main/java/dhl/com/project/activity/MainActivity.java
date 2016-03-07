package dhl.com.project.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import dhl.com.project.R;
import dhl.com.project.fragment.ContentFragment;
import dhl.com.project.fragment.LeftMenuFragment;

public class MainActivity extends SlidingFragmentActivity {

    private static final String FRAGMENT_CONTENT ="fragment_content" ;
    private static final String FRAGMENT_LEFT_MENU = "fragment_left_menu";
    //  private static final String FRAGMENT_CONTENT ="fragment_content" ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setBehindContentView(R.layout.left_menu);//设置侧边栏布局
        SlidingMenu slidingMenu=getSlidingMenu();//获取侧边栏对象
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//设置全屏触摸
        slidingMenu.setBehindOffset(700);//设置预留屏幕宽度

        initFragment();
    }
    //初始化fragment,将fragment数据填充给
    private void initFragment(){

     FragmentManager fm= getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction=fm.beginTransaction();
       transaction.replace(R.id.fl_content, new ContentFragment(), FRAGMENT_CONTENT);
    transaction.replace(R.id.fl_left_menu, new LeftMenuFragment(), FRAGMENT_LEFT_MENU);
transaction.commit();

    }
    //获取侧边栏对象
    public LeftMenuFragment getLeftMenuFragment(){
        FragmentManager fm= getSupportFragmentManager();
       LeftMenuFragment fragment= (LeftMenuFragment) fm.findFragmentByTag(FRAGMENT_LEFT_MENU);
        return fragment;
    }
    //获取主页面对象
    public ContentFragment getContentFragment(){
        FragmentManager fm= getSupportFragmentManager();
        ContentFragment fragment= (ContentFragment) fm.findFragmentByTag(FRAGMENT_CONTENT);
        return fragment;
    }
}
