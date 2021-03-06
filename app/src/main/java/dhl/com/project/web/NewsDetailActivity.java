package dhl.com.project.web;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;
import dhl.com.project.R;
import dhl.com.project.cn.sharesdk.onekeyshare.OnekeyShare;
import dhl.com.project.cn.sharesdk.onekeyshare.OnekeyShareTheme;

/**
 * 新闻详情页面webView
 */
public class NewsDetailActivity extends Activity {
@Bind(R.id.wv_web)WebView mWebView;
@Bind(R.id.btn_back)ImageButton btnBack;
@Bind(R.id.btn_size)ImageButton btnSize;
@Bind(R.id.btn_share)ImageButton btnShare;
@Bind(R.id.pb_progress)ProgressBar pbProgress;
@OnClick({R.id.btn_back,R.id.btn_size,R.id.btn_share})
    public void OnClickListener(View v){
        switch (v.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_size:
                showChooseDialog();
                break;
            case R.id.btn_share:
                showShare();
                break;
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
       ButterKnife.bind(this);
        String url = getIntent().getStringExtra("url");
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);// 表示支持js
        settings.setBuiltInZoomControls(true);// 显示放大缩小按钮
        settings.setUseWideViewPort(true);// 支持双击缩放

        mWebView.setWebViewClient(new WebViewClient() {

            /**
             * 网页开始加载
             */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
              //  System.out.println("网页开始加载");
                if (!pbProgress.isShown()) {
                    pbProgress.setVisibility(View.VISIBLE);
                }
            }

            /**
             * 网页加载结束
             */
            @Override
            public void onPageFinished(WebView view, String url) {
              //  System.out.println("网页开始结束");
                super.onPageFinished(view, url);
            }

            /**
             * 所有跳转的链接都会在此方法中回调
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // tel:110
                System.out.println("跳转url:" + url);
                view.loadUrl(url);
                return true;
                // return super.shouldOverrideUrlLoading(view, url);
            }
        });
        // mWebView.goBack()

        mWebView.setWebChromeClient(new WebChromeClient() {

            /**
             * 进度发生变化
             */
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
               // System.out.println("加载进度:" + newProgress);
                if (pbProgress.isShown()) {
                    pbProgress.setVisibility(View.INVISIBLE);
                }
                super.onProgressChanged(view, newProgress);
            }

            /**
             * 获取网页标题
             */
            @Override
            public void onReceivedTitle(WebView view, String title) {
               // System.out.println("网页标题:" + title);
                super.onReceivedTitle(view, title);
            }
        });

        mWebView.loadUrl(url);// 加载网页
    }



    private int mCurrentChooseItem;//记录当前选中的item

    private int mCurrentItem = 2;// 记录当前选中的item, 点击确定后

    /**
     * 显示选择对话框
     */

    private void showChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] items = new String[]{"超大号字体", "大号字体", "正常字体", "小号字体",
                "超小号字体"};
        builder.setTitle("字体设置");
        builder.setSingleChoiceItems(items, mCurrentItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              //  System.out.println("选中：" + which);
                mCurrentChooseItem = which;


            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                WebSettings settings=mWebView.getSettings();
                switch (mCurrentChooseItem) {
                    case 0:
                        settings.setTextZoom(200);//设置超大字体
                        break;
                    case 1:
                        settings.setTextZoom(150);
                        break;
                    case 2:
                        settings.setTextZoom(100);
                        break;
                    case 3:
                        settings.setTextZoom(75);
                        break;
                    case 4:
                        settings.setTextZoom(50);
                        break;

                }
                mCurrentItem=mCurrentChooseItem;
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    /**
     * 分享，注意
     */
    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        oks.setTheme(OnekeyShareTheme.CLASSIC);
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }
}
