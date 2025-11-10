package com.fomaxtro.core.data.session

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.fomaxtro.core.data.session.model.CartItemSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import timber.log.Timber

class SessionStorage(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val CART_ITEMS_KEY = stringPreferencesKey("cart_items")
    }

    suspend fun upsertCartItem(item: CartItemSession) {
        val cartItems = getCartItems().first()

        dataStore.edit { preferences ->
            val mutableCartItems = cartItems.toMutableList()
            val cartItemIndex = mutableCartItems.indexOfFirst { item.id == it.id }

            if (cartItemIndex != -1) {
                mutableCartItems[cartItemIndex] = item
            } else {
                mutableCartItems += item
            }

            preferences[CART_ITEMS_KEY] = Json.encodeToString(mutableCartItems.toList())
        }
    }

    suspend fun removeCartItem(cartId: String) {
        val cartItems = getCartItems().first()

        dataStore.edit { preferences ->
            val newCartItems = cartItems.filterNot { it.id == cartId }

            preferences[CART_ITEMS_KEY] = Json.encodeToString(newCartItems)
        }
    }

    fun getCartItems(): Flow<List<CartItemSession>> {
        return dataStore.data
            .map { preferences ->
                preferences[CART_ITEMS_KEY]?.let { cartItems ->
                    try {
                        Json.decodeFromString<List<CartItemSession>>(cartItems)
                    } catch (e: SerializationException) {
                        Timber.tag("SessionStorage").e(e)

                        emptyList()
                    }
                } ?: emptyList()
            }
            .distinctUntilChanged()
    }
}