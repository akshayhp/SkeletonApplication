package com.fallenapps.skeletonapp.repository

import com.fallenapps.skeletonapp.model.DataItem
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmConfiguration



class RealmDb{



    private lateinit var db: Realm

    private val config by lazy {
        // Generate a random encryption key
        val key = "1111111111111111111111111111111111111111111111111111111111111111".toByteArray()

// Open the encrypted Realm file
         RealmConfiguration.Builder()
            .encryptionKey(key)
            .build()
    }


    fun open() {

        Single.just("").observeOn(Schedulers.io()).map {         db = Realm.getInstance(config)
        }.subscribe()
    }

    fun clear(){
        Single.just("").observeOn(Schedulers.io()).map {                 db.executeTransaction{db.deleteAll()}

        }.subscribe()
    }

    fun close() {
        if (!db.isClosed)
            Single.just("").observeOn(Schedulers.io()).map {                 db.close()}.subscribe()

    }

    fun find(url:String): DataItem? {
      return db.where(DataItem::class.java).contains("key", url).findFirst()
    }

    fun save(key: String,value: String): DataItem {
        val di = DataItem(key, value)
         db.insertOrUpdate(di)
        return di
        }


}