package com.example.a97557.movie;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    private Button button;
    private SQLiteDatabase db;
    private EditText username;
    private EditText password;
    private SharedPreferences sp;
    private CheckBox rememberCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CloseActivityClass.exitClient(MainActivity.this);
        setContentView(R.layout.activity_main);
        db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString() + "/studios.db3", null);
        try{
            db.execSQL("create table studios(id integer primary key autoincrement,name varchar(50),number varchar(10),seats varchar(50),row varchar(10),column varchar(10),movie varchar(50))");
            db.execSQL("insert into studios(name,number,seats,row,column,movie)values(?,?,?,?,?,?)",new String[]{"1号","1","100","10","10","SUPERMAN"});

        } catch (Exception e) {
            e.printStackTrace();
        }
        db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString() + "/employee.db3", null);
        try{
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

                Toast.makeText(MainActivity.this, "登录失败！用户名或密码错误！",Toast.LENGTH_SHORT).show();

            }
        });
    }
}
