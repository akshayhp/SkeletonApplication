package com.fallenapps.skeletonapp.repository

import android.util.Base64
import java.nio.charset.Charset

class SafeRoomCacheRepository(private val safeRoom:SafeRoomDB): CacheRepository {
    override fun open() {
        safeRoom.open()
    }

    override fun close() {
        safeRoom.close()
    }

    override fun clear() {
        safeRoom.clear()
    }

    override fun write(url:String, body:String){
        safeRoom.save(Base64.encodeToString(url.toByteArray(Charset.defaultCharset()),Base64.DEFAULT),body)
    }

    override fun find(url:String): CacheRepository.DataResult {

        val dataItem = safeRoom.find(Base64.encodeToString(url.toByteArray(Charset.defaultCharset()),Base64.DEFAULT))
        return  if(dataItem==null){
             CacheRepository.DataResult.Error("not found")
        }else{
            CacheRepository.DataResult.Success(dataItem.body)
        }
    }
}