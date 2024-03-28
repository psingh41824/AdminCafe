package com.swi.admincafe

import HomeFragment
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.swi.admincafe.databinding.ActivityMainBinding
import com.swi.admincafe.databinding.NavigationLayoutBinding
import com.swi.admincafe.fragment.CategoryFragment
import com.swi.admincafe.fragment.ProductFragment

class MainActivity : AppCompatActivity(), View.OnClickListener{

    private  lateinit var mBinding: ActivityMainBinding
    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
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

        initNavigation()

        addFragment(HomeFragment(), false, args)

    }

    private fun initNavigation() {
        navigationLayout.rlBanner.setOnClickListener {
            toggleSectionVisibility(
                targetVisibilityView = mBinding.navigationLayout.rl1Banner,
                arrowView = mBinding.navigationLayout.bannerArrowDown,
                isCurrentlyVisible = mBinding.navigationLayout.rl1Banner.visibility == View.VISIBLE
            )
        }
        navigationLayout.rlCategory.setOnClickListener {
            toggleSectionVisibility(
                targetVisibilityView = mBinding.navigationLayout.rl1Category,
                arrowView = mBinding.navigationLayout.categoryArrowDown,
                isCurrentlyVisible = mBinding.navigationLayout.rl1Category.visibility == View.VISIBLE
            )
        }
        navigationLayout.rlProduct.setOnClickListener {
            toggleSectionVisibility(
                targetVisibilityView = mBinding.navigationLayout.rl1Product,
                arrowView = mBinding.navigationLayout.productArrowDown,
                isCurrentlyVisible = mBinding.navigationLayout.rl1Product.visibility == View.VISIBLE
            )
        }
        navigationLayout.rlUser.setOnClickListener {
            toggleSectionVisibility(
                targetVisibilityView = mBinding.navigationLayout.rl1User,
                arrowView = mBinding.navigationLayout.userArrowDown,
                isCurrentlyVisible = mBinding.navigationLayout.rl1User.visibility == View.VISIBLE
            )
        }
        navigationLayout.rlLogout.setOnClickListener(this)

        navigationLayout.bannerAddBtn.setOnClickListener (this)
        navigationLayout.bannerGetBtn.setOnClickListener (this)
        navigationLayout.categoryAddBtn.setOnClickListener (this)
        navigationLayout.categoryGetBtn.setOnClickListener (this)
        navigationLayout.productAddBtn.setOnClickListener (this)
        navigationLayout.productGetBtn.setOnClickListener (this)
    }

    override fun onBackPressed() {
        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        else {
            super.onBackPressed()

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
    override fun onClick(itnav: View?) {
        when(itnav!!.id){

            R.id.rl_logout ->showLogoutDialog()

            R.id.banner_add_Btn ->  {
                val args = Bundle().apply { putBoolean("showInsertUI", true) }
                addFragment(HomeFragment(),true, args)
            }
            R.id.banner_get_Btn -> {
                val args = Bundle().apply { putBoolean("showInsertUI", false) }
                addFragment(HomeFragment(), true, args)
            }
            R.id.category_add_Btn ->{
                val args = Bundle().apply { putBoolean("showCatInsertUI", true) }
                addFragment(CategoryFragment(), true, args)
            }
            R.id.category_get_Btn ->{
                val args = Bundle().apply { putBoolean("showCatInsertUI", false) }
                addFragment(CategoryFragment(),true, args)
            }
            R.id.product_add_Btn ->{
                val args = Bundle().apply { putBoolean("showProInsertUI", true) }
                addFragment(ProductFragment(),true, args)
            }
            R.id.product_get_Btn ->{
                val args = Bundle().apply { putBoolean("showProInsertUI", false) }
                addFragment(ProductFragment(),true, args)
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

    private fun toggleSectionVisibility(targetVisibilityView: View, arrowView: ImageView, isCurrentlyVisible: Boolean) {

        val allSections = listOf(
            mBinding.navigationLayout.rl1Banner,
            mBinding.navigationLayout.rl1Category,
            mBinding.navigationLayout.rl1Product,
            mBinding.navigationLayout.rl1User
        )
        allSections.forEach { it.visibility = View.GONE }

        val allArrows = listOf(
            mBinding.navigationLayout.bannerArrowDown,
            mBinding.navigationLayout.categoryArrowDown,
            mBinding.navigationLayout.productArrowDown,
            mBinding.navigationLayout.userArrowDown
        )
        allArrows.forEach { it.setImageResource(R.drawable.arrow_down) }

        if (!isCurrentlyVisible) {
            targetVisibilityView.visibility = View.VISIBLE
            arrowView.setImageResource(R.drawable.upper_arrow)
        }

    }

}