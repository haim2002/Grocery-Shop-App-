package grocery.shopping.listmanager

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import grocery.shopping.R

private lateinit var myAdapter: ListCreatorAdapter
class ListCreator : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.list_creator)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.list_maker)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Set up the RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        // Initialize the adapter
        myAdapter = ListCreatorAdapter()

        // Set the adapter and layout manager for the RecyclerView
        recyclerView.adapter = myAdapter
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
            R.id.addListItem -> {
                myAdapter.addNewItem()
                true
            }
            R.id.saveItems -> {

                myAdapter.saveItems()

                val wasSaved = myAdapter.wasSaved()
                if (wasSaved) {
                    finish()
                }
                else{
                    Toast.makeText(this, "אי אפשר לשמור רשימה ריקה", Toast.LENGTH_SHORT).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}