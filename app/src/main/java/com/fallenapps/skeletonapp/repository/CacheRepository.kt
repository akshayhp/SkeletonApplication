package com.fallenapps.skeletonapp.repository

interface CacheRepository {

    fun write(url:String, body:String)
    fun find(url:String): DataResult
    fun open()
    fun close()
    fun clear()

    // Definition
    sealed class DataResult {
        data class Success(val body: String) : DataResult()
        data class Error(val body: String) : DataResult()
    }
}