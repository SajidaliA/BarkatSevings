package com.barkat.barkatsevings.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.barkat.barkatsevings.helper.FirebaseHelper
import com.barkat.barkatsevings.utils.*
import com.example.barkatsevings.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    @Inject
    lateinit var mPreferenceProvider: PreferenceProvider

    private lateinit var mBinding: ActivityLoginBinding
    private lateinit var firebaseHelper: FirebaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        if (!checkConnection(this)) showSnackBar(mBinding.root, "No internet connection")
        firebaseHelper = FirebaseHelper(mPreferenceProvider)
        firebaseHelper.checkUserLoggedIn()?.apply {
            mPreferenceProvider.setValue(USER_ID,
                email.toString().substring(0, email.toString().indexOf("@"))
            )
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
        setClickListeners()
    }

    override fun onResume() {
        super.onResume()
        if (!checkConnection(this)) showSnackBar(mBinding.root, "No internet connection")
    }

    private fun setClickListeners() {
        mBinding.btnLogin.setOnClickListener {
            val email = mBinding.edtEmail.text.toString()
            val password = mBinding.edtPassword.text.toString()
            when {
                !checkConnection(this) -> {
                    showSnackBar(mBinding.root, "No internet connection")
                }
                email.isEmpty() -> {
                    showSnackBar(mBinding.root, "Please enter email")
                }
                password.isEmpty() -> {
                    showSnackBar(mBinding.root, "Please enter password")
                }
                else -> {
                    mBinding.btnLogin.hide()
                    mBinding.progressBar.show()
                    firebaseHelper.login(email, password).observe(this) {
                        if (it != null) {
                            mPreferenceProvider.setValue(USER_ID,
                                email.substring(0, email.indexOf("@"))
                            )
                            Toast.makeText(this, "Login Success", LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }else{
                            Toast.makeText(this, "Invalid username or password", LENGTH_SHORT).show()
                            mBinding.btnLogin.show()
                            mBinding.progressBar.hide()
                        }
                    }
                }
            }
        }
    }
}