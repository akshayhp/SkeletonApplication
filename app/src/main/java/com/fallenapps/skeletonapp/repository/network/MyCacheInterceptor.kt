package com.fallenapps.skeletonapp.repository.network

import android.util.Log
import com.fallenapps.skeletonapp.repository.PersistantCacheRepository
import okhttp3.*
import java.net.HttpURLConnection
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.internal.toHeaderList

class MyCacheInterceptor private constructor(val persistantStorageRepository: PersistantCacheRepository) : Interceptor{


    companion object{
        fun create(persistantStorageRepository: PersistantCacheRepository):MyCacheInterceptor{
            return MyCacheInterceptor(persistantStorageRepository)
        }
    }
    override fun intercept(chain: Interceptor.Chain): Response {

        val cache = persistantStorageRepository.read(chain.request().url.toString())

       // return   chain.proceed(chain.request())

        //val cache = persistantStorageRepository.read(chain.request().url.toString())
       return  if(cache is PersistantCacheRepository.DataResult.Success) {

             Response.Builder()
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .code(HttpURLConnection.HTTP_OK)
                .addHeader("from-cache-source","cache")
                .message("")
                .body(cache.value.toResponseBody())
                .sentRequestAtMillis(-1L)
                .receivedResponseAtMillis(System.currentTimeMillis())
                .build()
        }else {


           val response = chain.proceed(chain.request())
           val url = chain.request().url.toString()

           val resBody = ""+response.body!!.string()

           Log.d("response body",resBody)

           if (response.code == HttpURLConnection.HTTP_OK) {
               persistantStorageRepository.write(
                   url,
                   resBody
               )
           }

           response.newBuilder().addHeader("from-cache-source","online").body(resBody.toResponseBody()).build()

       }
    }


}