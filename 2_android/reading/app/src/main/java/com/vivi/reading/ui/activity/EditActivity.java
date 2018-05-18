package com.vivi.reading.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.vivi.reading.R;

public class EditActivity extends AppCompatActivity {

    private SharedPreferences.Editor editor;

    private String type;

    private EditText editText;
    private ImageView ivBack;
    private TextView tvTitle;
    private TextView tvSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editText = (EditText) findViewById(R.id.edit);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvSave = (TextView) findViewById(R.id.tv_save);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        if (type.equals("name")){
            tvTitle.setText("编辑用户名");
            editText.setHint("请输入新用户名");
        }
        else {
            tvTitle.setText("编辑个性签名");
            editText.setHint("请输入新个性签名");
        }

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("name")){
                    UserActivity.instance.name = editText.getText().toString();
                }
                else if (type.equals("sign")){
                    UserActivity.instance.sign = editText.getText().toString();
                }
                finish();
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
