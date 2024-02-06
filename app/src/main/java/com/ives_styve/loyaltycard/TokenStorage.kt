package com.ives_styve.loyaltycard

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

private val Context.tokenStore by preferencesDataStore(name = "token")
class TokenStorage(private var context: Context) {

    private var tokenKey = stringPreferencesKey("token")
    private var exprirationKey = longPreferencesKey("expiration")

    suspend fun write(token: String?, expiration: Long){
        this.context.tokenStore.edit { preferences -> preferences[tokenKey] = token.toString() }
        this.context.tokenStore.edit { preferences -> preferences[exprirationKey] = expiration }
    }

    suspend fun read(): String{
        val data = this.context.tokenStore.data.firstOrNull()?.get(tokenKey)
        return data.toString()
    }

    suspend fun readExpiration(): Long {
        val data = this.context.tokenStore.data.first()[exprirationKey] ?: 0
        return data.toLong()
    }
}