package com.app.pruebamercadopago.viewmodel.adapters.viewholders

import ProductoModel
import Results
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.app.pruebamercadopago.databinding.ItemProductBinding
import com.app.pruebamercadopago.viewmodel.adapters.OnItemClickListener
import com.squareup.picasso.Picasso
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class ProductViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = ItemProductBinding.bind(view)
    fun bind(product: Results, listener: OnItemClickListener){
        binding.root.setOnClickListener{
            listener.onItemClick(product)
        }

        val symbols = DecimalFormatSymbols(Locale.getDefault())
        symbols.groupingSeparator = '.'
        val formatter = DecimalFormat("#,###", symbols)

        try {
            Picasso.get().load(product.thumbnail.replace("http:","https:")).into(binding.imgProducto)
            binding.categoria.text = product.category_id
            binding.nombre.text = product.title
            binding.precio.text = "$${formatter.format(product.price)} ${product.currency_id}"
        }catch (e: Exception){
            Log.e("ProductViewHolder",e.message.toString())
            e.printStackTrace()
        }

    }
}