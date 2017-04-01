package com.bawei.todayheadline.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bawei.todayheadline.R;
import com.bawei.todayheadline.fragment.Frag_tui;
import com.bawei.todayheadline.fragment.Frag_voide;
import com.bawei.todayheadline.utils.ImageUpdate;
import com.jaeger.library.StatusBarUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;
import weeks.drag.com.draglibary.ManagerActivity;
import weeks.drag.com.draglibary.bean.ChannelItem;
import weeks.drag.com.draglibary.bean.ChannelManage;


public class MainActivity extends FragmentActivity {

    private String[] title = new String[]{"头条", "社会", "国际", "国内", "娱乐"
            , "体育", "军事", "科技", "财经", "时尚","视频"};
    /*private String[] str = new String[]{"http://ic.snssdk.com/2/article/v25/stream/?count=20&min_behot_time=1455521444&bd_city=%E5%8C%97%E4%BA%AC%E5%B8%82&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455521401&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000"
            , "http://ic.snssdk.com/2/article/v25/stream/?category=news_hot&count=20&min_behot_time=1455521166&bd_city=%E5%8C%97%E4%BA%AC%E5%B8%82&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455521401&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_nme=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000"
            , "http://ic.snssdk.com/2/article/v25/stream/?category=news_world&count=20&min_behot_time=1455523059&bd_city=%E5%8C%97%E4%BA%AC%E5%B8%82&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455523440&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000"
            , "http://ic.snssdk.com/2/article/v25/stream/?category=news_local&count=20&min_behot_time=1455521226&bd_city=%E5%8C%97%E4%BA%AC%E5%B8%82&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455521401&loc_mode=5&user_city=%E5%8C%97%E4%BA%AC&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&ap_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000"
            , "http://ic.snssdk.com/2/article/v25/stream/?category=news_society&count=20&min_behot_time=1455521720&bd_city=%E5%8C%97%E4%BA%AC%E5%B8%82&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455522107&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000"
            , "http://ic.snssdk.com/2/article/v25/stream/?category=news_entertainment&count=20&min_behot_time=1455522338&bd_city=%E5%8C%97%E4%BA%AC%E5%B8%82&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455522784&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000"
            , "http://ic.snssdk.com/2/article/v25/stream/?category=image_ppmm&count=20&min_behot_time=1455524172&bd_city=%E5%8C%97%E4%BA%AC%E5%B8%82&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455524092&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000"
            , "http://ic.snssdk.com/2/article/v25/stream/?category=video&count=20&min_behot_time=1455521349&bd_city=%E5%8C%97%E4%BA%AC%E5%B8%82&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455522107&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000"
            , "http://ic.snssdk.com/2/article/v25/stream/?category=news_car&count=20&bd_city=%E5%8C%97%E4%BA%AC%E5%B8%82&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455522784&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000"
            , "http://ic.snssdk.com/2/article/v25/stream/?category=news_sports&count=20&min_behot_time=1455522629&bd_city=%E5%8C%97%E4%BA%AC%E5%B8%82&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455522784&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000"
            , "http://ic.snssdk.com/2/article/v25/stream/?category=news_finance&count=20&min_behot_time=1455522899&bd_city=%E5%8C%97%E4%BA%AC%E5%B8%82&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455523440&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000"
            , "http://ic.snssdk.com/2/article/v25/stream/?category=news_military&count=20&min_behot_time=1455522991&bd_city=%E5%8C%97%E4%BA%AC%E5%B8%82&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455523440&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000"
            , "http://ic.snssdk.com/2/article/v25/stream/?category=essay_joke&count=20&bd_city=%E5%8C%97%E4%BA%AC%E5%B8%82&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455523440&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000"
            , "http://ic.snssdk.com/2/article/v25/stream/?category=image_funny&count=20&min_behot_time=1455524031&bd_city=%E5%8C%97%E4%BA%AC%E5%B8%82&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455523440&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000"
            , "http://ic.snssdk.com/2/article/v25/stream/?category=news_health&count=20&bd_city=%E5%8C%97%E4%BA%AC%E5%B8%82&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455524092&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000"
            , "http://ic.snssdk.com/2/article/v25/stream/?category=news_tech&count=20&min_behot_time=1455522427&bd_city=%E5%8C%97%E4%BA%AC%E5%B8%82&bd_latitude=40.049317&bd_longitude=116.296499&bd_loc_time=1455522784&loc_mode=5&lac=4527&cid=28883&iid=3642583580&device_id=11131669133&ac=wifi&channel=baidu&aid=13&app_name=news_article&version_code=460&device_platform=android&device_type=SCH-I919U&os_api=19&os_version=4.4.2&uuid=285592931621751&openudid=AC9E172CE2490000"};*/
    String[] str = new String[]{"http://v.juhe.cn/toutiao/index?type=top&key=49c4ede2b341aa948a3760c578647054"
            , "http://v.juhe.cn/toutiao/index?type=shehui&key=49c4ede2b341aa948a3760c578647054"
            , "http://v.juhe.cn/toutiao/index?type=guonei&key=49c4ede2b341aa948a3760c578647054"
            , "http://v.juhe.cn/toutiao/index?type=guoji&key=49c4ede2b341aa948a3760c578647054"
            , "http://v.juhe.cn/toutiao/index?type=yule&key=49c4ede2b341aa948a3760c578647054"
            , "http://v.juhe.cn/toutiao/index?type=tiyu&key=49c4ede2b341aa948a3760c578647054"
            , "http://v.juhe.cn/toutiao/index?type=junshi&key=49c4ede2b341aa948a3760c578647054"
            , "http://v.juhe.cn/toutiao/index?type=keji&key=49c4ede2b341aa948a3760c578647054"
            , "http://v.juhe.cn/toutiao/index?type=caijing&key=49c4ede2b341aa948a3760c578647054"
            , "http://v.juhe.cn/toutiao/index?type=shishang&key=49c4ede2b341aa948a3760c578647054"};
    private ImageView image_cela;
    private TabLayout tab;
    private ViewPager vp;

