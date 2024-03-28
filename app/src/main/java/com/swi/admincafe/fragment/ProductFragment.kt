package com.swi.admincafe.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.swi.admincafe.MainActivity
import com.swi.admincafe.R
import com.swi.admincafe.api.model.Category
import com.swi.admincafe.api.model.CategoryResponseModel
import com.swi.admincafe.api.observer.observe
import com.swi.admincafe.api.repository.AppRepository
import com.swi.admincafe.api.response.Response
import com.swi.admincafe.api.viewModel.LoginViewModel
import com.swi.admincafe.api.viewModelFactory.AuthViewModelFactory
import com.swi.admincafe.databinding.FragmentProductBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

class ProductFragment : Fragment() {
    private lateinit var mBinding : FragmentProductBinding
    private lateinit var mActivity : MainActivity
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var productUri : Uri
    private lateinit var spinner: Spinner
    private var selectedCategoryId = String()
    private var categoryList:ArrayList<Category> = ArrayList()
    private var showInsertUI = false

    private val contractProduct = registerForActivityResult(ActivityResultContracts.GetContent()){
        productUri = it!!
        val originalImageName = getOriginalImageName(productUri)
        mBinding.edtPath.setText(originalImageName)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_product, container, false)
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

        arguments?.let {
            showInsertUI = it.getBoolean("showProInsertUI", false)
        }

        if (showInsertUI) {
            mBinding.rlAddProduct.visibility = View.VISIBLE
            mBinding.rlGetAllProduct.visibility = View.GONE
        } else {
            mBinding.rlAddProduct.visibility = View.GONE
            mBinding.rlGetAllProduct.visibility = View.VISIBLE
        }

        return mBinding.root
    }

    private fun initialization() {
        val appRepository = AppRepository(mActivity.application)
        loginViewModel = ViewModelProvider(this, AuthViewModelFactory(requireActivity().application, appRepository)).get(LoginViewModel::class.java)
        observe(loginViewModel.categoryLiveData, :: categoryResult )
        loginViewModel.categoryData()

        mBinding.llProduct.setOnClickListener { contractProduct.launch("image/*") }
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
                        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)
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

        val name = mBinding.edtProductName.text.toString()
        val namePart : RequestBody = name.toRequestBody("text/plain".toMediaTypeOrNull())

        val description = mBinding.edtProductDescription.text.toString()
        val descriptionPart : RequestBody = description.toRequestBody("text/plain".toMediaTypeOrNull())

        val rating = mBinding.edtProductRating.text.toString()
        val ratingPart : RequestBody = rating.toRequestBody("text/plain".toMediaTypeOrNull())

        val numReviews = mBinding.edtProductReviews.text.toString()
        val numReviewsPart : RequestBody = numReviews.toRequestBody("text/plain".toMediaTypeOrNull())

        val price = mBinding.edtProductPrice.text.toString()
        val pricePart : RequestBody = price.toRequestBody("text/plain".toMediaTypeOrNull())

        val size = mBinding.edtProductSize.text.toString()
        val sizePart : RequestBody = size.toRequestBody("text/plain".toMediaTypeOrNull())

        loginViewModel.insertProduct(categoryId,descriptionPart,imagePart,namePart,numReviewsPart,pricePart,ratingPart,sizePart)
    }

    private fun getOriginalImageName(uri: Uri?): String {
        if (uri == null) return "Unknown Image"

        val projection = arrayOf(MediaStore.Images.Media.DISPLAY_NAME)
        mActivity.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val index = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
                if (index != -1) {
                    return cursor.getString(index)
                }
            }
        }
        return "Unknown Image"
    }


}