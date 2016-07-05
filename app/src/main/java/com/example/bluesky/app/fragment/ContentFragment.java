package com.example.bluesky.app.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.bluesky.app.R;
import com.example.bluesky.app.adapter.ListItemAdapter;
import com.example.bluesky.app.bean.ListItems;
import com.example.bluesky.app.constants.Constants;
import com.example.bluesky.app.event.NotifySelectedItemEvent;
import com.example.bluesky.app.httpinterface.GetCategoryData;
import com.example.bluesky.app.httpinterface.GetCategoryData1;
import com.google.gson.Gson;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import de.greenrobot.event.EventBus;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContentFragment extends Fragment {

    @BindView(R.id.listView1)
    ListView listView;

    ListItemAdapter adapter;


    public ContentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        ButterKnife.bind(this, view);
         //分类面板
        getFragmentManager().beginTransaction().replace(R.id.categorylayout, new CategoryFragment()).commit();
        getData2();
        return view;
    }

    public void getData2() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.CATEGORY_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetCategoryData1 data1 = retrofit.create(GetCategoryData1.class);
        final Call<ListItems> listItemsCall = data1.getCategoryData();
        listItemsCall.enqueue(new Callback<ListItems>() {
            @Override
            public void onResponse(Call<ListItems> call, Response<ListItems> response) {

                adapter = new ListItemAdapter(response.body().getData().getItems(), getActivity());
                listView.setAdapter(adapter);
                Intent intent = new Intent();
                intent.setAction("com.example.bluesky.listcheckbox");
                intent.putExtra("component", adapter.getItem(0).getComponent());
                if (getActivity()!=null) {
                    getActivity().sendBroadcast(intent);
                }

            }

            @Override
            public void onFailure(Call<ListItems> call, Throwable t) {

            }
        });

    }

    @OnItemClick(R.id.listView1)
    public void itemClick(AdapterView<?> adapterView, View view, int pos, long id) {
        Intent intent = new Intent();
        intent.setAction("com.example.bluesky.listcheckbox");
        intent.putExtra("component", adapter.getItem(pos).getComponent());
        getActivity().sendBroadcast(intent);
        EventBus.getDefault().post(new NotifySelectedItemEvent(pos));
    }



    public void getData1() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.CATEGORY_BASE)
                .build();
        GetCategoryData data = retrofit.create(GetCategoryData.class);
        Call<ResponseBody> listItemsCall = data.getCategoryData();
        listItemsCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Gson gson = new Gson();
                    ListItems listItems = gson.fromJson(response.body().string(), ListItems.class);
                    listView.setAdapter(new ListItemAdapter(listItems.getData().getItems(), getActivity()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });

    }
}
