package com.fallenapps.skeletonapp.repository

import android.util.Base64
import java.nio.charset.Charset
import java.util.*

class PersistantCacheRepository(val safeRoom:SafeRoomRepository) {


    // Definition
    sealed class DataResult {
        data class Success(val value: String) : DataResult()
        data class Error(val value: String) : DataResult()
    }

    fun write(url:String,body:String){

        safeRoom.save(Base64.encodeToString(url.toByteArray(Charset.defaultCharset()),Base64.DEFAULT),body)
    }

    fun read(url:String):DataResult{

        val dataItem = safeRoom.find(Base64.encodeToString(url.toByteArray(Charset.defaultCharset()),Base64.DEFAULT))
        return  if(dataItem==null){
             DataResult.Error("not found")
        }else{
            DataResult.Success(dataItem.value)
        }
    }
}