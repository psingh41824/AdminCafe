package com.swi.admincafe.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.swi.admincafe.MainActivity
import com.swi.admincafe.R
import com.swi.admincafe.api.model.CategoryResponseModel
import com.swi.admincafe.api.model.Category
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
import kotlin.math.log

class HomeFragment : Fragment() {

    private lateinit var mBinding : FragmentHomeBinding
    private lateinit var mActivity: MainActivity
    lateinit var loginViewModel: LoginViewModel
    private  lateinit var imageUri : Uri
    private lateinit var sliderUri : Uri
    private lateinit var productUri : Uri
    private lateinit var spinner: Spinner
    private var selectedCategoryId = String()
    private var categoryList:ArrayList<Category> = ArrayList()
    private val contract = registerForActivityResult(ActivityResultContracts.GetContent()){
        imageUri = it!!
        mBinding.imgCategory.setImageURI(it)
    }
    private val contracts = registerForActivityResult(ActivityResultContracts.GetContent()){
        sliderUri = it!!
        mBinding.imgSlider.setImageURI(it)
    }
    private val contractProduct = registerForActivityResult(ActivityResultContracts.GetContent()){
        productUri = it!!
        mBinding.imgProduct.setImageURI(it)
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

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                selectedCategoryId = categoryList[position]._id
                Log.d("TAG", "Selected Category ID: $selectedCategoryId")
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case where no item is selected
            }
        }

        return mBinding.root
    }

    private fun initialization() {
        val appRepository = AppRepository(mActivity.application)
        loginViewModel = ViewModelProvider(this, AuthViewModelFactory(requireActivity().application, appRepository)).get(LoginViewModel::class.java)
        observe(loginViewModel.categoryLiveData, :: categoryResult )
        loginViewModel.categoryData()

        mBinding.imgCategory.setOnClickListener { contract.launch("image/*") }
        mBinding.btnCategoryUpload.setOnClickListener { insertCategory() }

        mBinding.imgSlider.setOnClickListener { contracts.launch("image/*") }
        mBinding.btnBannerUpload.setOnClickListener { insertBanner() }

        mBinding.imgProduct.setOnClickListener { contractProduct.launch("image/*") }
        mBinding.btnProductUpload.setOnClickListener { insertProduct() }


        spinner = mBinding.spProductCategoryId
    }

    private fun categoryResult(it: Response<CategoryResponseModel>) {
        when (it) {
            is Response.Loading -> {
                Log.e("TAG", "categoryResult: 1", )
            }
            is Response.Success -> {
                it.data?.let { response ->
                    categoryList.addAll(response.data)
                    if(categoryList.isEmpty()){
                        Log.e("TAG", "Category list is empty or null")
                    }else{
                        val categoryNames = categoryList.mapNotNull { it.name }
                        val adapter = ArrayAdapter(mActivity, android.R.layout.simple_spinner_item, categoryNames)
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinner.adapter = adapter
                        spinner.setSelection(0) // Select the first item by default

                    }
                }

            }
            is Response.Error -> {
                Log.e("TAG", "categoryResult: 3", )
            }
        }
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
        Log.e("TAG", "insertCategory: ${namePart}" )

        loginViewModel.insertCategory(imagePart, namePart)

    }

    private fun insertProduct() {
        if (!this::productUri.isInitialized) {
            return
        }
        val filesDir = mActivity.applicationContext.filesDir
        val file = File(filesDir,"image.png")
        val inputStream = mActivity.contentResolver.openInputStream(productUri)
        val outputStream = FileOutputStream(file)
        inputStream!!.copyTo(outputStream)

        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", file.name, requestBody)

        val categoryId : RequestBody = selectedCategoryId.toRequestBody("text/plain".toMediaTypeOrNull())
        Log.e("categoryId", "insertProduct: ${selectedCategoryId}" )

        val name = mBinding.txtProductName.text.toString()
        val namePart : RequestBody = name.toRequestBody("text/plain".toMediaTypeOrNull())

        val description = mBinding.txtProductDescription.text.toString()
        val descriptionPart : RequestBody = description.toRequestBody("text/plain".toMediaTypeOrNull())

        val rating = mBinding.txtProductRating.text.toString()
        val ratingPart : RequestBody = rating.toRequestBody("text/plain".toMediaTypeOrNull())

        val numReviews = mBinding.txtProductReviews.text.toString()
        val numReviewsPart : RequestBody = numReviews.toRequestBody("text/plain".toMediaTypeOrNull())

        val price = mBinding.txtProductPrice.text.toString()
        val pricePart : RequestBody = price.toRequestBody("text/plain".toMediaTypeOrNull())

        val size = mBinding.txtProductSize.text.toString()
        val sizePart : RequestBody = size.toRequestBody("text/plain".toMediaTypeOrNull())

        loginViewModel.insertProduct(categoryId,descriptionPart,imagePart,namePart,numReviewsPart,pricePart,ratingPart,sizePart)
    }

}