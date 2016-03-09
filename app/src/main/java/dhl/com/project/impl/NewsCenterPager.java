package dhl.com.project.impl;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import dhl.com.project.activity.MainActivity;
import dhl.com.project.base.BaseMenuDetailPager;
import dhl.com.project.base.BasePage;
import dhl.com.project.domain.NewsData;
import dhl.com.project.fragment.LeftMenuFragment;
import dhl.com.project.global.GlobalContants;
import dhl.com.project.newsmenudetail.InteractMenuDetailPager;
import dhl.com.project.newsmenudetail.NewsMenuDetail;
import dhl.com.project.newsmenudetail.PhotoMenuDetailPager;
import dhl.com.project.newsmenudetail.TopicMenuDetailPager;
import dhl.com.project.util.CacheUtils;
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
 */
public class NewsCenterPager extends BasePage {
    private final static int NEWS_CENTER_MESSAGE_SUCCESS =99;
    private final static int NEWS_CENTER_MESSAGE_FALSE =100;

    //4个菜单详情页的集合
    private ArrayList<BaseMenuDetailPager> mPagers;
    private NewsData myNewsData;
    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        tvTitle.setText("新闻");
        setSlidingMenuEnable(true);//关闭侧边栏
       String cache= CacheUtils.getCache(GlobalContants.CATEGORIES_URL,mActivity);
        if (!TextUtils.isEmpty(cache)){
            //如果缓存存在无需访问网络直接解析数据
        parseData(cache);
        }
        //不管有没有缓存都获取网络数据
            getDataFromService();


    }

    /**
     * 从服务器获取数据
     */
    private void getDataFromService() {
        OkHttpClient client=new OkHttpClient();
        Request request = new Request.Builder().url(GlobalContants.CATEGORIES_URL).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Message message = Message.obtain();
                message.what = NEWS_CENTER_MESSAGE_FALSE;
                mHandler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                //    System.out.println("!!!!!!!!!返回结果!!!!!!!!!!：" + result);
                Message message = Message.obtain();
                message.what = NEWS_CENTER_MESSAGE_SUCCESS;
                message.obj = result;
                mHandler.sendMessage(message);
            }
        });
    }
    private  Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case NEWS_CENTER_MESSAGE_SUCCESS:
                    String result = (String) msg.obj;
                    parseData(result);
                    CacheUtils.setCache(mActivity,GlobalContants.CATEGORIES_URL,result);
                    break;
                case NEWS_CENTER_MESSAGE_FALSE:
                    Toast.makeText(mActivity, "访问失败", Toast.LENGTH_LONG).show();
                    break;
            }
            return false;
        }
    });

    /**
     * 解析数据
     */
    private void parseData(String result) {
        Gson gson=new Gson();
        myNewsData = gson.fromJson(result, NewsData.class);
       // System.out.println("解析结果："+ myNewsData);
        if (!TextUtils.isEmpty(myNewsData.toString().trim())) {
            //刷新侧边栏的数据
            MainActivity mainUi = (MainActivity) mActivity;

            LeftMenuFragment leftMenuFragment = mainUi.getLeftMenuFragment();
            leftMenuFragment.setMenuData(myNewsData);
        }
        //准备四个菜单详情页
        mPagers=new ArrayList<BaseMenuDetailPager>();
        mPagers.add(new NewsMenuDetail(mActivity,myNewsData.data.get(0).children));
        mPagers.add(new TopicMenuDetailPager(mActivity));
        mPagers.add(new PhotoMenuDetailPager(mActivity));
        mPagers.add(new InteractMenuDetailPager(mActivity));
        setCurrentMenuDetailPager(0);//设置菜单详情页新闻-为默认页面
    }

    /**
     * 设置当前菜单详情页
     */
    public  void setCurrentMenuDetailPager(int position){
        BaseMenuDetailPager pager=mPagers.get(position);//获取要显示的菜单详情页
        flContent.removeAllViews();//清除之前的布局
        flContent.addView(pager.mRootView);//将菜单详情页布局文件设置给帧布局
      NewsData.NewsMenuData menuData= myNewsData.data.get(position);
        tvTitle.setText(menuData.title);
        pager.initData();//初始化当前页面的数据

    }
}
