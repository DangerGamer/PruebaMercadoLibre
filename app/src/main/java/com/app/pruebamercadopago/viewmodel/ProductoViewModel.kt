package com.app.pruebamercadopago.viewmodel

import ProductoModel
import Results
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.pruebamercadopago.viewmodel.adapters.AdapterProducts
import com.app.pruebamercadopago.viewmodel.services.ProductoService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductoViewModel(): ViewModel() {
    private val _prods = MutableLiveData<List<Results>>()
    val products: LiveData<List<Results>> get() = _prods
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    private fun getRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://api.mercadolibre.com/sites/MCO/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun searchProducts(name: String){
        val nameTrim = name.trimEnd().replace(" ","%20")
        viewModelScope.launch(Dispatchers.IO){
            try {
                val call = getRetrofit().create(ProductoService::class.java).searchProduct("search?q=$nameTrim")
                val prods = call.body()?.results ?: emptyList()
                if (call.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        _prods.value = prods
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        _message.value = "No hay datos"
                    }
                    Log.i("isNotSuccessful",call.code().toString())
                }
            } catch (e: Exception) {
                Log.e("searchProducts",e.message.toString())
                e.printStackTrace()
            }
        }
    }
}

