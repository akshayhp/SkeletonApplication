package com.fallenapps.skeletonapp.repository

class RealmCacheRepository : CacheRepository {
    val db = RealmDb()

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
        db.save(url,body)
    }

    override fun find(url: String): CacheRepository.DataResult {
        var dataItem = db.find(url)
        return  if(dataItem==null){
            CacheRepository.DataResult.Error("not found")
        }else{
            CacheRepository.DataResult.Success(dataItem.body)
        }
    }
}