package com.amiao.bitmapimagelibary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

/**
 * Created by lenovo on 2017/3/23.
 */

public class BitmapUtils {
    //获得sd的文件夹的目录
    private String path= Environment.getExternalStorageDirectory()+"/imageLru";
    //声明文件夹
    File fileDir=new File(path);
    //内存缓存LruCache
    //图片内存缓存所占用内存的大小
    int maxSize = (int)(Runtime.getRuntime().maxMemory()/8);
    LruCache<String,Bitmap> lru=new LruCache<String,Bitmap>(maxSize){
        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getRowBytes()*value.getHeight();
        }
    };

    private Context context;
    public BitmapUtils(Context context) {
      this.context=context;
        if(!fileDir.exists()){
            fileDir.mkdirs();//创建文件夹的目录
        }
    }


    public void disPlay(ImageView iv, String url) {
        Bitmap bitmap = loadLruCache(url);
        if(bitmap!=null){
            iv.setImageBitmap(bitmap);

        }else{
            Bitmap bitmap1 = loadSD(iv, url);
            if(bitmap1!=null){
                iv.setImageBitmap(bitmap1);
            }else{

                //调用网络加载的方法
                loadIntentImage(iv,url);
            }

        }


 }

    private Bitmap loadLruCache( String url) {
        Bitmap bitmap = lru.get(url);
        return bitmap;

    }

    private Bitmap loadSD(ImageView iv, String url) {
        //缓存到sd卡中
        String name = getFileName(url);
        File file=new File(fileDir,name);
        if(file.exists()){
            //进行裁剪
            BitmapFactory.Options options=new BitmapFactory.Options();
            //第一次采样
            options.inJustDecodeBounds=true;
            //图片的名称和选项
            BitmapFactory.decodeFile(name,options);
            int height= options.outHeight;
            int width = options.outWidth;
            //获得手机屏幕
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            int heightPixels = metrics.heightPixels;
            int widthPixels = metrics.widthPixels;

            int scale=0;
            int scale_X=width/widthPixels;
            int scale_Y=height/heightPixels;
            scale=scale_X>scale_Y?scale_X:scale_Y;

            options.inJustDecodeBounds=false;
            options.inSampleSize=scale;
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
          //进行缓存到内存中
         lru.put(url,bitmap);

          return bitmap;
    }
        return null;
    }

    private void loadIntentImage(ImageView iv, String url) {
        //采用异步进行解析
        MyAsynecTask task=new MyAsynecTask();
        task.execute(new ImageViewPath(iv,url));


    }
    private class MyAsynecTask extends AsyncTask<ImageViewPath,Integer,ImageToBitmap>{


        private Bitmap bitmap;

        @Override
        protected ImageToBitmap doInBackground(ImageViewPath... params) {
            ImageViewPath param = params[0];
            String url=param.url;
            ImageView iv=param.iv;
            //进行解析
            HttpClient client=new DefaultHttpClient();
            HttpGet get=new HttpGet(url);
            try {
                HttpResponse response = client.execute(get);
                if(response.getStatusLine().getStatusCode()==200){
                    InputStream stream = response.getEntity().getContent();
                    //写入SD卡
                    //获得图片的名称
                    String name = getFileName(url);
                    File file=new File(fileDir,name);//将图片的名称放入
                    //通过文件输出流进行输出
                    FileOutputStream fos=new FileOutputStream(file);
                    byte[] b=new byte[1024];
                    int len=0;
                    while((len=stream.read(b))!=-1){
                        fos.write(b,0,len);
                    }
                    fos.close();
                    stream.close();
                    //需要一个bitmap
                    bitmap = loadSD(iv,url);

                 }

            } catch (Exception e) {
                e.printStackTrace();
            }
            //将ImageView转换为Bitmap
            ImageToBitmap ivb=new ImageToBitmap(iv,bitmap);
            return ivb;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ImageToBitmap imageToBitmap) {
            super.onPostExecute(imageToBitmap);
            if(imageToBitmap.bitmap!=null){
                imageToBitmap.iv.setImageBitmap(imageToBitmap.bitmap);

            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }
  public class ImageToBitmap {
      private ImageView iv;
      private Bitmap bitmap;
      public ImageToBitmap(ImageView iv,Bitmap bitmap) {
            this.iv=iv;
            this.bitmap=bitmap;
      }
  }


    public  class ImageViewPath{
        private ImageView iv;
        private String url;
        public ImageViewPath(ImageView iv,String url) {
         this.iv=iv;
         this.url=url;
        }
    }

    //获取图片的名称
    public String getFileName(String url){
        int i = url.length();
         //return  url.substring(i).lastIndexOf("/") + 1+"";
        return Md5Utils.encode(url)+".jpg";
    }
    public void deleteAllFiles(File root) {
                    File files[] = root.listFiles();
                    if (files != null)
                        for (File f : files) {
                            if (f.isDirectory()) { // 判断是否为文件夹
                                deleteAllFiles(f);
                                try {
                                    f.delete();
                                } catch (Exception e) {
                                }
                            } else {
                                if (f.exists()) { // 判断是否存在
                                    deleteAllFiles(f);
                                    try {
                                        f.delete();
                                    } catch (Exception e) {
                                    }
                                }
                }
            }


    }

}
