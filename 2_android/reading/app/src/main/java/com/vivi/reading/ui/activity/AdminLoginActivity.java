package com.vivi.reading.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.vivi.reading.R;
import com.vivi.reading.bean.User;
import com.vivi.reading.util.ConstUtils;

import java.util.HashMap;
import java.util.Map;

public class AdminLoginActivity extends Activity {

    private Button btnLogin;
    private ImageView ivBack;
    private EditText editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        btnLogin = findViewById(R.id.btn_login);
        editPassword = findViewById(R.id.edit_password);
        ivBack = findViewById(R.id.iv_back);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = editPassword.getText().toString();
                if (password.equals("")){
                    Toast.makeText(AdminLoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                }
                else if (!password.equals("admin")){
                    Toast.makeText(AdminLoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                }
                else {
                    startActivity(new Intent(AdminLoginActivity.this, AdminActivity.class));
                    finish();
                }
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
