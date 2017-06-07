package com.example.a97557.movie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
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

/**
 * Created by 97557 on 2017/4/8.
 */

public class StudioInfoActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView textName;
    private TextView textNumber;
    private TextView textSeats;
    private TextView textRow;
    private TextView textColumn;
    private TextView textMovie;
    private Bundle bundle;
    private ImageButton imageButton;
    private PopupWindow popupWindow;
    private SQLiteDatabase db;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studio_info);
        CloseActivityClass.activityList.add(this);
        db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString() + "/studios.db3", null);

        imageView = (ImageView) findViewById(R.id.back_image);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageButton = (ImageButton) findViewById(R.id.three_points);

        imageButton.setOnClickListener(popClick);

        imageView = (ImageView) findViewById(R.id.studio_image);
        textName = (TextView) findViewById(R.id.studio_name);
        textNumber = (TextView) findViewById(R.id.studio_number);
        textSeats = (TextView) findViewById(R.id.studio_seats);
        textRow = (TextView) findViewById(R.id.studio_row);
        textColumn = (TextView) findViewById(R.id.studio_column);
        textMovie = (TextView) findViewById(R.id.studio_movie);
        bundle = this.getIntent().getExtras();
        new Thread(getStudio).start();
        displayStudioInfo();
    }

    public void displayStudioInfo() {
        bundle = this.getIntent().getExtras();
        int image = bundle.getInt("image");
        int id = bundle.getInt("id");
        new Thread(getStudio).start();
        Cursor cursor = db.rawQuery("select * from studios where id=" + id + "", null);

        if (cursor.moveToNext() == false) {
            return ;
        }
        String name = cursor.getString(1).toString();
        String number = cursor.getString(2).toString();
        String seats = cursor.getString(3).toString();
        String row = cursor.getString(4).toString();
        String column = cursor.getString(5).toString();
        String movie = cursor.getString(6).toString();

        imageView.setImageResource(image);
        textName.setText(name);
        textNumber.setText(number);
        textSeats.setText(seats);
        textRow.setText(row);
        textColumn.setText(column);
        textMovie.setText(movie);
    }

    Runnable getStudio = new Runnable() {
        @Override
        public void run() {
            Message msg = new Message();
            int id = bundle.getInt("id");
            String path = "http://115.159.82.119:8080/Movie/studio/StudioQueryAll";

            try {
                HttpGet request = new HttpGet(path);
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(request);
                if (response.getStatusLine().getStatusCode() == 200) {
                    String strResult = EntityUtils.toString(response.getEntity());
;                   Log.e("e",strResult);
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
            int image = bundle.getInt("image");

            try {
                array = new JSONArray(val);
                JSONObject object = (JSONObject) array.get(0);
                String name = object.getString("name");
                String number = object.getString("number");
                String seats = object.getString("seats");
                String row = object.getString("row");
                String column = object.getString("column");
                String movie = object.getString("movie");

                imageView.setImageResource(image);
                textName.setText(name);
                textNumber.setText(number);
                textSeats.setText(seats);
                textRow.setText(row);
                textColumn.setText(column);
                textMovie.setText(movie);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("影厅详细信息", "请求结果为-->" + val);
            // TODO
            // UI界面的更新等相关操作
        }
    };


    View.OnClickListener popClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getPopupWindow();
            popupWindow.showAsDropDown(v);
        }
    };
    /**
     * 创建PopupWindow
     */
    protected void initPopuptWindow() {
        View popupWindow_view = getLayoutInflater().inflate(R.layout.pop, null,
                false);
        popupWindow = new PopupWindow(popupWindow_view, 300, 240, true);
        popupWindow_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
                return false;
            }
        });
        Button editButton = (Button) popupWindow_view.findViewById(R.id.edit);
        Button deleteButton = (Button) popupWindow_view.findViewById(R.id.delete);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("编辑操作");
                Intent in = new Intent(StudioInfoActivity.this, ModifyEmployeeActivity.class);
                in.putExtras(bundle);
                startActivity(in);
                popupWindow.dismiss();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("删除操作");
                sp = getSharedPreferences("SPShared", MODE_PRIVATE);
                final SharedPreferences.Editor editor = sp.edit();
                int id = bundle.getInt("id");
                int i = sp.getInt("ID", 0);
                System.out.println("SP>ID" + i + "---" + id);
                if (id != sp.getInt("ID", 0)) {
                    try {
                        db.execSQL("delete from studios where id=" + id + "");
                        Toast.makeText(StudioInfoActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(StudioInfoActivity.this, "删除失败",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(StudioInfoActivity.this, "不能删除自己信息！",Toast.LENGTH_SHORT).show();
                }
                popupWindow.dismiss();
            }
        });
    }

    private void getPopupWindow() {
        if (null != popupWindow) {
            popupWindow.dismiss();
            return;
        } else {
            initPopuptWindow();
        }
    }

    protected void onRestart() {
        super.onRestart();
        displayStudioInfo();
    }
}

