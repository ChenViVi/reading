package com.vivi.reading.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.vivi.reading.R;

import com.vivi.reading.util.BitmapCache;
import com.vivi.reading.util.ConstUtils;
import com.vivi.reading.bean.Article;

import java.util.ArrayList;

/**
 * Created by vivi on 2016/6/2.
 */
public class CollectAdapter extends BaseAdapter {

    private RequestQueue queue;
    private Context context;
    private ArrayList<Article> data;

    public CollectAdapter(Context context, ArrayList<Article> data, RequestQueue queue){
        this.context = context;
        this.data = data;
        this.queue = queue;
    }

    class ViewHolder{
        NetworkImageView nivArticleImg;
        TextView tvTitle;
        TextView tvInfo1;
        TextView tvInfo2;
        TextView tvDate;
        ViewHolder(View view){
            nivArticleImg = (NetworkImageView) view.findViewById(R.id.niv_article_img);
            tvTitle = (TextView) view.findViewById(R.id.tv_article_title);
            tvInfo1 = (TextView) view.findViewById(R.id.tv_article_info1);
            tvInfo2 = (TextView) view.findViewById(R.id.tv_article_info2);
            tvDate = (TextView) view.findViewById(R.id.tv_article_date);
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
        viewHolder.nivArticleImg.setDefaultImageResId(R.drawable.img_default);
        viewHolder.nivArticleImg.setErrorImageResId(R.drawable.img_default);
        viewHolder.nivArticleImg.setErrorImageResId(R.drawable.img_default);
        viewHolder.nivArticleImg.setImageUrl(ConstUtils.BASEURL + "img/" + article.getImgUrl(),new ImageLoader(queue, new BitmapCache()));
        viewHolder.tvTitle.setText(article.getTitle());
        viewHolder.tvInfo1.setText(article.getInfo1()+"，");
        viewHolder.tvInfo2.setText(article.getInfo2());
        viewHolder.tvDate.setText(article.getDate());
        return convertView;
    }
}
