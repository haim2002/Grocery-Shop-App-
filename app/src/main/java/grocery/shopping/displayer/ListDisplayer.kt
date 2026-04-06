package grocery.shopping.displayer

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import grocery.shopping.R
import grocery.shopping.creator.ListCreator
import grocery.shopping.data.ShoppingRepository.fetchGroceryList

private lateinit var listAdapter: ListDisplayAdapter
private lateinit var recyclerView: RecyclerView
private lateinit var listId: String


class ListDisplayer : AppCompatActivity() {
    val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {

                refreshList()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     //   enableEdgeToEdge()
        setContentView(R.layout.list_creator)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.list_maker)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent.getStringExtra("LIST_NAME")
        recyclerView = findViewById(R.id.recyclerView)

        // Initialize the adapter
        listId = intent.getStringExtra("LIST_ID").toString()
        Log.d("ListDisplayerID", "listId: $listId")

        fetchGroceryList(listId) { listFromFirebase ->
            listAdapter = ListDisplayAdapter(listFromFirebase, listId)
            Log.d("ListDisplayerDebug", "listId: $listId")

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

            R.id.editList -> {

                Log.d("ListDisplayerID2", "listId: $listId")
                val intent = Intent(this, ListCreator::class.java)
                intent.putExtra("ListID", listId)
                startForResult.launch(intent)
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshList() {


        fetchGroceryList(listId) { listFromFirebase ->
            listAdapter = ListDisplayAdapter(listFromFirebase, listId)
            Log.d("ListDisplayerDebug", "listId: $listId")
            Log.d("ListDisplayerDebug", "listFromFirebase: $listFromFirebase")
            // Set the adapter and layout manager for the RecyclerView
            recyclerView.adapter = listAdapter
            recyclerView.layoutManager = LinearLayoutManager(this)
            listAdapter.notifyDataSetChanged()

        }

    }


}