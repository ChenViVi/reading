package com.vivi.reading.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.vivi.reading.adapter.ArticleAdapter;
import com.vivi.reading.bean.Article;
import com.vivi.reading.util.ConstUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypeDetailActivity extends Activity {

    private RequestQueue queue;

    private ArrayList<Article> data = new ArrayList<>();
    private ArticleAdapter adapter;

    private ListView listView;
    private ImageView ivBack;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_detail);

        queue = Volley.newRequestQueue(this);

        listView = (ListView) findViewById(R.id.list_view);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText(getIntent().getStringExtra("name"));

        adapter = new ArticleAdapter(this,data,queue);

        listView.setAdapter(adapter);
        queue.add(getArticle(getIntent().getIntExtra("id",1)));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Article article = data.get(position);
                Intent intent = new Intent(TypeDetailActivity.this,ArticleDetailActivity.class);
                intent.putExtra("articleId",article.getId());
                intent.putExtra("title",article.getTitle());
                intent.putExtra("date",article.getDate());
                intent.putExtra("author",article.getType());
                intent.putExtra("content",article.getContent());
                intent.putExtra("fromCollect",true);
                startActivity(intent);
            }
        });
    }

    private StringRequest getArticle(final int typeId) {
        StringRequest request = new StringRequest(Request.Method.POST, ConstUtils.BASEURL + "getarticlebytype.php",
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
                            Toast.makeText(TypeDetailActivity.this, "获取文章失败", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Gson gson = new Gson();
                            List<Article> items = gson.fromJson(list, new TypeToken<List<Article>>() {}.getType());
                            data.clear();
                            if (items != null){
                                data.addAll(items);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TypeDetailActivity.this, "网络连接超时", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("typeId",String.valueOf(typeId));
                return map;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
}
