package com.app.eshopdaraja.di

import android.app.Application
import com.app.eshopdaraja.dao.CartDao
import com.app.eshopdaraja.utlis.AppDatabase
import dagger.hilt.android.HiltAndroidApp
import android.content.Context
import androidx.room.Room
import com.app.eshopdaraja.repository.CartRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase =
        Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "eshop_db"
        ).build()

    @Provides
    fun provideCartDao(db: AppDatabase): CartDao = db.cartDao()

    @Provides
    @Singleton
    fun provideCartRepository(dao: CartDao): CartRepo = CartRepo(dao)
}
