package com.fallenapps.skeletonapp.repository.network

import io.reactivex.Single
import retrofit2.http.GET

interface DummyApiInterface {

    @GET("post")
    fun getList():
            Single<retrofit2.Response<String>>

}