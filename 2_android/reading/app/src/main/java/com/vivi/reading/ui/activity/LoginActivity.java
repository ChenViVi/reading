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

public class LoginActivity extends Activity {

    public static LoginActivity instance = null;

    private RequestQueue queue;
    private SharedPreferences.Editor editor;

    private Button btnLogin;
    private ImageView ivBack;
    private EditText editPassword;
    private EditText editPhone;
    private TextView tvSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        instance = this;

        queue = Volley.newRequestQueue(this);
        btnLogin = findViewById(R.id.btn_login);
        tvSign = findViewById(R.id.tv_sign);
        editPhone = findViewById(R.id.edit_phone);
        editPassword = findViewById(R.id.edit_password);
        ivBack = findViewById(R.id.iv_back);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = editPhone.getText().toString();
                String password = editPassword.getText().toString();
                if (phone.equals("")){
                    Toast.makeText(LoginActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                }
                else if (password.equals("")){
                    Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                }
                else {
                    queue.add(getLoginRequest(phone,password));
                }
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.tv_admin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this ,AdminLoginActivity.class));
                finish();
            }
        });
    }

    private StringRequest getLoginRequest(final String phone, final String password) {
        return new StringRequest(Request.Method.POST,ConstUtils.BASEURL +"login.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response",response);
                        Gson gson = new Gson();
                        User user = gson.fromJson(response,User.class);
                        if (user.getResult() == 2){
                            Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                        }
                        else if (user.getResult() == 3){
                            Toast.makeText(LoginActivity.this, "你的账号被封，请联系管理员", Toast.LENGTH_SHORT).show();
                        }
                        else if (user.getResult() == 1){
                            Toast.makeText(LoginActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        }
                        else if(user.getResult() == 0){
                            editor = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit();
                            editor.putInt("id",user.getId());
                            editor.putString("name",user.getName());
                            editor.putString("sex",user.getSex());
                            editor.putString("sign",user.getSign());
                            editor.putBoolean("isLogin",true);
                            editor.apply();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "网络连接超时", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("phone",phone);
                map.put("password",password);
                return map;
            }
        };
    }
}
