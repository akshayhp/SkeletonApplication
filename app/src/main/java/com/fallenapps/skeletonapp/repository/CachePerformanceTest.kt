package com.fallenapps.skeletonapp.repository

import android.util.Log
import com.fallenapps.skeletonapp.model.ApiResponse
import com.fallenapps.skeletonapp.repository.network.DummyApiInterface
import com.fallenapps.skeletonapp.repository.network.MyCacheInterceptor
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.Result
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class CachePerformanceTest(val persistantRepo:PersistantCacheRepository?){

      private val  retrofitClient:Retrofit by lazy {

          val okHttpBuilder = OkHttpClient.Builder()

              okHttpBuilder.addInterceptor(MyCacheInterceptor.create(persistantRepo))
          okHttpBuilder.cache(null)
          Retrofit.Builder()
              .client(
                  okHttpBuilder.addInterceptor{chain -> Log.d("url",chain.request().url.toString())
                                            chain.proceed(chain.request())}.build())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
              .baseUrl("https://dummyapi.io/api/")

              .addConverterFactory(ScalarsConverterFactory.create()).build()
    }


    private val apiService:DummyApiInterface by lazy {
        retrofitClient.create(DummyApiInterface::class.java)
    }
    fun getList():Single<Response<String>>{
        return Single.just("")
            .flatMap {  apiService.getList() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getTags():Single<Response<String>>{
        return Single.just("")
            .flatMap {  apiService.getTags() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


}