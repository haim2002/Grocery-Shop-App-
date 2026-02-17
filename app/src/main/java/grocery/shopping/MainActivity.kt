package grocery.shopping

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
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
            //prevents back option when logging in
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            finish()
        }
        val addNewList: FloatingActionButton = findViewById(R.id.add_btn)

        addNewList.setOnClickListener {
            val listIntent = Intent(this@MainActivity, ListCreator::class.java)
            startActivity(listIntent)
        }
    }

    //implementing menu in the app
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }
    //implementing sign out function fromm google auth firebase
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Check which item was clicked
        if (item.itemId == R.id.logout) {
            // Sign out the user
            FirebaseAuth.getInstance().signOut()

            // going back to login screen
            val intent = Intent(this, GoogleSignInActivity::class.java)
            //prevents back option when signing out
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }



}




