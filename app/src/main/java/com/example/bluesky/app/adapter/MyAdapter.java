package com.example.bluesky.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.bluesky.app.bean.Item;
import com.example.bluesky.app.R;

import java.util.List;

/**
 * Created by bluesky on 16/6/15.
 */
public class MyAdapter extends BaseAdapter {
    Context context;
    List<Item> list;
    onCheckListener mOnCheckListener;

    public MyAdapter(Context context, List<Item> list) {
        this.context = context;
        this.list = list;
    }

    public void setmOnCheckListener(onCheckListener mOnCheckListener) {
        this.mOnCheckListener = mOnCheckListener;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Item getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item, null);
            viewHolder = new ViewHolder();
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(list.get(position).getName());
        viewHolder.checkBox.setChecked(list.get(position).isChecked());
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
             if (mOnCheckListener!=null){
                 mOnCheckListener.onCheckedChangeListener(position,isChecked);
             }
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView textView;
        CheckBox checkBox;
    }

    public interface onCheckListener{
        public void onCheckedChangeListener(int pos,boolean isChecked);
    }
}
