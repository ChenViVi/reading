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
import com.vivi.reading.adapter.CommentAdapter;
import com.vivi.reading.bean.Action;
import com.vivi.reading.bean.Comment;
import com.vivi.reading.ui.view.ShareDialog;
import com.vivi.reading.util.ConstUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArticleDetailActivity extends AppCompatActivity {

    private RequestQueue queue;
    private SharedPreferences preferences;

    private int userId;
    private int collectFlag = 0;
    private int favoriteFlag = 0;
    private int type = 0;
    private CommentAdapter adapter;
    private ArrayList<Comment> data = new ArrayList<>();

    private ListView listView;
    private ImageView ivCollect;
    private TextView tvArticleTitle;
    private TextView tvArticleDate;
    private TextView tvArticleAuthor;
    private TextView tvArticleContent;
    private ImageView ivBack;
    private EditText editComment;
    private LinearLayout layoutHot;
    private LinearLayout layoutNew;
    private ImageView ivShare;
    private ImageView ivFavorite;
    private TextView tvComment;
    private RelativeLayout layoutCommnet;
    private ImageView ivLineNew;
    private ImageView ivLineHot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        queue = Volley.newRequestQueue(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = preferences.getInt("id",-1);

        Intent intent = getIntent();
        final int articleId = intent.getIntExtra("articleId",0);
        final String title = intent.getStringExtra("title");
        String date = intent.getStringExtra("date");
        String author = intent.getStringExtra("author");
        final String content = intent.getStringExtra("content");
        collectFlag = intent.getIntExtra("collect",0);
        favoriteFlag= intent.getIntExtra("favorite",0);

        View layout = getLayoutInflater().inflate(R.layout.header_article_detail,null);
        tvArticleTitle = layout.findViewById(R.id.tv_article_title);
        tvArticleDate = layout.findViewById(R.id.tv_article_date);
        tvArticleAuthor = layout.findViewById(R.id.tv_action_author);
        tvArticleContent = layout.findViewById(R.id.tv_article_content);
        ivCollect = layout.findViewById(R.id.iv_collect);
        ivFavorite = layout.findViewById(R.id.iv_favorite);
        ivShare = layout.findViewById(R.id.iv_share);
        ivBack = layout.findViewById(R.id.iv_back);
        editComment = findViewById(R.id.edit_comment);
        tvComment = findViewById(R.id.tv_comment);
        layoutCommnet = findViewById(R.id.layout_comment);
        layoutHot = layout.findViewById(R.id.layout_comment_hot);
        layoutNew = layout.findViewById(R.id.layout_comment_new);
        listView = findViewById(R.id.list_view);
        ivLineNew = layout.findViewById(R.id.iv_line_new);
        ivLineHot = layout.findViewById(R.id.iv_line_hot);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (intent.getBooleanExtra("fromCollect",false)){
            queue.add(getActionRequest(userId,articleId));
            collectFlag = 1;
        }

        if (collectFlag == 1){
            ivCollect.setImageResource(R.drawable.ic_collect_focus);
        }
        if (favoriteFlag == 1){
            ivFavorite.setImageResource(R.drawable.ic_favorite_focus);
        }

        queue.add(getCommentRequest(articleId,0));
        adapter = new CommentAdapter(this,data,queue);

        ivCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userId == -1){
                    Intent intent1 = new Intent(ArticleDetailActivity.this,LoginActivity.class);
                    startActivity(intent1);
                }
                else {
                    if (collectFlag == 1){
                        queue.add(getSetCollectRequest(userId,articleId,0));
                    }
                    else {
                        queue.add(getSetCollectRequest(userId,articleId,1));
                    }
                }
            }
        });

        ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userId == -1){
                    Intent intent = new Intent(ArticleDetailActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    if (favoriteFlag == 0){
                        queue.add(getSetFavoriteRequest(userId,articleId,1));
                    }
                    else {
                        queue.add(getSetFavoriteRequest(userId,articleId,0));
                    }
                }
            }
        });

        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareDialog(ArticleDetailActivity.this, "http://android.myapp.com/myapp/detail.htm?apkName=com.vivi.reading", "同", "更多精彩内容尽在【同】").show();
            }
        });

        tvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
                String date = format.format(new Date());*/
                String content = editComment.getText().toString();
                if (userId == -1){
                    Intent intent1 = new Intent(ArticleDetailActivity.this,LoginActivity.class);
                    startActivity(intent1);
                }
                else if (content.equals("")){
                    Toast.makeText(ArticleDetailActivity.this, "评论不能为空", Toast.LENGTH_SHORT).show();
                }
                else {
                    queue.add(getSetCommentRequest(userId,articleId,content,type));
                    onFocusChange(false);
                }
            }
        });
        layoutNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 0;
                queue.add(getCommentRequest(articleId,0));
                ivLineNew.setBackgroundColor(getResources().getColor(R.color.textColorBlack));
                ivLineHot.setBackgroundColor(getResources().getColor(R.color.textColorGray));
            }
        });
        layoutHot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = 1;
                queue.add(getCommentRequest(articleId,1));
                ivLineHot.setBackgroundColor(getResources().getColor(R.color.textColorBlack));
                ivLineNew.setBackgroundColor(getResources().getColor(R.color.textColorGray));
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
        tvArticleTitle.setText(title);
        tvArticleDate.setText(date);
        tvArticleAuthor.setText(author);
        tvArticleContent.setText(content);
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

    private StringRequest getSetCollectRequest(final int userId, final int articleId, final int collect) {
         StringRequest request = new StringRequest(Request.Method.POST, ConstUtils.BASEURL + "setarticlecolt.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("collect","response=" + response);
                        Log.e("collect","userId=" + userId);
                        Log.e("collect","articleId=" + articleId);
                        Log.e("collect","collect=" + collect);
                        int result = 0;
                        try {
                            JSONObject json= new JSONObject(response);
                            result = json.getInt("result");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result != 0){
                            Toast.makeText(ArticleDetailActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            collectFlag = collect;
                            if (collectFlag == 1){
                                ivCollect.setImageResource(R.drawable.ic_collect_focus);
                            }
                            else {
                                ivCollect.setImageResource(R.drawable.ic_collect);
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ArticleDetailActivity.this, "网络连接超时", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("userId", String.valueOf(userId));
                map.put("articleId", String.valueOf(articleId));
                map.put("collect",String.valueOf(collect));
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
                            Toast.makeText(ArticleDetailActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        } else if (action.getResult() == 1) {
                            Toast.makeText(ArticleDetailActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        } else if (action.getResult() == 0) {
                            if(favorite == 1){
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
                Toast.makeText(ArticleDetailActivity.this, "网络连接超时", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("userId", String.valueOf(userId));
                map.put("articleId", String.valueOf(articleId));
                map.put("favorite",String.valueOf(favorite));
                return map;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }

    private StringRequest getCommentRequest(final int articleId,final int type) {
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
                                Toast.makeText(ArticleDetailActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ArticleDetailActivity.this, "网络连接超时", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("articleId",String.valueOf(articleId));
                map.put("type",String.valueOf(type));
                return map;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }

    private StringRequest getSetCommentRequest(final int userId,final int articleId,final String content,final int type) {
        StringRequest request = new StringRequest(Request.Method.POST, ConstUtils.BASEURL + "addcomment.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        queue.add(getCommentRequest(articleId,type));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ArticleDetailActivity.this, "网络连接超时", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("userId",String.valueOf(userId));
                map.put("articleId",String.valueOf(articleId));
                map.put("content",content);
                return map;
            }
        };
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
                            Toast.makeText(ArticleDetailActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                        } else if (action.getResult() == 1) {
                            ivFavorite.setImageResource(R.drawable.ic_favorite);
                        } else if (action.getResult() == 0) {
                            if(action.getFavorite() == 1){
                                ivFavorite.setImageResource(R.drawable.ic_favorite_focus);
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ArticleDetailActivity.this, "网络连接超时", Toast.LENGTH_SHORT).show();
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
}
