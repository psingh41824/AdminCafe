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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.swi.admincafe.databinding.ActivityMainBinding
import com.swi.admincafe.databinding.NavigationLayoutBinding
import com.swi.admincafe.fragment.CategoryFragment
import com.swi.admincafe.fragment.HomeFragment
import com.swi.admincafe.fragment.ProductFragment
import com.swi.admincafe.fragment.ProfileFragment

class MainActivity : AppCompatActivity(), View.OnClickListener{

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
        navigationLayout.rlBanner.setOnClickListener(this)
        navigationLayout.rlCategory.setOnClickListener(this)
        navigationLayout.rlProduct.setOnClickListener(this)
        navigationLayout.rlUser.setOnClickListener(this)
        navigationLayout.rlLogout.setOnClickListener(this)

        navigationLayout.bannerArrowDown.setOnClickListener {

            if (mBinding.navigationLayout.rl1Banner.visibility == View.VISIBLE) {

                mBinding.navigationLayout.rl1Banner.visibility = View.GONE
                mBinding.navigationLayout.bannerArrowDown.setImageResource(R.drawable.arrow_down)
            } else {

                mBinding.navigationLayout.rl1Banner.visibility = View.VISIBLE
                mBinding.navigationLayout.bannerArrowDown.setImageResource(R.drawable.upper_arrow)
            }
        }
        navigationLayout.categoryArrowDown.setOnClickListener {

            if (mBinding.navigationLayout.rl1Category.visibility == View.VISIBLE) {

                mBinding.navigationLayout.rl1Category.visibility = View.GONE
                mBinding.navigationLayout.categoryArrowDown.setImageResource(R.drawable.arrow_down)
            } else {

                mBinding.navigationLayout.rl1Category.visibility = View.VISIBLE
                mBinding.navigationLayout.categoryArrowDown.setImageResource(R.drawable.upper_arrow)
            }
        }
        navigationLayout.productArrowDown.setOnClickListener {

            if (mBinding.navigationLayout.rl1Product.visibility == View.VISIBLE) {

                mBinding.navigationLayout.rl1Product.visibility = View.GONE
                mBinding.navigationLayout.productArrowDown.setImageResource(R.drawable.arrow_down)
            } else {

                mBinding.navigationLayout.rl1Product.visibility = View.VISIBLE
                mBinding.navigationLayout.productArrowDown.setImageResource(R.drawable.upper_arrow)
            }
        }
        navigationLayout.userArrowDown.setOnClickListener {

            if (mBinding.navigationLayout.rl1User.visibility == View.VISIBLE) {

                mBinding.navigationLayout.rl1User.visibility = View.GONE
                mBinding.navigationLayout.userArrowDown.setImageResource(R.drawable.arrow_down)
            } else {

                mBinding.navigationLayout.rl1User.visibility = View.VISIBLE
                mBinding.navigationLayout.userArrowDown.setImageResource(R.drawable.upper_arrow)
            }
        }

        navigationLayout.bannerAddBtn.setOnClickListener (this)
        navigationLayout.bannerGetBtn.setOnClickListener (this)
        navigationLayout.categoryAddBtn.setOnClickListener (this)
        navigationLayout.categoryGetBtn.setOnClickListener (this)
        navigationLayout.productAddBtn.setOnClickListener (this)
        navigationLayout.productGetBtn.setOnClickListener (this)
    }

    private fun initBottom() {
        bottomNavigationView.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.Bot_home -> {
                    addFragment(HomeFragment(),true,args)
                    true
                }
                R.id.Bot_profile -> {
                    addFragment(ProfileFragment(),false,args)
                    true
                }
                R.id.Bot_category ->{
                    addFragment(CategoryFragment(),true,args)
                    true
                }
                R.id.Bot_Product ->{
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
                val args = Bundle().apply { putBoolean("showInsertUI", true) }
                navigateToFragment(HomeFragment(), R.id.Bot_home, true, args)
            }

            R.id.banner_get_Btn -> {
                val args = Bundle().apply { putBoolean("showInsertUI", false) }
                navigateToFragment(HomeFragment(), R.id.Bot_home, true, args)
            }
            R.id.category_add_Btn ->{
                val args = Bundle().apply { putBoolean("showCatInsertUI", true) }
                navigateToFragment(CategoryFragment(), R.id.Bot_category, true, args)
            }
            R.id.category_get_Btn ->{
                val args = Bundle().apply { putBoolean("showCatInsertUI", false) }
                navigateToFragment(CategoryFragment(), R.id.Bot_category, true, args)
            }
            R.id.product_add_Btn ->{
                val args = Bundle().apply { putBoolean("showProInsertUI", true) }
                navigateToFragment(ProductFragment(), R.id.Bot_Product, true, args)
            }
            R.id.product_get_Btn ->{
                val args = Bundle().apply { putBoolean("showProInsertUI", false) }
                navigateToFragment(ProductFragment(), R.id.Bot_Product, true, args)
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

    private fun navigateToFragment(fragment: Fragment, itemId: Int, addToStack: Boolean, args: Bundle) {
        // Update the BottomNavigationView to reflect the current selection.
        bottomNavigationView.selectedItemId = itemId

        addFragment(fragment, addToStack, args)
    }

}