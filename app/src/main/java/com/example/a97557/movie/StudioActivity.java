package com.example.a97557.movie;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 97557 on 2017/4/7.
 */

public class StudioActivity extends AppCompatActivity {
    private ImageView imageView, ponitView;
    private ListView listView;
    private ListViewAdapter listViewAdapter;
    private List<StudioListBean> listBeans = new ArrayList<>();
    private SQLiteDatabase db;
    private PopupWindow popupWindow;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_studio);
        db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString() + "/studios.db3", null);
        CloseActivityClass.activityList.add(this);
        imageView = (ImageView) findViewById(R.id.back_image);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ponitView = (ImageView) findViewById(R.id.three_points);

        listView = (ListView)findViewById(R.id.studio_list);

        new Thread(getAllStudios).start();
    }

    Runnable getAllStudios = new Runnable() {
        @Override
        public void run() {
            String path = "http://115.159.82.119:8080/Movie/studio/StudioQueryAll";
            Message msg = new Message();

            HttpGet request = new HttpGet(path);
            HttpClient client = new DefaultHttpClient();
            try {
                HttpResponse response = client.execute(request);
                if (response.getStatusLine().getStatusCode() == 200) {
                    String result = EntityUtils.toString(response.getEntity());
                    Bundle data = new Bundle();
                    data.putString("result",result);
                    msg.setData(data);
                    Log.i("演出厅：", result);
                    handler.sendMessage(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String result = data.getString("result");
            try {
                JSONArray array = new JSONArray(result);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = (JSONObject) array.get(i);
                    StudioListBean studio = new StudioListBean();
                    studio.setStudioName(object.getString("studio_name"));
                    studio.setStudioColumn(object.getInt("studio_col_count"));
                    studio.setStudioRow(object.getInt("studio_row_count"));
                    int seatNumber = object.getInt("studio_col_count") * object.getInt("studio_row_count");
                    studio.setStudioSeat(seatNumber);
                    studio.setId(object.getInt("studio_id"));
                    studio.setImage(R.drawable.seat);
                    listBeans.add(studio);
                }

                setListViewEvent();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    class ListViewAdapter extends BaseAdapter {

        private Context context;
        private List<StudioListBean> listBeans;

        //构造方法
        public ListViewAdapter(Context context,List<StudioListBean> listBeans){
            this.context = context;
            this.listBeans = listBeans;
        }

        //获取list的item的个数
        @Override
        public int getCount() {
            return listBeans.size();
        }

        //获取position位置的item的数据
        @Override
        public Object getItem(int position) {
            return listBeans.get(position);
        }
        //获取position位置的item
        @Override
        public long getItemId(int position) {
            return position;
        }

        //设置item的视图
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;

            if (convertView == null) {
                //获取item的资源
                convertView = LayoutInflater.from(context).inflate(R.layout.studios_info, null);
                viewHolder = new ViewHolder();
                //id的绑定
                viewHolder.imageView = (ImageView)convertView.findViewById(R.id.image_studio);
                viewHolder.nameTxet = (TextView)convertView.findViewById(R.id.studio_name);
                viewHolder.seatView = (TextView)convertView.findViewById(R.id.studio_seats);
                viewHolder.colText = (TextView)convertView.findViewById(R.id.studio_col);
                viewHolder.rowText = (TextView)convertView.findViewById(R.id.studio_row);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //设置item控件数据的绑定
            viewHolder.imageView.setImageResource(listBeans.get(position).getImage());
            viewHolder.nameTxet.setText(listBeans.get(position).getStudioName());
            viewHolder.seatView.setText(listBeans.get(position).getStudioSeat()+"" );
            viewHolder.colText.setText(listBeans.get(position).getStudioColumn() + "");
            viewHolder.rowText.setText(listBeans.get(position).getStudioRow() + "");
            return convertView;
        }
        public class ViewHolder {
            private ImageView imageView;
            private TextView nameTxet;
            private TextView seatView;
            private TextView colText;
            private TextView rowText;
        }
    }

    public void setListViewEvent() {
        listViewAdapter = new ListViewAdapter(this,listBeans);

        listView.setAdapter(listViewAdapter);
        //ListView的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(StudioActivity.this, StudioInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("image", listBeans.get(position).getImage());
                bundle.putInt("id", listBeans.get(position).getId());
                bundle.putInt("col", listBeans.get(position).getStudioColumn());
                bundle.putString("name", listBeans.get(position).getStudioName());
                bundle.putInt("row", listBeans.get(position).getStudioRow());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}


