package com.sefatombul.gcase.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.sefatombul.gcase.R
import com.sefatombul.gcase.data.model.RevokeAccessRequestModel
import com.sefatombul.gcase.databinding.ActivityMainBinding
import com.sefatombul.gcase.utils.*
import com.sefatombul.gcase.utils.PopupHelper.showAlertDialog
import com.sefatombul.gcase.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private var positiveButtonClickListener: (() -> Unit)? = null

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

    fun showLoading() {
        binding.apply {
            llLoading.show()
        }
    }

    fun hideLoading() {
        binding.apply {
            llLoading.remove()
        }
    }

    fun openSideMenu(f: () -> Unit) {
        binding.apply {
            drawerLayout.open()
            positiveButtonClickListener = f
        }
    }

    private fun sideMenuSetup() {
        binding.apply {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            navView.setNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.logout -> {
                        showAlertDialog(title = getString(R.string.logout),
                            message = getString(R.string.logout_message),
                            positiveButtonString = getString(R.string.yes),
                            positiveButtonClickListener = { dialog, which ->
                                positiveButtonClickListener?.let {
                                    it()
                                }
                            },
                            negativeButtonString = getString(R.string.cancel),
                            negativeButtonClickListener = { dialog, which ->

                            })
                    }
                }
                drawerLayout.close()
                return@setNavigationItemSelectedListener true
            }
        }
    }
}