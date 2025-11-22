package com.app.eshopdaraja.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.app.eshopdaraja.model.CartItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Query("SELECT *, COUNT(id) as quantity FROM cart_items GROUP BY productId")
    fun getAllCartItems():Flow<List<CartItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: CartItem)

    @Update
    suspend fun updateCartItem(item: CartItem)
    @Delete
    suspend fun deleteCartItem(item: CartItem)
    @Query("DELETE FROM cart_items")
    suspend fun clearCart()

    @Query("SELECT MAX(quantity) FROM cart_items WHERE productId = :id LIMIT 1")
    fun getQuantity(id:Int): Flow<Int?>

    @Query("DELETE FROM cart_items WHERE productId = :productId")
    suspend fun deleteByProductId(productId: Int)

}