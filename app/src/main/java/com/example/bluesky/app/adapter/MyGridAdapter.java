package com.example.bluesky.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bluesky.app.R;
import com.example.bluesky.app.bean.ListItems;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by bluesky on 16/6/16.
 */
public class MyGridAdapter extends BaseAdapter {
    List<ListItems.DataBean.ItemsBean.ComponentBean.ItemsBeans> itemsBeanList;
    Context context;

    public MyGridAdapter(List<ListItems.DataBean.ItemsBean.ComponentBean.ItemsBeans> itemsBeanList, Context context) {
        this.itemsBeanList = itemsBeanList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return itemsBeanList.size();
    }

    @Override
    public ListItems.DataBean.ItemsBean.ComponentBean.ItemsBeans getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gv, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textView.setText(itemsBeanList.get(position)
                .getComponent().getWord());
        Picasso.with(context).load(itemsBeanList.get(position)
                .getComponent().getPicUrl())
                .placeholder(R.mipmap.ic_launcher).into(viewHolder.imageView);

        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        TextView textView;
    }
}
