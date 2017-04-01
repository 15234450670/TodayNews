package com.bawei.todayheadline.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bawei.todayheadline.R;

public class Web extends AppCompatActivity {
    private ProgressDialog dialog = null;
    private WebView webView;
    final int RIGHT = 0;
    private GestureDetector gestureDetector;
    final int LEFT = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        gestureDetector = new GestureDetector(Web.this, onGestureListener);

        webView = (WebView) findViewById(R.id.web);
        Intent intent = getIntent();
        String webs = intent.getStringExtra("web");
        webView.loadUrl(webs);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        // 启用支持JavaScript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);


        // 进度条显示网页的加载过程
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                if (newProgress == 100) {
                    // 加载完毕
                    closeDialog(newProgress);
                } else {
                    openDialog(newProgress);
                }


                super.onProgressChanged(view, newProgress);
            }

            private void openDialog(int newProgress) {
                if (dialog == null) {
                    dialog = new ProgressDialog(Web.this);
                    dialog.setTitle("正在加载");
                    dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    dialog.setProgress(newProgress);
                    dialog.show();
                } else {
                    dialog.setProgress(newProgress);
                }
            }

            private void closeDialog(int newProgress) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog = null;
                }
            }
        });
        // 优先使用缓存优化效率
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
    private GestureDetector.OnGestureListener onGestureListener=new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            float x = e2.getX() - e1.getX();//滑动后的x值减去滑动前的x值 就是滑动的横向水平距离(x)
            float y = e2.getY() - e1.getY();//滑动后的y值减去滑动前的y值 就是滑动的纵向垂直距离(y)

            if (x > 100) {
                doResult(RIGHT);
                Log.w("tag", "RIGHT>" + x);
            }
            if (x < -100) {
                Log.w("tag", "LEFT>" + x);
                doResult(LEFT);
            }

            return true;
        }
    };
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                System.out.println(" ACTION_DOWN");//手指在屏幕上按下
                break;
            case MotionEvent.ACTION_MOVE:
                overridePendingTransition(R.anim.you, R.anim.zuo);
                System.out.println(" ACTION_MOVE");//手指正在屏幕上滑动
                break;
            case MotionEvent.ACTION_UP:

                System.out.println(" ACTION_UP");//手指从屏幕抬起了
                break;
            default:
                break;
        }

        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {    //注意这里不能用ONTOUCHEVENT方法，不然无效的
        gestureDetector.onTouchEvent(ev);
        webView.onTouchEvent(ev);//这几行代码也要执行，将webview载入MotionEvent对象一下，况且用载入把，不知道用什么表述合适
        return super.dispatchTouchEvent(ev);
    }


    public void doResult(int action) {

        switch (action) {
            case RIGHT:
                System.out.println("go right");
                finish();
                break;
            case LEFT:
                System.out.println("go left");
                break;
        }
    }

}
