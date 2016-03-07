package dhl.com.project.activity;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import dhl.com.project.R;
import dhl.com.project.util.PreUtils;

public class SplashActivity extends Activity{

    private RelativeLayout rlRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_splash);
        rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
        startAnim();
    }
    private void startAnim(){
        AnimationSet set=new AnimationSet(false);
        RotateAnimation rotate=new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotate.setDuration(3000);
        rotate.setFillAfter(true);
        ScaleAnimation scale=new ScaleAnimation(0,1,0,1,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scale.setDuration(3000);
        scale.setFillAfter(true);
        AlphaAnimation alpha=new AlphaAnimation(0,1);
        alpha.setDuration(3000);
        alpha.setFillAfter(true);
        set.addAnimation(rotate);
        set.addAnimation(scale);
        set.addAnimation(alpha);
        //设置动画监听
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //判断之前有没有显示过新手引导画面
                SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
                jumpNextPage();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        rlRoot.startAnimation(set);


    }
    /**
     * 跳转下一个页面
     */
    private  void jumpNextPage(){
        //跳转到新手引导页
        Boolean userGuide= PreUtils.getBoolean(getApplicationContext(), "is_user_guide_showed", false);
        if (!userGuide) {
            startActivity(new Intent(SplashActivity.this, GuideActivity.class));
        }else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
        finish();

    }
}
