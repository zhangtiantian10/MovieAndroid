package com.example.a97557.movie;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 97557 on 2017/4/7.
 */

public class ScheduleActivity extends AppCompatActivity {
    private ImageView imageView;
    private ScheduleListViewAdapter scheduleListViewAdapter;
    private ListView listView;
    private List<Schedule> schedules = new ArrayList<>();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_schedule);

        imageView = (ImageView) findViewById(R.id.back_image);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView = (ListView) findViewById(R.id.schedule_list);

        for (int i = 1; i <= 10; i++) {
            Schedule schedule = new Schedule();
            schedule.setDate("2017/6/1");
            schedule.setPlayId(1);
            schedule.setScheduleId(i);
            schedule.setScheduleTimeId(i);
            schedule.setStudioId(i);
            schedules.add(schedule);
        }

        scheduleListViewAdapter = new ScheduleListViewAdapter(this, schedules);

        listView.setAdapter(scheduleListViewAdapter);
    }

    class ScheduleListViewAdapter extends BaseAdapter {

        private Context context;
        private List<Schedule> schedules;

        //构造方法
        public ScheduleListViewAdapter(Context context, List<Schedule> schedules) {
            this.context = context;
            this.schedules = schedules;
        }

        //获取list的item的个数
        @Override
        public int getCount() {
            return schedules.size();
        }

        //获取position位置的item的数据
        @Override
        public Object getItem(int position) {
            return schedules.get(position);
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
                convertView = LayoutInflater.from(context).inflate(R.layout.schedules_info, null);
                viewHolder = new ViewHolder();
                //id的绑定
                viewHolder.scheduleId = (TextView) convertView.findViewById(R.id.schedule_id);
                viewHolder.studioId = (TextView) convertView.findViewById(R.id.studio_id);
                viewHolder.playId = (TextView) convertView.findViewById(R.id.play_id);
                viewHolder.timeId = (TextView) convertView.findViewById(R.id.time_id);
                viewHolder.date = (TextView) convertView.findViewById(R.id.date);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //设置item控件数据的绑定
            viewHolder.scheduleId.setText(schedules.get(position).getScheduleId() + "");
            viewHolder.studioId.setText(schedules.get(position).getStudioId() + "");
            viewHolder.playId.setText(schedules.get(position).getPlayId() + "");
            viewHolder.timeId.setText(schedules.get(position).getScheduleTimeId() + "");
            viewHolder.date.setText(schedules.get(position).getDate());
            return convertView;
        }
    }
    public class ViewHolder {
        private TextView scheduleId, studioId, playId, date, timeId;
    }
}
