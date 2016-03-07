package dhl.com.project.base;

import android.app.Activity;
import android.view.View;

/**
 * 菜单详情页
 */
public abstract class BaseMenuDetailPager {
    public Activity mActivity;
    public View mRootView;

    public BaseMenuDetailPager(Activity activity) {
      mActivity = activity;
        mRootView=initViews();
    }

    /**
     * 初始化界面
     * @return
     */
    public abstract View initViews();

    /**
     * 初始化数据
     */
    public void initData(){

    }
}
