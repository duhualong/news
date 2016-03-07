package dhl.com.project.web;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import dhl.com.project.R;

/**
 * 新闻详情页面webView
 */
public class NewsDetailActivity extends Activity {

    private WebView mWebView;
    private ImageButton btnShare;
    private ImageButton btnBack;
    private ImageButton btnSize;
    private ProgressBar pbProgress;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        mWebView = (WebView) findViewById(R.id.wv_web);
        btnShare = (ImageButton) findViewById(R.id.btn_share);
        btnBack = (ImageButton) findViewById(R.id.btn_back);
        btnSize = (ImageButton) findViewById(R.id.btn_size);
        pbProgress= (ProgressBar) findViewById(R.id.pb_progress);
        String url = getIntent().getStringExtra("url");
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);//显示支持js
        settings.setBuiltInZoomControls(true);//显示放大缩小按钮
        settings.setUseWideViewPort(true);
        mWebView.setWebViewClient(new WebViewClient() {
            /**
             * 网页加载开始
             * @param view
             * @param url
             * @param favicon
             */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pbProgress.setVisibility(View.VISIBLE);


            }

            /**
             * 网页加载结束
             * @param view
             * @param url
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pbProgress.setVisibility(View.GONE);
            }

            /**
             * 所有网页跳转的连接都在此方法中回调
             * @param view
             * @param url
             * @return
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
        });
        mWebView.goBack();
        mWebView.loadUrl("http://news.baidu.com/");//加载网页


    }
}
