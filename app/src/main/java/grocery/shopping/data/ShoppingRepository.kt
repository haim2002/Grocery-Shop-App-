package grocery.shopping.data

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.tasks.await

object ShoppingRepository {

    val database = Firebase.database(DATABASE_URL).reference
    // 1. Add 'suspend' so we can wait for the network
    suspend fun saveList(
        shoppingList: MutableList<GroceryItems>,
        shoppingListDetails: ListInfo
    ): Result<Unit> = try {

        // 2. Generate the key
        val newListKey = database.child(LISTS_PATH).push().key
            ?: throw Exception("Could not generate Firebase Key")

        // 3. Create a Map for an "Atomic Update"
        // This sends BOTH pieces of data in a single network request.
        // It's all or nothing - no partial saves!
        val updates = mapOf<String, Any>(
            "$METADATA_PATH/$newListKey" to shoppingListDetails,
            "$LISTS_PATH/$newListKey" to shoppingList
        )

        // 4. Use .updateChildren().await() to wait for completion
        database.updateChildren(updates).await()

        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }


    fun fetchListMetadata(onResult: (List<ListInfo>) -> Unit) {
        // Point specifically to the "list_info" child
        database.child(METADATA_PATH).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val allMetadata = mutableListOf<ListInfo>()

                for (listSnapshot in snapshot.children) {
                    Log.d("FIREBASE_KEYS", "Found keys: ${listSnapshot.value}")
                    val metadata = listSnapshot.getValue(ListInfo::class.java)
                    if (metadata != null) {
                        // This is great—you're capturing the key for later use (like deletes/edits)
                        metadata.firebaseKey = listSnapshot.key.toString()
                        allMetadata.add(metadata)
                        Log.d("FIREBASE_KEYS", "Found keys: ${listSnapshot.value}")
                    }
                }

                onResult(allMetadata)
            }

            override fun onCancelled(error: DatabaseError) {
                // Log the error so you aren't flying blind!
                Log.e("Fetching Metadata", "Failed to Fetching: ${error.message}")
            }
        })
    }


    fun fetchSpecificList(key: String?, onResult: (MutableList<GroceryItems>) -> Unit) {
        // 1. Point to the "lists" node, then the specific key
        val specificRef = database.child(LISTS_PATH).child(key!!)

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
                ("Error: ${error.message}")
            }
        })
    }

    fun deleteListFromFirebase(firebaseKey: String) {
        if (firebaseKey.isNotEmpty()) {
            database.child(METADATA_PATH).child(firebaseKey).removeValue()
            database.child(LISTS_PATH).child(firebaseKey).removeValue()
                .addOnSuccessListener {
                    Log.d("DELETE_CHECK", "Successfully deleted: $firebaseKey")
                }
                .addOnFailureListener { error ->
                    Log.e("DELETE_CHECK", "Failed to delete: ${error.message}")
                }
        }
    }






}