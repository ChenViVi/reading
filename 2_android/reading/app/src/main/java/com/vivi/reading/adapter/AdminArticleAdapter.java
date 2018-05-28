package com.vivi.reading.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.vivi.reading.R;
import com.vivi.reading.bean.Article;
import com.vivi.reading.bean.ArticleType;
import com.vivi.reading.ui.activity.AdminArticleActivity;
import com.vivi.reading.ui.activity.AdminArticleDetailActivity;
import com.vivi.reading.ui.activity.ArticleDetailActivity;
import com.vivi.reading.util.ConstUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vivi on 2016/6/2.
 */
public class AdminArticleAdapter extends BaseAdapter {

    private RequestQueue queue;
    private Context context;
    private ArrayList<Article> data;

    public AdminArticleAdapter(Context context, ArrayList<Article> data, RequestQueue queue){
        this.context = context;
        this.data = data;
        this.queue = queue;
    }

    class ViewHolder{
        TextView tvTitle;
        TextView tvInfo;
        TextView tvDate;
        Button btnDelete;
        LinearLayout llArticle;
        ViewHolder(View view){
            tvTitle = view.findViewById(R.id.tv_article_title);
            tvInfo = view.findViewById(R.id.tv_article_info);
            tvDate = view.findViewById(R.id.tv_article_date);
            btnDelete  = view.findViewById(R.id.btn_delete);
            llArticle = view.findViewById(R.id.ll_article);
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Article getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_admin_article,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Article article = getItem(position);
        viewHolder.tvTitle.setText(article.getTitle());
        viewHolder.tvInfo.setText(article.getInfo());
        viewHolder.tvDate.setText(article.getDate());
        viewHolder.btnDelete.setOnClickListener(new MyListener(article.getId()));
        viewHolder.llArticle.setOnClickListener(new MyListener2(article));
        return convertView;
    }

    private class MyListener implements View.OnClickListener {
        private int id;
        public MyListener(int id){
            this.id = id;
        }
        @Override
        public void onClick(View v) {
            queue.add(delRequest(id));
        }

        private StringRequest delRequest(final int id) {
            return new StringRequest(Request.Method.POST, ConstUtils.BASEURL + "delarticle.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            int i = 0;
                            for (Article article :data){
                                if (article.getId() == id){
                                    data.remove(i);
                                    notifyDataSetChanged();
                                    break;
                                }
                                i++;
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "网络连接超时", Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map = new HashMap<>();
                    map.put("articleId",String.valueOf(id));
                    return map;
                }
            };
        }
    }

    private class MyListener2 implements View.OnClickListener {
        private Article article;
        public MyListener2(Article article){
            this.article = article;
        }
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context,AdminArticleDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("article",article);
            intent.putExtras(bundle);
            context.startActivity(intent);
        }
    }
}
