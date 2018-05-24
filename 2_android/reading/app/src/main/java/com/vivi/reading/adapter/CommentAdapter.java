package com.vivi.reading.adapter;

import android.content.Context;
import android.util.Log;
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
import com.vivi.reading.util.ConstUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vivi on 2016/6/2.
 */
public class CommentAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Comment> data;
    private RequestQueue queue;

    public CommentAdapter(Context context,ArrayList<Comment> data,RequestQueue queue){
        this.context = context;
        this.data = data;
        this.queue = queue;
    }

    class ViewHolder{
        ImageView userImg;
        TextView tvName;
        TextView tvDate;
        TextView tvContent;
        ImageView ivFavorite;
        ViewHolder(View view){
            userImg = (ImageView) view.findViewById(R.id.iv_user_img);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvDate = (TextView) view.findViewById(R.id.tv_date);
            tvContent = (TextView) view.findViewById(R.id.tv_content);
            ivFavorite = (ImageView) view.findViewById(R.id.iv_favorite);;
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Comment getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_article_comment,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Comment comment = getItem(position);
        viewHolder.tvName.setText(comment.getName());
        if (comment.getName() == null){
            viewHolder.tvName.setText("读者");
        }
        else {
            viewHolder.tvName.setText(comment.getName());
        }
        viewHolder.tvDate.setText(comment.getDate());
        viewHolder.tvContent.setText(comment.getContent());
        MyListener myListener = new MyListener(comment.getId());
        viewHolder.ivFavorite.setOnClickListener(myListener);
        return convertView;
    }

    private class MyListener implements View.OnClickListener {
        private int id;
        private boolean clicked = false;
        public MyListener(int id){
            this.id = id;
        }
        @Override
        public void onClick(View v) {
            if (!clicked){
                queue.add(getSetCommentFavRequest(id,this));
                ImageView imageView = (ImageView)v;
                imageView.setImageResource(R.drawable.ic_favorite_focus);
                imageView.setClickable(false);
            }
        }

        public void setClicked(boolean clicked) {
            this.clicked = clicked;
        }
    }

    private StringRequest getSetCommentFavRequest(final int commentId, final MyListener myListener) {
        return new StringRequest(Request.Method.POST, ConstUtils.BASEURL + "setcommentfav.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("commentFav","response="+response);
                        Log.e("commentFav","id="+commentId);
                        myListener.setClicked(true);
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
                map.put("commentId",String.valueOf(commentId));
                return map;
            }
        };
    }
}
