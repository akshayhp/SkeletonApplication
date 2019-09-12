package com.fallenapps.skeletonapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.fallenapps.skeletonapp.R
import com.fallenapps.skeletonapp.model.ControllerModel
import com.fallenapps.skeletonapp.repository.SafeRoomRepository
import com.fallenapps.skeletonapp.viewmodel.ApiPerformanceViewModel
import kotlinx.android.synthetic.main.activity_api_performance.*
import kotlinx.android.synthetic.main.layout_controls.*


class ApiPerformanceActivity : AppCompatActivity(){

    private lateinit var apiPerformanceViewModel:ApiPerformanceViewModel



    private val controllerSatateObserver = Observer<ControllerModel> {

        switch1.isChecked = it.isApiSmall
        switch1.isChecked = it.isCacheEnable
        switch1.isChecked = it.isRealm

        button_test.isEnabled = it.isReady
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api_performance)
        apiPerformanceViewModel = ApiPerformanceViewModel.Factory(application,SafeRoomRepository(this,"PASS-PHRASE".toCharArray())).create(ApiPerformanceViewModel::class.java)
        initialSetup()
    }

    private fun initialSetup() {

        recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        recyclerView.adapter = TestRunAdapter(apiPerformanceViewModel.liveData.value!!)

        button_test.setOnClickListener{apiPerformanceViewModel.request()}
        button_clear.setOnClickListener{apiPerformanceViewModel.clearCache()}

        switch1.setOnCheckedChangeListener{it1,it2->apiPerformanceViewModel.toggleApiSize()}
        switch2.setOnCheckedChangeListener{it1,it2->apiPerformanceViewModel.toggleCache()}
        switch3.setOnCheckedChangeListener{it1,it2->apiPerformanceViewModel.toggleDb()}

    }

    override fun onStart() {
        super.onStart()
        apiPerformanceViewModel.init()
    }
    override fun onResume() {
        super.onResume()
        recyclerView.adapter!!.notifyDataSetChanged()

        apiPerformanceViewModel.controlerState.observe(this,controllerSatateObserver)
        apiPerformanceViewModel.liveData.observe(this, Observer {
            recyclerView.adapter!!.notifyDataSetChanged()
        })

    }

    override fun onPause() {
        super.onPause()

        apiPerformanceViewModel.controlerState.removeObserver(controllerSatateObserver)
        apiPerformanceViewModel.liveData.removeObservers(this)
    }


    override fun onStop() {
        super.onStop()
        apiPerformanceViewModel.terminate()
    }
}
