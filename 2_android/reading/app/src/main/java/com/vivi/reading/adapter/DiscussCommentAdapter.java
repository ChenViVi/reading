package com.vivi.reading.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vivi.reading.R;
import com.vivi.reading.bean.DiscussComment;

import java.util.ArrayList;

/**
 * Created by vivi on 2016/6/2.
 */
public class DiscussCommentAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<DiscussComment> data;

    public DiscussCommentAdapter(Context context, ArrayList<DiscussComment> data){
        this.context = context;
        this.data = data;
    }

    class ViewHolder{
        TextView tvName;
        TextView tvContent;
        TextView tvDate;
        ViewHolder(View view){
            tvName = view.findViewById(R.id.tv_name);
            tvContent = view.findViewById(R.id.tv_content);
            tvDate = view.findViewById(R.id.tv_date);
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public DiscussComment getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_discuss_comment,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        DiscussComment discuss = getItem(position);
        viewHolder.tvName.setText(discuss.getUser());
        viewHolder.tvContent.setText(discuss.getContent());
        viewHolder.tvDate.setText(discuss.getDate());
        return convertView;
    }
}
