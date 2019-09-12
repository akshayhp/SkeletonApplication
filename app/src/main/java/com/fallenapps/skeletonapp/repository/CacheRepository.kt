package com.fallenapps.skeletonapp.repository

interface CacheRepository {

    fun write(key:String, value:String)
    fun find(key:String): DataResult

    // Definition
    sealed class DataResult {
        data class Success(val value: String) : DataResult()
        data class Error(val value: String) : DataResult()
    }
}