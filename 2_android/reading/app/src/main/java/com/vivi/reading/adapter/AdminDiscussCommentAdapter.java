package com.vivi.reading.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.vivi.reading.R;
import com.vivi.reading.bean.Comment;
import com.vivi.reading.bean.DiscussComment;
import com.vivi.reading.util.ConstUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vivi on 2016/6/2.
 */
public class AdminDiscussCommentAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<DiscussComment> data;
    private RequestQueue queue;

    public AdminDiscussCommentAdapter(Context context, ArrayList<DiscussComment> data, RequestQueue queue){
        this.context = context;
        this.data = data;
        this.queue = queue;
    }

    class ViewHolder{
        ImageView userImg;
        TextView tvName;
        TextView tvDate;
        TextView tvContent;
        ImageView ivDelete;
        ViewHolder(View view){
            userImg = view.findViewById(R.id.iv_user_img);
            tvName = view.findViewById(R.id.tv_name);
            tvDate = view.findViewById(R.id.tv_date);
            tvContent = view.findViewById(R.id.tv_content);
            ivDelete = view.findViewById(R.id.iv_delete);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_admin_article_comment,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        DiscussComment comment = getItem(position);
        viewHolder.tvName.setText(comment.getUser());
        if (comment.getUser() == null){
            viewHolder.tvName.setText("读者");
        }
        else {
            viewHolder.tvName.setText(comment.getUser());
        }
        viewHolder.tvDate.setText(comment.getDate());
        viewHolder.tvContent.setText(comment.getContent());
        viewHolder.ivDelete.setOnClickListener(new MyListener(comment.getId()));
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
            return new StringRequest(Request.Method.POST, ConstUtils.BASEURL + "deldiscusscomt.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            int i = 0;
                            for (DiscussComment comment :data){
                                if (comment.getId() == id){
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
                    map.put("commentId",String.valueOf(id));
                    return map;
                }
            };
        }
    }
}
