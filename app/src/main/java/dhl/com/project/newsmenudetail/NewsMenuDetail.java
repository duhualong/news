package dhl.com.project.newsmenudetail;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import dhl.com.project.R;
import dhl.com.project.activity.MainActivity;
import dhl.com.project.base.BaseMenuDetailPager;
import dhl.com.project.base.TabDetailPager;
import dhl.com.project.domain.NewsData;

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
 * 菜单详情页——新闻
 */
public class NewsMenuDetail extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener {

    private ArrayList<TabDetailPager> mPagerList;
    private ArrayList<NewsData.NewsTabData> mNewsTabData;//页签网络数据
@Bind(R.id.menu_detail) ViewPager mViewPager;
    //初始化控件TabPageIndicator
@Bind(R.id.indicator) TabPageIndicator mIndicator;
    public NewsMenuDetail(Activity activity, ArrayList<NewsData.NewsTabData> children) {
        super(activity);
        mNewsTabData=children;
    }

    @Override
    public View initViews() {
        View view=View.inflate(mActivity, R.layout.news_menu_detail, null);
        ButterKnife.bind(this,view);
     //   mViewPager.addOnPageChangeListener(this);

        mIndicator.setOnPageChangeListener(this);

        return view;
    }

    @Override
    public void initData() {
        mPagerList=new ArrayList<TabDetailPager>();
        for (int i=0;i<mNewsTabData.size();i++){
            //初始化页签数据
            TabDetailPager pager=new TabDetailPager(mActivity,mNewsTabData.get(i));
            mPagerList.add(pager);
        }
        mViewPager.setAdapter(new MenuDetailAdapter());
        mIndicator.setViewPager(mViewPager);
    }
    //跳转下一页面
    @OnClick(R.id.btn_next)
    public  void nextPage(View view){
      int currentItem=mViewPager.getCurrentItem();
        mViewPager.setCurrentItem(++currentItem);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        MainActivity mainUi= (MainActivity) mActivity;
       SlidingMenu slidingMenu= mainUi.getSlidingMenu();
        if (position==0){
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        }else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {


    }

    class MenuDetailAdapter extends PagerAdapter{
        /**
         * 重新此方法，返回页面标题，用于页签显示
         * @param position
         * @return
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return mNewsTabData.get(position).title;//必须在viewpager设置adapter后才能调用
        }

        @Override
        public int getCount() {
            return mPagerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
          TabDetailPager pager=mPagerList.get(position);
            container.addView(pager.mRootView);
            pager.initData();

            return pager.mRootView;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
           container.removeView((View) object);
        }
    }

}
