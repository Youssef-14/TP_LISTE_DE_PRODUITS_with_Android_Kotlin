package com.example.todolistkotlin.utils

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.todolistkotlin.models.Product

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "product_database"

        private const val TABLE_PRODUCTS = "products"
        private const val KEY_ID = "_id"
        private const val KEY_NAME = "name"
        private const val KEY_PRICE = "price"
        private const val KEY_Codebarre = "codebarre"
        private const val KEY_PHOTO = "photo"


        // Add other fields as needed
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableProducts = "CREATE TABLE $TABLE_PRODUCTS ($KEY_ID INTEGER PRIMARY KEY, $KEY_NAME TEXT, $KEY_PRICE REAL, $KEY_Codebarre TEXT, $KEY_PHOTO BLOB)"

        db.execSQL(createTableProducts)

    }



    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTS")
        onCreate(db)
    }

    fun addProduct(product: Product): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_NAME, product.Libelle)
        values.put(KEY_PRICE, product.PrixVente)
        values.put(KEY_Codebarre, product.Codebarre)
        values.put(KEY_PHOTO, product.Photo)
        // Add other fields as needed

        return db.insert(TABLE_PRODUCTS, null, values)
    }

    fun getAllProducts(): List<Product> {
        val productList = mutableListOf<Product>()
        val selectQuery = "SELECT * FROM $TABLE_PRODUCTS"

        try {
            val db = this.readableDatabase
            val cursor: Cursor = db.rawQuery(selectQuery, null)

            val nameIndex = cursor.getColumnIndex(KEY_NAME)
            val priceIndex = cursor.getColumnIndex(KEY_PRICE)
            val idIndex = cursor.getColumnIndex(KEY_ID)
            val codebarreIndex = cursor.getColumnIndex(KEY_Codebarre)
            val photoIndex = cursor.getColumnIndex(KEY_PHOTO)



            while (cursor.moveToNext()) {
                if (nameIndex != -1 && priceIndex != -1) {
                    val product = Product(
                        cursor.getInt(idIndex),
                        cursor.getString(nameIndex),
                        cursor.getDouble(priceIndex),
                        cursor.getString(codebarreIndex),
                        cursor.getBlob(photoIndex)
                        // Retrieve other fields as needed
                    )
                    productList.add(product)
                } else {
                    // Handle the case where columns are not found
                    // Log an error, throw an exception, or handle it as appropriate for your app
                }
            }

            cursor.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }

        return productList
    }

    fun updateProduct(product: Product): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_NAME, product.Libelle)
        values.put(KEY_PRICE, product.PrixVente)
        values.put(KEY_Codebarre, product.Codebarre)
        values.put(KEY_PHOTO, product.Photo)

        return db.update(
            TABLE_PRODUCTS,
            values,
            "$KEY_ID = ?",
            arrayOf(product.id.toString())
        )
    }

    fun deleteProduct(product: Product): Int {
        val db = this.writableDatabase

        return db.delete(
            TABLE_PRODUCTS,
            "$KEY_ID = ?",
            arrayOf(product.id.toString())
        )
    }

    fun getProduct(id: Int): Product? {
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_PRODUCTS WHERE $KEY_ID = $id"
        val cursor: Cursor = db.rawQuery(selectQuery, null)

        val nameIndex = cursor.getColumnIndex(KEY_NAME)
        val priceIndex = cursor.getColumnIndex(KEY_PRICE)
        val idIndex = cursor.getColumnIndex(KEY_ID)
        val availableIndex = cursor.getColumnIndex(KEY_Codebarre)
        val photoIndex = cursor.getColumnIndex(KEY_PHOTO)

        if (cursor.moveToNext()) {
            if (nameIndex != -1 && priceIndex != -1) {
                val product = Product(
                    cursor.getInt(idIndex),
                    cursor.getString(nameIndex),
                    cursor.getDouble(priceIndex),
                    cursor.getString(availableIndex),
                    cursor.getBlob(photoIndex)
                )
                cursor.close()
                return product
            } else {
                // Handle the case where columns are not found
                // Log an error, throw an exception, or handle it as appropriate for your app
            }
        }

        return null
    }

}
