package com.sefatombul.gcase.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.sefatombul.gcase.R
import com.sefatombul.gcase.databinding.ActivityMainBinding
import com.sefatombul.gcase.utils.PopupHelper.showAlertDialog
import com.sefatombul.gcase.utils.safeNavigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navControllerSetup()
        sideMenuSetup()
    }


    private fun navControllerSetup() {
        binding.apply {
            this@MainActivity.navHostFragment =
                supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
            this@MainActivity.navController = this@MainActivity.navHostFragment.navController
        }
    }


    fun openSideMenu() {
        binding.apply {
            drawerLayout.open()
        }
    }

    private fun sideMenuSetup() {
        binding.apply {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            navView.setNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.logout -> {
                        showAlertDialog(
                            title = getString(R.string.logout),
                            message = getString(R.string.logout_message),
                            positiveButtonString = getString(R.string.yes),
                            positiveButtonClickListener = { dialog, which ->
                                //navController.safeNavigate(R.id.action_listFragment_to_loginFragment)
                            },
                            negativeButtonString = getString(R.string.cancel),
                            negativeButtonClickListener = { dialog, which ->

                            }
                        )
                    }
                }
                drawerLayout.close()
                return@setNavigationItemSelectedListener true
            }
        }
    }
}