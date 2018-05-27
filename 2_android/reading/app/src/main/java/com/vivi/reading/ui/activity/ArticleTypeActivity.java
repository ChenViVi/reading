package com.vivi.reading.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

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
import com.vivi.reading.adapter.ArticleTypeAdapter;
import com.vivi.reading.bean.ArticleType;
import com.vivi.reading.util.ConstUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ArticleTypeActivity extends Activity {

    private SharedPreferences preferences;
    private RequestQueue queue;

    private ArrayList<ArticleType> data = new ArrayList<>();
    private ArticleTypeAdapter adapter;

    private ListView listView;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);

        queue = Volley.newRequestQueue(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        listView = findViewById(R.id.list_view);
        ivBack = findViewById(R.id.iv_back);

        adapter = new ArticleTypeAdapter(this,data);

        listView.setAdapter(adapter);
        queue.add(getArticleType());

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArticleType articleType = data.get(position);
                Intent intent = new Intent(ArticleTypeActivity.this, ArticleTypeDetailActivity.class);
                intent.putExtra("id", articleType.getId());
                intent.putExtra("name", articleType.getName());
                startActivity(intent);
            }
        });
    }

    private StringRequest getArticleType() {
        StringRequest request = new StringRequest(Request.Method.POST, ConstUtils.BASEURL + "getarticletype.php",
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
                            Toast.makeText(ArticleTypeActivity.this, "获取文章分类失败", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Gson gson = new Gson();
                            List<ArticleType> items = gson.fromJson(list, new TypeToken<List<ArticleType>>() {}.getType());
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
                Toast.makeText(ArticleTypeActivity.this, "网络连接超时", Toast.LENGTH_SHORT).show();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
}
