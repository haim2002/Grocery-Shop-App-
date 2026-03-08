package grocery.shopping.creator

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import grocery.shopping.R
import kotlinx.coroutines.launch

private lateinit var listAdapter: ListCreatorAdapter
private lateinit var recyclerView: RecyclerView

class ListCreator : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.list_creator)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.list_maker)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
        // Set up the RecyclerView
        recyclerView = findViewById(R.id.recyclerView)

        // Initialize the adapter
        listAdapter = ListCreatorAdapter()

        // Set the adapter and layout manager for the RecyclerView
        recyclerView.adapter = listAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
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
                    when (val result = listAdapter.saveItems(recyclerView)) {
                        is ListCreatorAdapter.SaveResult.Success -> {
                            finish() // Only close if it actually reached the cloud
                        }

                        is ListCreatorAdapter.SaveResult.EmptyList -> {
                            Toast.makeText(this@ListCreator, "אי אפשר לשמור רשימה ריקה", Toast.LENGTH_SHORT).show()
                        }

                        is ListCreatorAdapter.SaveResult.NetworkError -> {
                            Toast.makeText(this@ListCreator, "יש תקלה ברשת", Toast.LENGTH_SHORT).show()
                        }

                        is ListCreatorAdapter.SaveResult.CanceledByUserInfo -> {
                            Toast.makeText(this@ListCreator, "שמירת הרשימה בוטלה", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                true

            }
            // Handle deleting list item
            R.id.deleteListItem -> {
                listAdapter.removeItem(recyclerView.getChildAdapterPosition(recyclerView.focusedChild))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}