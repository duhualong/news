package dhl.com.project.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

import dhl.com.project.R;
import dhl.com.project.domain.NewsData;
import dhl.com.project.domain.TabData;
import dhl.com.project.global.GlobalContants;
import dhl.com.project.util.PreUtils;
import dhl.com.project.view.RefreshListView;
import dhl.com.project.view.TopNewsViewPager;
import dhl.com.project.web.NewsDetailActivity;

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
 * 页签详情页
 */
public class TabDetailPager extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener {
    NewsData.NewsTabData mTabData;
    private TextView tvText;
    private String mUrl;
    private TabData mTabDetailData;
    @ViewInject(R.id.tv_title)
    private TextView tvTitle;//头条新闻的标题
    @ViewInject(R.id.indicator)
    private CirclePageIndicator mIndicator;//头条新闻位置指示器
    @ViewInject(R.id.vp_news)
    private TopNewsViewPager mViewPager;
    private ArrayList<TabData.TopNewsData> mTopNewsList;
    @ViewInject(R.id.lv_list)
    private RefreshListView lvList;//新闻列表
    private ArrayList<TabData.TabNewsData> mNewsList;
    private NewsAdapter mNewsAdapter;
    private String mMoreUrl;

    public TabDetailPager(Activity activity, NewsData.NewsTabData newsTabData) {
        super(activity);
        mTabData = newsTabData;
        mUrl = GlobalContants.SERVICE_URL + mTabData.url;
    }

    @Override
    public View initViews() {
//        tvText = new TextView(mActivity);
//        tvText.setText("页签详情页");
//        tvText.setTextColor(Color.RED);
//        tvText.setGravity(Gravity.CENTER);
//        tvText.setTextSize(25);
        View view = View.inflate(mActivity, R.layout.tab_detail_pager, null);
        //加载头布局
        View headerView = View.inflate(mActivity, R.layout.list_header_topnews, null);
        ViewUtils.inject(this, view);
        ViewUtils.inject(this, headerView);
        lvList.addHeaderView(headerView);
        //设置下拉刷新
        lvList.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromService();

            }

            @Override
            public void onLoadMore() {
                if (mMoreUrl != null) {

                    getMoreDataFromService();
                } else {
                    Toast.makeText(mActivity, "已经是最后一页", Toast.LENGTH_SHORT).show();
                    lvList.onRefreshComplete(false);
                }

            }
        });
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            System.out.println("点击当前页面：" + position);
                //在本地记录已读
              String ids=PreUtils.getString(mActivity,"read_ids","");
                String readId=mNewsList.get(position).id;
                if (!ids.contains(readId)) {
                    ids = ids + readId+ ",";
                    PreUtils.setString(mActivity,"read_ids",ids);
                }
             //  mNewsAdapter.notifyDataSetChanged();
                changeReadState(view);//实现局部界面刷新，这个view就是被点击的对象
               //跳转到新闻详情页
                Intent intent=new Intent();
                intent.setClass(mActivity, NewsDetailActivity.class);
                intent.putExtra("url", mNewsList.get(position).url);
                mActivity.startActivity(intent);

            }
        });
        return view;

    }

    /**
     * 改变已读新闻的颜色
     */
    private void changeReadState(View view){
      TextView tvTitle= (TextView) view.findViewById(R.id.tv_title);
        tvTitle.setTextColor(Color.GRAY);


    }

    /**
     * 加载下一页
     */
    private void getMoreDataFromService() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                parseData(result, true);
                lvList.onRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                lvList.onRefreshComplete(false);
            }
        });
    }

    @Override
    public void initData() {

        getDataFromService();
    }

    private void getDataFromService() {

        HttpUtils utils = new HttpUtils();
//        RequestParams requestParams=new RequestParams();
//        requestParams.addBodyParameter("traineruid", "57");
//        utils.send(HttpRequest.HttpMethod.POST,GlobalContants.LEARNCAR_URL,requestParams, new RequestCallBack<String>() {
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo) {
//                String result = (String) responseInfo.result;
//                System.out.println("页签详情页返回结果：" + result);
//                parseData1(result);
//            }
//
//            @Override
//            public void onFailure(HttpException e, String s) {
//
//            }
//        });


        utils.send(HttpRequest.HttpMethod.GET, mUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = (String) responseInfo.result;
                System.out.println("页签详情页返回结果：" + result);
                parseData(result, false);
                lvList.onRefreshComplete(true);

            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(mActivity, "访问失败", Toast.LENGTH_LONG).show();
                e.printStackTrace();
                lvList.onRefreshComplete(false);

            }
        });
    }

