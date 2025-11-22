package com.app.eshopdaraja.utlis

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.eshopdaraja.dao.CartDao
import com.app.eshopdaraja.model.CartItem

@Database(entities = [CartItem::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun  cartDao():CartDao
}