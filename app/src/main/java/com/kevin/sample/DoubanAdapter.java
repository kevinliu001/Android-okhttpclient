package com.kevin.sample;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kevin.sample.bean.Douban;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * @author kevinliu
 */
public class DoubanAdapter extends RecyclerView.Adapter<DoubanAdapter.DoubanViewHolder> {

    Context context;
    ArrayList<Douban> doubans;

    public DoubanAdapter(Context context,ArrayList<Douban> doubans) {
        this.context = context;
        this.doubans = doubans;
    }

    @NonNull
    @Override
    public DoubanAdapter.DoubanViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.item_douban, null, false);
        return new DoubanViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull DoubanAdapter.DoubanViewHolder holder, int i) {

        Douban douban = doubans.get(i);

        List<Douban.CastsBean> castsBeans = douban.getCasts();
        if (castsBeans != null && castsBeans.size() > 0) {
            Douban.CastsBean castsBean = castsBeans.get(0);
            if (castsBean != null && castsBean.getAvatars() != null){
                Picasso.with(context).load(Uri.parse(castsBean.getAvatars().getSmall())).into(holder.doubanImg);
            }
        }

        holder.doubanTitle.setText(douban.getTitle());
    }

    @Override
    public int getItemCount() {
        return doubans.size();
    }



    class DoubanViewHolder extends RecyclerView.ViewHolder{

        ImageView doubanImg;
        TextView doubanTitle;

        public DoubanViewHolder(@NonNull View itemView) {
            super(itemView);
            doubanImg = itemView.findViewById(R.id.douban_img);
            doubanTitle = itemView.findViewById(R.id.douban_title);
        }
    }
}
