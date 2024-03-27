package com.swi.admincafe.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.swi.admincafe.MainActivity
import com.swi.admincafe.R
import com.swi.admincafe.api.repository.AppRepository
import com.swi.admincafe.api.viewModel.LoginViewModel
import com.swi.admincafe.api.viewModelFactory.AuthViewModelFactory
import com.swi.admincafe.databinding.FragmentHomeBinding
import com.swi.admincafe.databinding.FragmentProfileBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

class HomeFragment : Fragment() {

    private lateinit var mBinding : FragmentHomeBinding
    private lateinit var mActivity :MainActivity
    lateinit var loginViewModel: LoginViewModel
    private  lateinit var imageUri : Uri
    private var showInsertUI = false

    private val contracts = registerForActivityResult(ActivityResultContracts.GetContent()){
        imageUri = it!!
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

        arguments?.let {
            showInsertUI = it.getBoolean("showInsertUI", false)
        }

        if (showInsertUI) {
            mBinding.rlAddUi.visibility = View.VISIBLE
            mBinding.rlGetAllUi.visibility = View.GONE
        } else {
            mBinding.rlAddUi.visibility = View.GONE
            mBinding.rlGetAllUi.visibility = View.VISIBLE
        }

        return mBinding.root
    }

    private fun initialization() {
        val appRepository = AppRepository(mActivity.application)
        loginViewModel = ViewModelProvider(this, AuthViewModelFactory(requireActivity().application, appRepository)).get(LoginViewModel::class.java)

        mBinding.imgSlider.setOnClickListener { contracts.launch("image/*") }
        mBinding.btnBannerUpload.setOnClickListener { insertBanner() }

    }
    private fun insertBanner() {
        if (!this::imageUri.isInitialized) {
            return
        }
        val filesDir = mActivity.applicationContext.filesDir
        val file = File(filesDir,"image.png")
        val inputStream = mActivity.contentResolver.openInputStream(imageUri)
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
}