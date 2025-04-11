package com.example.lostintravel.util

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.MasterKey
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject

class SecurityUtils @Inject constructor(
    private val context: Context
) {
    companion object {
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val KEY_ALIAS = "LostInTravelKey"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val GCM_TAG_LENGTH = 128
    }
    
    fun getMasterKey(): MasterKey {
        return MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }
    
    fun encrypt(plaintext: String): ByteArray {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getOrCreateSecretKey())
        
        val ciphertext = cipher.doFinal(plaintext.toByteArray())
        val iv = cipher.iv
        
        // Prepend IV to ciphertext
        return iv + ciphertext
    }
    
    fun decrypt(ciphertext: ByteArray): String {
        // Extract IV from ciphertext
        val iv = ciphertext.sliceArray(0 until 12)
        val actualCiphertext = ciphertext.sliceArray(12 until ciphertext.size)
        
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val spec = GCMParameterSpec(GCM_TAG_LENGTH, iv)
        cipher.init(Cipher.DECRYPT_MODE, getOrCreateSecretKey(), spec)
        
        val plaintext = cipher.doFinal(actualCiphertext)
        return String(plaintext)
    }
    
    private fun getOrCreateSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null)
        
        if (keyStore.containsAlias(KEY_ALIAS)) {
            return keyStore.getKey(KEY_ALIAS, null) as SecretKey
        }
        
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            ANDROID_KEYSTORE
        )
        
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .setRandomizedEncryptionRequired(true)
            .build()
        
        keyGenerator.init(keyGenParameterSpec)
        return keyGenerator.generateKey()
    }
    
    // Utility method to securely store sensitive strings
    fun securelyStoreString(key: String, value: String) {
        val encryptedSharedPreferences = androidx.security.crypto.EncryptedSharedPreferences.create(
            context,
            "secure_prefs",
            getMasterKey(),
            androidx.security.crypto.EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            androidx.security.crypto.EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        
        encryptedSharedPreferences.edit().putString(key, value).apply()
    }
    
    // Utility method to retrieve securely stored strings
    fun retrieveSecureString(key: String): String? {
        val encryptedSharedPreferences = androidx.security.crypto.EncryptedSharedPreferences.create(
            context,
            "secure_prefs",
            getMasterKey(),
            androidx.security.crypto.EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            androidx.security.crypto.EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        
        return encryptedSharedPreferences.getString(key, null)
    }
    
    // Utility method to check if a device has secure lock screen
    fun hasSecureLockScreen(): Boolean {
        val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as android.app.KeyguardManager
        return keyguardManager.isDeviceSecure
    }
    
    // Utility method to validate token expiration
    fun isTokenExpired(token: String): Boolean {
        try {
            // Simple JWT token expiration check
            // This is a basic implementation and should be expanded based on your token format
            val parts = token.split(".")
            if (parts.size != 3) return true
            
            val payload = android.util.Base64.decode(parts[1], android.util.Base64.URL_SAFE)
            val payloadJson = String(payload)
            
            // Extract expiration time from payload
            val expirationPattern = "\"exp\":(\\d+)".toRegex()
            val match = expirationPattern.find(payloadJson)
            
            if (match != null) {
                val expTime = match.groupValues[1].toLong()
                val currentTime = System.currentTimeMillis() / 1000
                return currentTime > expTime
            }
            
            return true
        } catch (e: Exception) {
            // If we can't parse the token, consider it expired for safety
            return true
        }
    }
}