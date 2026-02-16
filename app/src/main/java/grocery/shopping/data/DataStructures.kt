package grocery.shopping.data

class DataStructures {

    data class NamedShoppingList(
        var listName: String,
        val items: MutableList<GroceryItems> = mutableListOf(),
        val dateCreated: Long = System.currentTimeMillis()
    )




}