    List<Fragment> list_frag = new ArrayList<>();
    private static ImageView qq_log;
    private static final String TAG = "MainActivity";
    private static final String APP_ID = "1105602574";//官方获取的APPID
    private Tencent mTencent;
    private BaseUiListener mIUiListener;
    private UserInfo mUserInfo;
    private SlidingMenu menu;
    private LinearLayout lin_log;
    private LinearLayout lin_xinxi;
    private TextView te_name;
    private ImageView te_image;
    private ImageView image_shouji;
    private CheckBox image_yejian;
    private int theme = R.style.AppTheme;
    private TextView text_log;
    private TextView cela_lixian;
    private TextView text_shoucang;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    private static final int REQUESTCODE_PICK =1 ;
    protected static Uri tempUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 判断是否有主题存储
        if (savedInstanceState != null) {
            theme = savedInstanceState.getInt("theme");
            setTheme(theme);
        }
        setContentView(R.layout.activity_main);
     StatusBarUtil.setTransparent(MainActivity.this);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        x.view().inject(this);
        mTencent = Tencent.createInstance(APP_ID, MainActivity.this.getApplicationContext());
        initView();
        SMSSDK.initSDK(this, "1c110a0b30e2f", "0aad20f11aaf9b664fadaa28686f235c");

    }



    private void initView() {
        image_cela = (ImageView) findViewById(R.id.image_cela);

         ImageView jia = (ImageView) findViewById(R.id.jia);
        tab = (TabLayout) findViewById(R.id.tab);
        vp = (ViewPager) findViewById(R.id.vp);
        menu = new SlidingMenu(MainActivity.this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setBehindOffset(200);
        menu.setMenu(R.layout.cela_list);
        menu.attachToActivity(MainActivity.this, SlidingMenu.SLIDING_CONTENT);
        cela_lixian = (TextView) findViewById(R.id.cela_lixian);
        qq_log = (ImageView) findViewById(R.id.qq_log);
        image_shouji = (ImageView) findViewById(R.id.image_shouji);
        image_yejian = (CheckBox) findViewById(R.id.image_yejian);
        lin_log = (LinearLayout) findViewById(R.id.lin_log);
        lin_xinxi = (LinearLayout) findViewById(R.id.lin_xinxi);
        te_name = (TextView) findViewById(R.id.te_name);
        te_image = (ImageView) findViewById(R.id.te_image);
        text_log = (TextView) findViewById(R.id.text_log);
        text_shoucang = (TextView) findViewById(R.id.text_shoucang);
       TextView te_she =(TextView) findViewById(R.id.te_she);
        MyAdapter adapter = new MyAdapter(getSupportFragmentManager());
        for (int i = 0; i < 10; i++) {
            //  tab.addTab(tab.newTab().setText(title[i]));
            Frag_tui frag = new Frag_tui();
            Bundle bundle = new Bundle();
            bundle.putString("path",str[i]);
            frag.setArguments(bundle);
            list_frag.add(frag);
        }
        list_frag.add(new Frag_voide());
        Log.i("dd",list_frag.size()+""+"sssssssss");
        Log.i("dd",title.length+""+"dasdasd");
        vp.setAdapter(adapter);
        tab.setupWithViewPager(vp);
        vp.setOffscreenPageLimit(3);
      jia.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              for (int i = 0; i < 10; i++) {
                  ChannelItem item=new ChannelItem(i+2,"生活",i+2,1);//上面的gridView的值
                  ChannelItem itemo=new ChannelItem(i+10,"豪车",i+10,0);//下面的gridView的值
                  ChannelManage manage=new ChannelManage(item,itemo);
              }
          startActivity(new Intent(MainActivity.this,ManagerActivity.class));
          }
      });
        //日夜间模式切换
        image_yejian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                theme = (theme == R.style.AppTheme) ? R.style.NightAppTheme : R.style.AppTheme;
                MainActivity.this.recreate();
            }
        });
        //日夜间模式图标的转换
        image_yejian.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    image_yejian.setSelected(true);
                    text_log.setTextColor(Color.RED);
                } else {
                    image_yejian.setSelected(false);
                }
            }
        });
        //点击显示侧拉
        image_cela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.toggle();
            }
        });

        //收藏
        text_shoucang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Shoucang.class));
            }
        });
        //点击QQ登录
        qq_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIUiListener = new BaseUiListener();
                //all表示获取所有权限
                mTencent.login(MainActivity.this, "all", mIUiListener);

            }
        });
        //点击进入离线界面
        cela_lixian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LiXian.class));
            }
        });
        //点击进入设置界面
        te_she.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Shezhi.class));

            }
        });
        //点击手机登录
        image_shouji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开注册页面
                RegisterPage registerPage = new RegisterPage();
                registerPage.setRegisterCallback(new EventHandler() {


                    public void afterEvent(int event, int result, Object data) {
                        // 解析注册结果
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            @SuppressWarnings("unchecked")
                            HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                            String country = (String) phoneMap.get("country");
                            String phone = (String) phoneMap.get("phone");
                            lin_log.setVisibility(View.INVISIBLE);
                            lin_xinxi.setVisibility(View.VISIBLE);
                            image_cela.setImageResource(R.mipmap.person4);
                            lin_xinxi.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);  //先得到构造器
                                    builder.setTitle("提示"); //设置标题
                                    builder.setMessage("是否确认退出?"); //设置内容
                                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //关闭dialog
                                            lin_log.setVisibility(View.VISIBLE);
                                            lin_xinxi.setVisibility(View.INVISIBLE);
                                            image_cela.setImageResource(R.mipmap.image_cela);
                                            dialog.dismiss();
                                        }
                                    });
                                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    //参数都设置完成了，创建并显示出来
                                    builder.create().show();
                                }
                            });
                        }
                    }
                });
                registerPage.show(MainActivity.this);


            }
        });


    }




    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return list_frag.get(position);
        }

        @Override
        public int getCount() {
            return list_frag.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];

        }
    }
        //QQ授权与获取信息
    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            Toast.makeText(MainActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "response:" + response);
            JSONObject obj = (JSONObject) response;
            try {
                String openID = obj.getString("openid");
                String accessToken = obj.getString("access_token");
                String expires = obj.getString("expires_in");
                mTencent.setOpenId(openID);
                mTencent.setAccessToken(accessToken, expires);
                QQToken qqToken = mTencent.getQQToken();
                mUserInfo = new UserInfo(getApplicationContext(), qqToken);
                mUserInfo.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object response) {
                        Log.e(TAG, "登录成功" + response.toString());
                        JSONObject obj = (JSONObject) response;
                        try {
                            String figureurl_qq_1 = obj.getString("figureurl_qq_1");
                            String figureurl_qq_2 = obj.getString("figureurl_qq_2");
                            String name = obj.getString("nickname");
                            ImageOptions options = new ImageOptions.Builder().setCircular(true).setUseMemCache(true).build();
                            x.image().bind(image_cela, figureurl_qq_1, options);
                            lin_log.setVisibility(View.INVISIBLE);
                            lin_xinxi.setVisibility(View.VISIBLE);
                            x.image().bind(te_image, figureurl_qq_2, options);
                            te_name.setText(name);
                           if (lin_xinxi.getVisibility()==View.VISIBLE){
                              te_image.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View v) {
                                      showChoosePicDialog();
                                  }
                              });
                           }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(UiError uiError) {
                        Log.e(TAG, "登录失败" + uiError.toString());
                    }

                    @Override
                    public void onCancel() {
                        Log.e(TAG, "登录取消");

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(MainActivity.this, "授权失败", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCancel() {
            Toast.makeText(MainActivity.this, "授权取消", Toast.LENGTH_SHORT).show();

        }

    }
    //拍照
    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置头像");
        String[] items = { "选择本地照片", "拍照" };
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                        // 如果朋友们要限制上传到服务器的图片类型时可以直接写如：image/jpeg 、 image/png等的类型
                        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(pickIntent, REQUESTCODE_PICK);
                        break;
                    case TAKE_PICTURE: // 拍照
                        Intent openCameraIntent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri = Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory(), "image.jpg"));
                        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                        break;
                }
            }
        });
        builder.create().show();
    }
      //回调方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mIUiListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    /**
     * 保存裁剪之后的图片数据
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            photo = ImageUpdate.toRoundBitmap(photo, tempUri); // 这个时候的图片已经被处理成圆形的了
            te_image.setImageBitmap(photo);
            image_cela.setImageBitmap(photo);
            uploadPic(photo);
        }
    }

    private void uploadPic(Bitmap bitmap) {
        // 上传至服务器
        // ... 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
        // 注意这里得到的图片已经是圆形图片了
        // bitmap是没有做个圆形处理的，但已经被裁剪了

        String imagePath = ImageUpdate.savePhoto(bitmap, Environment
                .getExternalStorageDirectory().getAbsolutePath(), String
                .valueOf(System.currentTimeMillis()));
        Log.e("imagePath", imagePath+"");
        if(imagePath != null){
            // 拿着imagePath上传了
            // ...
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("theme", theme);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        theme = savedInstanceState.getInt("theme");
    }


}

