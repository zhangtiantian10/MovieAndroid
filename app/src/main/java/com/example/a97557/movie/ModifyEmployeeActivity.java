package com.example.a97557.movie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 97557 on 2017/4/8.
 */

public class ModifyEmployeeActivity extends AppCompatActivity {
    private TextView imageView;
    private Bundle bundle;
    private EditText editName;
    private EditText editNumber;
    private EditText editSex;
    private EditText editPosition;
    private EditText editTelNumber;
    private EditText editAddress, editPassword;
    private ImageView imageHead;
    private TextView textSaved;
    private SQLiteDatabase db;
    private SharedPreferences sp;
//    private ImageView imageChoose;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_employee);
        db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString() + "/employee.db3", null);

        imageView = (TextView) findViewById(R.id.cancel);

//        imageChoose = (ImageView) findViewById(R.id.image);
//
//        imageChoose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_PICK, null);
//                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                startActivityForResult(intent, 0x1);
//            }
//        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bundle = this.getIntent().getExtras();
//        int image = bundle.getInt("image");
//        int id = bundle.getInt("id");


//        Cursor cursor = db.rawQuery("select * from employees where id='" + id + "'", null);
//
//        if (cursor.moveToNext() == false) {
//            return ;
//        }
//        String name = cursor.getString(1).toString();
//        String number = cursor.getString(2).toString();
//        String address = cursor.getString(7).toString();
//        String sex = cursor.getString(3).toString();
//        String position = cursor.getString(5).toString();
//        String telNumber = cursor.getString(6).toString();
//        String password = cursor.getString(4).toString();

        editName = (EditText) findViewById(R.id.name);
        editNumber = (EditText) findViewById(R.id.number);
        editSex = (EditText) findViewById(R.id.sex);
        editPosition = (EditText) findViewById(R.id.position);
        editTelNumber = (EditText) findViewById(R.id.tel_number);
        editAddress = (EditText) findViewById(R.id.address);
        imageHead = (ImageView) findViewById(R.id.image);
        editPassword = (EditText) findViewById(R.id.password);

//        editName.setText(name);
//        imageHead.setImageResource(image);
//        editNumber.setText(number);
//        editPosition.setText(position);
//        editSex.setText(sex);
//        editTelNumber.setText(telNumber);
//        editAddress.setText(address);
//        editPassword.setText(password);
        new Thread(getEmployee).start();

        textSaved = (TextView) findViewById(R.id.saved);

        textSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp = getSharedPreferences("SPShared", MODE_PRIVATE);
//                    db.execSQL("update employees set name='" + name +  "',sex='" + sex + "',password='" + password + "', position='" + position + "',tel='" + tel + "',addr='" + addr + "' where id='" + id + "'");
                new Thread(modifyEmployee).start();

            }
        });
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
                    String result = EntityUtils.toString(response.getEntity());
                    Bundle data = new Bundle();
                    data.putString("value", result);
                    msg.setData(data);
                    handler.sendMessage(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    android.os.Handler handler = new android.os.Handler() {
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

                editName.setText(name);
                imageHead.setImageResource(image);
                editNumber.setText(number);
                editPosition.setText(position);
                editSex.setText(sex);
                editTelNumber.setText(telNumber);
                editAddress.setText(address);
                editPassword.setText(password);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("员工详细信息", "请求结果为-->" + val);
        }
    };

    Runnable modifyEmployee = new Runnable() {
        @Override
        public void run() {
            String name = editName.getText().toString();
            String number = editNumber.getText().toString();
            String sex = editSex.getText().toString();
            String password = editPassword.getText().toString();
            String position = editPosition.getText().toString();
            String tel = editTelNumber.getText().toString();
            String addr = editAddress.getText().toString();
            int id = bundle.getInt("id");
//            BasicNameValuePair namePair = new BasicNameValuePair("name", name);
//            BasicNameValuePair passwordPair = new BasicNameValuePair("password", password);
//            BasicNameValuePair positionPair = new BasicNameValuePair("position", position);
//            BasicNameValuePair addrPair = new BasicNameValuePair("addr", addr);
//            BasicNameValuePair telPair = new BasicNameValuePair("tel", tel);
//            BasicNameValuePair sexPair = new BasicNameValuePair("sex", sex);
//            params.add(namePair);
//            params.add(passwordPair);
//            params.add(positionPair);
//            params.add(addrPair);
//            params.add(sexPair);
//            params.add(telPair);
            List<NameValuePair> params = new ArrayList<>();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("name", name);
                jsonObject.put("addr", addr);
                jsonObject.put("position", position);
                jsonObject.put("password", password);
                jsonObject.put("tel", tel);
                jsonObject.put("sex", sex);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            params.add(new BasicNameValuePair("param", jsonObject.toString()));

            Message msg = new Message();
            String path = "http://192.168.1.41:81/ServerTry/modifyEmployee";
            try {
                HttpGet request = new HttpGet(path);
//                request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(request);
                if (response.getStatusLine().getStatusCode() == 200) {
                    String result = EntityUtils.toString(response.getEntity());
                    Bundle data = new Bundle();
                    data.putInt("value",Integer.parseInt(result));
                    msg.setData(data);
                    handlerModifyEmployee.sendMessage(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

    android.os.Handler handlerModifyEmployee = new android.os.Handler() {
        public void handlerMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            int val = data.getInt("value");
            final SharedPreferences.Editor editor = sp.edit();
            int id = bundle.getInt("id");
            Log.i("修改员工信息", "请求结果为-->" + val);

            if(val == 1) {
                if (id == sp.getInt("ID", 0)) {
                    editor.putBoolean("REMEMBER_USER", false);
                    editor.commit();
                    Toast.makeText(ModifyEmployeeActivity.this, "保存成功,请重新登录！", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(ModifyEmployeeActivity.this, MainActivity.class);
                    startActivity(in);
                    CloseActivityClass.exitClient(ModifyEmployeeActivity.this);
                    finish();
                }
                Toast.makeText(ModifyEmployeeActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(ModifyEmployeeActivity.this, "保存失败",Toast.LENGTH_SHORT).show();
            }
        }
    };
    //
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == 0x1 && resultCode == RESULT_OK) {
//            if (data != null) {
//                imageChoose.setImageURI(data.getData());
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
}
