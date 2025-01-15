package com.app.pruebamercadopago.view

import Results
import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.pruebamercadopago.databinding.ActivityHomeViewBinding
import com.app.pruebamercadopago.viewmodel.ProductoViewModel
import com.app.pruebamercadopago.viewmodel.adapters.AdapterProducts
import com.app.pruebamercadopago.viewmodel.adapters.OnItemClickListener
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

class HomeView : AppCompatActivity(), OnClickListener, OnItemClickListener {
    private lateinit var binding: ActivityHomeViewBinding
    private lateinit var productoViewModel: ProductoViewModel
    private lateinit var adapterProduct: AdapterProducts
    private var products = mutableListOf<Results>()
    private var locationData = ""
    private val REQUEST_CODE_LOCATION = 101
    private var message = ""
    private var busqueda = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeViewBinding.inflate(layoutInflater)
        binding.lyBusqueda.imFiltrar.setOnClickListener(this)
        setContentView(binding.root)

        productoViewModel = ViewModelProvider(this)[ProductoViewModel::class.java]

        //Validacion de intent para reutilizacion de campo de busqueda
        busqueda = intent.getStringExtra("busqueda") ?: ""
        if(!busqueda.isEmpty()) executeSearch()

        //observadores del view model para ejecutar cambios de UI
        productoViewModel.products.observe(this, Observer{
            newList -> products = newList.toMutableList()
            adapterProduct = AdapterProducts(products, this)
            binding.rvProductos.layoutManager = LinearLayoutManager(this)
            binding.rvProductos.adapter = adapterProduct
        })
        productoViewModel.message.observe(this, Observer {
            newMessage -> message = newMessage
            if(message.isNotEmpty()) showMessage(message)
        })

        checkAndRequestLocationPermission()

    }

    //evento de busqueda de productos
    override fun onClick(v: View?) {
        busqueda = binding.lyBusqueda.etBusqueda.text.toString()
        if (!busqueda.isNullOrEmpty()) executeSearch()
    }

    //ejecusion de busqueda servicio de productos
    private fun executeSearch(){
        binding.tvSinDatos.visibility = View.GONE
        productoViewModel.searchProducts(busqueda)
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let { imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0) }
        showCharge()
    }

    //Alerta para visualizacion de errores para el usuario
    private fun showMessage(text: String) {
        AlertDialog.Builder(this)
            .setTitle("Alerta")
            .setMessage(text)
            .setPositiveButton("Ok") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    //Pantalla de cargue UI
    private fun showCharge(){
        binding.rvProductos.visibility = View.GONE
        binding.progress.visibility = View.VISIBLE
        lifecycleScope.launch {
            delay(2000)
            binding.progress.visibility = View.GONE
            binding.rvProductos.visibility = View.VISIBLE
        }
    }

    //envio de producto a view de detalle
    override fun onItemClick(product: Results) {
        val productJson = Gson().toJson(product)
        val intent = Intent(this, DetailsView::class.java)
        intent.putExtra("product", productJson)
        intent.putExtra("location", locationData)
        startActivity(intent)
    }

    private fun checkAndRequestLocationPermission() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    getCityAndState(location.latitude, location.longitude)
                } else {
                    Log.e("LocationService", "No se pudo obtener la ubicación")
                }
            }

        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION
            )
        }
    }

    private fun getCityAndState(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(this, Locale.getDefault())

        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                binding.lyBusqueda.lyUbicacion.visibility = View.VISIBLE
                locationData = "${addresses[0].locality ?: ""}, ${addresses[0].adminArea ?: ""}"
                binding.lyBusqueda.tvUbicacion.text = locationData
            } else {
                Log.e("LocationService", "No se encontró información de la ubicación")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("LocationService", "Error al obtener información de la ubicación")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkAndRequestLocationPermission()
                } else {
                    Toast.makeText(this, "El acceso a la ubicación es requerido", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}