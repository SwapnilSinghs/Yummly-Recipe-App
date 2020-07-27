package com.swapnil.yummly_proj.activity;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RequestInterface {

    @GET("search?key=53addf3a237a42de7d7855e2a4c19d8c")
    Call<JSONResponse> getJSON();
}//turns your HTTP API into a Java interface.


