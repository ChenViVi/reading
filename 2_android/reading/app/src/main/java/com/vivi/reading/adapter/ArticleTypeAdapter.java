package com.vivi.reading.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.vivi.reading.R;
import com.vivi.reading.bean.ArticleType;

import java.util.ArrayList;

/**
 * Created by vivi on 2016/6/2.
 */
public class ArticleTypeAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ArticleType> data;

    public ArticleTypeAdapter(Context context, ArrayList<ArticleType> data){
        this.context = context;
        this.data = data;
    }

    class ViewHolder{
        TextView tvName;
        ViewHolder(View view){
            tvName = view.findViewById(R.id.tvName);
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public ArticleType getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_article_type,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ArticleType articleType = getItem(position);
        viewHolder.tvName.setText(articleType.getName());
        return convertView;
    }
}
