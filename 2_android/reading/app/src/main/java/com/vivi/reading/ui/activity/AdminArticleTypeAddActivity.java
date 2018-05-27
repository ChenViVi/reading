package com.vivi.reading.ui.activity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.google.gson.reflect.TypeToken;
import com.vivi.reading.R;
import com.vivi.reading.bean.DiscussType;
import com.vivi.reading.util.ConstUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminArticleTypeAddActivity extends AppCompatActivity implements View.OnClickListener{

    private RequestQueue queue;

    private EditText etName;
    private ImageView ivBack;
    private TextView tvSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_article_type_add);
        queue = Volley.newRequestQueue(this);
        etName = findViewById(R.id.et_name);

        ivBack = findViewById(R.id.iv_back);
        tvSend = findViewById(R.id.tv_add);

        tvSend.setOnClickListener(this);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        String name = etName.getText().toString();
        if (name.length() == 0){
            Toast.makeText(AdminArticleTypeAddActivity.this, "请把信息填写完整", Toast.LENGTH_SHORT).show();
        }
        else {
            queue.add(addType(name));
        }
    }

    private StringRequest addType(final String name) {
        StringRequest request = new StringRequest(Request.Method.POST, ConstUtils.BASEURL + "addarticletype.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int result = 0;
                        try {
                            JSONObject json= new JSONObject(response);
                            result = json.getInt("result");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result != 0){
                            Toast.makeText(AdminArticleTypeAddActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(AdminArticleTypeAddActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminArticleTypeAddActivity.this, "网络连接超时", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("name",name);
                return map;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
}
