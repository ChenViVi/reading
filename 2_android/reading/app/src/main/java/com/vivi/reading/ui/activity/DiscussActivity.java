package com.vivi.reading.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
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
import com.vivi.reading.adapter.TabViewPagerAdapter;
import com.vivi.reading.bean.DiscussType;
import com.vivi.reading.ui.fragment.DiscussFragment;
import com.vivi.reading.util.ConstUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class DiscussActivity extends AppCompatActivity {

    private RequestQueue queue;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView tvAdd;
    private TabViewPagerAdapter adapterTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discuss);
        queue = Volley.newRequestQueue(this);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager= (ViewPager) findViewById(R.id.viewPager);
        tvAdd = findViewById(R.id.tv_add);
        adapterTab = new TabViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterTab);
        tabLayout.setupWithViewPager(viewPager);
        queue.add(getType());
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DiscussActivity.this, AddDiscussActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void addFragment(Fragment fragment, String title){
        adapterTab.addFragment(fragment,title);
        adapterTab.notifyDataSetChanged();
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
                            Toast.makeText(DiscussActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Gson gson = new Gson();
                            List<DiscussType> items = gson.fromJson(list, new TypeToken<List<DiscussType>>() {}.getType());
                            for (DiscussType type : items){
                                DiscussFragment fragment = new DiscussFragment();
                                Bundle bundle = new Bundle();
                                bundle.putInt("id", type.getId());
                                bundle.putString("name", type.getName());
                                fragment.setArguments(bundle);
                                addFragment(fragment, type.getName());
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DiscussActivity.this, "网络连接超时", Toast.LENGTH_SHORT).show();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
}
