package com.example.a97557.movie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        int image = bundle.getInt("image");
        int id = bundle.getInt("id");

        Cursor cursor = db.rawQuery("select * from employees where id='" + id + "'", null);

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

        editName = (EditText) findViewById(R.id.name);
        editNumber = (EditText) findViewById(R.id.number);
        editSex = (EditText) findViewById(R.id.sex);
        editPosition = (EditText) findViewById(R.id.position);
        editTelNumber = (EditText) findViewById(R.id.tel_number);
        editAddress = (EditText) findViewById(R.id.address);
        imageHead = (ImageView) findViewById(R.id.image);
        editPassword = (EditText) findViewById(R.id.password);

        editName.setText(name);
        imageHead.setImageResource(image);
        editNumber.setText(number);
        editPosition.setText(position);
        editSex.setText(sex);
        editTelNumber.setText(telNumber);
        editAddress.setText(address);
        editPassword.setText(password);

        textSaved = (TextView) findViewById(R.id.saved);

        textSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString();
                String number = editNumber.getText().toString();
                String sex = editSex.getText().toString();
                String password = editPassword.getText().toString();
                String position = editPosition.getText().toString();
                String tel = editTelNumber.getText().toString();
                String addr = editAddress.getText().toString();
                sp = getSharedPreferences("SPShared", MODE_PRIVATE);
                int id = bundle.getInt("id");
                final SharedPreferences.Editor editor = sp.edit();

                try {
                    db.execSQL("update employees set name='" + name + "',number='" + number + "',sex='" + sex + "',password='" + password + "', position='" + position + "',tel='" + tel + "',addr='" + addr + "' where id='" + id + "'");
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
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ModifyEmployeeActivity.this, "保存失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
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
