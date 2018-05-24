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
import com.vivi.reading.bean.DiscussComment;
import com.vivi.reading.bean.DiscussType;
import com.vivi.reading.ui.fragment.DiscussFragment;
import com.vivi.reading.util.ConstUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddDiscussActivity extends AppCompatActivity implements View.OnClickListener{

    private RequestQueue queue;

    private EditText etTitle;
    private EditText etContent;
    private Spinner spinner;
    private ImageView ivBack;
    private TextView tvSend;

    private List<DiscussType> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_discuss);
        queue = Volley.newRequestQueue(this);
        etTitle = findViewById(R.id.et_title);
        etContent = findViewById(R.id.et_content);
        ivBack = findViewById(R.id.iv_back);
        tvSend = findViewById(R.id.tv_send);
        spinner = findViewById(R.id.spinner);
        queue.add(getType());
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
        String title = etTitle.getText().toString();
        String content = etContent.getText().toString();
        if (title.length() == 0 || content.length() == 0){
            Toast.makeText(AddDiscussActivity.this, "请把信息填写完整", Toast.LENGTH_SHORT).show();
        }
        else {
            queue.add(setComment(title, content));
        }
    }

    private StringRequest getType() {
        StringRequest request = new StringRequest(Request.Method.POST, ConstUtils.BASEURL + "getdiscusstype.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int result = 0;
                        String list = "";
                        try {
                            JSONObject json= new JSONObject(response);
                            result = json.getInt("result");
                            list = json.getJSONArray("list").toString();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result != 0){
                            Toast.makeText(AddDiscussActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Gson gson = new Gson();
                            items = gson.fromJson(list, new TypeToken<List<DiscussType>>() {}.getType());
                            ArrayList<String> arrayList = new ArrayList<>();
                            for (DiscussType type : items){
                                arrayList.add(type.getName());
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(AddDiscussActivity.this,android.R.layout.simple_spinner_item, arrayList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner .setAdapter(adapter);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddDiscussActivity.this, "网络连接超时", Toast.LENGTH_SHORT).show();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }

    private StringRequest setComment(final String title , final String content) {
        final DiscussType type = items.get(spinner.getSelectedItemPosition());
        StringRequest request = new StringRequest(Request.Method.POST, ConstUtils.BASEURL + "adddiscuss.php",
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
                            Toast.makeText(AddDiscussActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(AddDiscussActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddDiscussActivity.this, "网络连接超时", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("userId", String.valueOf(PreferenceManager.getDefaultSharedPreferences(AddDiscussActivity.this).getInt("id",-1)));
                map.put("typeId",String.valueOf(type.getId()));
                map.put("title",title);
                map.put("content",content);
                return map;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
}
