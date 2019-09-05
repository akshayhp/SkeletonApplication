package com.fallenapps.skeletonapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.fallenapps.skeletonapp.repository.CachePerformanceTest
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

class ApiPerformanceActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api_performance)
    }

    override fun onResume() {
        super.onResume()
        CachePerformanceTest().getList().subscribe({
            Log.d("response",""+it)

        },{e-> e.printStackTrace()}
        )
    }
}
