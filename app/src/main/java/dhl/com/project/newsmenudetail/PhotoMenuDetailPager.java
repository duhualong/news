package dhl.com.project.newsmenudetail;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import dhl.com.project.R;
import dhl.com.project.base.BaseMenuDetailPager;
import dhl.com.project.domain.PhotosData;
import dhl.com.project.global.GlobalContants;
import dhl.com.project.util.CacheUtils;
import dhl.com.project.util.MyBitmapUtils;
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
 * 菜单详情页——新闻
 */
public class PhotoMenuDetailPager extends BaseMenuDetailPager {
    private static final int PHOTO_MENU_SUCCESS=201;
    private static final int PHOTO_MENU_FALSE=202;

    @Bind(R.id.gv_photo)GridView gvPhoto;
    @Bind(R.id.lv_photo)ListView lvPhoto;
    private ArrayList<PhotosData.PhotoInfo> mPhotoList;
    private PhotoAdapter mAdapter;
    private ImageButton btnPhoto;

    public PhotoMenuDetailPager(Activity activity, ImageButton btnPhoto) {
        super(activity);
        this.btnPhoto=btnPhoto;
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDisplay();


            }
        });
    }

    @Override
    public View initViews() {
        View view=View.inflate(mActivity, R.layout.menu_photo_pager, null);
        ButterKnife.bind(this, view);


        return view;
    }

    @Override
    public void initData() {
       String cache= CacheUtils.getCache(GlobalContants.PHOTOS_URL, mActivity);
        if (!TextUtils.isEmpty(cache)){

        }
        getDataFromServe();

    }

    private void getDataFromServe() {
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(GlobalContants.PHOTOS_URL).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Message message = Message.obtain();
                message.what = PHOTO_MENU_FALSE;
                mHandler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result=response.body().string();
                Message message = Message.obtain();
                message.what = PHOTO_MENU_SUCCESS;
                message.obj = result;
                mHandler.sendMessage(message);

            }
        });

    }
    private  Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case PHOTO_MENU_SUCCESS:
                    String result= (String) msg.obj;
                    parseData(result);
                    break;
                case PHOTO_MENU_FALSE:
                    Toast.makeText(mActivity,"加载失败",Toast.LENGTH_SHORT).show();

                    break;
            }

            return false;
        }
    });

    private void parseData(String result) {
        Gson gson=new Gson();
       PhotosData data=gson.fromJson(result, PhotosData.class);
        mPhotoList = data.data.news;//获取组图列表集合
        if (mPhotoList!=null) {
            mAdapter = new PhotoAdapter();
            lvPhoto.setAdapter(mAdapter);
            gvPhoto.setAdapter(mAdapter);
        }
    }


    class PhotoAdapter extends BaseAdapter{

       // private final BitmapUtils utils;
private MyBitmapUtils utils;
        public PhotoAdapter(){
          //  utils = new BitmapUtils(mActivity);
           // utils.configDefaultLoadingImage(R.drawable.news_pic_default);
utils=new MyBitmapUtils();

        }

        @Override
        public int getCount() {
            return mPhotoList.size();
        }

        @Override
        public Object getItem(int position) {
            return mPhotoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.list_photo_item, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
           PhotosData.PhotoInfo item= (PhotosData.PhotoInfo) getItem(position);
            holder.tvTitle.setText(item.title);
            utils.display(holder.ivPic,item.listimage);

            return convertView;
        }
    }
    static class ViewHolder {
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.iv_pic)
         ImageView ivPic;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private boolean isListDisplay=true;//是否是列表展示

    /**
     * 切换展现方式
     */
    private void changeDisplay(){
        if (isListDisplay){
            isListDisplay=false;
            lvPhoto.setVisibility(View.GONE);
            gvPhoto.setVisibility(View.VISIBLE);
            btnPhoto.setImageResource(R.drawable.icon_pic_list_type);

        }else {
            isListDisplay=true;
            lvPhoto.setVisibility(View.VISIBLE);
            gvPhoto.setVisibility(View.GONE);
            btnPhoto.setImageResource(R.drawable.icon_pic_grid_type);
        }


    }
}
