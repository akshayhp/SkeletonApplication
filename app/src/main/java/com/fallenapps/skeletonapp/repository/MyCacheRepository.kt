package com.fallenapps.skeletonapp.repository

import android.app.Application

class MyCacheRepository( useRealm:Boolean, app:Application) :CacheRepository {

    val db = if(useRealm) RealmCacheRepository() else SafeRoomCacheRepository(SafeRoomDB(app))

    override fun open() {
        db.open()
    }

    override fun close() {
        db.close()
    }

    override fun clear() {
        db.clear()
    }
    override fun write(url: String, body: String) {
        db.write(url,body)
    }

    override fun find(url: String): CacheRepository.DataResult {
        return db.find(url)
    }
}