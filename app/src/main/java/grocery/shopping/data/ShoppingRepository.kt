package grocery.shopping.data

import com.google.firebase.database.FirebaseDatabase

object ShoppingRepository {
    // 1. Get a reference to the root of your database
    private val database = FirebaseDatabase.getInstance().getReference("users")

    fun saveList(shoppingList: CompleteShoppingList) {
        // 2. Use your UserExtensions shortcut to get the current UID
        val uid = getGoogleUser() ?: return

        // 3. Create a unique path: users -> [UID] -> shopping_lists -> [RandomID]
        val listRef = database.child(uid).child("shopping_lists").push()

        // 4. Push the actual object
        listRef.setValue(shoppingList)
            .addOnSuccessListener {
                // List is safely in the cloud!
            }
            .addOnFailureListener { error ->
                // Something went wrong (check internet or rules)
            }
    }
}