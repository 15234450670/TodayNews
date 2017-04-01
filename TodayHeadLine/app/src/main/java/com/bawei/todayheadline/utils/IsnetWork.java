package com.bawei.todayheadline.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 类的用途：
 * Created by ：杨珺达
 * date：2017/3/17
 */

public class IsnetWork {
    //判断是否有网
    public static boolean IsNet(Context context){
        ConnectivityManager com = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = com.getActiveNetworkInfo();
        if (info!=null){
            return true;
        }
        return false;
    }

    //判断是否是手机流量
    public static boolean isMobile(Context context) {
        //网络连接管理器
        ConnectivityManager com = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取网络信息
        NetworkInfo info = com.getActiveNetworkInfo();
        if (info != null && info.getType() == com.TYPE_MOBILE) {
            return true;
        }

        return false;
    }
    //判断是否是WIFI
    public static boolean isWIFI(Context context) {
        //网络连接管理器
        ConnectivityManager com = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取网络信息
        NetworkInfo info = com.getActiveNetworkInfo();
        if (info != null && info.getType() == com.TYPE_WIFI) {
            return true;
        }

        return false;
    }
}
