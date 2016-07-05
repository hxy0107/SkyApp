package com.example.bluesky.app.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.bluesky.app.R;
import com.example.bluesky.app.adapter.MyDetailAdapter;
import com.example.bluesky.app.bean.CategoryList;
import com.example.bluesky.app.constants.Constants;
import com.example.bluesky.app.httpinterface.GetCategoryListData;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailListActivity extends AppCompatActivity {

    @BindView(R.id.gv_detail)
    GridView gridView;

    MyDetailAdapter myDetailAdapter;

    @BindView(R.id.main_toolbar)
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        toolbar.setTitle("列表详情");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        setSupportActionBar(toolbar);
        String key = getIntent().getStringExtra("key");
        getCategoryList(key);
    }

    @OnItemClick(R.id.gv_detail)
    public void itemClick(AdapterView<?> adapterView, View view, int pos, long id) {
        Intent intent = new Intent(this, ItemListDetailActivity.class);
        String sid = myDetailAdapter.getItem(pos).getComponent().getAction().getSourceId();
        intent.putExtra("id", sid);
        intent.putExtra("type","0");
        intent.putExtra("itembean",myDetailAdapter.getItem(pos));
        startActivity(intent);

    }

    public void getCategoryList(String key) {

        Retrofit rt = new Retrofit.Builder()
                .baseUrl(Constants.CATEGORY_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetCategoryListData categoryListData = rt.create(GetCategoryListData.class);
        //query={query}++&sort=all&ga=%2Fsearch%2Fskus&flag=&cat=&asc=1
        Call<CategoryList> categoryListCall =
                categoryListData.getCategoryData(
                        key,
                        "all",
                        "%2Fsearch%2Fskus",
                        "",
                        ""
                        , "1");

        categoryListCall.enqueue(new Callback<CategoryList>() {
            @Override
            public void onResponse(Call<CategoryList> call, Response<CategoryList> response) {
                myDetailAdapter = new MyDetailAdapter(response.body().getData().getItems(), DetailListActivity.this);
                gridView.setAdapter(myDetailAdapter);
            }

            @Override
            public void onFailure(Call<CategoryList> call, Throwable t) {

            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
