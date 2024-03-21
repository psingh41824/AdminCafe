package com.swi.admincafe.api.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.swi.admincafe.R
import com.swi.admincafe.api.model.BannerResponseModel
import com.swi.admincafe.api.model.CategoryResponseModel
import com.swi.admincafe.api.model.ProductResponseModel
import com.swi.admincafe.api.networkUtils.NetworkUtils
import com.swi.admincafe.api.repository.AppRepository
import com.swi.admincafe.api.response.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.net.SocketException


class LoginViewModel(
    app: Application, private val repository: AppRepository
) : AndroidViewModel(app) {

    private val _categoryLiveData = MutableLiveData<Response<CategoryResponseModel>>()
    val categoryLiveData: LiveData<Response<CategoryResponseModel>>
        get() = _categoryLiveData

    private val _bannerLiveData = MutableLiveData<Response<BannerResponseModel>>()
    val bannerLiveData: LiveData<Response<BannerResponseModel>>
        get() = _bannerLiveData

    private val _productLiveData = MutableLiveData<Response<ProductResponseModel>>()
    val productLiveData: LiveData<Response<ProductResponseModel>>
        get() = _productLiveData


    fun bannerImg() = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            _bannerLiveData.postValue(Response.Loading())
            val result = repository.getBannerImg()
            if (result.body() != null) {
                _bannerLiveData.postValue(Response.Success(result.body()))
            } else {
                _bannerLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.msg_improper_response_server)))
            }
        } else {
            _bannerLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.internet_connection)))
        }
    }

    fun insertBanner(banner:MultipartBody.Part,offer:RequestBody,offerDate:RequestBody) {
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {

                _bannerLiveData.postValue(Response.Loading())
                val result = repository.insertBanner(banner, offer, offerDate)
                if (result.body() != null) {
                    _bannerLiveData.postValue(Response.Success(result.body()))
                } else {
                    _bannerLiveData.postValue(Response.Error(getApplication<Application>().getString(
                        R.string.msg_improper_response_server
                    )))
                }
            } else {
                _bannerLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.internet_connection)))
            }
        }
    }

    fun insertProduct(category : RequestBody, description : RequestBody, image: MultipartBody.Part, name : RequestBody,
                      numReviews : RequestBody, price : RequestBody, rating : RequestBody, size: RequestBody) = viewModelScope.launch(Dispatchers.IO) {
        if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
            _productLiveData.postValue(Response.Loading())
            val result = repository.insertProduct(category,description,image,name,numReviews,price,rating,size)
            if (result.body() != null) {
                _productLiveData.postValue(Response.Success(result.body()))
            } else {
                _productLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.msg_improper_response_server)))
            }
        } else {
            _productLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.internet_connection)))
        }
    }

    fun categoryData() =
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                    _categoryLiveData.postValue(Response.Loading())
                    val result = repository.getCategory()
                    if (result.isSuccessful) {
                        val responseData = result.body()
                        if (responseData != null) {
                            _categoryLiveData.postValue(Response.Success(responseData))
                        } else {
                            _categoryLiveData.postValue(
                                Response.Error(
                                    getApplication<Application>().getString(
                                        R.string.msg_improper_response_server
                                    )
                                )
                            )
                        }
                    } else {
                        _categoryLiveData.postValue(
                            Response.Error(
                                getApplication<Application>().getString(
                                    R.string.msg_improper_response_server
                                )
                            )
                        )
                    }
                } else {
                    _categoryLiveData.postValue(
                        Response.Error(
                            getApplication<Application>().getString(
                                R.string.internet_connection
                            )
                        )
                    )
                }
            } catch (e: SocketException) {
                // Handle socket closed exception
                _categoryLiveData.postValue(Response.Error("Network error: ${e.message}"))
            } catch (e: Exception) {
                // Handle other exceptions
                _categoryLiveData.postValue(Response.Error("An error occurred: ${e.message}"))
            }
        }

   /* fun insertCategory(category: Category) =
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {
                _categoryLiveData.postValue(Response.Loading())
                val result = repository.insertCategory(category)
                if (result.body() != null) {
                    _categoryLiveData.postValue(Response.Success(result.body()))
                } else {
                    _categoryLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.msg_improper_response_server)))
                }
            } else {
                _productLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.internet_connection)))
            }
        }*/

    fun insertCategory(image: MultipartBody.Part, name : RequestBody) {
        viewModelScope.launch(Dispatchers.IO) {
            if (NetworkUtils.isInternetAvailable(getApplication<Application>())) {

                _categoryLiveData.postValue(Response.Loading())
                val result = repository.insertCategory(image, name)
                if (result.body() != null) {
                    _categoryLiveData.postValue(Response.Success(result.body()))
                } else {
                    _categoryLiveData.postValue(Response.Error(getApplication<Application>().getString(
                                R.string.msg_improper_response_server
                            )))
                }
            } else {
                _categoryLiveData.postValue(Response.Error(getApplication<Application>().getString(R.string.internet_connection)))
            }
        }
    }
}
