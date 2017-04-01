package com.bawei.todayheadline.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.bawei.todayheadline.R;
import com.bumptech.glide.Glide;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LiXian extends AppCompatActivity {
    List<String> lt = new ArrayList<>();
    private ListView lv;
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_li_xian);

        Button button = (Button) findViewById(R.id.button);
        lv = (ListView) findViewById(R.id.iv);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adapter = new ArrayAdapter<String>(LiXian.this,android.R.layout.simple_list_item_1);
                 adapter.addAll( getImagePathFromSD());
                lv.setAdapter(adapter);
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View parents = View.inflate(LiXian.this, R.layout.activity_li_xian, null);
                View childer = View.inflate(LiXian.this, R.layout.pic, null);
                PopupWindow pop = new PopupWindow(childer, ActionBar.LayoutParams.MATCH_PARENT,600);
                pop.setTouchable(true);
                //设置popupWindown获取焦点 这样输入框才能获取到焦点  默认为false
                pop.setFocusable(true);

                //设置窗体外面部分可以触摸 如果true 触摸外面的时候将会隐藏窗体
                pop.setOutsideTouchable(true);
                //上面的方法要结合着 设置背景去用 new BitmapDrawable()意思是一个空的背景
                /**
                 * 两个方法结合使用 可以使点击周围的时候 窗体消失 ;点击返回键的时候窗体消失 而不是直接作用在activity上
                 */
                pop.setBackgroundDrawable(new BitmapDrawable());
                pop.showAtLocation(parents, Gravity.CENTER, 0, 0);
               ImageView image = (ImageView) childer.findViewById(R.id.image);
                Glide.with(LiXian.this).load(getImagePathFromSD().get(position)).into(image);
            }
        });

    }

    private List<String> getImagePathFromSD() {
      // 图片列表
      List<String> imagePathList = new ArrayList<String>();
      // 得到sd卡内image文件夹的路径   File.separator(/)
      String filePath = Environment.getExternalStorageDirectory().toString() + File.separator
              + "imageLru";
      // 得到该路径文件夹下所有的文件
      File fileAll = new File(filePath);
      File[] files = fileAll.listFiles();
      // 将所有的文件存入ArrayList中,并过滤所有图片格式的文件
      for (int i = 0; i < files.length; i++) {
          File file = files[i];
          if (checkIsImageFile(file.getPath())) {
              imagePathList.add(file.getPath());
          }
      }
      // 返回得到的图片列表
      return imagePathList;
  }
    private boolean checkIsImageFile(String fName) {
        boolean isImageFile = false;
        // 获取扩展名
        String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,
                fName.length()).toLowerCase();
        if (FileEnd.equals("jpg") || FileEnd.equals("png") || FileEnd.equals("gif")
                || FileEnd.equals("jpeg")|| FileEnd.equals("bmp") ) {
            isImageFile = true;
        } else {
            isImageFile = false;
        }
        return isImageFile;
    }
}
