package grocery.shopping

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import grocery.shopping.data.GroceryItems
import grocery.shopping.data.ShoppingRepository.fetchSpecificList

private lateinit var listAdapter: ListDisplayAdapter
private lateinit var recyclerView: RecyclerView
class ListDisplayer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.list_creator)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.list_maker)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        recyclerView = findViewById(R.id.recyclerView)

        // Initialize the adapter
        val listId = intent.getStringExtra("LIST_ID")
        Log.d("MY_TAG", "listId: $listId")

            fetchSpecificList(listId) { listFromFirebase ->
                listAdapter = ListDisplayAdapter(listFromFirebase)
                // Set the adapter and layout manager for the RecyclerView
                recyclerView.adapter = listAdapter
                recyclerView.layoutManager = LinearLayoutManager(this)

            }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.list_display_menu, menu)
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

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

}