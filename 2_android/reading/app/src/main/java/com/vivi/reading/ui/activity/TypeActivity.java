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
import com.vivi.reading.adapter.CollectAdapter;
import com.vivi.reading.adapter.TypeAdapter;
import com.vivi.reading.bean.Article;
import com.vivi.reading.bean.Type;
import com.vivi.reading.util.ConstUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypeActivity extends Activity {

    private SharedPreferences preferences;
    private RequestQueue queue;

    private int id;
    private ArrayList<Type> data = new ArrayList<>();
    private TypeAdapter adapter;

    private ListView listView;
    private ImageView ivBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);

        queue = Volley.newRequestQueue(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        id = preferences.getInt("id",-1);

        listView = (ListView) findViewById(R.id.list_view);
        ivBack = (ImageView) findViewById(R.id.iv_back);

        adapter = new TypeAdapter(this,data,queue);
        listView.setEmptyView(findViewById(R.id.layout_empty_collect));

        listView.setAdapter(adapter);
        //queue.add(getArticleClotRequest(id));

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //List<Type> items = gson.fromJson(list, new TypeToken<List<Type>>() {}.getType());
        for (int i = 0; i < 10; i++){
            data.add(new Type(i, "name" + i));
        }
        data.add(new Type(0,"排序算法"));
        data.add(new Type(1,"回归算法"));
        data.add(new Type(2,"正则化算法"));
        data.add(new Type(3,"决策树算法"));
        data.add(new Type(4,"贝叶斯算法"));
        data.add(new Type(5,"基于核的算法"));
        data.add(new Type(6,"聚类算法"));
        data.add(new Type(7,"深度学习算法"));
        data.add(new Type(8,"降低维度算法"));
        data.add(new Type(9,"集成算法"));
        data.add(new Type(10,"关联规则学习"));
        data.add(new Type(11,"深度学习算法"));
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Type type = data.get(position);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //queue.add(getArticleClotRequest(id));
    }

    private StringRequest getArticleClotRequest(final int userId) {
        StringRequest request = new StringRequest(Request.Method.POST, ConstUtils.BASEURL + "getarticlecolt.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.e("articleIdFlag","getArticleRequest() response" + response);
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
                            Toast.makeText(TypeActivity.this, "获取文章失败", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Gson gson = new Gson();
                            List<Type> items = gson.fromJson(list, new TypeToken<List<Type>>() {}.getType());
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
                Toast.makeText(TypeActivity.this, "网络连接超时", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("userId",String.valueOf(userId));
                return map;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
}
