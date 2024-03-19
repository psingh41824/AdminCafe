package com.swi.admincafe.api.repository

import android.content.Context
import android.net.Uri
import com.swi.admincafe.api.model.Category
import com.swi.admincafe.api.myApplication.MyApplication
import okhttp3.MultipartBody
import okhttp3.RequestBody


class AppRepository( applicationContext: Context) {

    var apiService = (applicationContext as MyApplication).apiService

    suspend fun getCategory()=apiService.getCategory()
    suspend fun insertCategory(image: MultipartBody.Part,name : RequestBody)=apiService.insertCategory(image,name)
    //suspend fun insertCategory(category: Category) =apiService.insertCategory(category)
    suspend fun getBannerImg()=apiService.getBannerImg()
    suspend fun insertBanner(banner:MultipartBody.Part,offer:RequestBody,offerDate:RequestBody) = apiService.insertBanner(banner,offer,offerDate)
    suspend fun getProduct()=apiService.getProduct()
}

