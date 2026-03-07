package grocery.shopping.data

import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

object ShoppingRepository {

    val database = Firebase.database(DATABASE_URL).reference
    fun saveList(shoppingList: MutableList<GroceryItems>, shoppingListDetails: ListInfo) {

        // 1. Create the key first
        val newListKey = database.child("lists").push().key

// 2. Use that SAME key for both locations
        database.child("list_info").child(newListKey!!).setValue(shoppingListDetails)
        database.child("lists").child(newListKey).setValue(shoppingList)

    }


    fun fetchUserSummaries(onResult: (List<ListInfo>) -> Unit) {
        // Point specifically to the "list_info" child
        database.child("list_info").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val allSummaries = mutableListOf<ListInfo>()

                for (listSnapshot in snapshot.children) {
                    val summary = listSnapshot.getValue(ListInfo::class.java)
                    if (summary != null) {
                        // This is great—you're capturing the key for later use (like deletes/edits)
                        summary.firebaseKey = listSnapshot.key
                        allSummaries.add(summary)
                    }
                }

                onResult(allSummaries)
            }

            override fun onCancelled(error: DatabaseError) {
                // Log the error so you aren't flying blind!
                println("Database Error: ${error.message}")
            }
        })
    }


    fun fetchSpecificList(key: String, onResult: (List<GroceryItems>?) -> Unit) {
        // 1. Point to the "lists" node, then the specific key
        val specificRef = database.child("lists").child(key)

        specificRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = mutableListOf<GroceryItems>()

                // 2. Iterate through the children to build your list
                for (itemSnapshot in snapshot.children) {
                    val item = itemSnapshot.getValue(GroceryItems::class.java)
                    if (item != null) {
                        items.add(item)
                    }
                }

                onResult(items)
            }

            override fun onCancelled(error: DatabaseError) {
                onResult(null)
            }
        })
    }








}