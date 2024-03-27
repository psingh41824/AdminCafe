package com.swi.admincafe.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.swi.admincafe.MainActivity
import com.swi.admincafe.R
import com.swi.admincafe.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var mBinding : FragmentProfileBinding
    private lateinit var mActivity :MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        //initialization()

        return mBinding.root
    }

    private fun initialization() {

    }
}