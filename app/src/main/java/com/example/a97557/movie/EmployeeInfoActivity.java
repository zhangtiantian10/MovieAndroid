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

public class EmployeeInfoActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView textName;
    private TextView textNumber;
    private TextView textPassword;
    private TextView textSex;
    private TextView textPosition;
    private TextView textTelNumber;
    private TextView textAddress;
    private Bundle bundle;
    private ImageButton imageButton;
    private PopupWindow popupWindow;
    private SQLiteDatabase db;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_info);
        CloseActivityClass.activityList.add(this);
        db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString() + "/employee.db3", null);

        imageView = (ImageView) findViewById(R.id.back_image);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageButton = (ImageButton) findViewById(R.id.three_points);

        imageButton.setOnClickListener(popClick);

        imageView = (ImageView) findViewById(R.id.employee_image);
        textName = (TextView) findViewById(R.id.employee_name);
        textNumber = (TextView) findViewById(R.id.number);
        textSex = (TextView) findViewById(R.id.sex);
        textPosition = (TextView) findViewById(R.id.position);
        textTelNumber = (TextView) findViewById(R.id.tel_number);
        textAddress = (TextView) findViewById(R.id.address);
        textPassword = (TextView) findViewById(R.id.password);
        bundle = this.getIntent().getExtras();
        new Thread(getEmployee).start();
//        displayEmployeeInfo();
    }

    public void displayEmployeeInfo() {
        bundle = this.getIntent().getExtras();
        int image = bundle.getInt("image");
        int id = bundle.getInt("id");
        new Thread(getEmployee).start();
        Cursor cursor = db.rawQuery("select * from employees where id=" + id + "", null);

        if (cursor.moveToNext() == false) {
            return ;
        }
        String name = cursor.getString(1).toString();
        String number = cursor.getString(2).toString();
        String address = cursor.getString(7).toString();
        String sex = cursor.getString(3).toString();
        String position = cursor.getString(5).toString();
        String telNumber = cursor.getString(6).toString();
        String password = cursor.getString(4).toString();

        imageView.setImageResource(image);
        textName.setText(name);
        textNumber.setText(number);
        textAddress.setText(address);
        textTelNumber.setText(telNumber);
        textPosition.setText(position);
        textSex.setText(sex);
        textPassword.setText(password);
    }

    Runnable getEmployee = new Runnable() {
        @Override
        public void run() {
            Message msg = new Message();
            int id = bundle.getInt("id");
            String path = "http://192.168.1.41:81/ServerTry/employee?id=" + id;

            try {
                HttpGet request = new HttpGet(path);
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(request);
                if (response.getStatusLine().getStatusCode() == 200) {
                    String strResult = EntityUtils.toString(response.getEntity());
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
            int image = bundle.getInt("image");

            try {
                array = new JSONArray(val);
                JSONObject object = (JSONObject) array.get(0);
                String name = object.getString("name");
                String number = object.getString("number");
                String address = object.getString("addr");
                String sex = object.getString("sex");
                String position = object.getString("position");
                String telNumber = object.getString("telNumber");
                String password = object.getString("password");

                imageView.setImageResource(image);
                textName.setText(name);
                textNumber.setText(number);
                textAddress.setText(address);
                textTelNumber.setText(telNumber);
                textPosition.setText(position);
                textSex.setText(sex);
                textPassword.setText(password);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("员工详细信息", "请求结果为-->" + val);
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
                Intent in = new Intent(EmployeeInfoActivity.this, ModifyEmployeeActivity.class);
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
                        db.execSQL("delete from employees where id=" + id + "");
                        Toast.makeText(EmployeeInfoActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(EmployeeInfoActivity.this, "删除失败",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EmployeeInfoActivity.this, "不能删除自己信息！",Toast.LENGTH_SHORT).show();
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
        displayEmployeeInfo();
    }
}
