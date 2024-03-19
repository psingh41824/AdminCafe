package com.swi.admincafe.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.swi.admincafe.MainActivity
import com.swi.admincafe.R
import com.swi.admincafe.api.model.BannerResponseModel
import com.swi.admincafe.api.model.CategoryResponseModel
import com.swi.admincafe.api.model.Category
import com.swi.admincafe.api.model.ProductResponseModel
import com.swi.admincafe.api.observer.observe
import com.swi.admincafe.api.repository.AppRepository
import com.swi.admincafe.api.response.Response
import com.swi.admincafe.api.viewModel.LoginViewModel
import com.swi.admincafe.api.viewModelFactory.AuthViewModelFactory
import com.swi.admincafe.databinding.FragmentHomeBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

class HomeFragment : Fragment() {

    private lateinit var mBinding : FragmentHomeBinding
    private lateinit var mActivity: MainActivity
    lateinit var loginViewModel: LoginViewModel
    private  lateinit var imageUri : Uri
    private lateinit var sliderUri : Uri
    private val contract = registerForActivityResult(ActivityResultContracts.GetContent()){
        imageUri = it!!
        mBinding.imgCategory.setImageURI(it)
    }
    private val contracts = registerForActivityResult(ActivityResultContracts.GetContent()){
        sliderUri = it!!
        mBinding.imgSlider.setImageURI(it)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        initialization()
        return mBinding.root
    }

    private fun initialization() {
        val appRepository = AppRepository(mActivity.application)
        loginViewModel = ViewModelProvider(this, AuthViewModelFactory(requireActivity().application, appRepository)).get(LoginViewModel::class.java)

        mBinding.imgCategory.setOnClickListener { contract.launch("image/*") }
        mBinding.btnCategoryUpload.setOnClickListener { insertCategory() }

        mBinding.imgSlider.setOnClickListener { contracts.launch("image/*") }
        mBinding.btnBannerUpload.setOnClickListener { insertBanner() }

    }

    private fun insertBanner() {
        if (!this::sliderUri.isInitialized) {
            return
        }
        val filesDir = mActivity.applicationContext.filesDir
        val file = File(filesDir,"image.png")
        val inputStream = mActivity.contentResolver.openInputStream(sliderUri)
        val outputStream = FileOutputStream(file)
        inputStream!!.copyTo(outputStream)
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val banner = MultipartBody.Part.createFormData("image", file.name, requestBody)

        val offerEdt = mBinding.edtOffer.text.toString()
        val offer : RequestBody = offerEdt.toRequestBody("text/plain".toMediaTypeOrNull())
        val offerDateEdt = mBinding.edtOfferDate.text.toString()
        val offerDate : RequestBody = offerDateEdt.toRequestBody("text/plain".toMediaTypeOrNull())

        loginViewModel.insertBanner(banner,offer,offerDate)

    }

    private fun insertCategory() {
        if (!this::imageUri.isInitialized) {
            return
        }

        val filesDir = mActivity.applicationContext.filesDir
        val file = File(filesDir,"image.png")
        val inputStream = mActivity.contentResolver.openInputStream(imageUri)
        val outputStream = FileOutputStream(file)
        inputStream!!.copyTo(outputStream)
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", file.name, requestBody)

        val name = mBinding.edtTxtCategoryName.text.toString()
        val namePart : RequestBody = name.toRequestBody("text/plain".toMediaTypeOrNull())

        loginViewModel.insertCategory(imagePart, namePart)

    }

    private fun insertProduct(){}

}