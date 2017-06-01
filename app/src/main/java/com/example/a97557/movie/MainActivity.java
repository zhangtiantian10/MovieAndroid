package com.example.a97557.movie;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class MainActivity extends Activity {

    private Button button;
    private SQLiteDatabase db;
    private EditText username;
    private EditText password;
    private SharedPreferences sp;
    private CheckBox rememberCheckBox;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CloseActivityClass.exitClient(MainActivity.this);
        setContentView(R.layout.activity_main);
        db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString() + "/employee.db3", null);
        try {
            db.execSQL("create table employees(id integer primary key autoincrement, name varchar(50), number varchar(10), sex varchar(20), password varchar(50), position varchar(20), tel varchar(20), addr varchar(50))");
            db.execSQL("insert into employees (name,number,sex,password, position,tel,addr)values(?,?,?,?,?,?,?)", new String[]{"zhangtian", "04143110", "male", "123456", "管理员", "18092512006", "陕西 西安"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        sp = getSharedPreferences("SPShared", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sp.edit();
        button = (Button) findViewById(R.id.login);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        rememberCheckBox = (CheckBox) findViewById(R.id.rememberUser);

        if (sp.getBoolean("REMEMBER_USER", false)) {
            Intent in = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(in);
            finish();
        }

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new Thread(networkTask).start();

                //db.execSQL("insert into employee values(?,?,?)", new String[]{"zhangTian", "123456", "female"});

                String name = username.getText().toString();
                String pass = password.getText().toString();
                Cursor cursor = db.rawQuery("select * from employees where name='" + name + "'", null);
                while (true) {
                    if (!cursor.moveToNext()) {
                        break;
                    }

                    String p = cursor.getString(4);
                    int id = cursor.getInt(0);
                    String position = cursor.getString(5);
                    if (p.equals(pass)) {
                        if (position.equals("管理员")) {
                            editor.putString("USERNAME", name);
                            editor.putString("PASSWORD", pass);
                            editor.putInt("ID", id);
                            if (rememberCheckBox.isChecked()) {
                                editor.putBoolean("REMEMBER_USER", true);
                            }
                            editor.commit();
                            Intent in = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(in);
                            finish();
                            return;
                        } else {
                            Toast.makeText(MainActivity.this, "您不是管理员，不能登录！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }

                Toast.makeText(MainActivity.this, "登录失败！用户名或密码错误！", Toast.LENGTH_SHORT).show();

            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            String nameTwo = "张甜";
            Message msg = new Message();
            try {
                String path = "http://192.168.1.41:81/ServerTry/login?name=" + nameTwo;
                HttpGet httpRequest = new HttpGet(path);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse httpResponse = httpclient.execute(httpRequest);
                String strResult = EntityUtils.toString(httpResponse.getEntity());
                Log.i("返回", strResult);
                Bundle data = new Bundle();
                data.putString("value", strResult);
                msg.setData(data);
                handler.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            Log.i("mylog", "请求结果为-->" + val);
            // TODO
            // UI界面的更新等相关操作
        }
    };
}
