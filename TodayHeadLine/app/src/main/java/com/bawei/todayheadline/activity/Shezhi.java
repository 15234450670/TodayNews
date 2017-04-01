package com.bawei.todayheadline.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amiao.bitmapimagelibary.BitmapUtils;
import com.bawei.todayheadline.R;
import com.bawei.todayheadline.utils.IsnetWork;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class Shezhi extends AppCompatActivity {
    private  ProgressBar pb;
    String url = "http://mapp.qzone.qq.com/cgi-bin/mapp/mapp_subcatelist_qq?yyb_cateid=-10&categoryName=%E8%85%BE%E8%AE%AF%E8%BD%AF%E4%BB%B6&pageNo=1&pageSize=20&type=app&platform=touch&network_type=unknown&resolution=412x732";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shezhi);

        TextView te_clear = (TextView) findViewById(R.id.te_clear);
        ImageView back = (ImageView) findViewById(R.id.back);
      TextView te_xiazai = (TextView) findViewById(R.id.te_xiazai);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Shezhi.this,MainActivity.class));
            }
        });

        te_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapUtils bitmapUtils = new BitmapUtils(Shezhi.this);
                bitmapUtils.deleteAllFiles(new File(Environment.getExternalStorageDirectory()+"/imageLru"));

            }
        });

        te_xiazai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isNet = IsnetWork.IsNet(Shezhi.this);
                boolean wifi = IsnetWork.isWIFI(Shezhi.this);
                boolean mobile = IsnetWork.isMobile(Shezhi.this);
                if (isNet) {
                    if (wifi) {
                        uploadApk(url);

                    }
                    if (mobile) {
                        AlertDialog.Builder bu = new AlertDialog.Builder(Shezhi.this);
                        bu.setTitle("请考虑");
                        bu.setMessage("也许会消耗大量流量");
                        bu.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Shezhi.this, "您以取消", Toast.LENGTH_SHORT).show();
                            }
                        });
                        bu.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                uploadApk(url);
                            }
                        });

                    }
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(Shezhi.this);
                    builder.setTitle("提示");
                    builder.setMessage("请开启网络连接");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            builder.create().dismiss();
                        }
                    });
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //通过隐式开启
                            Intent intent = null;
                            //有个版本号的判断
                            if (android.os.Build.VERSION.SDK_INT > 10) {
                                intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                            } else {
                                intent = new Intent();
                                intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");

                                intent.setAction("android.intent.action.VIEW");

                            }
                            startActivity(intent);
                        }
                    });
                    builder.create().show();
                }
            }
        });

    }

    /**
     * 显示更新选择对话框
     *
     * @param
     */
    private void showChoiseDialog(final String urls) {


        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        android.app.AlertDialog dialog = null;

        builder.setTitle("版本更新");
        builder.setMessage("检测到新版本，是否下载更新？");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //下载

                downLoadApk(urls);

            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    //获取版本更新数据
    private void uploadApk(String url) {
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("xxx", result.toString());
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("app");
                    JSONObject jo = jsonArray.getJSONObject(0);

                    //url  apk地址
                    String urls = jo.getString("url");

                    Log.i("xxx", "url:" + urls );


                    showChoiseDialog(urls);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void downLoadApk(final String urls) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = builder.create();
        View view = View.inflate(this, R.layout.alertdalog, null);

        alertDialog.setView(view);
        alertDialog.show();
        pb = (ProgressBar) view.findViewById(R.id.pb);
        Button canlue = (Button) view.findViewById(R.id.canlue);
        Button sure = (Button) view.findViewById(R.id.sure);
        canlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestParams params = new RequestParams(urls);
                //自定义保存路径 Environment.getExternalStorageDirectory() sdcard 根目录
                params.setSaveFilePath(Environment.getExternalStorageDirectory() + "/app/");
                //自动为文件命令
                params.setAutoRename(false);
                params.setAutoResume(true);
                x.http().post(params, new Callback.ProgressCallback<File>() {

                    //网络请求成功时回调
                    @Override
                    public void onSuccess(File result) {
                        Toast.makeText(Shezhi.this, "下载成功", Toast.LENGTH_SHORT).show();
                        //apk下载完成后 调用系统的安装方法
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(result), "application/vnd.android.package-archive");
                        startActivity(intent);
                        Toast.makeText(Shezhi.this, "安装成功", Toast.LENGTH_SHORT).show();

                    }

                    //网络请求错误时回调
                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {

                    }

                    //网络请求取消的时候回调
                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    //网络请求完成的时候回调
                    @Override
                    public void onFinished() {

                    }

                    //网络请求之前回调
                    @Override
                    public void onWaiting() {

                    }

                    //网络请求开始的时候回调
                    @Override
                    public void onStarted() {

                    }

                    //下载的时候不断回调的方法
                    @Override
                    public void onLoading(long total, long current, boolean isDownloading) {
                        pb.setMax((int) total);
                        pb.setProgress((int) current);
                    }
                });
            }
        });

    }





}
