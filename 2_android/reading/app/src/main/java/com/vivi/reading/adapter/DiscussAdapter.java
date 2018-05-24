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
import com.vivi.reading.bean.Discuss;
import com.vivi.reading.bean.DiscussType;

import java.util.ArrayList;

/**
 * Created by vivi on 2016/6/2.
 */
public class DiscussAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Discuss> data;

    public DiscussAdapter(Context context, ArrayList<Discuss> data){
        this.context = context;
        this.data = data;
    }

    class ViewHolder{
        TextView tvTitle;
        TextView tvContent;
        TextView tvDate;
        ViewHolder(View view){
            tvTitle = view.findViewById(R.id.tv_title);
            tvContent = view.findViewById(R.id.tv_content);
            tvDate = view.findViewById(R.id.tv_date);
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Discuss getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_discuss,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Discuss discuss = getItem(position);
        viewHolder.tvTitle.setText(discuss.getTitle());
        viewHolder.tvContent.setText(discuss.getContent());
        viewHolder.tvDate.setText(discuss.getDate());
        return convertView;
    }
}
