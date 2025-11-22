package com.app.eshopdaraja.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem (

    @PrimaryKey(autoGenerate = true)
    val id:Int =0,
    val productId:Int,
    val title:String,
    val price:Double,
    val imageUrl:String,
    val quantity:Int

)