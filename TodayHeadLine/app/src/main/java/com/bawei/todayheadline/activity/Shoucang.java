package com.bawei.todayheadline.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bawei.todayheadline.R;
import com.bawei.todayheadline.Sqlite.Sqlite;
import com.bawei.todayheadline.bean.Shou;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class Shoucang extends AppCompatActivity {
    private final int TYPE_1 = 0;//文本
    private final int TYPE_2 = 1;//一张图片
    private final int TYPE_3 = 2;//3张图片
    private SQLiteDatabase db;
    private ListView lv;
     List<Shou>list = new ArrayList<>();
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoucang);
        x.view().inject(this);
        lv = (ListView) findViewById(R.id.lv);
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Shoucang.this,MainActivity.class));
                finish();
            }
        });
        Sqlite sqlite = new Sqlite(this);
        db = sqlite.getWritableDatabase();
        String sql="select * from hide";
        Cursor cursor = db.rawQuery(sql, null);
        Shou shou=null;
        while(cursor.moveToNext()){
            shou = new Shou();
            String   title = cursor.getString(1);
            String   image = cursor.getString(2);
            String   imagea =  cursor.getString(3);
            String   imageb =  cursor.getString(4);

            shou.setTitle(title);
            shou.setImage(image);
            shou.setImagea(imagea);
            shou.setImageb(imageb);
            list.add(shou);
        }
        adapter = new Adapter();
        lv.setAdapter(adapter);
       lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
           @Override
           public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
               AlertDialog.Builder builder = new AlertDialog.Builder(Shoucang.this);
               builder.setTitle("提示");
               builder.setMessage(" 删除 ？");
               builder.setNegativeButton("取消",null);
               builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       String title = list.get(position).getTitle();
                     //  Toast.makeText(Shoucang.this, ""+title, Toast.LENGTH_SHORT).show();
                       list.remove(position);
                     db.delete("hide","title=?",new String[]{title});
                       adapter.notifyDataSetChanged();
                   }
               });
               builder.create().show();
               return true;
           }
       });
      // Toast.makeText(this, ""+list.toString(), Toast.LENGTH_SHORT).show();


    }
    private class Adapter extends  BaseAdapter{
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

            if(list.get(position).getImage()!=null&&list.get(position).getImagea()==null&&list.get(position).getImageb()==null){
                return TYPE_1;
            }else  if(list.get(position).getImage()!=null&&list.get(position).getImagea()!=null&&list.get(position).getImageb()==null){
                return TYPE_2;

            }else  if(list.get(position).getImage()!=null&&list.get(position).getImagea()!=null&&list.get(position).getImageb()!=null){
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
                        convertView = View.inflate(Shoucang.this, R.layout.list_other, null);
                        holder.textView = (TextView) convertView.findViewById(R.id.list_title);
                        holder.textView1 = (TextView) convertView.findViewById(R.id.list_con);

                        break;
                    case TYPE_2:
                        convertView = View.inflate(Shoucang.this, R.layout.list_1_image, null);
                        holder.textView = (TextView) convertView.findViewById(R.id.list_1_title);
                        holder.textView1 = (TextView) convertView.findViewById(R.id.list_1_con);
                        holder.imageView = (ImageView) convertView.findViewById(R.id.list_1_image);
                        break;
                    case TYPE_3:
                        convertView = View.inflate(Shoucang.this, R.layout.list_3_image, null);
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
                    break;
                case TYPE_2:
                    holder.textView.setText(list.get(position).getTitle());
                    x.image().bind(holder.imageView, list.get(position).getImage());
                    break;
                case TYPE_3:
                    holder.textView.setText(list.get(position).getTitle());
                    x.image().bind(holder.imageView, list.get(position).getImage());
                    x.image().bind(holder.imageView1, list.get(position).getImagea());
                    x.image().bind(holder.imageView2, list.get(position).getImageb());
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
}
