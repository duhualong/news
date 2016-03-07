package dhl.com.project.impl;

import android.app.Activity;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

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
//        TextView text=new TextView(mActivity);
//        text.setText("");
//        text.setTextColor(Color.RED);
//        text.setTextSize(25);
//        text.setGravity(Gravity.CENTER);
//        //向framentlayout动态添加neir
//        flContent.addView(text);
        getDataFromService();


    }

    /**
     * 从服务器获取数据
     */
    private void getDataFromService() {
        HttpUtils utils=new HttpUtils();
        //使用xutils发送请求
        utils.send(HttpRequest.HttpMethod.GET, GlobalContants.CATEGORIES_URL, new RequestCallBack<String>() {
            //访问成功
            @Override
            public void onSuccess(ResponseInfo responseInfo) {
                String result = (String) responseInfo.result;
                System.out.println("返回结果：" + result);
                parseData(result);


            }

            //访问失败
            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(mActivity, "访问失败", Toast.LENGTH_LONG).show();
                e.printStackTrace();

            }
        });

    }

    /**
     * 解析数据
     */
    private void parseData(String result) {
        Gson gson=new Gson();
        myNewsData = gson.fromJson(result, NewsData.class);
        System.out.println("解析结果："+ myNewsData);
        //刷新侧边栏的数据
       MainActivity mainui= (MainActivity) mActivity;
        LeftMenuFragment leftMenuFragment=mainui.getLeftMenuFragment();
      leftMenuFragment.setMenuData(myNewsData);
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
