package com.swi.admincafe

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.swi.admincafe.databinding.ActivityMainBinding
import com.swi.admincafe.databinding.NavigationLayoutBinding
import com.swi.admincafe.fragment.HomeFragment
import com.swi.admincafe.fragment.ProfileFragment
import com.swi.admincafe.iterface.BannerActionListener

class MainActivity : AppCompatActivity(), View.OnClickListener, BannerActionListener {

    private  lateinit var mBinding: ActivityMainBinding
    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navigationLayout : NavigationLayoutBinding
    val  args = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        drawerLayout = mBinding.drawerLayout
        toolbar = mBinding.toolBar
        setSupportActionBar(toolbar)

        mBinding.imageMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        navigationLayout = mBinding.navigationLayout
        bottomNavigationView = mBinding.bottomNavigation
        mBinding.bottomNavigation.selectedItemId = R.id.Bot_home

        initBottom()
        initNavigation()


        addFragment(HomeFragment(), false, args)

    }

    private fun initNavigation() {
        mBinding.navigationLayout.rlBanner.setOnClickListener(this)
        mBinding.navigationLayout.rlCategory.setOnClickListener(this)
        mBinding.navigationLayout.rlProduct.setOnClickListener(this)
        mBinding.navigationLayout.rlUser.setOnClickListener(this)
        mBinding.navigationLayout.rlLogout.setOnClickListener(this)
        mBinding.navigationLayout.bannerAddBtn.setOnClickListener (this)
        mBinding.navigationLayout.bannerGetBtn.setOnClickListener (this)

        mBinding.navigationLayout.bannerArrowDown.setOnClickListener {

            if (mBinding.navigationLayout.rl1Banner.visibility == View.VISIBLE) {

                mBinding.navigationLayout.rl1Banner.visibility = View.GONE
                mBinding.navigationLayout.bannerArrowDown.setImageResource(R.drawable.arrow_down)
            } else {

                mBinding.navigationLayout.rl1Banner.visibility = View.VISIBLE
                mBinding.navigationLayout.bannerArrowDown.setImageResource(R.drawable.upper_arrow)
            }
        }
        mBinding.navigationLayout.categoryArrowDown.setOnClickListener {

            if (mBinding.navigationLayout.rl1Category.visibility == View.VISIBLE) {

                mBinding.navigationLayout.rl1Category.visibility = View.GONE
                mBinding.navigationLayout.categoryArrowDown.setImageResource(R.drawable.arrow_down)
            } else {

                mBinding.navigationLayout.rl1Category.visibility = View.VISIBLE
                mBinding.navigationLayout.categoryArrowDown.setImageResource(R.drawable.upper_arrow)
            }
        }
        mBinding.navigationLayout.productArrowDown.setOnClickListener {

            if (mBinding.navigationLayout.rl1Product.visibility == View.VISIBLE) {

                mBinding.navigationLayout.rl1Product.visibility = View.GONE
                mBinding.navigationLayout.productArrowDown.setImageResource(R.drawable.arrow_down)
            } else {

                mBinding.navigationLayout.rl1Product.visibility = View.VISIBLE
                mBinding.navigationLayout.productArrowDown.setImageResource(R.drawable.upper_arrow)
            }
        }
        mBinding.navigationLayout.userArrowDown.setOnClickListener {

            if (mBinding.navigationLayout.rl1User.visibility == View.VISIBLE) {

                mBinding.navigationLayout.rl1User.visibility = View.GONE
                mBinding.navigationLayout.userArrowDown.setImageResource(R.drawable.arrow_down)
            } else {

                mBinding.navigationLayout.rl1User.visibility = View.VISIBLE
                mBinding.navigationLayout.userArrowDown.setImageResource(R.drawable.upper_arrow)
            }
        }

    }

    private fun initBottom() {
        bottomNavigationView.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.Bot_home -> {

                    toolbar.title = "Home"
                    addFragment(HomeFragment(),true,args)
                    true
                }

                R.id.Bot_profile -> {

                    addFragment(ProfileFragment(),true,args)
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    override fun onBackPressed() {

        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if ( mBinding.bottomNavigation.selectedItemId== R.id.Bot_home) {
                finishAffinity()
            } else {
                mBinding.bottomNavigation.selectedItemId = R.id.Bot_home
            }
        }
    }

    private fun addFragment(mCurrentLoadedFragment: Fragment, addToStack: Boolean, args: Bundle) {
        val ft = supportFragmentManager.beginTransaction()
        mCurrentLoadedFragment.arguments = args
        ft.replace(R.id.fragment_container, mCurrentLoadedFragment, mCurrentLoadedFragment.javaClass.simpleName)
        if (addToStack) {
            ft.addToBackStack(mCurrentLoadedFragment.javaClass.simpleName)
        }
        ft.commit()
    }

    override fun onClick(itnav: View?)

    {
        when(itnav!!.id){

            R.id.rl_logout ->showLogoutDialog()

            R.id.banner_add_Btn ->  {
                onAddClicked()
                mBinding.bottomNavigation.selectedItemId = R.id.Bot_profile
            }

            R.id.banner_get_Btn -> {
                onGetAllClicked()
                mBinding.bottomNavigation.selectedItemId = R.id.Bot_profile
            }

        }
        drawerLayout.closeDrawer(GravityCompat.START)
    }
    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                getSharedPreferences("MyPrefs", MODE_PRIVATE).edit().apply {
                    putBoolean("isLoggedIn", false)
                    apply()
                }
                finish()
            }
            .setNegativeButton("No", null)
            .show()
    }

    override fun onAddClicked() {
        val fragment = ProfileFragment()
        val args = Bundle().apply { putBoolean("showInsertUI", true) }
        addFragment(fragment, true, args)
    }

    override fun onGetAllClicked() {
        val fragment = ProfileFragment()
        val args = Bundle().apply { putBoolean("showInsertUI", false) }
        addFragment(fragment, true, args)
    }
}