package grocery.shopping.creator

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import grocery.shopping.R
import grocery.shopping.data.GroceryItems
import grocery.shopping.data.ShoppingRepository.fetchGroceryList
import grocery.shopping.data.ShoppingRepository.fetchMetadataList
import kotlinx.coroutines.launch

private lateinit var listAdapter: ListCreatorAdapter
private lateinit var recyclerView: RecyclerView

class ListCreator : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //  enableEdgeToEdge()
        setContentView(R.layout.list_creator)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.list_maker)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
        // Set up the RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        val listID = intent.getStringExtra("ListID")
        Log.d("ListCreator", "listId: $listID")
        // Initialize the adapter

        val listOfProducts: MutableList<GroceryItems> = mutableListOf()
        val metadataList = grocery.shopping.data.ListInfo()
        listAdapter = ListCreatorAdapter(listOfProducts, metadataList, listID)
        Log.d("ListCreatorSent", "listId: $listID")

        recyclerView.adapter = listAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        if (listID != null) {
            fetchMetadataList(listID) { listFromFirebase ->
                metadataList.listName = listFromFirebase.listName
                metadataList.createdBy = listFromFirebase.createdBy
                Log.d("ListCreator", "listFromFirebase: $listFromFirebase")
                Log.d("ListCreator", "metadataList: $metadataList")


            }

            fetchGroceryList(listID) { listFromFirebase2 ->
                listOfProducts.addAll(listFromFirebase2)
                Log.d("ListCreator", "listFromFirebase2: $listFromFirebase2")
                Log.d("ListCreator", "listOfProducts: $listOfProducts")
                listAdapter.notifyItemInserted(listOfProducts.size)

            }

        }

    }

    //implementing menu in the app
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.list_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        // Check which item was clicked
        return when (item.itemId) {
            // Handle the home button click
            android.R.id.home -> {
                // This acts like the hardware back button
                onBackPressedDispatcher.onBackPressed()
                true
            }
            // Handle adding list item
            R.id.addListItem -> {
                listAdapter.addNewItem(recyclerView)
                true
            }
            // Handle saving list
            R.id.saveItems -> {

                lifecycleScope.launch {
                    when (listAdapter.saveItems(recyclerView)) {
                        is ListCreatorAdapter.SaveResult.Success -> {
                            val returnIntent = Intent()
                            setResult(RESULT_OK, returnIntent)
                            finish() // Only close if it actually reached the cloud
                        }

                        is ListCreatorAdapter.SaveResult.EmptyList -> {
                            Toast.makeText(
                                this@ListCreator,
                                "אי אפשר לשמור רשימה ריקה",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is ListCreatorAdapter.SaveResult.NetworkError -> {
                            Toast.makeText(this@ListCreator, "יש תקלה ברשת", Toast.LENGTH_SHORT)
                                .show()
                        }

                        is ListCreatorAdapter.SaveResult.CanceledByUserInfo -> {
                            Toast.makeText(
                                this@ListCreator,
                                "שמירת הרשימה בוטלה",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                true

            }
            // Handle deleting list item
            R.id.deleteListItem -> {
                val position = recyclerView.getChildAdapterPosition(recyclerView.focusedChild)
                if (position != RecyclerView.NO_POSITION) {
                    listAdapter.removeItem(position)
                } else {
                    Toast.makeText(this, "יש לבחור מוצר למחיקה", Toast.LENGTH_SHORT).show()
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}