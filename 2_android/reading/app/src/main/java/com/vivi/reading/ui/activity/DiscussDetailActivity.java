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

public class DiscussDetailActivity extends AppCompatActivity {

    private RequestQueue queue;
    private SharedPreferences preferences;

    private int userId;
    private DiscussCommentAdapter adapter;
    private ArrayList<DiscussComment> data = new ArrayList<>();

    private ListView listView;
    private TextView tvArticleTitle;
    private TextView tvArticleDate;
    private TextView tvArticleAuthor;
    private TextView tvArticleContent;
    private ImageView ivBack;
    private EditText editComment;
    private TextView tvComment;
    private RelativeLayout layoutCommnet;
    private Discuss discuss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discuss_detail);

        queue = Volley.newRequestQueue(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = preferences.getInt("id",-1);

        Intent intent = getIntent();
        discuss = (Discuss) intent.getSerializableExtra("discuss");

        View layout = getLayoutInflater().inflate(R.layout.header_article_detail,null);
        tvArticleTitle = (TextView) layout.findViewById(R.id.tv_article_title);
        tvArticleDate = (TextView) layout.findViewById(R.id.tv_article_date);
        tvArticleAuthor = (TextView) layout.findViewById(R.id.tv_action_author);
        tvArticleContent = (TextView) layout.findViewById(R.id.tv_article_content);
        ivBack = (ImageView) layout.findViewById(R.id.iv_back);
        editComment = (EditText) findViewById(R.id.edit_comment);
        tvComment = (TextView) findViewById(R.id.tv_comment);
        layoutCommnet = (RelativeLayout) findViewById(R.id.layout_comment);
        listView = (ListView) findViewById(R.id.list_view);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        queue.add(getCommentRequest(discuss.getId()));
        adapter = new DiscussCommentAdapter(this,data);

        tvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = editComment.getText().toString();
                if (userId == -1){
                    Intent intent1 = new Intent(DiscussDetailActivity.this,LoginActivity.class);
                    startActivity(intent1);
                }
                else if (content.equals("")){
                    Toast.makeText(DiscussDetailActivity.this, "评论不能为空", Toast.LENGTH_SHORT).show();
                }
                else {
                    queue.add(getSetCommentRequest(userId,discuss.getId(),content));
                    onFocusChange(false);
                }
            }
        });
        listView.addHeaderView(layout);
        listView.setAdapter(adapter);
        if (intent.getBooleanExtra("comment",false)){
            //onFocusChange(true);
            layoutCommnet.setFocusableInTouchMode(false);
            listView.setStackFromBottom(true);
            listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        }
        tvArticleTitle.setText(discuss.getTitle());
        tvArticleDate.setText(discuss.getDate());
        tvArticleAuthor.setText(discuss.getUser());
        tvArticleContent.setText(discuss.getContent());
    }

    public void onFocusChange(final boolean hasFocus){
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                InputMethodManager imm = (InputMethodManager) editComment.getContext().getSystemService(INPUT_METHOD_SERVICE);
                if (hasFocus) {
                    //显示输入法
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    editComment.setFocusable(true);
                    editComment.setFocusableInTouchMode(true);
                    editComment.requestFocus();
                } else {
                    editComment.setText("");
                    //隐藏输入法
                    imm.hideSoftInputFromWindow(editComment.getWindowToken(),0);
                }
            }
        }, 100);
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
                                Toast.makeText(DiscussDetailActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(DiscussDetailActivity.this, "网络连接超时", Toast.LENGTH_SHORT).show();
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

    private StringRequest getSetCommentRequest(final int userId,final int discussId,final String content) {
        StringRequest request = new StringRequest(Request.Method.POST, ConstUtils.BASEURL + "adddiscusscomment.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        queue.add(getCommentRequest(discussId));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DiscussDetailActivity.this, "网络连接超时", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("userId",String.valueOf(userId));
                map.put("discussId",String.valueOf(discussId));
                map.put("content",content);
                return map;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
}
