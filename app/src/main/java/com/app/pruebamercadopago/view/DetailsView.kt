package com.app.pruebamercadopago.view

import Attribute
import Results
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.view.View
import android.view.View.OnClickListener
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.app.pruebamercadopago.databinding.ActivityDetailsViewBinding
import com.app.pruebamercadopago.viewmodel.ProductoViewModel
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class DetailsView : AppCompatActivity(), OnClickListener{
    private lateinit var binding: ActivityDetailsViewBinding
    private lateinit var productoViewModel: ProductoViewModel
    private lateinit var productoSeleccionado: Results

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        productoViewModel = ViewModelProvider(this)[ProductoViewModel::class.java]

        //asignacion de onclick de componentes del layout
        with(binding){
            btnComprar.setOnClickListener(this@DetailsView)
            btnAgregar.setOnClickListener(this@DetailsView)
            lyElegirCantidad.setOnClickListener(this@DetailsView)
            lyBusqueda.imFiltrar.setOnClickListener(this@DetailsView)
        }

        val productJson = intent?.getStringExtra("product")
        val location = intent?.getStringExtra("location")
        productoSeleccionado = Gson().fromJson(productJson, Results::class.java)

        val symbols = DecimalFormatSymbols(Locale.getDefault())
        symbols.groupingSeparator = '.'
        val formatter = DecimalFormat("#,###", symbols)

        with(binding){
            lyBusqueda.lyUbicacion.visibility = View.VISIBLE
            lyBusqueda.tvUbicacion.text = location

            tvNombre.text = productoSeleccionado.title
            Picasso.get().load(productoSeleccionado.thumbnail.replace("http:","https:")).into(ivProducto)
            val precioAnterior = productoSeleccionado.original_price
            if (precioAnterior != 0) {
                val spannable = SpannableString("$ ${formatter.format(precioAnterior)}") //modificacion para texto tachado
                spannable.setSpan(StrikethroughSpan(), 0, spannable.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                tvPrecioAnterior.text = spannable
            }
            tvPrecioActual.text = "$ ${formatter.format(productoSeleccionado.price)}"
            tvCantidadMin.text = "Cantidad: 1"
            if (productoSeleccionado.available_quantity.toInt() > 50) {
                tvCantidadStock.text = "(+50 disponibles)"
            } else {
                tvCantidadStock.text = "(${productoSeleccionado.available_quantity.toInt()} disponibles)"
            }

            for (i in productoSeleccionado.attributes){
                lyContenidoDetalle.addView(createTextView(i))
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun createTextView(attribute: Attribute): TextView{
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        val textView = TextView(this)
        textView.text = "- ${attribute.name}: ${attribute.value_name}"
        textView.layoutParams = layoutParams
        textView.setPaddingRelative(16, 10, 0,0 )
        return textView
    }

    //administracion de eventos onclick
    override fun onClick(v: View?) {
        when(v?.id){
            binding.btnComprar.id -> {
                Toast.makeText(this, "Producto comprado", Toast.LENGTH_SHORT).show()
            }
            binding.btnAgregar.id -> {
                Toast.makeText(this, "Pedido agregado al carrito", Toast.LENGTH_SHORT).show()
            }
            binding.lyElegirCantidad.id -> {
                Toast.makeText(this, "Selección de cantidad a agregar", Toast.LENGTH_SHORT).show()
            }
            binding.lyBusqueda.imFiltrar.id -> {
                val intent = Intent(this, HomeView::class.java)
                intent.putExtra("busqueda",binding.lyBusqueda.etBusqueda.text.toString())
                startActivity(intent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        with(binding){
            btnComprar.setOnClickListener(null)
            btnAgregar.setOnClickListener(null)
            lyElegirCantidad.setOnClickListener(null)
            lyBusqueda.imFiltrar.setOnClickListener(null)
        }
    }
}