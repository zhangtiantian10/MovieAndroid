package com.example.a97557.movie;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by 97557 on 2017/4/7.
 */

public class ScheduleActivity extends AppCompatActivity {
    private ImageView imageView;
    private ScheduleListViewAdapter scheduleListViewAdapter;
    private ListView listView;
    private List<Schedule> schedules = new ArrayList<>();
    private Button todayButton, tomorrowButton, houButton, finalDateButton;
    private String date;
    private boolean first = true;

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

        todayButton = (Button) findViewById(R.id.today);
        tomorrowButton = (Button) findViewById(R.id.tomorrow);
        houButton = (Button) findViewById(R.id.hou);
        finalDateButton = (Button) findViewById(R.id.final_date);
        new Thread(getSchedules).start();
        date = showDate(2);
        houButton.setText(date);
        date = showDate(3);
        finalDateButton.setText(date);

        todayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = showDate(0);
                new Thread(getSchedules).start();
            }
        });

        tomorrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = showDate(1);
                new Thread(getSchedules).start();
            }
        });

        houButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = showDate(2);
                new Thread(getSchedules).start();
            }
        });

        finalDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = showDate(3);
                new Thread(getSchedules).start();
            }
        });
    }

    Runnable getSchedules = new Runnable() {
        @Override
        public void run() {
            if (first) {
                date = showDate(0);
                first = false;
            }
            String path = "http://115.159.82.119:8080/Movie/schedule/ScheduleQueryDate?date=" + date;
            Message msg = new Message();
            HttpGet request = new HttpGet(path);
            HttpClient client = new DefaultHttpClient();
            try {
                HttpResponse response = client.execute(request);

                if (response.getStatusLine().getStatusCode() == 200) {
                    String result = EntityUtils.toString(response.getEntity());
                    Log.i("演出计划:", result);
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
            Bundle data = msg.getData();
            String result = data.getString("result");
            schedules.clear();
            try {
                JSONArray array = new JSONArray(result);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = (JSONObject) array.get(i);
                    Schedule schedule = new Schedule();
                    schedule.setPlayName(object.getString("playName"));
                    schedule.setDate(object.getString("date"));
                    schedule.setStartTime(object.getString("startTime") + "开始");
                    schedule.setStudioName(object.getString("studioName"));
                    schedule.setPrice(object.getString("price") + "元");
                    schedules.add(schedule);
                }

                scheduleListViewAdapter = new ScheduleListViewAdapter(ScheduleActivity.this, schedules);

                listView.setAdapter(scheduleListViewAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

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
                viewHolder.playName = (TextView) convertView.findViewById(R.id.play_name);
                viewHolder.studioName = (TextView) convertView.findViewById(R.id.studio_name);
                viewHolder.playPrice = (TextView) convertView.findViewById(R.id.play_price);
                viewHolder.timeId = (TextView) convertView.findViewById(R.id.time_id);
                viewHolder.date = (TextView) convertView.findViewById(R.id.date);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //设置item控件数据的绑定
            viewHolder.playName.setText(schedules.get(position).getPlayName());
            viewHolder.studioName.setText(schedules.get(position).getStudioName());
            viewHolder.playPrice.setText(schedules.get(position).getPrice());
            viewHolder.timeId.setText(schedules.get(position).getStartTime());
            viewHolder.date.setText(schedules.get(position).getDate());
            return convertView;
        }
    }
    public class ViewHolder {
        private TextView playName, studioName, playPrice, date, timeId;
    }

    public String showDate(int day){
        Date date=new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE,day);
        date=calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        return dateString;
    }
}
