package com.flj.latte.net;

import java.util.List;
import java.util.WeakHashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by yb
 */
public interface RestService {


    //final static String token="eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJlY3Nob3AiLCJ1c2VySWQiOiIxIiwibmFtZSI6IiIsImV4cCI6MTUyNzMyOTUzNX0.EugbITYUy09Zw2YzqHM7YZWFTvhsJbCSs36FXXwYug-t1af0KOlFUj2glea8Rdky8ycBFSCrPTRvF6KyROCTX_a7g9hdzLFe8xzvTdGM9t_PavRDsNMjEufoNkWNd1g2URri2HUbrqUkixd9guPn7i9VEAp7Rf2sZA2rVTCp-_w";

    @GET
    Call<String> get(@Url String url, @QueryMap WeakHashMap<String, Object> params);

    @FormUrlEncoded
    @POST
//    @Headers({
//            "Authorization:"+token,
//            "Content-Type: application/json;charset=UTF-8"
//    })
    Call<String> post(@Url String url, @FieldMap WeakHashMap<String, Object> params);

    @POST
//    @Headers({
//            "Authorization:"+token,
//            "Content-Type: application/json;charset=UTF-8"
//    })
    Call<String> postRaw(@Url String url, @Body RequestBody body);

    @FormUrlEncoded
    @PUT
    Call<String> put(@Url String url, @FieldMap WeakHashMap<String, Object> params);

    @PUT
    Call<String> putRaw(@Url String url, @Body RequestBody body);

    @DELETE
    Call<String> delete(@Url String url, @QueryMap WeakHashMap<String, Object> params);

    @Streaming
    @GET
    Call<ResponseBody> download(@Url String url, @QueryMap WeakHashMap<String, Object> params);

    @Multipart
    @POST
    Call<String> upload(@Url String url, @Part MultipartBody.Part file);

    @Multipart
    @POST
    Call<String> upLoadFiles(@Url String url, @Part List<MultipartBody.Part> files);
}
