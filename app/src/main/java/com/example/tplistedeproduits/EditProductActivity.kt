package com.example.tplistedeproduits

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.tplistedeproduits.models.Product
import android.widget.Button


// EditProductActivity.kt
class EditProductActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product)

        val productList: MutableList<Product> = mutableListOf()


        val productNameEditText: EditText = findViewById(R.id.productNameEditText)
        val productPriceEditText: EditText = findViewById(R.id.productPriceEditText)
        val saveButton: Button = findViewById(R.id.saveButton)

        saveButton.setOnClickListener {
            val productName = productNameEditText.text.toString()
            val productPrice = productPriceEditText.text.toString().toDouble()

            // Ajoutez le nouveau produit à la liste (ou modifiez un existant)
            // (Notez que ceci est une implémentation très basique et non persistante)
            val product = Product(productName, productPrice)
            // Ajoutez le produit à la liste ou mettez à jour s'il existe déjà
            productList.find { it.name == productName }?.let {
                it.price = productPrice
            } ?: productList.add(product)

            // Terminez l'activité pour revenir à la liste des produits
            finish()
        }
    }
}
