package com.fallenapps.skeletonapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.fallenapps.skeletonapp.model.ApiResponse
import com.fallenapps.skeletonapp.model.ControllerModel
import com.fallenapps.skeletonapp.repository.CachePerformanceTest
import com.fallenapps.skeletonapp.repository.MyCacheRepository
import com.fallenapps.skeletonapp.repository.SafeRoomDB



class ApiPerformanceViewModel(app:Application) : AndroidViewModel(app) {


     private var myCacheRepository:MyCacheRepository? = null

    private val mLiveData: MutableLiveData<ArrayList<ApiResponse>> = MutableLiveData()
    private val mControllerState = MutableLiveData<ControllerModel>()

    val liveData: LiveData<ArrayList<ApiResponse>> = mLiveData
    val controllerState: LiveData<ControllerModel> = mControllerState

    init {
        mLiveData.value = ArrayList()
        mControllerState.value = ControllerModel(
            isApiSmall = false,
            isCacheEnable = false,
            isRealm = false,
            isReady = true
        )
    }


    fun request() {
        mControllerState.value!!.isReady=false
        mControllerState.postValue(mControllerState.value)
        val sentTime = System.currentTimeMillis()

        if(myCacheRepository==null){
            myCacheRepository = MyCacheRepository(mControllerState.value!!.isRealm,getApplication())
        }
        var repo = CachePerformanceTest(if(mControllerState.value!!.isCacheEnable) myCacheRepository else null)
            val observer = if(mControllerState.value!!.isApiSmall)repo.getTags()else repo.getList()
        val disposable = observer.subscribe({it->
            val time = (it.raw().receivedResponseAtMillis-sentTime)
            mLiveData.value!!.add(
                ApiResponse(it.headers()["from-cache-source"]+"", "${it.body()!!.toByteArray().size}b","$time ms")
            )
            mLiveData.postValue(mLiveData.value)
            mControllerState.value!!.isReady=true
            mControllerState.postValue(mControllerState.value)

        },{
            Log.d("error",""+it.message)
            it.printStackTrace()
            mControllerState.value!!.isReady=true
            mControllerState.postValue(mControllerState.value)

        })

    }



    fun clearCache(){
            myCacheRepository = MyCacheRepository(mControllerState.value!!.isRealm,getApplication())
            myCacheRepository?.open()
        myCacheRepository?.clear()
        myCacheRepository?.close()

        mLiveData.value!!.clear()
        mLiveData.postValue(mLiveData.value)
    }


    fun terminate() {
//        myCacheRepository?.close()
//        myCacheRepository=null
    }

    fun toggleApiSize() {
        mControllerState.value!!.isApiSmall=!mControllerState.value!!.isApiSmall
    }

    fun toggleCache() {
        mControllerState.value!!.isCacheEnable=!mControllerState.value!!.isCacheEnable
    }

    fun toggleDb() {
        mControllerState.value!!.isRealm=!mControllerState.value!!.isRealm
    }


    class Factory(val app: Application, val safeRoom: SafeRoomDB) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ApiPerformanceViewModel::class.java)) {
                return ApiPerformanceViewModel(app) as T
            }

            return modelClass.newInstance()
        }
    }



}