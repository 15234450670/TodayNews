package com.bawei.todayheadline.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amiao.bitmapimagelibary.BitmapUtils;
import com.bawei.todayheadline.R;
import com.bawei.todayheadline.bean.HttpBean;
import com.bumptech.glide.Glide;

import org.xutils.image.ImageOptions;
import org.xutils.x;
import java.util.List;


/**
 * 类的用途：
 * Created by ：杨珺达
 * date：2017/3/15
 */

public class MyAdapter extends BaseAdapter {
    private final int TYPE_1 = 0;//文本
    private final int TYPE_2 = 1;//一张图片
    private final int TYPE_3 = 2;//3张图片

    private List<HttpBean.ResultBean.DataBean> list;
    private Context context;
    private final BitmapUtils utils;

    public MyAdapter(Context context, List<HttpBean.ResultBean.DataBean> list) {
        this.list = list;
        this.context = context;
        utils = new BitmapUtils(context);
    }

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
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {

               if(list.get(position).getThumbnail_pic_s()!=null&&list.get(position).getThumbnail_pic_s02()==null&&list.get(position).getThumbnail_pic_s03()==null){
            return TYPE_1;
        }else  if(list.get(position).getThumbnail_pic_s()!=null&&list.get(position).getThumbnail_pic_s02()!=null&&list.get(position).getThumbnail_pic_s03()==null){
            return TYPE_2;

        }else  if(list.get(position).getThumbnail_pic_s()!=null&&list.get(position).getThumbnail_pic_s02()!=null&&list.get(position).getThumbnail_pic_s03()!=null){
            return TYPE_3;

        }
        return TYPE_1;



    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder holder;
        int type = getItemViewType(position);
        if (convertView == null) {
            holder = new viewHolder();

            switch (type) {
                case TYPE_1:
                    convertView = View.inflate(context, R.layout.list_other, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.list_title);
                    holder.textView1 = (TextView) convertView.findViewById(R.id.list_con);

                    break;
                case TYPE_2:
                    convertView = View.inflate(context, R.layout.list_1_image, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.list_1_title);
                    holder.textView1 = (TextView) convertView.findViewById(R.id.list_1_con);
                    holder.imageView = (ImageView) convertView.findViewById(R.id.list_1_image);
                    break;
                case TYPE_3:
                    convertView = View.inflate(context, R.layout.list_3_image, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.list_3_image_title);
                    holder.textView1 = (TextView) convertView.findViewById(R.id.list_3_image_con);
                    holder.imageView = (ImageView) convertView.findViewById(R.id.i1);
                    holder.imageView1 = (ImageView) convertView.findViewById(R.id.i2);
                    holder.imageView2 = (ImageView) convertView.findViewById(R.id.i3);
                    break;

            }
            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();
        }
        switch (type) {
            case TYPE_1:
                holder.textView.setText(list.get(position).getTitle());
                holder.textView1.setText(list.get(position).getAuthor_name());
                break;
            case TYPE_2:
                holder.textView.setText(list.get(position).getTitle());
                holder.textView1.setText(list.get(position).getAuthor_name());
              //  x.image().bind(holder.imageView, list.get(position).getThumbnail_pic_s());
                utils.disPlay(holder.imageView,list.get(position).getThumbnail_pic_s());
                break;
            case TYPE_3:
                holder.textView.setText(list.get(position).getTitle());
                holder.textView1.setText(list.get(position).getAuthor_name());
           //     x.image().bind(holder.imageView, list.get(position).getThumbnail_pic_s());
              //  x.image().bind(holder.imageView1, list.get(position).getThumbnail_pic_s02());
              //  x.image().bind(holder.imageView2, list.get(position).getThumbnail_pic_s03());
                utils.disPlay(holder.imageView,list.get(position).getThumbnail_pic_s());
                utils.disPlay(holder.imageView1, list.get(position).getThumbnail_pic_s02());
                utils.disPlay(holder.imageView2,list.get(position).getThumbnail_pic_s03());
                break;
        }
        return convertView;
    }

    class viewHolder {
        TextView textView;
        TextView textView1;
        ImageView imageView;
        ImageView imageView1;
        ImageView imageView2;

    }

}
