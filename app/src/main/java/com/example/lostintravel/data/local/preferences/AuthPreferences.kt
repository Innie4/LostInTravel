package com.example.lostintravel.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.lostintravel.domain.model.AuthState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_preferences")

@Singleton
class AuthPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()
    
    private val encryptedSharedPreferences = EncryptedSharedPreferences.create(
        context,
        "encrypted_auth_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    
    companion object {
        private val IS_AUTHENTICATED = booleanPreferencesKey("is_authenticated")
        private val USER_ID = stringPreferencesKey("user_id")
        private val DISPLAY_NAME = stringPreferencesKey("display_name")
        private val EMAIL = stringPreferencesKey("email")
        private val PHOTO_URL = stringPreferencesKey("photo_url")
        private val ID_TOKEN = stringPreferencesKey("id_token")
    }
    
    val authStateFlow: Flow<AuthState> = context.dataStore.data.map { preferences ->
        AuthState(
            isAuthenticated = preferences[IS_AUTHENTICATED] ?: false,
            userId = preferences[USER_ID],
            displayName = preferences[DISPLAY_NAME],
            email = preferences[EMAIL],
            photoUrl = preferences[PHOTO_URL]
        )
    }
    
    suspend fun updateAuthState(authState: AuthState) {
        context.dataStore.edit { preferences ->
            preferences[IS_AUTHENTICATED] = authState.isAuthenticated
            authState.userId?.let { preferences[USER_ID] = it }
            authState.displayName?.let { preferences[DISPLAY_NAME] = it }
            authState.email?.let { preferences[EMAIL] = it }
            authState.photoUrl?.let { preferences[PHOTO_URL] = it }
        }
    }
    
    suspend fun clearAuthState() {
        context.dataStore.edit { preferences ->
            preferences[IS_AUTHENTICATED] = false
            preferences.remove(USER_ID)
            preferences.remove(DISPLAY_NAME)
            preferences.remove(EMAIL)
            preferences.remove(PHOTO_URL)
        }
    }
    
    fun saveIdToken(token: String) {
        encryptedSharedPreferences.edit().putString(ID_TOKEN.name, token).apply()
    }
    
    fun getIdToken(): String? {
        return encryptedSharedPreferences.getString(ID_TOKEN.name, null)
    }
    
    fun clearIdToken() {
        encryptedSharedPreferences.edit().remove(ID_TOKEN.name).apply()
    }
}