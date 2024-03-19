package com.swi.admincafe

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private  lateinit var mBinding: ActivityMainBinding
    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navigationLayout : NavigationLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        drawerLayout = mBinding.drawerLayout
        toolbar = mBinding.toolBar
        setSupportActionBar(toolbar)

        mBinding.imageMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        navigationLayout = mBinding.navigtionLayout
        bottomNavigationView = mBinding.bottomNavigation
        mBinding.bottomNavigation.selectedItemId = R.id.Bot_home

        initBottom()
        initNavigation()

        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragment_container, HomeFragment())
        transaction.commit()

    }

    private fun initNavigation() {
        mBinding.navigtionLayout.rlHome.setOnClickListener(this)
        mBinding.navigtionLayout.rlOrder.setOnClickListener(this)
        mBinding.navigtionLayout.rlMyCart.setOnClickListener(this)
        mBinding.navigtionLayout.rlProfile.setOnClickListener(this)
        mBinding.navigtionLayout.rlLogout.setOnClickListener(this)
    }

    private fun initBottom() {
        bottomNavigationView.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.Bot_home -> {

                    toolbar.title = "Home"
                    replaceFragment(HomeFragment())
                    true
                }

                R.id.Bot_profile -> {

                    replaceFragment(ProfileFragment())
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


    private fun replaceFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }

    override fun onClick(itnav: View?) {
        when(itnav!!.id){

            R.id.rl_home ->{

                bottomNavigationView.selectedItemId = R.id.Bot_home

            }

            R.id.rl_order ->{

            }
            R.id.rl_myCart ->{

            }
            R.id.rl_profile ->{

                bottomNavigationView.selectedItemId = R.id.Bot_profile
            }

            R.id.rl_logout ->{
                val builder = AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure want to logout")
                    .setPositiveButton("yes", DialogInterface.OnClickListener { dialog, which ->

                        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putBoolean("isLoggedIn", false)
                        editor.apply()

                        finish()
                    })
                    .setNegativeButton("No") { dialog, which ->
                        Toast.makeText(this, "Not Logout", Toast.LENGTH_SHORT).show()
                    }
                val alertDialog = builder.create()

                alertDialog.show()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
    }
}