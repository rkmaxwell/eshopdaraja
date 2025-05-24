package com.app.eshopdaraja.api

import com.app.eshopdaraja.utlis.Constants.Companion.List_Of_product
import com.app.eshopdaraja.model.ProductList
import retrofit2.http.GET

interface ApiService {
    @GET(List_Of_product)
    suspend fun getProducts(): ProductList
}
