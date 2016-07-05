package com.example.bluesky.app.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bluesky.app.R;
import com.example.bluesky.app.ShoppingApplication;
import com.example.bluesky.app.activity.ItemListDetailActivity;
import com.example.bluesky.app.adapter.StorageAdapter;
import com.example.bluesky.app.bean.StorageItem;

import org.xutils.ex.DbException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class StorageFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    List<StorageItem> list;
    public StorageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_storage, container, false);
        ButterKnife.bind(this, view);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);


        try {
            list = ShoppingApplication.dbManager.findAll(StorageItem.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        final StorageAdapter adapter = new StorageAdapter(list, getActivity());
        recyclerView.setAdapter(adapter);
        adapter.setmOnItemClickListener(new StorageAdapter.onItemClickListener() {
            @Override
            public void OnItemClickListener(int pos) {
                Intent intent = new Intent(getActivity(), ItemListDetailActivity.class);
                StorageItem storageItem =   list.get(pos);
                intent.putExtra("type","1");
                intent.putExtra("storageItem",storageItem);
                startActivity(intent);

            }
        });
        adapter.setmOnItemLongClickListener(new StorageAdapter.onItemLongClickListener() {
            @Override
            public void OnItemLongClickListener(final int pos) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("提示信息");
                builder.setMessage("是否删除该条信息");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            ShoppingApplication.dbManager.deleteById(StorageItem.class,list.get(pos).getId());
                            adapter.remove(pos);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }


                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
        return view;
    }




}
