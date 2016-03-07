package dhl.com.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import dhl.com.project.R;
import dhl.com.project.util.PreUtils;

public class GuideActivity extends AppCompatActivity {

    private ViewPager vpGuide;
    private static final  int[] mImageIds=new int[]{R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};
    private ArrayList<ImageView> mImageViewList;
    private LinearLayout llPointGroup;
    private int mPointWidth;
    private View viewRedPoint;
    private Button btStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        vpGuide = (ViewPager) findViewById(R.id.vp_guide);
        llPointGroup = (LinearLayout) findViewById(R.id.ll_point_group);
        viewRedPoint = findViewById(R.id.view_red_point);
        btStart = (Button) findViewById(R.id.bt_start);
        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更新sp,表示已经展示新手引导

                PreUtils.setBoolean(getApplicationContext(), "is_user_guide_showed", true);
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
                finish();
            }
        });
        initViews();
        vpGuide.setAdapter(new GuideAdapter());
        vpGuide.addOnPageChangeListener(new GuidePageListener());

    }


    class GuidePageListener implements ViewPager.OnPageChangeListener{
//滑动事件
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//positionOffset偏移的百分比，position当前所在位置，positionOffsetPixels 偏移的多少距离
          int len= (int) (positionOffset*mPointWidth)+position*mPointWidth;
            //获取红点的布局参数
         RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) viewRedPoint.getLayoutParams();
        params.leftMargin=len;//设置左边距
            viewRedPoint.setLayoutParams(params);
        }
//某个页面被选中的事件
        @Override
        public void onPageSelected(int position) {
            if (position==(mImageIds.length-1)){
                //最后一个页面button可见
                btStart.setVisibility(View.VISIBLE);
            }else {
                btStart.setVisibility(View.INVISIBLE);
            }

        }
//滑动状态发生变化
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * 初始化界面
     */

    private void initViews(){
        mImageViewList = new ArrayList<ImageView>();
        /**
         * 初始化引导页3个页面
         */
        for (int i=0;i<mImageIds.length;i++){
            ImageView image=new ImageView(this);
            image.setBackgroundResource(mImageIds[i]);//设置引导页背景
            mImageViewList.add(image);

        }
        /**
         * 初始化引导页的小圆点
         *
         */
        for (int i=0;i<mImageIds.length;i++){
            View point=new ImageView(this);
            point.setBackgroundResource(R.drawable.shape_point_gray);//设置引导页背景
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(30,30);//设置圆点的宽高
            if (i>0){
                params.leftMargin=45;
            }
            point.setLayoutParams(params);
           llPointGroup.addView(point);//将圆点添加给线性布局

        }

//获取视图树,对layout视图进行监听
        llPointGroup.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
           //当layout执行完结束此方法
            @Override
            public void onGlobalLayout() {
                llPointGroup.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                //measure(测量大小) layout(界面位置)
                mPointWidth = llPointGroup.getChildAt(1).getLeft()-llPointGroup.getChildAt(0).getLeft();
            }
        });
    }

    /**
     * ViewPager的适配数据
     */
    class GuideAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mImageIds.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //container 容器
            container.addView(mImageViewList.get(position));
            return mImageViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
    }
}
