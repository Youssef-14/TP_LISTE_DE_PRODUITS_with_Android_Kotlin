package com.example.tplistedeproduits

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.tplistedeproduits.models.Product

class GridAdapter(private val context: Context, private val productList: List<Product>) : BaseAdapter() {

    override fun getCount(): Int {
        return productList.size
    }

    override fun getItem(position: Int): Any {
        return productList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        val holder: ViewHolder

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        val product = productList[position]

        // Set the data to the views in the grid item layout
        holder.productName.text = product.name

        return view!!
    }

    private class ViewHolder(view: View) {
        val productName: TextView = view.findViewById(R.id.gridItemProductName)
        val productImage: ImageView = view.findViewById(R.id.gridItemProductImage)
    }
}
