package com.bawei.todayheadline.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bawei.todayheadline.R;
import com.bawei.todayheadline.activity.MainActivity;
import com.bawei.todayheadline.bean.VideoBean;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import maqian.baidu.com.xlistviewlibrary.XListView;

/**
 * 类的用途：
 * Created by ：杨珺达
 * date：2017/3/16
 */

public class Frag_voide extends Fragment {
    private XListView xl;
    List<VideoBean> list = new ArrayList<>();
    String http_url = "http://c.3g.163.com/nc/video/list/V9LG4CHOR/n/10-10.html";
    private View v;
    private MainActivity activity;
    private Tencent tencent;
    private ProgressBar pb;
    Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.frag, null);
        activity = (MainActivity) getActivity();
        x.view().inject(activity);
        tencent = Tencent.createInstance("1105602574", activity);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        xl = (XListView) v.findViewById(R.id.xl);
         xl.setPullLoadEnable(true);
        xl.setPullRefreshEnable(true);
        xl.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        xl.stopRefresh();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        xl.stopLoadMore();
                    }
                }, 2000);
            }
        });
        getData();
        getLoadMp4();

    }

    private void getLoadMp4() {

    }


    private void getData() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(activity, http_url, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new Gson();
                try {
                    JSONObject obj = new JSONObject(responseString);
                    //获得key键
                    Iterator<String> keys = obj.keys();
                    while (keys.hasNext()) {
                        String next = keys.next();//每个key
                        JSONArray jsonArray = obj.optJSONArray(next);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.optJSONObject(i);
                            VideoBean bean = gson.fromJson(object.toString(), VideoBean.class);
                            list.add(bean);

                        }
                    }

                    xl.setAdapter(new MyAdapter());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }


    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            viewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(activity, R.layout.video, null);
                holder = new viewHolder();
                holder.jj = (JCVideoPlayerStandard) convertView.findViewById(R.id.player_list_video);
                holder.te_fen = (TextView) convertView.findViewById(R.id.te_fen);
                holder.te_s = (TextView) convertView.findViewById(R.id.te_s);
                convertView.setTag(holder);
            } else {
                holder = (viewHolder) convertView.getTag();
            }
            boolean setUp = holder.jj.setUp(list.get(position).getMp4_url(), list.get(position).getTitle());

            if (setUp) {
                Glide.with(getActivity()).load(list.get(position).getCover()).into(holder.jj.thumbImageView);
            }
            holder.te_fen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View view = View.inflate(getActivity(), R.layout.pop, null);
                    View view1 = View.inflate(getActivity(), R.layout.activity_main, null);
                   final PopupWindow popupWindow =  new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,300);
                    popupWindow.setTouchable(true);
                    //设置popupWindown获取焦点 这样输入框才能获取到焦点  默认为false
                    popupWindow.setFocusable(true);

                    //设置窗体外面部分可以触摸 如果true 触摸外面的时候将会隐藏窗体
                    popupWindow.setOutsideTouchable(true);
                    //上面的方法要结合着 设置背景去用 new BitmapDrawable()意思是一个空的背景
                    /**
                     * 两个方法结合使用 可以使点击周围的时候 窗体消失 ;点击返回键的时候窗体消失 而不是直接作用在activity上
                     */
                    popupWindow.setBackgroundDrawable(new BitmapDrawable());
                    popupWindow.showAtLocation(view1, Gravity.BOTTOM, 0, 0);
                    ImageView qq = (ImageView) view.findViewById(R.id.qq_fen);
                    ImageView qq_kong = (ImageView) view.findViewById(R.id.qq_kong);
                    qq.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ShareListener myListener = new ShareListener();
                            Bundle params = new Bundle();
                            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
                            params.putString(QQShare.SHARE_TO_QQ_TITLE, list.get(position).getTitle());//分享的标题
                            params.putString(QQShare.SHARE_TO_QQ_SUMMARY, list.get(position).getMp4_url());//分享的内容
                            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, list.get(position).getMp4_url());//链接
                            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, list.get(position).getCover());//图片
                            params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "今日头条");//应用名
                            tencent.shareToQQ(activity, params, myListener);
                            popupWindow.dismiss();
                        }
                    });
                    qq_kong.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ShareListener myListener = new ShareListener();
                            Bundle params = new Bundle();
                            params.putInt(QQShare.SHARE_TO_QQ_EXT_INT,  QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
                            params.putString(QQShare.SHARE_TO_QQ_TITLE, list.get(position).getTitle());//分享的标题
                            params.putString(QQShare.SHARE_TO_QQ_SUMMARY, list.get(position).getMp4_url());//分享的内容
                            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, list.get(position).getMp4_url());//链接
                            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, list.get(position).getCover());//图片
                            params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "今日头条");//应用名
                            tencent.shareToQQ(activity, params, myListener);
                            popupWindow.dismiss();
                        }
                    });
                }
            });
            holder.te_s.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    final AlertDialog alertDialog = builder.create();
                    View view = View.inflate(activity, R.layout.alertdalog, null);
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
                            RequestParams params = new RequestParams(list.get(position).getMp4_url());
                            params.setSaveFilePath(Environment.getExternalStorageDirectory() + "/" + list.get(position).getTitle() + ".mp4");
                            params.setUseCookie(true);
                            params.setAutoResume(true);
                            params.setAutoRename(false);

                            x.http().get(params, new Callback.ProgressCallback<File>() {
                                @Override
                                public void onSuccess(File result) {
                                    Toast.makeText(activity, "onSuccess" + result.toString(), Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
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

                                @Override
                                public void onWaiting() {

                                }

                                @Override
                                public void onStarted() {

                                }

                                @Override
                                public void onLoading(long total, long current, boolean isDownloading) {
                                    pb.setMax((int) total);
                                    pb.setProgress((int) current);


                                }
                            });

                        }
                    });


                }
            });
            return convertView;

        }

        class viewHolder {
            JCVideoPlayerStandard jj;
            TextView te_fen;
            TextView te_s;
        }
    }

    private class ShareListener implements IUiListener {

        @Override
        public void onCancel() {
            // TODO Auto-generated method stub
            // activity.toast("分享取消");
        }

        @Override
        public void onComplete(Object arg0) {
            // TODO Auto-generated method stub
            Toast.makeText(activity, "分享成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(UiError arg0) {
            // TODO Auto-generated method stub
            //  activity.toast("分享出错");
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ShareListener myListener = new ShareListener();
        Tencent.onActivityResultData(requestCode, resultCode, data, myListener);
    }
}
