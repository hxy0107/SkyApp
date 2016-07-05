package com.example.bluesky.app.httpinterface;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by bluesky on 16/6/16.
 */
public interface GetCategoryData {
    @GET("category/list?ga=%2Fcategory%2Flist")
    Call<ResponseBody> getCategoryData();

}
