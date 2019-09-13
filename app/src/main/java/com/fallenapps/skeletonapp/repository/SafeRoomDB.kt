package com.fallenapps.skeletonapp.repository

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.commonsware.cwac.saferoom.SafeHelperFactory
import com.fallenapps.skeletonapp.model.DataItem

class SafeRoomDB  constructor(val ctxt: Context, val passphrase: CharArray) {

    private  lateinit var db: SupportSQLiteDatabase

    fun open() {
        val factory = SafeHelperFactory(passphrase)
        val cfgBuilder = SupportSQLiteOpenHelper.Configuration.builder(ctxt)

        cfgBuilder
            .name("cache.db")
            .callback(object : SupportSQLiteOpenHelper.Callback(1) {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    db.execSQL("CREATE TABLE data (url_key String PRIMARY KEY , value TEXT);")
                }

                override fun onUpgrade(
                    db: SupportSQLiteDatabase, oldVersion: Int,
                    newVersion: Int
                ) {
                    throw RuntimeException("How did we get here?")
                }
            })

        db = factory.create(cfgBuilder.build()).writableDatabase
    }

    fun clear(){
        db.delete("data","1=1",null)
    }

    fun close() {
        if (db.isOpen)
            db.close()
    }

    fun find(url:String): DataItem? {
            val c = db.query("SELECT url_key, value FROM data WHERE url_key=?", arrayOf(url))

            return if (c.isAfterLast) {
                null
            } else {
                c.moveToFirst()
                val dataItm =DataItem(c.getString(0), c.getString(1))
                c.close()
                dataItm
            }
    }


        fun save(key: String,value: String): DataItem {
            val cv = ContentValues(1)

            cv.put("url_key", key)
            cv.put("value", value)

            return if (find(key)==null) {
                db.insert("data", SQLiteDatabase.CONFLICT_ABORT, cv)

                DataItem(key, value)
            } else {
                db.update(
                    "data", SQLiteDatabase.CONFLICT_REPLACE, cv, "url_key=?",
                    arrayOf(key)
                )

                DataItem( key, value)
            }
        }
    }
