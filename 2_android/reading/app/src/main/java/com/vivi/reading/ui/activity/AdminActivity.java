package com.vivi.reading.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vivi.reading.R;

public class AdminActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        findViewById(R.id.tv_article_type).setOnClickListener(this);
        findViewById(R.id.tv_discuss).setOnClickListener(this);
        findViewById(R.id.tv_discuss_type).setOnClickListener(this);
        findViewById(R.id.tv_user).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_article_type:
                startActivity(new Intent(AdminActivity.this, AdminArticleTypeActivity.class));
                break;
        }
    }
}
