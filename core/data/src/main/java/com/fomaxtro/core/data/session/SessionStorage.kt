package com.fomaxtro.core.data.session

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.fomaxtro.core.data.session.model.CartItemSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
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

    private fun getCartItems(cartItemPreferences: String?): List<CartItemSession> {
        return cartItemPreferences?.let {
            Json.decodeFromString<List<CartItemSession>>(it)
        } ?: emptyList()
    }

    suspend fun addCartItem(item: CartItemSession) {
        dataStore.edit { preferences ->
            val cartItems = getCartItems(preferences[CART_ITEMS_KEY])

            preferences[CART_ITEMS_KEY] = Json.encodeToString(cartItems + item)
        }
    }

    suspend fun saveCartItems(items: List<CartItemSession>) {
        dataStore.edit { preferences ->
            preferences[CART_ITEMS_KEY] = Json.encodeToString(items)
        }
    }

    suspend fun updateCartItem(item: CartItemSession) {
        dataStore.edit { preferences ->
            val cartItems = getCartItems(preferences[CART_ITEMS_KEY])
                .toMutableList()
            val cartItemIndex = cartItems.indexOfFirst { it.id == item.id }

            if (cartItemIndex != -1) {
                cartItems[cartItemIndex] = item

                preferences[CART_ITEMS_KEY] = Json.encodeToString(cartItems)
            }
        }
    }

    suspend fun removeCartItem(cartId: String) {
        dataStore.edit { preferences ->
            val cartItems = getCartItems(preferences[CART_ITEMS_KEY])
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

    suspend fun clearCartItems() {
        dataStore.edit { preferences ->
            preferences.remove(CART_ITEMS_KEY)
        }
    }
}