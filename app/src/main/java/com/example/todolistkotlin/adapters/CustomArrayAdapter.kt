package com.example.todolistkotlin.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.todolistkotlin.models.Product
import com.example.todolistkotlin.R
import com.example.todolistkotlin.utils.DatabaseHelper


class CustomArrayAdapter(context: Context, resource: Int, textViewResourceId: Int, objects: List<Product>) :
    ArrayAdapter<Product>(context, resource, textViewResourceId, objects) {

    private var databaseHelper = DatabaseHelper(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = inflater.inflate(R.layout.list_item_layout, parent, false)

        // Get references to the views in your custom layout
        val imageView = rowView.findViewById<ImageView>(R.id.item_image)
        val textView = rowView.findViewById<TextView>(R.id.item_text)
        val libelleView = rowView.findViewById<TextView>(R.id.item_libelle)

        // Set data for each item
        val product = getItem(position)

        // Modify text color and size
        libelleView.apply {
            text = product?.Libelle
            textSize = 20f
        }

        textView.text = "\n${product?.PrixVente}€\n${product?.Codebarre}"

        // Convert byte array to bitmap and set in the image view
        imageView.setImageBitmap(convertByteArrayToImage(product?.Photo!!))

        return rowView
    }

    private fun convertByteArrayToImage(imageByteArray: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
    }
}
