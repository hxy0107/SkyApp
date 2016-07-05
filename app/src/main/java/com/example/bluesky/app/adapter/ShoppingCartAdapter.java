package com.example.bluesky.app.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bluesky.app.R;
import com.example.bluesky.app.bean.ShoppingCartItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bluesky on 16/6/18.
 */
public class ShoppingCartAdapter extends BaseAdapter {
    List<ShoppingCartItem> cartItemList = new ArrayList<>();
    Context context;
    onAscNumListener mOnAscNumListener;
    onDescNumListener mOnDescNumListener;
    onSelectItemListener mOnSelectItemListener;
    /**
     * To save checked items, and <b>re-add</b> while scrolling.
     * SparseArray是android里为<Interger,Object>这样的Hashmap而专门写的class,目的是提高效率，其核心是折半查找函数（binarySearch
     */
    SparseBooleanArray mChecked = new SparseBooleanArray();

    public ShoppingCartAdapter(Context context) {
        this.context = context;
    }

    public void setCartItemList(List<ShoppingCartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }

    public void setmOnSelectItemListener(onSelectItemListener mOnSelectItemListener) {
        this.mOnSelectItemListener = mOnSelectItemListener;
    }

    public SparseBooleanArray getmChecked() {
        return mChecked;
    }

    public void setmChecked(SparseBooleanArray mChecked) {
        this.mChecked = mChecked;
    }

    public void setOnAscNumListener(ShoppingCartAdapter.onAscNumListener mOnAscNumListener) {
        this.mOnAscNumListener = mOnAscNumListener;
    }

    public void setOnDescNumListener(ShoppingCartAdapter.onDescNumListener mOnDescNumListener) {
        this.mOnDescNumListener = mOnDescNumListener;
    }

    @Override
    public int getCount() {
        return cartItemList != null ? cartItemList.size() : 0;
    }

    @Override
    public ShoppingCartItem getItem(int position) {
        return cartItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_shopping_cart, null);
            viewHolder = new ViewHolder();
            viewHolder.img = (ImageView) convertView.findViewById(R.id.img_shopping_cart);
            viewHolder.tv_description = (TextView) convertView.findViewById(R.id.tv_shopping_cart_title);
            viewHolder.tv_price = (TextView) convertView.findViewById(R.id.tv_shopping_cart_price);
            viewHolder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
            viewHolder.tv_asc = (TextView) convertView.findViewById(R.id.tv_asc);
            viewHolder.tv_desc = (TextView) convertView.findViewById(R.id.tv_desc);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox3);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Picasso.with(context).load(cartItemList.get(position).getImgUrl()).into(viewHolder.img);
        viewHolder.tv_description.setText(cartItemList.get(position).getDescription());
        viewHolder.tv_price.setText(cartItemList.get(position).getPrice() + "元");
        viewHolder.tv_num.setText(String.valueOf(cartItemList.get(position).getNum()));
        viewHolder.checkBox.setChecked((mChecked.get(position) == true ? true : false));
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 if (mOnSelectItemListener!=null){
                     mOnSelectItemListener.onSelectItemListener(position,isChecked);
                 }
            }
        });
        viewHolder.tv_asc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnAscNumListener != null) {
                    mOnAscNumListener.onAscNumListener(position);
                }

            }
        });
        viewHolder.tv_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnDescNumListener != null) {
                    mOnDescNumListener.onDescNumListener(position);
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView tv_description;
        TextView tv_price;
        TextView tv_num;
        TextView tv_asc;
        TextView tv_desc;
        ImageView img;
        CheckBox checkBox;
    }

    public interface onSelectItemListener {
        public void onSelectItemListener(int pos, boolean isChecked);
    }

    public interface onAscNumListener {
        public void onAscNumListener(int pos);
    }

    public interface onDescNumListener {
        public void onDescNumListener(int pos);
    }


}
