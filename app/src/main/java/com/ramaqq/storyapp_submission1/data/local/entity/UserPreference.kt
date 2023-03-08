package com.ramaqq.storyapp_submission1.data.local.entity

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ramaqq.storyapp_submission1.data.response.LoginResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val USER_KEY = stringPreferencesKey("name")
        private val USER_ID = stringPreferencesKey("email")
        private val TOKEN_KEY = stringPreferencesKey("password")
        private val STATE_KEY = booleanPreferencesKey("state")
        private val EMAIL = stringPreferencesKey("email_")

        // Singleton pattern
        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

    // get data
    fun getUser(): Flow<UserModel> {
        return dataStore.data.map { value ->
            UserModel(
                value[USER_KEY] ?:"",
                value[USER_ID] ?:"",
                value[TOKEN_KEY] ?:"",
                value[STATE_KEY] ?: false
            )
        }
    }

    fun getEmail(): Flow<String>{
        return dataStore.data.map {
            it[EMAIL] ?: ""
        }

    }

    // save data
    suspend fun saveUser(user: LoginResult) {
        dataStore.edit { preferences ->
            preferences[USER_KEY] = user.userId
            preferences[USER_ID] = user.name
            preferences[TOKEN_KEY] = user.token
            preferences[STATE_KEY] = true
        }
    }

    suspend fun saveEmail(email: String){
        dataStore.edit {
            it [EMAIL] = email
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }




   /* fun getUserData(): Flow<LoginResult> {
        return dataStore.data.map { value ->
            LoginResult(
                value[USER_KEY] ?:"",
                value[USER_ID] ?:"",
                value[TOKEN_KEY] ?:"",
            )
        }
    }
    suspend fun saveUserData(user: LoginResult) {
        dataStore.edit { preferences ->
            if (user.userId != null)
                preferences[USER_KEY] = user.userId
            if (user.name != null)
                preferences[USER_ID] = user.name
            if (user.token != null)
                preferences[TOKEN_KEY] = user.token
            preferences[STATE_KEY] = true
        }
    }*/

}