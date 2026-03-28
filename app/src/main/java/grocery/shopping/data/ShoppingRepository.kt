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

    //function that saves the list to firebase
    suspend fun saveList(
        shoppingList: MutableList<GroceryItems>,
        shoppingListDetails: ListInfo
    ): Result<Unit> = try {

        //generates firebase key
        val newListKey = database.child(LISTS_PATH).push().key
            ?: throw Exception("Could not generate Firebase Key")

        //sends the data to firebase
        val updates = mapOf(
            "$METADATA_PATH/$newListKey" to shoppingListDetails,
            "$LISTS_PATH/$newListKey" to shoppingList
        )

        //waits for request to complete
        database.updateChildren(updates).await()

        //returns success or failure
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    //function that fetches the metadata from firebase
    fun fetchAllMetadata(onResult: (List<ListInfo>) -> Unit) {
        // Point to the "METADATA_PATH" node
        database.child(METADATA_PATH).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val allMetadata = mutableListOf<ListInfo>()

                // Iterate through the children the metadata node
                for (listSnapshot in snapshot.children) {
                    Log.d("FIREBASE_KEYS", "Found keys: ${listSnapshot.value}")
                    val metadata = listSnapshot.getValue(ListInfo::class.java)
                    if (metadata != null) {
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

    //function that fetches the specific list from firebase
    fun fetchGroceryList(key: String?, onResult: (MutableList<GroceryItems>) -> Unit) {

        if (key == null) return
        // Point to the "LISTS_PATH" node, then the specific key
        val specificRef = database.child(LISTS_PATH).child(key)

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

    // function that fetches specific metadata list from firebase
    fun fetchMetadataList(key: String?, onResult: (ListInfo) -> Unit) {
        if (key == null) return
        // Point to the "METADATA_PATH" node, then the specific key
        val specificRef = database.child(METADATA_PATH).child(key)

        specificRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val item = snapshot.getValue(ListInfo::class.java)


                if (item != null) {
                    onResult(item)
                }
            }

            override fun onCancelled(error: DatabaseError) {

                Log.e("Firebase", error.message)
            }
        })
    }

    //function that deletes the list from firebase
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

    // a function that updates specific branch in the firebase
    suspend fun updateListInFirebase(
        id: String,
        items: List<GroceryItems>,
        meta: ListInfo
    ): Result<Unit> {
        return try {
            // update metadata and grocery list at a specific branch
            database.child(LISTS_PATH).child(id).setValue(items).await()
            database.child(METADATA_PATH).child(id).setValue(meta).await()


            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}