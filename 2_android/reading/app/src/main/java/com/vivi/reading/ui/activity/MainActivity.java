package com.vivi.reading.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.vivi.reading.bean.Action;
import com.vivi.reading.bean.Article;
import com.vivi.reading.ui.fragment.DrawerFragment;
import com.vivi.reading.util.ConstUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private RequestQueue queue;

    private boolean isLogin;
    private int userId;
    private Article article;
    private int articleIdFlag;
    private int favoriteFlag = 0;
    private int collect = 0;

    private DrawerFragment drawerFragment;
    private Toolbar toolbar;
    private LinearLayout layout;
    private TextView tvArticleTitle;
    private TextView tvArticleInfo1;
    private TextView tvArticleDate;
    private TextView tvArticleAuthor;
    private ImageView ivFavorite;
    private ImageView ivComment;
    private List<Article> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        queue = Volley.newRequestQueue(this);

        toolbar = findViewById(R.id.toolbar);
        drawerFragment = (DrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        layout = findViewById(R.id.layout_article_info);
        tvArticleTitle = findViewById(R.id.tv_article_title);
        tvArticleInfo1 = findViewById(R.id.tv_article_info);
        tvArticleDate = findViewById(R.id.tv_article_date);
        tvArticleAuthor = findViewById(R.id.tv_action_author);
        ivFavorite = findViewById(R.id.iv_favorite);
        ivComment = findViewById(R.id.iv_comment);

        ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userId == -1){
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    if (favoriteFlag == 0){
                        queue.add(getSetFavoriteRequest(userId,articleIdFlag,1));
                    }
                    else {
                        queue.add(getSetFavoriteRequest(userId,articleIdFlag,0));
                    }
                }
            }
        });

        ivComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ArticleDetailActivity.class);
                intent.putExtra("articleId", article.getId());
                intent.putExtra("title", article.getTitle());
                intent.putExtra("date", article.getDate());
                intent.putExtra("author", article.getType());
                intent.putExtra("content", article.getContent());
                intent.putExtra("collect",collect);
                intent.putExtra("favorite",favoriteFlag);
                intent.putExtra("comment",true);
                startActivity(intent);
            }
        });

        layout.setOnTouchListener(new View.OnTouchListener() {
            float y1 = 0;
            float y2 = 0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    y1 = event.getY();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    y2 = event.getY();
                    if (Math.abs(y2 - y1) > 50){
                        if (y2 - y1 < 0) {
                            if (articleIdFlag == 0)
                                Toast.makeText(MainActivity.this, "没有更多文章了", Toast.LENGTH_SHORT).show();
                            else {
                                article = items.get(--articleIdFlag);
                                tvArticleTitle.setText(article.getTitle());
                                tvArticleInfo1.setText(article.getInfo());
                                tvArticleDate.setText(article.getDate());
                                tvArticleAuthor.setText(article.getType());
                                queue.add(getActionRequest(userId,article.getId()));
                            }
                        } else if (y2 - y1 > 0) {
                            if (articleIdFlag == items.size())
                                Toast.makeText(MainActivity.this, "没有更多文章了", Toast.LENGTH_SHORT).show();
                            else {
                                article = items.get(++articleIdFlag);
                                tvArticleTitle.setText(article.getTitle());
                                tvArticleInfo1.setText(article.getInfo());
                                tvArticleDate.setText(article.getDate());
                                tvArticleAuthor.setText(article.getType());
                                queue.add(getActionRequest(userId,article.getId()));
                            }
                        }
                    }
                    else if (article != null) {
                        Intent intent = new Intent(MainActivity.this, ArticleDetailActivity.class);
                        intent.putExtra("articleId", article.getId());
                        intent.putExtra("title", article.getTitle());
                        intent.putExtra("date", article.getDate());
                        intent.putExtra("author", article.getType());
                        intent.putExtra("content", article.getContent());
                        intent.putExtra("collect",collect);
                        intent.putExtra("favorite",favoriteFlag);
                        startActivity(intent);
                    }
                }
                return true;
            }
        });
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //final DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(new DrawerFragment.FragmentDrawerListener() {
            @Override
            public void onDrawerItemSelected(View view, int position) {
                Intent intent = null;
                switch (position) {
                    case 0:
                        if (isLogin) {
                            intent = new Intent(MainActivity.this, CollectActivity.class);
                        } else {
                            intent = new Intent(MainActivity.this, LoginActivity.class);
                        }
                        //mDrawerLayout.closeDrawers();
                        break;
                    case 1:
                        intent = new Intent(MainActivity.this, ArticleTypeActivity.class);
                        break;
                    case 2:
                        intent = new Intent(MainActivity.this, DiscussActivity.class);
                        break;
                    case 3:
                        intent = new Intent(MainActivity.this, SubmitActivity.class);
                        //mDrawerLayout.closeDrawers();
                        break;
                }
                startActivity(intent);
            }
        });
        queue.add(getArticleRequest());
    }

    @Override
    protected void onResume() {
        super.onResume();
        isLogin = preferences.getBoolean("isLogin", false);
        userId = preferences.getInt("id", -1);
        queue.add(getActionRequest(userId,articleIdFlag));
    }

    private StringRequest getArticleRequest() {
        StringRequest request = new StringRequest(Request.Method.GET, ConstUtils.BASEURL + "getarticle.php",
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
                            Toast.makeText(MainActivity.this, "获取文章失败", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Gson gson = new Gson();
                            items = gson.fromJson(list, new TypeToken<List<Article>>() {}.getType());
                            if (items.size() == 0)
                                Toast.makeText(MainActivity.this, "没有任何文章", Toast.LENGTH_SHORT).show();
                            else {
                                article = items.get(0);
                                tvArticleTitle.setText(article.getTitle());
                                tvArticleInfo1.setText(article.getInfo());
                                tvArticleDate.setText(article.getDate());
                                tvArticleAuthor.setText(article.getType());
                                queue.add(getActionRequest(userId,article.getId()));
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "网络连接超时", Toast.LENGTH_SHORT).show();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }

    private StringRequest getActionRequest(final int userId,final int articleId) {
        StringRequest request = new StringRequest(Request.Method.POST, ConstUtils.BASEURL + "getarticleact.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.e("articleIdFlag","getActionRequest() response" + response);
                        //Log.e("articleIdFlag","getActionRequest() userId" + String.valueOf(userId));
                        //Log.e("articleIdFlag","getActionRequest() articleId" + String.valueOf(articleId));
                        Gson gson = new Gson();
                        Action action = gson.fromJson(response, Action.class);
                        if (action.getResult() == 2) {
                            Toast.makeText(MainActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        } else if (action.getResult() == 1) {
                            ivFavorite.setImageResource(R.drawable.ic_favorite);
                        } else if (action.getResult() == 0) {
                            favoriteFlag = action.getFavorite();
                            collect = action.getCollect();
                            if(favoriteFlag == 1){
                                ivFavorite.setImageResource(R.drawable.ic_favorite_focus);
                            }
                            else {
                                ivFavorite.setImageResource(R.drawable.ic_favorite);
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "网络连接超时", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("userId", String.valueOf(userId));
                map.put("articleId", String.valueOf(articleId));
                return map;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }

    private StringRequest getSetFavoriteRequest(final int userId, final int articleId,final int favorite) {
        StringRequest request = new StringRequest(Request.Method.POST, ConstUtils.BASEURL + "setarticlefav.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        Action action = gson.fromJson(response, Action.class);
                        if (action.getResult() == 2) {
                            Toast.makeText(MainActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        } else if (action.getResult() == 1) {
                            Toast.makeText(MainActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        } else if (action.getResult() == 0) {
                            if(favorite == 1){
                                ivFavorite.setImageResource(R.drawable.ic_favorite_focus);
                            }
                            else {
                                ivFavorite.setImageResource(R.drawable.ic_favorite);
                            }
                            favoriteFlag = favorite;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "网络连接超时", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("userId", String.valueOf(userId));
                map.put("articleId", String.valueOf(articleId));
                map.put("favorite",String.valueOf(favorite));
                Log.e("articleIdFlag","getSetFavoriteRequest userId="+userId);
                Log.e("articleIdFlag","getSetFavoriteRequest articleId="+articleId);
                Log.e("articleIdFlag","getSetFavoriteRequest favorite="+favorite);
                return map;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
}
