package com.ftg2024.ftgcylinderapp.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ftg2024.ftgcylinderapp.R
import com.ftg2024.ftgcylinderapp.auth.model.LoginRequest
import com.ftg2024.ftgcylinderapp.auth.viewmodel.LoginViewModelFactory
import com.ftg2024.ftgcylinderapp.auth.viewmodel.LoginViewmodel
import com.ftg2024.ftgcylinderapp.dashboard.DashboardActivity
import com.ftg2024.ftgcylinderapp.databinding.ActivityLoginBinding
import com.ftg2024.ftgcylinderapp.progressdialog.ProgressDialog
import com.ftg2024.ftgcylinderapp.sharedprefs.SharedPrefManager
import com.ftg2024.ftgcylinderapp.uidata.Response
import dagger.hilt.android.AndroidEntryPoint
import java.net.SocketTimeoutException
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    @Inject
    lateinit var factory : LoginViewModelFactory

    /*@Inject
    lateinit var progressDialog : ProgressDialog*/

    @Inject
    lateinit var prefManager: SharedPrefManager

    private lateinit var binding: ActivityLoginBinding
    private lateinit var progressDialog: ProgressDialog

    private val viewModel by viewModels<LoginViewmodel> { factory }

    private var isAdmin = true
    private var role : String = "U"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setOnClickListeners()
        setUpObservers()
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
    }

    private fun setOnClickListeners() {
        binding.buttonLoginActivitySubmit.setOnClickListener {
            if (validateFields()) {
                //progressDialog.show()
                progressDialog = ProgressDialog(this, "Validating User")
                progressDialog.show()
                Log.d("####", "setOnClickListeners: ${binding.radioLoginAdmin.isChecked}, ${binding.radioLoginAgent.isChecked}")
                viewModel.login(LoginRequest(binding.editextLoginActivityUsername.text.toString(), binding.editextLoginActivityPassword.text.toString(), "",role))
            }
        }

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioLoginAdmin -> {
                    isAdmin = true
                    role = "U"
                }
                R.id.radioLoginAgent -> {
                    isAdmin = false
                    role = "A"
                }
            }
        }

    }

   private fun navigateToDashboard(isAdmin: Boolean) {
       val intent = Intent(this, DashboardActivity::class.java)
       intent.putExtra("is_admin", isAdmin)
       startActivity(intent)
   }

    private fun setUpObservers() {
        viewModel.loginLiveData.observe(this){ response ->
            when(response) {
                is Response.Success -> {
                    val loginResponse = response.data
                    Log.d("####", "setUpObservers: $loginResponse")
                    progressDialog.dismiss()
                    val data = loginResponse.data
                    if (loginResponse.code == 200 && data.isNotEmpty()) {
                        prefManager.saveToken(data[0].token)
                        prefManager.setLoginResponseData(data)
                        navigateToDashboard(isAdmin)
                        //startActivity(Intent(this, DashboardActivity::class.java))
                    } else if (loginResponse.code == 304) {
                        showToast("Incorrect Username And Password")
                    } else {
                        showToast("Something Went Wrong")
                    }
                }

                is Response.Error -> {
                    progressDialog.dismiss()
                    val exception = response.exception
                    val code = response.code
                    if (exception is SocketTimeoutException) {
                        showToast("Request timed out, please try again!")
                    } else {
                        showToast("Something Went Wrong")
                    }
                }

                else -> return@observe
            }
        }
    }

    private fun validateFields() : Boolean {
        if (binding.editextLoginActivityUsername.text.isNullOrEmpty()) {
            return false
        }

        if (binding.textviewLoginActivityForgotPassword.text.isNullOrEmpty()) {
            return false
        }

        return true
    }

    private fun showToast(msg : String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}