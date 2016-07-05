package com.example.bluesky.app.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.bluesky.app.R;
import com.example.bluesky.app.activity.DetailListActivity;
import com.example.bluesky.app.adapter.MyGridAdapter;
import com.example.bluesky.app.bean.ListItems;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    MyReceiver myReceiver;

    @BindView(R.id.gv)
    GridView gridView;

    MyGridAdapter myGridAdapter;

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.bluesky.listcheckbox");
        getActivity().registerReceiver(myReceiver, filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, null);
        ButterKnife.bind(this, view);
        return view;
    }

    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals("com.example.bluesky.listcheckbox")) {
                ListItems.DataBean.ItemsBean.ComponentBean componentBean =
                        (ListItems.DataBean.ItemsBean.ComponentBean)
                                intent.getSerializableExtra("component");
                List<ListItems.DataBean.ItemsBean.ComponentBean.ItemsBeans>
                        itemsBeanses = componentBean.getItems();
                myGridAdapter = new MyGridAdapter(itemsBeanses, getActivity());
                gridView.setAdapter(myGridAdapter);
            }
        }
    }

    @OnItemClick(R.id.gv)
    public void onItemClickByGv(AdapterView<?> adapterView,
                                View view,
                                int pos,
                                long id) {
        ListItems.DataBean.ItemsBean.ComponentBean.ItemsBeans itemsBeans = myGridAdapter.getItem(pos);
        String query = itemsBeans.getComponent().getAction().getQuery();
        Intent intent = new Intent(getActivity(), DetailListActivity.class);
        intent.putExtra("key", query);
        startActivity(intent);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(myReceiver);
    }
}
