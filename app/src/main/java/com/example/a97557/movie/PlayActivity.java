package com.example.a97557.movie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

        for (int i = 1; i <= 10; i++ ) {
            Play play = new Play();
            play.setImageId(R.drawable.head);
            play.setName("星球大战");
            play.setLanguage("国语");
            play.setType("喜剧");
            play.setId(i);
            plays.add(play);
        }

        playsListViewAdapter = new PlayListViewAdapter(this, plays);
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
    }

    public class PlayViewHolder {
        private ImageView playImage;
        private TextView playName, playType, playLanguage;
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
                convertView.setTag(playViewHolder);
            } else {
                playViewHolder = (PlayViewHolder) convertView.getTag();
            }
            //设置item控件数据的绑定
            playViewHolder.playImage.setImageResource(plays.get(position).getImageId());
            playViewHolder.playName.setText(plays.get(position).getName());
            playViewHolder.playType.setText(plays.get(position).getType());
            playViewHolder.playLanguage.setText(plays.get(position).getLanguage());
            return convertView;
        }
    }
}


