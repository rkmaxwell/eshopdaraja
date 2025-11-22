package com.app.eshopdaraja.repository

import com.app.eshopdaraja.dao.CartDao
import com.app.eshopdaraja.model.CartItem
import kotlinx.coroutines.flow.Flow

class CartRepo(private val cartDao: CartDao) {

    val cartItems:Flow<List<CartItem>> = cartDao.getAllCartItems()
    suspend fun addItem(item: CartItem) = cartDao.insertCartItem(item)
    suspend fun updateItem(item: CartItem) = cartDao.updateCartItem(item)
    suspend fun deleteItem(item: CartItem) = cartDao.deleteCartItem(item)
    suspend fun clearCart() = cartDao.clearCart()

    suspend fun deleteByProductId(productId: Int) = cartDao.deleteByProductId(productId)


}