package com.barkat.barkatsevings.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.barkat.barkatsevings.helper.FirebaseHelper
import com.barkat.barkatsevings.utils.PreferenceProvider
import com.barkat.barkatsevings.utils.USER_ID
import com.example.barkatsevings.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
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

    private fun setClickListeners() {
        mBinding.btnLogin.setOnClickListener {
            val email = mBinding.edtEmail.text.toString()
            val password = mBinding.edtPassword.text.toString()
            when {
                email.isEmpty() -> {
                    Snackbar.make(mBinding.root, "Please enter email", Snackbar.LENGTH_SHORT).show()
                }
                password.isEmpty() -> {
                    Snackbar.make(mBinding.root, "Please enter password", Snackbar.LENGTH_SHORT).show()
                }
                else -> {
                    firebaseHelper.login(email, password).observe(this) {
                        if (it != null) {
                            Toast.makeText(this, "Login Success", LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }else{
                            Toast.makeText(this, "Invalid username or password", LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}