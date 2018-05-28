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
import android.widget.LinearLayout;
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
import com.vivi.reading.adapter.AdminCommentAdapter;
import com.vivi.reading.adapter.CommentAdapter;
import com.vivi.reading.bean.Action;
import com.vivi.reading.bean.Article;
import com.vivi.reading.bean.Comment;
import com.vivi.reading.ui.view.ShareDialog;
import com.vivi.reading.util.ConstUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminArticleDetailActivity extends AppCompatActivity {

    private RequestQueue queue;
    private AdminCommentAdapter adapter;
    private ArrayList<Comment> data = new ArrayList<>();

    private ListView listView;
    private TextView tvArticleTitle;
    private TextView tvArticleDate;
    private TextView tvArticleType;
    private TextView tvArticleContent;
    private TextView tvUpdate;
    private ImageView ivBack;

    private Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_article_detail);

        queue = Volley.newRequestQueue(this);

        Intent intent = getIntent();
        article = (Article) intent.getSerializableExtra("article");


        View layout = getLayoutInflater().inflate(R.layout.header_admin_article_detail,null);
        tvArticleTitle = layout.findViewById(R.id.tv_article_title);
        tvArticleDate = layout.findViewById(R.id.tv_article_date);
        tvArticleType = layout.findViewById(R.id.tv_type);
        tvArticleContent = layout.findViewById(R.id.tv_article_content);
        tvUpdate = layout.findViewById(R.id.tv_update);
        ivBack = layout.findViewById(R.id.iv_back);
        listView = findViewById(R.id.list_view);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        queue.add(getCommentRequest(article.getId()));
        adapter = new AdminCommentAdapter(this,data,queue);
        listView.addHeaderView(layout);
        listView.setAdapter(adapter);
        tvArticleTitle.setText(article.getTitle());
        tvArticleDate.setText(article.getDate());
        tvArticleType.setText(article.getType());
        tvArticleContent.setText(article.getContent());

        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(AdminArticleDetailActivity.this, AdminArticleUpdateActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("article",article);
                intent1.putExtras(bundle);
                startActivity(intent1);
            }
        });
    }

    private StringRequest getCommentRequest(final int articleId) {
        StringRequest request = new StringRequest(Request.Method.POST, ConstUtils.BASEURL + "getarticlecomt.php",
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
                                Toast.makeText(AdminArticleDetailActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Gson gson = new Gson();
                                List<Comment> items = gson.fromJson(list, new TypeToken<List<Comment>>() {}.getType());
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
                Toast.makeText(AdminArticleDetailActivity.this, "网络连接超时", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("articleId",String.valueOf(articleId));
                map.put("type","1");
                return map;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
}
