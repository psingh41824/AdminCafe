package com.swi.admincafe.api.myApplication

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.swi.admincafe.api.apiService.APIService
import com.swi.admincafe.api.apiService.RetrofitHelper

class MyApplication:Application(),LifecycleObserver {
    var mInstance: MyApplication? = null
    lateinit var apiService: APIService

    @Synchronized
    fun getInstance(): MyApplication? {
        return mInstance
    }

    override fun onCreate() {
        super.onCreate()

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        apiService = RetrofitHelper.getInstance(applicationContext)
        mInstance=this

    }
}