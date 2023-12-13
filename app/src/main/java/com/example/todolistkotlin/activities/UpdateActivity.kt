package com.example.todolistkotlin.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.todolistkotlin.R
import com.example.todolistkotlin.models.Product
import com.example.todolistkotlin.utils.DatabaseHelper
import java.io.IOException

class UpdateActivity : AppCompatActivity() {

    private lateinit var et_item: EditText
    private lateinit var et_price: EditText
    private lateinit var et_codebarre: EditText
    private lateinit var bt_update: Button
    private lateinit var bt_add: Button
    private lateinit var page_title: TextView


    private lateinit var ivItemImage: ImageView
    private lateinit var btnSelectImage: Button

    private lateinit var imageByte: ByteArray

    private lateinit var product: Product

    private var databaseHelper = DatabaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        imageByte = ByteArray(0)
        page_title = findViewById(R.id.page_title)
        et_item = findViewById(R.id.et_item)
        et_price = findViewById(R.id.et_price)
        et_codebarre = findViewById(R.id.et_codebarre)

        bt_update = findViewById(R.id.bt_update)
        bt_add = findViewById(R.id.bt_add)

        ivItemImage = findViewById(R.id.iv_item_image)
        btnSelectImage = findViewById(R.id.btn_select_image)




        val id = intent.getIntExtra("id", -1)

        if (id != -1) {
            product = databaseHelper.getProduct(id)!!
            page_title.text = "Modifier un produit"
            et_item.setText(product.Libelle)
            et_price.setText(product.PrixVente.toString())
            et_codebarre.setText(product.Codebarre)
            imageByte = product.Photo!!
            ivItemImage.setImageBitmap(convertByteArrayToImage(imageByte))
            bt_add.visibility = Button.GONE
        } else {
            page_title.text = "Ajouter un produit"
            bt_update.visibility = Button.GONE
        }

        btnSelectImage.setOnClickListener {
            openFileExplorer()
        }

        bt_add.setOnClickListener {
            if (et_item.text.isEmpty()|| et_price.text.isEmpty()|| imageByte.isEmpty()) {
                val emptyNameBuilder = AlertDialog.Builder(this@UpdateActivity)
                emptyNameBuilder.setTitle("Champs vides")
                emptyNameBuilder.setMessage("S'il vous plaît, remplissez tous les champs y compris l'image.")
                emptyNameBuilder.setPositiveButton("OK") { _, _ ->
                    // Do nothing, just close the dialog
                }
                emptyNameBuilder.create().show()
                return@setOnClickListener
            }
            et_item = findViewById(R.id.et_item)
            et_price = findViewById(R.id.et_price)
            et_codebarre = findViewById(R.id.et_codebarre)

            val itemName: String = et_item.text.toString()
            val itemPrice: Double = et_price.text.toString().toDouble()
            val itemCodebarre: String = et_codebarre.text.toString()
            product = Product(Id = null,Libelle = itemName, PrixVente = itemPrice, Codebarre = itemCodebarre, Photo = imageByte)
            DatabaseHelper(this).addProduct(product)
            et_item.setText("")
            et_price.setText("")
            et_codebarre.setText("")
            Toast.makeText(this, "Item added", Toast.LENGTH_SHORT).show()
            setResult(RESULT_OK)
            finish()
        }

        bt_update.setOnClickListener {
            if (et_item.text.isEmpty()|| et_price.text.isEmpty() || imageByte.isEmpty()) {
                val emptyNameBuilder = AlertDialog.Builder(this@UpdateActivity)
                emptyNameBuilder.setTitle("Champs vides")
                emptyNameBuilder.setMessage("S'il vous plaît, remplissez tous les champs.")
                emptyNameBuilder.setPositiveButton("OK") { _, _ ->
                    // Do nothing, just close the dialog
                }
                emptyNameBuilder.create().show()
                return@setOnClickListener
            }
            et_item = findViewById(R.id.et_item)
            et_price = findViewById(R.id.et_price)
            et_codebarre = findViewById(R.id.et_codebarre)
            val itemName: String = et_item.text.toString()
            val itemPrice: Double = et_price.text.toString().toDouble()
            val itemCodebarre: String = et_codebarre.text.toString()
            product = Product(Id = id,Libelle = itemName, PrixVente = itemPrice, Codebarre = itemCodebarre, Photo = imageByte)
            databaseHelper.updateProduct(product)
            et_item.setText("")
            et_price.setText("")
            Toast.makeText(this, "Item updated", Toast.LENGTH_SHORT).show()
            setResult(RESULT_OK)
            finish()
        }
    }


    private fun convertImageToByteArray(imageUri: Uri): ByteArray? {
        try {
            val inputStream = contentResolver.openInputStream(imageUri)
            return inputStream?.readBytes()
        } catch (e: IOException) {
            Log.e("UpdateActivity", "Error converting image to byte array", e)
        }
        return null
    }

    private fun convertByteArrayToImage(imageByteArray: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
    }

    private fun openFileExplorer() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    companion object {
        const val PICK_IMAGE_REQUEST = 1
    }

    // Existing code...

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Handle the selected image
            val selectedImageUri: Uri = data.data!!

            // Set the image in the ImageView
            ivItemImage.setImageURI(selectedImageUri)
            imageByte = convertImageToByteArray(selectedImageUri)!!
        }
    }
}
