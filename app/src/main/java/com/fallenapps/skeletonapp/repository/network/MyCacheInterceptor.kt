package com.fallenapps.skeletonapp.repository.network

import com.fallenapps.skeletonapp.repository.CacheRepository
import com.fallenapps.skeletonapp.repository.SafeRoomCacheRepository
import okhttp3.*
import java.net.HttpURLConnection
import okhttp3.ResponseBody.Companion.toResponseBody

class MyCacheInterceptor private constructor(private val cacheRepository: CacheRepository?) : Interceptor{


    companion object{
        fun create(persistantStorageRepository: SafeRoomCacheRepository?):MyCacheInterceptor{
            return MyCacheInterceptor(persistantStorageRepository)
        }
    }
    override fun intercept(chain: Interceptor.Chain): Response {

        if(cacheRepository == null){
            return connectToNetwork(chain)
        }

        val cache = cacheRepository.find(chain.request().url.toString())
        return  if(cache is CacheRepository.DataResult.Success) {
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

            connectToNetwork(chain,true)
       }
    }

    private fun connectToNetwork(chain : Interceptor.Chain, save:Boolean = false) : Response{
        val response = chain.proceed(chain.request())
        val url = chain.request().url.toString()
        val resBody = ""+response.body?.string()
        if (save && resBody!=null && resBody.isNotBlank() && response.code == HttpURLConnection.HTTP_OK) {
            cacheRepository?.write(
                url,
                resBody
            )
        }
        return response.newBuilder().addHeader("from-cache-source","online").body(resBody.toResponseBody()).build()
    }

}