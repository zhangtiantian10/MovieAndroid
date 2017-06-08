package com.example.a97557.movie;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 97557 on 2017/4/8.
 */

public class StudioInfoActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView titleText;
    private Bundle bundle;
    private ImageButton imageButton;
    private PopupWindow popupWindow;
    private SQLiteDatabase db;
    private SharedPreferences sp;
    private int[] seats;
    private GridView seatGrid, numberGrid;
    private int col, row;
    private List<Map<String, Object>> displayArray = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> numberArray = new ArrayList<Map<String, Object>>();
    private SimpleAdapter simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studio_info);
        CloseActivityClass.activityList.add(this);
        db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString() + "/studios.db3", null);

        imageView = (ImageView) findViewById(R.id.back_image);
        titleText = (TextView) findViewById(R.id.studio_show);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        seatGrid = (GridView) findViewById(R.id.gridView);
        numberGrid = (GridView) findViewById(R.id.number);

        bundle = this.getIntent().getExtras();
        col = bundle.getInt("col");
        row = bundle.getInt("row");

        int length = col * row;

        seats = new int[length];
        String name = bundle.getString("name");
        titleText.setText(name);

        seatGrid.setNumColumns(col);

        new Thread(getStudio).start();
    }

    public void displaySeats() {
        displayNumber();
        String [] from ={"image"};
        int [] to = {R.id.show_seat};
        simpleAdapter = new SimpleAdapter(this, displayArray, R.layout.seats_image, from, to);
        seatGrid.setAdapter(simpleAdapter);
    }

    public void displayNumber() {
        String[] textArray = new String[row];
        for (int i = 0; i < row; i++) {
            int j = i + 1;
            textArray[i] = j + "";
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("text", textArray[i]);
            numberArray.add(map);
        }
        String[] from = {"text"};
        int[] to = {R.id.number_text};
        simpleAdapter = new SimpleAdapter(this, numberArray, R.layout.seat_row_number, from, to);
        numberGrid.setAdapter(simpleAdapter);
    }

    Runnable getStudio = new Runnable() {
        @Override
        public void run() {
            Message msg = new Message();
            int id = bundle.getInt("id");
            String path = "http://115.159.82.119:8080/Movie/seat/SeatQueryStudioId?studio_id=" + id;

            try {
                HttpGet request = new HttpGet(path);
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(request);
                if (response.getStatusLine().getStatusCode() == 200) {
                    String strResult = EntityUtils.toString(response.getEntity());
;                   Log.i("座位",strResult);
                    Bundle data = new Bundle();
                    data.putString("value", strResult);
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
            String val = data.getString("value");
            JSONArray array = null;

            try {
                array = new JSONArray(val);
                int k = 0;
                for(int i = 1; i <= row; i++) {
                    for (int j = 1; j <= col; j ++) {
                        JSONObject object = (JSONObject) array.get(k);
                        int colObject = object.getInt("seat_column");
                        int rowObject = object.getInt("seat_row");
                        int status = object.getInt("seat_status");
                        if (rowObject == i && colObject == j) {
                            if (status == 1) {
                                seats[k] = R.drawable.write;
                            } else {
                                seats[k] = R.drawable.red;
                            }
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("image", seats[k]);
                            displayArray.add(map);
                            k ++;
                        }
                    }
                }
                displaySeats();
                Log.i("k---", ">" + k);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}

