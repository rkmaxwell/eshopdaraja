package com.app.eshopdaraja.mvvm

import com.app.eshopdaraja.api.ApiService
import com.app.eshopdaraja.model.ProductList
import javax.inject.Inject

class ProductRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getProducts(): ProductList {
        return apiService.getProducts()
    }
}