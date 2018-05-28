package com.vivi.reading.ui.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.vivi.reading.adapter.AdminDiscussCommentAdapter;
import com.vivi.reading.adapter.DiscussCommentAdapter;
import com.vivi.reading.bean.Discuss;
import com.vivi.reading.bean.DiscussComment;
import com.vivi.reading.util.ConstUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminDiscussDetailActivity extends AppCompatActivity {

    private RequestQueue queue;
    private SharedPreferences preferences;

    private int userId;
    private AdminDiscussCommentAdapter adapter;
    private ArrayList<DiscussComment> data = new ArrayList<>();

    private ListView listView;
    private TextView tvDiscussTitle;
    private TextView tvDate;
    private TextView tvAuthor;
    private TextView tvContent;
    private TextView tvTitle;
    private ImageView ivBack;
    private TextView tvDelete;
    private Discuss discuss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_discuss_detail);

        queue = Volley.newRequestQueue(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = preferences.getInt("id",-1);

        Intent intent = getIntent();
        discuss = (Discuss) intent.getSerializableExtra("discuss");

        View layout = getLayoutInflater().inflate(R.layout.header_admin_discuss_detail,null);
        tvDiscussTitle = layout.findViewById(R.id.tv_article_title);
        tvDate = layout.findViewById(R.id.tv_article_date);
        tvAuthor = layout.findViewById(R.id.tv_action_author);
        tvContent = layout.findViewById(R.id.tv_article_content);
        tvTitle = layout.findViewById(R.id.tv_title);
        ivBack = layout.findViewById(R.id.iv_back);
        tvDelete = layout.findViewById(R.id.tv_delete);
        listView = findViewById(R.id.list_view);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                queue.add(delRequest());
            }
        });

        queue.add(getCommentRequest(discuss.getId()));
        adapter = new AdminDiscussCommentAdapter(this,data, queue);


        listView.addHeaderView(layout);
        listView.setAdapter(adapter);
        tvDiscussTitle.setText(discuss.getTitle());
        tvTitle.setText(discuss.getTitle());
        tvDate.setText(discuss.getDate());
        tvAuthor.setText(discuss.getUser());
        tvContent.setText(discuss.getContent());
    }

    private StringRequest getCommentRequest(final int id) {
        StringRequest request = new StringRequest(Request.Method.POST, ConstUtils.BASEURL + "getdiscusscomt.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("comment","getCommentRequest="+response);
                        int result = 0;
                        String list = "";
                        try {
                            JSONObject json= new JSONObject(response);
                            list=json.getJSONArray("list").toString();
                            result = json.getInt("result");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        finally {
                            if (result != 0){
                                Toast.makeText(AdminDiscussDetailActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Gson gson = new Gson();
                                List<DiscussComment> items = gson.fromJson(list, new TypeToken<List<DiscussComment>>() {}.getType());
                                if (items != null){
                                    data.clear();
                                    data.addAll(items);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminDiscussDetailActivity.this, "网络连接超时", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("discussId",String.valueOf(id));
                return map;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }

    private StringRequest delRequest() {
        return new StringRequest(Request.Method.POST, ConstUtils.BASEURL + "deldiscuss.php",
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
                            Toast.makeText(AdminDiscussDetailActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(AdminDiscussDetailActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminDiscussDetailActivity.this, "网络连接超时", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("discussId",String.valueOf(discuss.getId()));
                return map;
            }
        };
    }
}
