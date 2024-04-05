package com.ikp.transcribe.auth.ui

import android.app.Activity
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ikp.transcribe.MainActivity
import com.ikp.transcribe.R
import com.ikp.transcribe.auth.data.Result
import com.ikp.transcribe.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var connectivityManager : ConnectivityManager
    private var isConnected : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        val factory = LoginViewModelFactory(this)
        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                runBlocking {
                    while (true) {
                        val result = loginViewModel.checkToken()
                        if (result is Result.Success) {
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        break
                    }
                }
                false
            }
        }

        super.onCreate(savedInstanceState)

        connectivityManager = getSystemService(ConnectivityManager::class.java)
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = binding.email
        val password = binding.password
        val login = binding.login
        val loading = binding.loading

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both email / password is valid
            login.isEnabled = loginState.isDataValid and isConnected

            if (loginState.emailError != null) {
                email.error = getString(loginState.emailError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            setResult(Activity.RESULT_OK)
        })

        email.afterTextChanged {
            loginViewModel.loginDataChanged(
                email.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    email.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        CoroutineScope(Dispatchers.Main).launch {
                            loginViewModel.login(email.text.toString(), password.text.toString())
                        }
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                CoroutineScope(Dispatchers.Main).launch {
                    loginViewModel.login(email.text.toString(), password.text.toString())
                }
            }
        }
    }

    private fun updateUiWithUser(email: String) {
        val welcome = getString(R.string.welcome)
        Toast.makeText(
            applicationContext,
            "$welcome $email",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network : Network) {
            isConnected = true
            runOnUiThread {
                binding.login.isEnabled = true
                binding.noConnectionError.visibility = View.GONE
            }

        }

        override fun onLost(network : Network) {
            isConnected = false
            runOnUiThread {
                binding.login.isEnabled = false
                binding.noConnectionError.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
