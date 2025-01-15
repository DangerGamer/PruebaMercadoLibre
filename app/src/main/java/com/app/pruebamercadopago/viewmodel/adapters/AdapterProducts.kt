package com.app.pruebamercadopago.viewmodel.adapters

import Results
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.pruebamercadopago.R
import com.app.pruebamercadopago.viewmodel.adapters.viewholders.ProductViewHolder

//administracion de click para items del recyclerview
interface OnItemClickListener {
    fun onItemClick(product: Results)
}

class AdapterProducts(
    private val productos: List<Results>,
    private val listener: OnItemClickListener
): RecyclerView.Adapter<ProductViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ProductViewHolder(layoutInflater.inflate(R.layout.item_product, parent, false))
    }

    override fun getItemCount(): Int {
        return productos.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = productos[position]
        holder.bind(item, listener)
    }
}