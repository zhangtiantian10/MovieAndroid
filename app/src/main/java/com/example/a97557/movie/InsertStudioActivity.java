package com.example.a97557.movie;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class InsertStudioActivity extends Activity {
    private EditText nameText,numberText,seatsText,rowText,columnText,movieText;
    private TextView saveButton, cancelButton;
    private SQLiteDatabase db;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_studio);

        db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString() + "/studios.db3", null);
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
        seatsText = (EditText) findViewById(R.id.seats);
        rowText = (EditText) findViewById(R.id.row);
        columnText = (EditText) findViewById(R.id.column);
        movieText = (EditText) findViewById(R.id.movie);
        saveButton = (TextView) findViewById(R.id.saved);
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String name = nameText.getText().toString();
                String number = numberText.getText().toString();
                String seats = seatsText.getText().toString();
                String row = rowText.getText().toString();
                String column = columnText.getText().toString();
                String movie = movieText.getText().toString();

                if (name.equals("") || number.equals("") || seats.equals("") || row.equals("") || column.equals("") || movie.equals("") ) {
                    Toast.makeText(InsertStudioActivity.this, "所填项不能为空！", Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    db.execSQL("insert into studios (name,number,seats,row,column,movie)values(?,?,?,?,?,?)", new String[]{name,number,seats,row,column,movie});
                    Toast.makeText(InsertStudioActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(InsertStudioActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

