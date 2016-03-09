package dhl.com.project.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
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
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import dhl.com.project.R;
import dhl.com.project.domain.NewsData;
import dhl.com.project.domain.TabData;
import dhl.com.project.global.GlobalContants;
import dhl.com.project.util.CacheUtils;
import dhl.com.project.util.PreUtils;
import dhl.com.project.view.RefreshListView;
import dhl.com.project.view.TopNewsViewPager;
import dhl.com.project.web.NewsDetailActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
    private final static int TABLE_DETAIL_GET_MORE_SUCCESS = 101;
    private final static int TABLE_DETAIL_GET_MORE_FALSE = 102;
    private final static int TABLE_DETAIL_GET_DATA_SUCCESS = 103;
    private final static int TABLE_DETAIL_GET_DATA_FALSE = 104;
    NewsData.NewsTabData mTabData;
    private TextView tvText;
    private String mUrl;
    private TabData mTabDetailData;
    @Bind(R.id.tv_title)TextView tvTitle;//头条新闻的标题
    @Bind(R.id.indicator)CirclePageIndicator mIndicator;//头条新闻位置指示器
    @Bind(R.id.vp_news)TopNewsViewPager mViewPager;
    @ViewInject(R.id.lv_list)
    private RefreshListView lvList;//新闻列表
    private ArrayList<TabData.TopNewsData> mTopNewsList;
    private ArrayList<TabData.TabNewsData> mNewsList;
    private NewsAdapter mNewsAdapter;
    private String mMoreUrl;
    private Handler mHandlers;
    public TabDetailPager(Activity activity, NewsData.NewsTabData newsTabData) {
        super(activity);
        mTabData = newsTabData;
        mUrl = GlobalContants.SERVICE_URL + mTabData.url;
    }
    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.tab_detail_pager, null);
        ViewUtils.inject(this,view);
        //加载头布局
        View headerView = View.inflate(mActivity, R.layout.list_header_topnews, null);
        ButterKnife.bind(this, headerView);
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
                String ids = PreUtils.getString(mActivity, "read_ids", "");
                String readId = mNewsList.get(position).id;
                if (!ids.contains(readId)) {
                    ids = ids + readId + ",";
                    PreUtils.setString(mActivity, "read_ids", ids);
                }
                //  mNewsAdapter.notifyDataSetChanged();
                changeReadState(view);//实现局部界面刷新，这个view就是被点击的对象
                //跳转到新闻详情页
                Intent intent = new Intent();
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
    private void changeReadState(View view) {
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvTitle.setTextColor(Color.GRAY);
    }
    /**
     * 加载下一页
     */
    private void getMoreDataFromService() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(mMoreUrl).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Message message = Message.obtain();
                message.what = TABLE_DETAIL_GET_MORE_FALSE;
                mHandler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Message message = Message.obtain();
                message.what = TABLE_DETAIL_GET_MORE_SUCCESS;
                message.obj = result;
                mHandler.sendMessage(message);

            }
        });
    }
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case TABLE_DETAIL_GET_MORE_SUCCESS:
                    String result = (String) msg.obj;
                    parseData(result, true);
                    lvList.onRefreshComplete(true);
                    break;
                case TABLE_DETAIL_GET_MORE_FALSE:
                    Toast.makeText(mActivity, "请求数据失败", Toast.LENGTH_SHORT).show();
                    lvList.onRefreshComplete(false);
                    break;
                case TABLE_DETAIL_GET_DATA_SUCCESS:
                    String resultData = (String) msg.obj;
                    parseData(resultData, false);
                    lvList.onRefreshComplete(true);
                    //设置缓存
                    CacheUtils.setCache(mActivity,mUrl,resultData);
                    break;
                case TABLE_DETAIL_GET_DATA_FALSE:
                    Toast.makeText(mActivity, "访问失败", Toast.LENGTH_LONG).show();
                    lvList.onRefreshComplete(false);
                    break;
            }
            return false;
        }
    });

    @Override
    public void initData() {
       String cache= CacheUtils.getCache(mUrl, mActivity);
        if (!TextUtils.isEmpty(cache)){
            parseData(cache,false);

        }
        getDataFromService();
    }
    private void getDataFromService() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(mUrl).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Message message = Message.obtain();
                message.what = TABLE_DETAIL_GET_DATA_FALSE;
                mHandler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                //   System.out.println("页签详情页返回结果：" + result);
                Message message = Message.obtain();
                message.what = TABLE_DETAIL_GET_DATA_SUCCESS;
                message.obj = result;
                mHandler.sendMessage(message);
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
        //System.out.println("页签解析详情：" + mTabDetailData);
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
            //自动轮播条显示
            if (mHandlers==null){
                mHandlers=new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                      int currentItem=mViewPager.getCurrentItem();
                        if (currentItem<mTopNewsList.size()-1){
                            currentItem++;
                        }else {
                            currentItem=0;
                        }
                        mViewPager.setCurrentItem(currentItem);//切换到下一个页面
                        mHandlers.sendEmptyMessageDelayed(0,3000);//继续延迟3秒发消息形成循环
                        return false;
                    }
                });
                mHandlers.sendEmptyMessageDelayed(0,3000);//延迟3秒发消息
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
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            TabData.TabNewsData item = getItem(position);
            holder.tvTitle.setText(item.title);
            holder.tvData.setText(item.pubdate);
            utils.display(holder.ivPic, item.listimage);
            String ids = PreUtils.getString(mActivity, "read_ids", "");
            if (ids.contains(getItem(position).id)) {
                holder.tvTitle.setTextColor(Color.GRAY);

            } else {
                holder.tvTitle.setTextColor(Color.BLACK);
            }
            return convertView;
        }
    }
    static class ViewHolder {
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.tv_data)
        TextView tvData;
        @Bind(R.id.iv_pic)
        ImageView ivPic;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
