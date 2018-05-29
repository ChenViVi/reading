package com.vivi.reading.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vivi.reading.R;
import com.vivi.reading.bean.ArticleType;
import com.vivi.reading.bean.User;
import com.vivi.reading.ui.activity.AdminArticleActivity;
import com.vivi.reading.ui.activity.AdminArticleAddActivity;
import com.vivi.reading.ui.activity.AdminUserActivity;
import com.vivi.reading.util.ConstUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vivi on 2016/6/2.
 */
public class AdminUserAdapter extends BaseAdapter{

    private RequestQueue queue;
    private Context context;
    private ArrayList<User> data;

    public AdminUserAdapter(Context context, ArrayList<User> data, RequestQueue queue){
        this.context = context;
        this.data = data;
        this.queue = queue;
    }

    class ViewHolder{
        TextView tvName;
        Button btnSeal;
        ViewHolder(View view){
            tvName = view.findViewById(R.id.tvName);
            btnSeal  = view.findViewById(R.id.btn_seal);
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public User getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_admin_user,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        User user = getItem(position);
        viewHolder.tvName.setText(user.getName());
        if (user.getSeal() == 1) viewHolder.btnSeal.setText("恢复");
        else  viewHolder.btnSeal.setText("封号");
        viewHolder.btnSeal.setOnClickListener(new MyListener(user));
        return convertView;
    }

    private class MyListener implements View.OnClickListener {
        private User user;
        public MyListener(User user){
            this.user = user;
        }
        @Override
        public void onClick(View v) {
            queue.add(delRequest(user));
        }

        private StringRequest delRequest(final User user) {
            return new StringRequest(Request.Method.POST, ConstUtils.BASEURL + "sealuser.php",
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
                                Toast.makeText(context, "网络连接失败", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(context, "操作成功", Toast.LENGTH_SHORT).show();
                                Gson gson = new Gson();
                                List<User> items = gson.fromJson(list, new TypeToken<List<User>>() {}.getType());
                                data.clear();
                                if (items != null){
                                    data.addAll(items);
                                }
                                notifyDataSetChanged();
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
                    map.put("userId",String.valueOf(user.getId()));
                    if (user.getSeal() == 0) map.put("seal", "1");
                    else map.put("seal", "0");
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
