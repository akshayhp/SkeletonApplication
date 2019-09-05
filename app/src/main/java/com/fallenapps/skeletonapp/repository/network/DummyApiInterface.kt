package com.fallenapps.skeletonapp.repository.network

import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface DummyApiInterface {

    @GET("users?page=2")
    fun getList():
            Single<String>

}