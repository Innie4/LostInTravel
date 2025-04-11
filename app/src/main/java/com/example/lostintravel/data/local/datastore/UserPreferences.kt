package com.example.lostintravel.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.lostintravel.data.local.entity.UserEntity
import com.example.lostintravel.domain.model.User
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferences @Inject constructor(
    private val context: Context
) {
    private val gson = Gson()
    
    companion object {
        private val USER_KEY = stringPreferencesKey("user")
    }
    
    suspend fun saveUser(user: User) {
        val userEntity = UserEntity(
            id = user.id,
            name = user.name,
            email = user.email,
            profilePictureUrl = user.profilePictureUrl,
            favoriteDestinations = user.favoriteDestinations,
            visitedDestinations = user.visitedDestinations
        )
        
        val userJson = gson.toJson(userEntity)
        context.dataStore.edit { preferences ->
            preferences[USER_KEY] = userJson
        }
    }
    
    suspend fun getUser(): User? {
        val userJson = context.dataStore.data.map { preferences ->
            preferences[USER_KEY]
        }.firstOrNull() ?: return null
        
        return try {
            val userEntity = gson.fromJson(userJson, UserEntity::class.java)
            userEntity.toDomain()
        } catch (e: Exception) {
            null
        }
    }
    
    fun getUserFlow(): Flow<User?> {
        return context.dataStore.data.map { preferences ->
            val userJson = preferences[USER_KEY] ?: return@map null
            try {
                val userEntity = gson.fromJson(userJson, UserEntity::class.java)
                userEntity.toDomain()
            } catch (e: Exception) {
                null
            }
        }
    }
    
    suspend fun clearUser() {
        context.dataStore.edit { preferences ->
            preferences.remove(USER_KEY)
        }
    }
}