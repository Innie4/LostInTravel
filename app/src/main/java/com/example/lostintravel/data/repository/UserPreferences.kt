package com.example.lostintravel.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.lostintravel.domain.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore
    
    private object PreferencesKeys {
        val USER_ID = stringPreferencesKey("user_id")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_PHOTO_URL = stringPreferencesKey("user_photo_url")
    }
    
    val userFlow: Flow<User?> = dataStore.data.map { preferences ->
        val userId = preferences[PreferencesKeys.USER_ID] ?: return@map null
        val userName = preferences[PreferencesKeys.USER_NAME] ?: ""
        val userEmail = preferences[PreferencesKeys.USER_EMAIL] ?: ""
        val userPhotoUrl = preferences[PreferencesKeys.USER_PHOTO_URL]
        
        User(
            id = userId,
            name = userName,
            email = userEmail,
            photoUrl = userPhotoUrl
        )
    }
    
    suspend fun saveUser(user: User) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_ID] = user.id
            preferences[PreferencesKeys.USER_NAME] = user.name
            preferences[PreferencesKeys.USER_EMAIL] = user.email
            user.photoUrl?.let { preferences[PreferencesKeys.USER_PHOTO_URL] = it }
        }
    }
    
    suspend fun clearUser() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}