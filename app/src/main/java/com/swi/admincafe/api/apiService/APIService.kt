package com.swi.admincafe.api.apiService

import com.swi.admincafe.api.model.BannerResponseModel
import com.swi.admincafe.api.model.CategoryResponseModel
import com.swi.admincafe.api.model.Category
import com.swi.admincafe.api.model.ProductResponseModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface APIService {

    @GET("/api/category")
    suspend fun getCategory(): Response<CategoryResponseModel>

 /*   @POST("/api/category")
    suspend fun insertCategory(@Body category: Category) : Response<CategoryResponseModel>*/

    @Multipart
    @POST("/api/category")
    suspend fun insertCategory(@Part image: MultipartBody.Part,
                               @Part("name") name : RequestBody ): Response<CategoryResponseModel>

    @GET("/api/banner")
    suspend fun getBannerImg(): Response<BannerResponseModel>

    @Multipart
    @POST("/api/banner")
    suspend fun insertBanner(@Part banner: MultipartBody.Part,
                             @Part("offer") offer : RequestBody,
                             @Part("offerDate") offerDate : RequestBody): Response<BannerResponseModel>

    @Multipart
    @POST("/api/product")
    suspend fun insertProduct(@Part("category") category : RequestBody,
                           @Part("description") description : RequestBody,
                           @Part image: MultipartBody.Part,
                           @Part("name") name : RequestBody,
                           @Part("numReviews") numReviews : RequestBody,
                           @Part("price") price : RequestBody,
                           @Part("rating") rating : RequestBody,
                           @Part("size") size: RequestBody): Response<ProductResponseModel>



}