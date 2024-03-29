package com.swi.admincafe.api.repository

import android.content.Context
import android.net.Uri
import com.swi.admincafe.api.model.Category
import com.swi.admincafe.api.myApplication.MyApplication
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part


class AppRepository( applicationContext: Context) {

    var apiService = (applicationContext as MyApplication).apiService

    suspend fun getCategory()=apiService.getCategory()
    suspend fun insertCategory(image: MultipartBody.Part,name : RequestBody)=apiService.insertCategory(image,name)
    //suspend fun insertCategory(category: Category) =apiService.insertCategory(category)
    suspend fun getBannerImg()=apiService.getBannerImg()
    suspend fun insertBanner(banner:MultipartBody.Part,offer:RequestBody,offerDate:RequestBody) = apiService.insertBanner(banner,offer,offerDate)
    suspend fun insertProduct(category : RequestBody, description : RequestBody, image: MultipartBody.Part, name : RequestBody,
                           numReviews : RequestBody,
                           price : RequestBody,
                           rating : RequestBody,
                           size: RequestBody)=apiService.insertProduct(category,description,image,name,numReviews,price,rating,size)
}

