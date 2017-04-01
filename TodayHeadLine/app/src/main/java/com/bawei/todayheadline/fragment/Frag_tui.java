package com.bawei.todayheadline.fragment;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.bawei.todayheadline.Sqlite.Sqlite;
import com.bawei.todayheadline.activity.Web;
import com.bawei.todayheadline.adapter.MyAdapter;
import com.bawei.todayheadline.R;
import com.bawei.todayheadline.bean.HttpBean;
import com.bawei.todayheadline.bean.Tuijian;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import maqian.baidu.com.xlistviewlibrary.XListView;

/**
 * 类的用途：
 * Created by ：杨珺达
 * date：2017/3/10
 */

public class Frag_tui extends Fragment {
    private  String p;


    private List<HttpBean.ResultBean.DataBean> data;
    private String path;
    private SQLiteDatabase db;

    public void setP(String p) {
        this.p = p;
    }

    private XListView xl;
   Handler handler = new Handler();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag, null);
        xl = (XListView) view.findViewById(R.id.xl);
        xl.setPullLoadEnable(true);
        xl.setPullRefreshEnable(true);
        xl.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getActivity(), "刷新中", Toast.LENGTH_SHORT).show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        xl.stopRefresh();
                    }
                },2000);
            }

            @Override
            public void onLoadMore() {
                Toast.makeText(getActivity(), "加载中", Toast.LENGTH_SHORT).show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        xl.stopLoadMore();
                    }
                },2000);
            }
        });
        Bundle arguments = getArguments();
        path = arguments.getString("path");

        xl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), Web.class);
                intent.putExtra("web",data.get(position-1).getUrl());
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Sqlite sqlite = new Sqlite(getActivity());
        db = sqlite.getWritableDatabase();
        AsyncHttpClient ah  = new AsyncHttpClient();
        ah.get(getActivity(), path, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new Gson();
                HttpBean tuijian = gson.fromJson(responseString, HttpBean.class);
                data = tuijian.getResult().getData();
                xl.setAdapter(new MyAdapter(getActivity(),data));
            }
        });
        xl.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,final int position, long id) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("提醒");
                builder.setMessage("是否收藏");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        builder.create().dismiss();
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ContentValues values = new ContentValues();
                        values.put("title",data.get(position-1).getTitle());
                        values.put("image",data.get(position-1).getThumbnail_pic_s());
                        values.put("image1",data.get(position-1).getThumbnail_pic_s02());
                        values.put("image2",data.get(position-1).getThumbnail_pic_s03());
                        db.insert("hide",null,values);
                    }
                });
                builder.create().show();
                return true;
            }
        });

    }

}
