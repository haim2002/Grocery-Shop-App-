package grocery.shopping.data

val typeDetermine = mapOf(

    "תפוח" to "Fruit"
)

object GroceryData {

    val currentItems = mutableListOf<GroceryItems>()
    // list of all saved lists
    val allSavedLists = mutableMapOf<String, MutableList<GroceryItems>>()
}

open class GroceryItems(
    open var id: String? = null,
    open val type: String = "General",
    open var name: String? = "",
    open var quantity: Int=1

)


class Vegetables(
    id: String? = null,
    name: String = "",
    quantity: Int = 1

) : GroceryItems(id, "Vegetable", name, quantity)



class Fruit(
    id: String? = null,
    name: String = "",
    quantity: Int = 1

) : GroceryItems(id, "Fruit", name, quantity)




class Dairy(
    id: String? = null,
    name: String = "",
    quantity: Int = 1

) : GroceryItems(id, "Dairy", name, quantity)

