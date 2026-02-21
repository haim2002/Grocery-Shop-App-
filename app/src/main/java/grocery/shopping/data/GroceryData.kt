package grocery.shopping.data

import grocery.shopping.DAIRY_TYPE
import grocery.shopping.DEFAULT_ID
import grocery.shopping.DEFAULT_ITEM_QUANTITY
import grocery.shopping.EMPTY_STRING
import grocery.shopping.FRUIT_TYPE
import grocery.shopping.GENERAL_TYPE
import grocery.shopping.VEGETABLES_TYPE

val typeDetermine = mapOf(

    "תפוח" to "Fruit"
)

object GroceryData {

    val currentItems = mutableListOf<GroceryItems>()
    // list of all saved lists
    val allSavedLists = mutableMapOf<String, MutableList<GroceryItems>>()
}

open class GroceryItems(
    open var id: Int = DEFAULT_ID,
    open val type: String = GENERAL_TYPE,
    open var name: String = EMPTY_STRING,
    open var quantity: Int= DEFAULT_ITEM_QUANTITY

)


class Vegetables(
    id: Int = DEFAULT_ID,
    name: String = EMPTY_STRING,
    quantity: Int = DEFAULT_ITEM_QUANTITY

) : GroceryItems(id=id, type=VEGETABLES_TYPE, name=name, quantity=quantity)



class Fruit(
    id: Int = DEFAULT_ID,
    name: String = EMPTY_STRING,
    quantity: Int = DEFAULT_ITEM_QUANTITY

) : GroceryItems(id=id, type= FRUIT_TYPE, name=name, quantity=quantity)




class Dairy(
    id: Int = DEFAULT_ID,
    name: String = EMPTY_STRING,
    quantity: Int = DEFAULT_ITEM_QUANTITY

) : GroceryItems(id=id, type= DAIRY_TYPE, name=name, quantity=quantity)

