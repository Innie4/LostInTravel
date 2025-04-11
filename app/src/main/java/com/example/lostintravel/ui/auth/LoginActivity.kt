package com.example.lostintravel.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.lostintravel.databinding.ActivityLoginBinding
import com.example.lostintravel.domain.util.Result
import com.example.lostintravel.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: AuthViewModel by viewModels()
    
    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        viewModel.signInWithGoogle(result.data)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupObservers()
        setupClickListeners()
    }
    
    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.authState.collectLatest { authState ->
                if (authState.isAuthenticated) {
                    navigateToMain()
                }
            }
        }
        
        lifecycleScope.launch {
            viewModel.authResult.collectLatest { result ->
                when (result) {
                    is Result.Loading -> {
                        // Show loading indicator
                        binding.googleSignInButton.isEnabled = false
                    }
                    is Result.Success -> {
                        // Hide loading indicator
                        binding.googleSignInButton.isEnabled = true
                        if (result.data.isAuthenticated) {
                            navigateToMain()
                        }
                    }
                    is Result.Error -> {
                        // Hide loading indicator
                        binding.googleSignInButton.isEnabled = true
                        Toast.makeText(
                            this@LoginActivity,
                            "Authentication failed: ${result.exception.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    null -> {
                        // Initial state, do nothing
                    }
                }
            }
        }
    }
    
    private fun setupClickListeners() {
        binding.googleSignInButton.setOnClickListener {
            launchGoogleSignIn()
        }
    }
    
    private fun launchGoogleSignIn() {
        val signInIntent = viewModel.getSignInIntent()
        googleSignInLauncher.launch(signInIntent)
    }
    
    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}