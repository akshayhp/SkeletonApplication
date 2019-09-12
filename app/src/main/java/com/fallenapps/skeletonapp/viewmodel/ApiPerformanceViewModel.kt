package com.fallenapps.skeletonapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.fallenapps.skeletonapp.model.ApiResponse
import com.fallenapps.skeletonapp.model.ControllerModel
import com.fallenapps.skeletonapp.repository.CachePerformanceTest
import com.fallenapps.skeletonapp.repository.PersistantCacheRepository
import com.fallenapps.skeletonapp.repository.SafeRoomRepository



class ApiPerformanceViewModel(app:Application, val safeRoom:SafeRoomRepository) : AndroidViewModel(app) {

    private val mLiveData: MutableLiveData<ArrayList<ApiResponse>> = MutableLiveData()
    private val mControlerState = MutableLiveData<ControllerModel>()

    val liveData: LiveData<ArrayList<ApiResponse>> = mLiveData
    val controlerState: LiveData<ControllerModel> = mControlerState

    init {
        mLiveData.value = ArrayList()
        mControlerState.value = ControllerModel(
            isApiSmall = false,
            isCacheEnable = false,
            isRealm = false,
            isReady = true
        )
    }


    fun request() {

        mControlerState.value!!.isReady=false
        val sentTime = System.currentTimeMillis()
        var repo = CachePerformanceTest(if(mControlerState.value!!.isCacheEnable) PersistantCacheRepository(safeRoom) else null)

            val observer = if(mControlerState.value!!.isApiSmall)repo.getTags()else repo.getList()


        val disposable = observer.subscribe({it->

            val time = (it.raw().receivedResponseAtMillis-sentTime)
            mLiveData.value!!.add(
                ApiResponse(it.headers()["from-cache-source"]+"", "${it.body()!!.toByteArray().size}b","$time ms")
            )
                mLiveData.postValue(mLiveData.value)
                mControlerState.value!!.isReady=true
        },{
            Log.d("error",it.message)
            it.printStackTrace()
            mControlerState.value!!.isReady=true
        })

    }


    fun init() {
        safeRoom.open()
        safeRoom.clear()
    }

    fun clearCache(){
        safeRoom.clear()
        mLiveData.value!!.clear()
        mLiveData.postValue(mLiveData.value)


    }


    fun terminate() {
        safeRoom.close()
    }

    fun toggleApiSize() {
        mControlerState.value!!.isApiSmall=!mControlerState.value!!.isApiSmall
    }

    fun toggleCache() {
        mControlerState.value!!.isCacheEnable=!mControlerState.value!!.isCacheEnable
    }

    fun toggleDb() {
        mControlerState.value!!.isRealm=!mControlerState.value!!.isRealm
    }


    class Factory(val app: Application, val safeRoom: SafeRoomRepository) :
        ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ApiPerformanceViewModel::class.java)) {
                return ApiPerformanceViewModel(app, safeRoom) as T
            }

            return modelClass.newInstance()
        }
    }



}