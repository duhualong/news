package dhl.com.project.fragment;


import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import dhl.com.project.R;
import dhl.com.project.base.BasePage;
import dhl.com.project.impl.GovAffairsPager;
import dhl.com.project.impl.HomePager;
import dhl.com.project.impl.NewsCenterPager;
import dhl.com.project.impl.SettingPager;
import dhl.com.project.impl.SmartServicePager;

public class ContentFragment extends BaseFragment {
    @Bind(R.id.rg_group) RadioGroup rgGroup;
    @Bind(R.id.vp_content) ViewPager mViewPage;

    private List<BasePage> mPageList;

    @Override
    public View initViews() {
      View view=  View.inflate(mActivity, R.layout.fragment_content,null);
      //  ViewUtils.inject(this,view);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void initData() {
        rgGroup.check(R.id.rb_home);//默认勾选首页
        /**
         * 初始化页面
         */
        mPageList=new ArrayList<BasePage>();
//        for (int i=0;i<5;i++){
//            BasePage page=new BasePage(mActivity);
//            mPageList.add(page);
//
//        }
        mPageList.add(new HomePager(mActivity));
        mPageList.add(new NewsCenterPager(mActivity));
        mPageList.add(new SmartServicePager(mActivity));
        mPageList.add(new GovAffairsPager(mActivity));
        mPageList.add(new SettingPager(mActivity));
        mViewPage.setAdapter(new ContentAdapter());
        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        // mViewPage.setCurrentItem(0);
                        mViewPage.setCurrentItem(0, false);
                        break;
                    case R.id.rb_news:
                        mViewPage.setCurrentItem(1, false);
                        break;
                    case R.id.rb_smart:
                        mViewPage.setCurrentItem(2, false);
                        break;
                    case R.id.rb_gov:
                        mViewPage.setCurrentItem(3, false);
                        break;
                    case R.id.rb_setting:
                        mViewPage.setCurrentItem(4, false);//设置当前页面,去掉页面的动画
                        break;
                }
            }
        });
        mViewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
               mPageList.get(position).initData();//获取当前被选中的页面，初始化页面的数据
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //手动初始化首页
        mPageList.get(0).initData();
    }
    class ContentAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mPageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePage page=mPageList.get(position);
            container.addView(page.mRootView);
         //   page.initData();//初始化数据
            return page.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }
    //获取新闻中心页面
    public NewsCenterPager getNewsCenterPager(){
        return (NewsCenterPager) mPageList.get(1);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
