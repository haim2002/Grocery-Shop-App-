package grocery.shopping
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import grocery.shopping.creator.ListCreator
import grocery.shopping.data.ShoppingRepository.fetchUserSummaries
import grocery.shopping.ui.login.GoogleSignInActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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

/*
        // 1. Find the view
        val recyclerView = findViewById<RecyclerView>(R.id.mainRecyclerView)

// 2. Set the Grid Manager (Don't forget this part for the squares!)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

// 3. Initialize the adapter with a lowercase 'm' in mutableListOf
        val myAdapter = MetadataListAdapter(mutableListOf())
        recyclerView.adapter = myAdapter

// 4. Fetch the data and update the UI when it arrives
        fetchUserSummaries { listFromFirebase ->
            // This is the "Pizza is ready" callback
            myAdapter.updateData(listFromFirebase)
        }

 */
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

           val intent = Intent(this, ListDisplayer::class.java)
            startActivity(intent)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }


}




