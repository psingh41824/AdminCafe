package com.swi.admincafe.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.swi.admincafe.MainActivity
import com.swi.admincafe.R
import com.swi.admincafe.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var mBinding : FragmentProfileBinding
    private lateinit var mActivity :MainActivity
    private var showInsertUI = false
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        initialization()

        arguments?.let {
            showInsertUI = it.getBoolean("showInsertUI", false)
        }

        // Show or hide the UI based on the showInsertUI value
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



    }
}