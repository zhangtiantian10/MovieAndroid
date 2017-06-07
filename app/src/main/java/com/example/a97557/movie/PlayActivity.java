package com.example.a97557.movie;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

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

public class PlayActivity extends AppCompatActivity {
    private ImageView imageView;
    private List<Play> plays = new ArrayList<>();
    private PlayListViewAdapter playsListViewAdapter;
    private ListView listView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_play);

        imageView = (ImageView) findViewById(R.id.back_image);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView = (ListView) findViewById(R.id.play_list);

        new Thread(getAllPlays).start();
    }

    public class PlayViewHolder {
        private ImageView playImage;
        private TextView playName, playType, playLanguage, playStatus;
    }

    class PlayListViewAdapter extends BaseAdapter {

        private Context context;
        private List<Play> plays;

        //构造方法
        public PlayListViewAdapter(Context context, List<Play> plays) {
            this.context = context;
            this.plays = plays;
        }

        //获取list的item的个数
        @Override
        public int getCount() {
            return plays.size();
        }

        //获取position位置的item的数据
        @Override
        public Object getItem(int position) {
            return plays.get(position);
        }

        //获取position位置的item
        @Override
        public long getItemId(int position) {
            return position;
        }

        //设置item的视图
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final PlayViewHolder playViewHolder;

            if (convertView == null) {
                //获取item的资源
                convertView = LayoutInflater.from(context).inflate(R.layout.plays_info, null);
                playViewHolder = new PlayViewHolder();
                //id的绑定
                playViewHolder.playImage = (ImageView) convertView.findViewById(R.id.play_image);
                playViewHolder.playName = (TextView) convertView.findViewById(R.id.play_name);
                playViewHolder.playLanguage = (TextView) convertView.findViewById(R.id.play_language);
                playViewHolder.playType = (TextView) convertView.findViewById(R.id.play_type);
                playViewHolder.playStatus = (TextView) convertView.findViewById(R.id.play_status);
                convertView.setTag(playViewHolder);
            } else {
                playViewHolder = (PlayViewHolder) convertView.getTag();
            }
            //设置item控件数据的绑定
            playViewHolder.playImage.setImageResource(plays.get(position).getImageId());
            playViewHolder.playName.setText(plays.get(position).getName());
            playViewHolder.playType.setText(plays.get(position).getType());
            playViewHolder.playLanguage.setText(plays.get(position).getLanguage());
            if (plays.get(position).getPlayStatus().equals("已安排")) {
                playViewHolder.playStatus.setTextColor(Color.rgb(0, 255, 0));
            }
            playViewHolder.playStatus.setText(plays.get(position).getPlayStatus());
            return convertView;
        }
    }

    Runnable getAllPlays = new Runnable() {
        @Override
        public void run() {
            String path = "http://115.159.82.119:8080/Movie/play/PlayQueryAll";
            Message msg = new Message();

            try {
                HttpGet request = new HttpGet(path);
                HttpClient client = new DefaultHttpClient();
                    HttpResponse response = client.execute(request);
                if (response.getStatusLine().getStatusCode() == 200) {
                    String result = EntityUtils.toString(response.getEntity());
                    Bundle data = new Bundle();
                    data.putString("result", result);
                    msg.setData(data);
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
            Bundle bundle = msg.getData();
            String result = bundle.getString("result");
            Log.i("剧目信息", "请求结果为-->" + result);
            JSONArray array = null;

            try {
                array = new JSONArray(result);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = (JSONObject) array.get(i);
                    Play play = new Play();
                    play.setId(object.getInt("play_id"));
                    play.setImageId(R.drawable.head);
                    play.setName(object.getString("play_name"));
                    play.setType(object.getString("play_type_id"));
                    play.setLanguage(object.getString("play_lang_id"));
                    play.setPlayStatus(object.getString("play_status"));
                    plays.add(play);
                }
                playsListViewAdapter = new PlayListViewAdapter(PlayActivity.this, plays);
                listView.setAdapter(playsListViewAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent in = new Intent(PlayActivity.this, PlayInfoActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("image", plays.get(position).getImageId());
                        bundle.putInt("id", plays.get(position).getId());
                        in.putExtras(bundle);
                        startActivity(in);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(PlayActivity.this, "获取剧目信息失败", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    };
}


