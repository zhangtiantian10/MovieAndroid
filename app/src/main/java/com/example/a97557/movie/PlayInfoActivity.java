package com.example.a97557.movie;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
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

/**
 * Created by 97557 on 2017/6/4.
 */

public class PlayInfoActivity extends Activity {

    private TextView textView, playNameText, playEditorText, playTypeText, playLanguageText, playTimeText, playDateText, playPriceText, playIntroduction;
    private Bundle bundle;
    private ImageView backImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_info);

        backImage = (ImageView) findViewById(R.id.back_image);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        playNameText = (TextView) findViewById(R.id.play_name);
        playEditorText = (TextView) findViewById(R.id.play_editor);
        playDateText = (TextView) findViewById(R.id.play_date);
        playIntroduction = (TextView) findViewById(R.id.play_introduction);
        playLanguageText = (TextView) findViewById(R.id.play_language);
        playPriceText = (TextView) findViewById(R.id.play_price);
        playTimeText = (TextView) findViewById(R.id.play_time);
        playTypeText = (TextView) findViewById(R.id.play_type);

        bundle = this.getIntent().getExtras();
        new Thread(getPlayInfo).start();
    }

    Runnable getPlayInfo = new Runnable() {
        @Override
        public void run() {
            int id = bundle.getInt("id");
            String path = "http://115.159.82.119:8080/Movie/play/PlayQueryId?id=" + id;
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
            Bundle data = msg.getData();
            String result = data.getString("result");
            try {
                JSONArray array = new JSONArray(result);
                JSONObject object = (JSONObject) array.get(0);
                playNameText.setText(object.getString("play_name"));
                playTimeText.setText(object.getString("play_length" ) + "分钟");
                playIntroduction.setText(object.getString("play_introduction"));
                playPriceText.setText(object.getString("play_ticket_price") + "元");
                playTypeText.setText(object.getString("play_type_id"));
                playLanguageText.setText(object.getString("play_lang_id"));
                playEditorText.setText(object.getString("play_daoyan"));
                playDateText.setText(object.getString("play_time") + "上映");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };
}
