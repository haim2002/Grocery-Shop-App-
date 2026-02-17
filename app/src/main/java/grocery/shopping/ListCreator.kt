package grocery.shopping

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView

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
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val myAdapter = ListAdapter()
        recyclerView.adapter = myAdapter
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)


        val addListItem: Button =findViewById<Button>(R.id.addListItem)
        val saveListButton = findViewById<Button>(R.id.saveList)

        addListItem.setOnClickListener {
            myAdapter.addNewItem()

        }


    }
}