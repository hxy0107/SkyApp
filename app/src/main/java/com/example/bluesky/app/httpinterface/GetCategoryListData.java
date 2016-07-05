package com.example.bluesky.app.httpinterface;

import com.example.bluesky.app.bean.CategoryList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by bluesky on 16/6/16.
 * //%E8%BF%9E%E8%A1%A3%E8%A3%99
 * query={query}++&sort=all&ga=%2Fsearch%2Fskus&flag=&cat=&asc=1
 */
//
public interface GetCategoryListData {
    @GET("search/skus")
    Call<CategoryList> getCategoryData(
            @Query("query") String query,
            @Query("sort") String sort,
            @Query("ga") String ga,
            @Query("flag") String flag,
            @Query("cat") String cat,
            @Query("asc") String asc);

}
