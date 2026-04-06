package grocery.shopping

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import grocery.shopping.creator.ListCreator
import grocery.shopping.data.ShoppingRepository.fetchAllMetadata
import grocery.shopping.metadata.MetadataListAdapter
import grocery.shopping.ui.login.GoogleSignInActivity

lateinit var myAdapter: MetadataListAdapter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            // If no user, jump to the Google Sign-In screen
            val intent = Intent(this, GoogleSignInActivity::class.java)
            startActivity(intent)
            finish()
        }
        val addNewList: FloatingActionButton = findViewById(R.id.add_btn)

        addNewList.setOnClickListener {
            val listIntent = Intent(this@MainActivity, ListCreator::class.java)
            startActivity(listIntent)
        }


        fetchAllMetadata { listFromFirebase ->
            // Switch back to the UI thread to prevent the crash
            runOnUiThread {
                myAdapter = MetadataListAdapter(listFromFirebase)

                val recyclerView = findViewById<RecyclerView>(R.id.mainRecyclerView)
                recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                recyclerView.adapter = myAdapter
                myAdapter.updateData(listFromFirebase)
            }
        }


    }


    //implementing menu in the app
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    //implementing sign out function from Google auth firebase
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Check which item was clicked
        if (item.itemId == R.id.logout) {
            // Sign out the user
            FirebaseAuth.getInstance().signOut()

            // going back to log in screen
            val intent = Intent(this, GoogleSignInActivity::class.java)
            startActivity(intent)
            finish()
            return true
        }


        if (item.itemId == R.id.settings) {

            // TODO:  
            return true
        }
        return super.onOptionsItemSelected(item)
    }


}




