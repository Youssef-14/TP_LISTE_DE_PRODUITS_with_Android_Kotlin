package com.example.todolistkotlin.activities

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.example.todolistkotlin.R
import com.example.todolistkotlin.adapters.CustomArrayAdapter
import com.example.todolistkotlin.models.Product
import com.example.todolistkotlin.utils.DatabaseHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    lateinit var bt_add: Button
    lateinit var bt_delete: Button
    lateinit var bt_update: Button
    lateinit var listView: ListView


    var selectedPosition: Int? = null

    var productList = ArrayList<Product>()
    var databaseHelper = DatabaseHelper(this)

    private val updateActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            // Update the list in the main activity
            productList = databaseHelper.getAllProducts() as ArrayList<Product>
            val customAdapter = CustomArrayAdapter(this,
                R.layout.list_item_layout,
                R.id.item_text, productList)
            listView.adapter = customAdapter
        }
        selectedPosition?.let { listView.setItemChecked(it, false) }
        selectedPosition = null
        bt_update.isEnabled = false
        bt_delete.isEnabled = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bt_add = findViewById(R.id.bt_add)
        bt_delete = findViewById(R.id.bt_delete)
        bt_update = findViewById(R.id.bt_update)
        listView = findViewById(R.id.list)

        bt_update.isEnabled = false
        bt_delete.isEnabled = false

        productList = databaseHelper.getAllProducts() as ArrayList<Product>
        val customAdapter = CustomArrayAdapter(this,
            R.layout.list_item_layout,
            R.id.item_text, productList)
        listView.adapter = customAdapter


        bt_add.setOnClickListener {
            val intent = Intent(this, UpdateActivity::class.java)
            updateActivityResultLauncher.launch(intent)
        }

        bt_update.setOnClickListener {
            if (selectedPosition == null) {
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle("No Item Selected")
                builder.setMessage("Please select an item to update.$selectedPosition")
                builder.setPositiveButton("OK") { _, _ ->
                    // Do nothing, just close the dialog
                }
                builder.create().show()
                return@setOnClickListener
            }
            val intent = Intent(this, UpdateActivity::class.java)
            intent.putExtra("id", productList[selectedPosition!!].id)
            updateActivityResultLauncher.launch(intent)
        }

        bt_delete.setOnClickListener {
            if (selectedPosition != null) {
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle("Delete Item")
                builder.setMessage("Are you sure you want to delete this item?")

                builder.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
                    runOnUiThread {
                        // Update the adapter data and notify the change on the UI thread
                        databaseHelper.deleteProduct(productList[selectedPosition!!])
                        productList.removeAt(selectedPosition!!)
                        customAdapter.notifyDataSetChanged()
                        listView.adapter = customAdapter
                        selectedPosition = null
                    }
                }

                builder.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int ->
                    // Do nothing if "No" is clicked
                }

                val alertDialog: AlertDialog = builder.create()
                alertDialog.show()
            } else {
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle("No Item Selected")
                builder.setMessage("Please select an item to delete.")
                builder.setPositiveButton("OK") { _, _ ->
                    // Do nothing, just close the dialog
                }
                builder.create().show()
            }
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            //select or unselect item
            if (selectedPosition == position) {
                listView.setItemChecked(position, false)
                selectedPosition = null
            } else {
                listView.setItemChecked(position, true)
                selectedPosition = position
            }
            // Enable or disable buttons based on selectedPosition
            bt_update.isEnabled = selectedPosition != null
            bt_delete.isEnabled = selectedPosition != null
        }
    }
}
