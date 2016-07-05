package com.example.bluesky.app.httpinterface;

import com.example.bluesky.app.bean.ListItems;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by bluesky on 16/6/16.
 */
public interface GetCategoryData1 {
    @GET("category/list?ga=%2Fcategory%2Flist")
    Call<ListItems> getCategoryData();
}
