package com.example.tplistedeproduits
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tplistedeproduits.models.Product

class MainActivity : AppCompatActivity() {

    private val productList = mutableListOf<Product>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var addButton: Button
    private lateinit var editButton: Button
    private lateinit var deleteButton: Button
    private lateinit var gridView: GridView
    private var selectedProduct = 0;

    // Adapter for RecyclerView
    private lateinit var productAdapter: ProductAdapter

    // Adapter for GridView
    private lateinit var gridAdapter: GridAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        addButton = findViewById(R.id.addButton)
        editButton = findViewById(R.id.editButton)
        deleteButton = findViewById(R.id.deleteButton)
        gridView = findViewById(R.id.gridView)

        // Initialize and configure the RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        productAdapter = ProductAdapter(productList)
        recyclerView.adapter = productAdapter

        // Initialize and configure the GridView
        gridAdapter = GridAdapter(this, productList)
        gridView.adapter = gridAdapter

        // Add an event listener to the "Add" button
        addButton.setOnClickListener {
            // Logic to add a product
            // For demonstration purposes, let's add a sample product
            // nombre aleatoire entre 0 et 100
            val random = (0..100).random()
            val newProduct = Product("Product $random", random.toDouble())
            productList.add(newProduct)

            // Notify the adapters about the data change
            productAdapter.notifyDataSetChanged()
            gridAdapter.notifyDataSetChanged()
        }

        // Add an event listener to the "Edit" button
        editButton.setOnClickListener {
            // Logic to redirect to the edit product activity
            // For demonstration purposes, let's assume you have an EditProductActivity
            val intent = Intent(this, EditProductActivity::class.java)
            startActivity(intent)
        }

        // Add an event listener to the "Delete" button
        deleteButton.setOnClickListener {
            // Logic to delete a product
            if (productList.isNotEmpty()) {
                // For demonstration purposes, let's remove the last product
                productList.removeAt(selectedProduct)

                // Notify the adapters about the data change
                productAdapter.notifyDataSetChanged()
                gridAdapter.notifyDataSetChanged()
            }
        }

        // Add an item click listener to the GridView for product selection
        gridView.setOnItemClickListener { _, _, position, _ ->
            selectedProduct = position
            // Logic to select a product
            // For demonstration purposes, let's just display the selected product's name
            val product = productList[position]
            println("Selected product: ${product.name}")

        }
    }
}
