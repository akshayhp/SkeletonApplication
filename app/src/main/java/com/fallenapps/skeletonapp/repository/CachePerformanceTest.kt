package com.fallenapps.skeletonapp.repository

import android.util.Log
import com.fallenapps.skeletonapp.repository.network.DummyApiInterface
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class CachePerformanceTest{

      private val  retrofitClient:Retrofit by lazy {
          Retrofit.Builder().client(OkHttpClient.Builder().addInterceptor{chain -> Log.d("url",chain.request().url.toString())
           chain.proceed(chain.request())}.build()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).baseUrl("https://reqres.in/api/").addConverterFactory(ScalarsConverterFactory.create()).build()
    }
    private val apiService:DummyApiInterface by lazy {
        retrofitClient.create(DummyApiInterface::class.java)
    }
    fun getList():Single<String>{
        return Single.just("").flatMap {  apiService.getList() }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
}