package com.vivi.reading.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import com.vivi.reading.bean.ArticleType;
import com.vivi.reading.ui.activity.AdminArticleActivity;
import com.vivi.reading.ui.activity.AdminArticleTypeActivity;
import com.vivi.reading.util.ConstUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vivi on 2016/6/2.
 */
public class AdminArticleTypeAdapter extends BaseAdapter{

    private RequestQueue queue;
    private Context context;
    private ArrayList<ArticleType> data;

    public AdminArticleTypeAdapter(Context context, ArrayList<ArticleType> data, RequestQueue queue){
        this.context = context;
        this.data = data;
        this.queue = queue;
    }

    class ViewHolder{
        TextView tvName;
        Button btnDelete;
        ViewHolder(View view){
            tvName = view.findViewById(R.id.tvName);
            btnDelete  = view.findViewById(R.id.btn_delete);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_admin_article_type,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ArticleType articleType = getItem(position);
        viewHolder.tvName.setText(articleType.getName());
        viewHolder.btnDelete.setOnClickListener(new MyListener(articleType.getId()));
        viewHolder.tvName.setOnClickListener(new MyListener2(articleType));
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
            return new StringRequest(Request.Method.POST, ConstUtils.BASEURL + "delarticletype.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            int i = 0;
                            for (ArticleType type :data){
                                if (type.getId() == id){
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
                    map.put("typeId",String.valueOf(id));
                    return map;
                }
            };
        }
    }

    private class MyListener2 implements View.OnClickListener {
        private ArticleType articleType;
        public MyListener2(ArticleType articleType){
            this.articleType = articleType;
        }
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, AdminArticleActivity.class);
            intent.putExtra("id", articleType.getId());
            intent.putExtra("name", articleType.getName());
            context.startActivity(intent);
        }
    }
}
