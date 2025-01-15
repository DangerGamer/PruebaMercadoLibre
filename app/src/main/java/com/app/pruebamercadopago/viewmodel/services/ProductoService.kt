package com.app.pruebamercadopago.viewmodel.services

import ProductoModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ProductoService {
    @GET
    suspend fun searchProduct(@Url url: String): Response<ProductoModel>
}