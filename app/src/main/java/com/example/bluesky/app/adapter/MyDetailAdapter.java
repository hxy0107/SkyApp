package com.example.bluesky.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bluesky.app.R;
import com.example.bluesky.app.bean.CategoryList;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by bluesky on 16/6/16.
 */
public class MyDetailAdapter extends BaseAdapter {
    List<CategoryList.DataBean.ItemsBean> itemsBeanList;
    Context context;

    public MyDetailAdapter(List<CategoryList.DataBean.ItemsBean> itemsBeanList, Context context) {
        this.itemsBeanList = itemsBeanList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return itemsBeanList.size();
    }

    @Override
    public CategoryList.DataBean.ItemsBean getItem(int position) {
        return itemsBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gv_detail, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
            viewHolder.tv_price = (TextView) convertView.findViewById(R.id.tv_shopping_cart_price);
            viewHolder.tv_origin_price = (TextView) convertView.findViewById(R.id.tv_origin_price);
            viewHolder.tv_sales = (TextView) convertView.findViewById(R.id.tv_sales);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textView.setText(itemsBeanList.get(position)
                .getComponent().getDescription());
        Picasso.with(context).load(itemsBeanList.get(position)
                .getComponent().getPicUrl())
                .placeholder(R.mipmap.ic_launcher).into(viewHolder.imageView);
        viewHolder.tv_price.setText("¥"+itemsBeanList.get(position).getComponent().getPrice());
        viewHolder.tv_origin_price.setText("原¥"+itemsBeanList.get(position).getComponent().getOrigin_price());
        viewHolder.tv_sales.setText("销量:"+itemsBeanList.get(position).getComponent().getSales());
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        TextView textView;
        TextView tv_price;
        TextView tv_origin_price;
        TextView tv_sales;
    }
}
