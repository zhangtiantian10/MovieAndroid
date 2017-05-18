package com.example.a97557.movie;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class InsertEmployeeActivity extends Activity {
    private EditText nameText, numberText, sexText, positionText, telText, addressText, passwordText;
    private TextView saveButton, cancelButton;
    private SQLiteDatabase db;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_employee);

        db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString() + "/employee.db3", null);
//        db.execSQL("create table employees(name varchar(50), number varchar(10), sex varchar(20), password varchar(50), position varchar(20), tel varchar(20), addr varchar(50))");

        cancelButton = (TextView) findViewById(R.id.cancel);

        cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nameText = (EditText) findViewById(R.id.name);
        numberText = (EditText) findViewById(R.id.number);
        sexText = (EditText) findViewById(R.id.sex);
        positionText = (EditText) findViewById(R.id.position);
        telText = (EditText) findViewById(R.id.tel_number);
        addressText = (EditText) findViewById(R.id.address);
        saveButton = (TextView) findViewById(R.id.saved);
        passwordText = (EditText) findViewById(R.id.password);

        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String name = nameText.getText().toString();
                String number = numberText.getText().toString();
                String sex = sexText.getText().toString();
                String position = positionText.getText().toString();
                String tel = telText.getText().toString();
                String addr = addressText.getText().toString();
                String password = passwordText.getText().toString();

                if (name.equals("") || number.equals("") || sex.equals("") || position.equals("") || tel.equals("") || addr.equals("") || password.equals("")) {
                    Toast.makeText(InsertEmployeeActivity.this, "所填项不能为空！", Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    db.execSQL("insert into employees (name,number,sex,password, position,tel,addr)values(?,?,?,?,?,?,?)", new String[]{name, number, sex, password, position, tel, addr});
                    Toast.makeText(InsertEmployeeActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(InsertEmployeeActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
