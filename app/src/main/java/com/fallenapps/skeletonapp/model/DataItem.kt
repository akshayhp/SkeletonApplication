package com.fallenapps.skeletonapp.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

 open  class DataItem(@PrimaryKey var url:String = "",var body:String = ""):RealmObject()