package com.example.a97557.movie;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by 97557 on 2017/6/4.
 */

public class PlayInfoActivity extends Activity {

    private TextView textView;
    private Bundle bundle;
    private ImageView backImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_info);

        backImage = (ImageView) findViewById(R.id.back_image);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bundle = this.getIntent().getExtras();
        textView = (TextView) findViewById(R.id.play_id);
        textView.setText(bundle.getInt("id") + "");
    }
}
