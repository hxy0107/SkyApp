package com.example.bluesky.app.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bluesky.app.R;
import com.example.bluesky.app.bean.ListItems;
import com.example.bluesky.app.event.NotifySelectedItemEvent;

import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by bluesky on 16/6/16.
 */
public class ListItemAdapter extends BaseAdapter {
    List<ListItems.DataBean.ItemsBean> list;
    Context context;
    private int selectedPosition = 0;// 初始选中第一行
    public ListItemAdapter(List<ListItems.DataBean.ItemsBean> list, Context context) {
        this.list = list;
        this.context = context;
        EventBus.getDefault().register(this);

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ListItems.DataBean.ItemsBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_tv, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.tv);
            viewHolder.item_content_layout = (RelativeLayout) convertView.findViewById(R.id.item_content_layout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        if (selectedPosition == position) {
            viewHolder.item_content_layout.setBackgroundColor(
                    context.getResources().getColor(R.color.colorPrimary)
            );
        } else {
            viewHolder.item_content_layout.setBackgroundColor(
                    context.getResources().getColor(android.R.color.transparent));
        }
        Log.e("===","=title==="+list.get(position).getComponent().getAction().getTitle());
        viewHolder.textView.setText(list.get(position).getComponent().getAction().getTitle());

        return convertView;
    }

    @Subscribe(threadMode = ThreadMode.PostThread)
    public void onEvent(NotifySelectedItemEvent notifySelectedItemEvent){
       selectedPosition = notifySelectedItemEvent.getPos();
        notifyDataSetChanged();
    }
    class ViewHolder {
        TextView textView;
        RelativeLayout item_content_layout;
    }
}