//    private void parseData1(String result) {
//        Gson gson=new Gson();
//        LearnCar learnCar = gson.fromJson(result, LearnCar.class);
//       String sex= learnCar.getSex();
//        System.out.println("sssss"+sex+"ssssss");
//        System.out.println("页签解析详情："+learnCar);
//
//    }

    private void parseData(String result, boolean isMore) {
        Gson gson = new Gson();
        mTabDetailData = gson.fromJson(result, TabData.class);
        System.out.println("页签解析详情：" + mTabDetailData);
        //处理下一页链接
        String more = mTabDetailData.data.more;//更多页面的地址
        if (!TextUtils.isEmpty(more)) {
            mMoreUrl = GlobalContants.SERVICE_URL + more;
        } else {
            mMoreUrl = null;
        }
        if (!isMore) {
            mTopNewsList = mTabDetailData.data.topnews;//头条新闻数据
            mNewsList = mTabDetailData.data.news;//新闻数据集合
            if (mTopNewsList != null) {
                mViewPager.setAdapter(new TopNewsAdapter());
                mIndicator.setViewPager(mViewPager);
                mIndicator.setSnap(true);//支持快照显示
                mIndicator.setOnPageChangeListener(this);
                mIndicator.onPageSelected(0);//让指示器重新定位到第一个点
                tvTitle.setText(mTopNewsList.get(0).title);
            }
            //填充新闻列表数据
            if (mNewsList != null) {
                mNewsAdapter = new NewsAdapter();
                lvList.setAdapter(mNewsAdapter);
            }
        } else {
            //如果加载下一页，需要把数据加载到原来的集合
            ArrayList<TabData.TabNewsData> news = mTabDetailData.data.news;
            mNewsList.addAll(news);
            mNewsAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        TabData.TopNewsData topNewsData = mTopNewsList.get(position);
        tvTitle.setText(topNewsData.title);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    class TopNewsAdapter extends PagerAdapter {

        private final BitmapUtils utils;

        public TopNewsAdapter() {
            utils = new BitmapUtils(mActivity);
            utils.configDefaultLoadingImage(R.drawable.topnews_item_default);//设置默认图片
        }

        @Override
        public int getCount() {
            return mTabDetailData.data.topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView image = new ImageView(mActivity);
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            TabData.TopNewsData topNewsData = mTopNewsList.get(position);
            utils.display(image, topNewsData.topimage);//传递imagView对象的图片地址
            container.addView(image);
            return image;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView((View) object);
        }
    }

    /**
     * 新闻列表的适配器
     */
    class NewsAdapter extends BaseAdapter {

        private final BitmapUtils utils;

        public NewsAdapter() {
            utils = new BitmapUtils(mActivity);
            utils.configDefaultLoadingImage(R.drawable.pic_item_list_default);

        }

        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public TabData.TabNewsData getItem(int position) {
            return mNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.list_news_item, null);
                holder = new ViewHolder();
                holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_pic);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                holder.tvData = (TextView) convertView.findViewById(R.id.tv_data);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            TabData.TabNewsData item = getItem(position);
            holder.tvTitle.setText(item.title);
            holder.tvData.setText(item.pubdate);
            utils.display(holder.ivPic, item.listimage);
            String ids=PreUtils.getString(mActivity,"read_ids","");
            if (ids.contains(getItem(position).id)){
                holder.tvTitle.setTextColor(Color.GRAY);

            }else {
                holder.tvTitle.setTextColor(Color.BLACK);
            }
            return convertView;
        }
    }

    static class ViewHolder {
        public TextView tvTitle;
        public TextView tvData;
        public ImageView ivPic;
    }
}
