package com.example.a97557.movie;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
 * Created by 97557 on 2017/4/7.
 */

public class PersonActivity extends AppCompatActivity {
    private ImageView backImage;
    private SharedPreferences sp;
    private ImageView imageView;
    private TextView textName;
    private TextView textNumber;
    private TextView textPassword;
    private TextView textSex;
    private TextView textPosition;
    private TextView textTelNumber;
    private TextView textAddress;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person);

        backImage = (ImageView) findViewById(R.id.back_image);

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imageView = (ImageView) findViewById(R.id.employee_image);
        textName = (TextView) findViewById(R.id.employee_name);
        textNumber = (TextView) findViewById(R.id.number);
        textSex = (TextView) findViewById(R.id.sex);
        textPosition = (TextView) findViewById(R.id.position);
        textTelNumber = (TextView) findViewById(R.id.tel_number);
        textAddress = (TextView) findViewById(R.id.address);
        textPassword = (TextView) findViewById(R.id.password);

        new Thread(getEmployee).start();
    }

    Runnable getEmployee = new Runnable() {
        @Override
        public void run() {
            sp = getSharedPreferences("SPShared", MODE_PRIVATE);
            int id = sp.getInt("ID", 0);
            String path = "http://115.159.82.119:8080/Movie/employee/EmployeeQueryId?id=" + id;
            Message msg = new Message();
            try {
                HttpGet request = new HttpGet(path);
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(request);
                if (response.getStatusLine().getStatusCode() == 200) {
                    String result = EntityUtils.toString(response.getEntity());
                    Bundle bundle = new Bundle();
                    bundle.putString("value", result);
                    msg.setData(bundle);
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
            String result = bundle.getString("value");
            Log.i("员工详细信息", "请求结果为-->" + result);

            try {
                JSONArray array = new JSONArray(result);
                JSONObject object = (JSONObject) array.get(0);
                String name = object.getString("emp_name");
                String number = object.getString("emp_id");
                String address = object.getString("emp_addr");
                String sex = object.getString("emp_sex");
                String position = object.getString("emp_position");
                String telNumber = object.getString("emp_tel_num");
                String password = object.getString("emp_password");

                imageView.setImageResource(R.drawable.head);
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
        }
    };
}
