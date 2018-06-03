package com.vivi.reading.ui.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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

public class SignActivity extends Activity {

    private RequestQueue queue;
    private SharedPreferences.Editor editor;

    private Button btnSign;
    private EditText editPassword;
    private EditText editPhone;
    private EditText editPasswordRepeat;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        queue = Volley.newRequestQueue(this);

        btnSign = findViewById(R.id.btn_sign);
        editPhone = findViewById(R.id.edit_phone);
        editPassword = findViewById(R.id.edit_password);
        editPasswordRepeat = findViewById(R.id.edit_password_repeat);
        ivBack = findViewById(R.id.iv_back);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = editPhone.getText().toString();
                String password = editPassword.getText().toString();
                String passwordRepeat = editPasswordRepeat.getText().toString();
                if (!password.equals(passwordRepeat)){
                    Toast.makeText(SignActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                }
                else if (editPassword.length()<6){
                    Toast.makeText(SignActivity.this, "密码不得小于6位", Toast.LENGTH_SHORT).show();
                }
                else if (editPassword.length()>16){
                    Toast.makeText(SignActivity.this, "密码不得大于16位", Toast.LENGTH_SHORT).show();
                }
                else {
                    queue.add(getSignRequest(phone,password));
                }
            }
        });
    }

    private StringRequest getSignRequest(final String phone, final String password) {
        StringRequest request = new StringRequest(Request.Method.POST,ConstUtils.BASEURL +"register.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response",response);
                        Gson gson = new Gson();
                        User user = gson.fromJson(response, User.class);
                        if (user.getResult() == 2){
                            Toast.makeText(SignActivity.this, "无效的用户名", Toast.LENGTH_SHORT).show();
                        }
                        else if (user.getResult() == 1){
                            Toast.makeText(SignActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        }
                        else if (user.getResult() == 0) {
                            editor = PreferenceManager.getDefaultSharedPreferences(SignActivity.this).edit();
                            editor.putInt("id", user.getId());
                            editor.putString("name", user.getName());
                            editor.putString("sex", user.getSex());
                            editor.putString("sign", user.getSign());
                            editor.putBoolean("isLogin", true);
                            editor.apply();
                            LoginActivity.instance.finish();
                            finish();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignActivity.this, "网络连接超时", Toast.LENGTH_SHORT).show();
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
        request.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
}
