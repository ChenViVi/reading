package com.vivi.reading.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.vivi.reading.R;

import com.vivi.reading.bean.Article;

import java.util.ArrayList;

/**
 * Created by vivi on 2016/6/2.
 */
public class ArticleAdapter extends BaseAdapter {

    private RequestQueue queue;
    private Context context;
    private ArrayList<Article> data;

    public ArticleAdapter(Context context, ArrayList<Article> data, RequestQueue queue){
        this.context = context;
        this.data = data;
        this.queue = queue;
    }

    class ViewHolder{
        TextView tvTitle;
        TextView tvInfo;
        TextView tvDate;
        ViewHolder(View view){
            tvTitle = view.findViewById(R.id.tv_article_title);
            tvInfo = view.findViewById(R.id.tv_article_info);
            tvDate = view.findViewById(R.id.tv_article_date);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_collect,null);
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
        return convertView;
    }
}
