package grocery.shopping.data

import com.google.firebase.database.FirebaseDatabase

object ShoppingRepository {
    // 1. Get a reference to the root of your database
    private val database = FirebaseDatabase.getInstance(DATABASE_URL).getReference("all lists")

    fun saveList(shoppingList: CompleteShoppingList) {
        val uid = getGoogleUserID() ?: return
        val name = getGoogleUserName()

        // 1. Save/Update the Profile Name (so you know who the UID belongs to)
        database.child(uid).child("profile").child("name").setValue(name)

        // 2. Save the Shopping List under the "shopping_lists" folder
        val listRef = database.child(uid).child("shopping_list").push()

        listRef.setValue(shoppingList)
            .addOnSuccessListener { /* Success! */ }
            .addOnFailureListener { /* Handle Error */ }
    }
}