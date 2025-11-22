package com.app.eshopdaraja.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.eshopdaraja.dao.CartDao
import com.app.eshopdaraja.model.CartItem
import com.app.eshopdaraja.repository.CartRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepo: CartRepo,
    private val cartDao: CartDao
):ViewModel() {


    val cartItems = cartRepo.cartItems
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addToCart(item: CartItem){
        viewModelScope.launch { cartRepo.addItem(item) }
    }
    fun updateCart(item: CartItem){
        viewModelScope.launch { cartRepo.updateItem(item) }
    }

    fun removeItem(item: CartItem){
        viewModelScope.launch { cartRepo.deleteItem(item) }
    }

    fun clearCart(){

        viewModelScope.launch { cartRepo.clearCart() }
    }

    fun getQuantity(productId: Int): Int {
        return cartItems.value.firstOrNull { it.productId == productId }?.quantity ?: 0
    }

    fun removeItemByProductId(productId: Int) {
        viewModelScope.launch {
            cartRepo.deleteByProductId(productId)
        }
    }



    fun cartQuantity(productId: Int): StateFlow<Int> =
        cartDao.getQuantity(productId)
            .map { it ?: 0 }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                0
            )

}