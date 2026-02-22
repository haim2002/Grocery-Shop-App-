package grocery.shopping.data

import grocery.shopping.DAIRY_TYPE
import grocery.shopping.DEFAULT_ID
import grocery.shopping.DEFAULT_ITEM_QUANTITY
import grocery.shopping.DEFAULT_PRODUCT_NAME
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
    open var name: String = DEFAULT_PRODUCT_NAME,
    open var quantity: Int= DEFAULT_ITEM_QUANTITY

)


class Vegetables(
    id: Int = DEFAULT_ID,
    name: String = DEFAULT_PRODUCT_NAME,
    quantity: Int = DEFAULT_ITEM_QUANTITY

) : GroceryItems(id=id, type=VEGETABLES_TYPE, name=name, quantity=quantity)



class Fruit(
    id: Int = DEFAULT_ID,
    name: String = DEFAULT_PRODUCT_NAME,
    quantity: Int = DEFAULT_ITEM_QUANTITY

) : GroceryItems(id=id, type= FRUIT_TYPE, name=name, quantity=quantity)


class Dairy(
    id: Int = DEFAULT_ID,
    name: String = DEFAULT_PRODUCT_NAME,
    quantity: Int = DEFAULT_ITEM_QUANTITY

) : GroceryItems(id=id, type= DAIRY_TYPE, name=name, quantity=quantity)


fun sortGroceryInput(listOfProducts: MutableList<GroceryItems>) :MutableList<GroceryItems>{

    val listOfVegetables: MutableList<Vegetables> = mutableListOf()
    val listOfFruit: MutableList<Fruit> = mutableListOf()
    val listOfDairy: MutableList<Dairy> = mutableListOf()
    val listOfGeneralItems: MutableList<GroceryItems> = mutableListOf()
    val finalSortedList: MutableList<GroceryItems> = mutableListOf()

    for (product in listOfProducts) {
        val itemQuantity = product.quantity
        val productName = product.name
        val detectedType = typeDetermine[productName] ?: "GENERAL"

        if (productName.isNotBlank()) {
            when (detectedType) {
                FRUIT_TYPE -> {
                    listOfFruit.add(Fruit(name = productName, quantity = itemQuantity))
                }

                VEGETABLES_TYPE -> {
                    listOfVegetables.add(Vegetables(name = productName, quantity = itemQuantity))
                }

                DAIRY_TYPE -> {
                    listOfDairy.add(Dairy(name = productName, quantity = itemQuantity))
                }

                else -> {
                    listOfGeneralItems.add(GroceryItems(name = productName, quantity = itemQuantity))
                }
            }
        }
    }

    finalSortedList.addAll(listOfFruit)
    finalSortedList.addAll(listOfVegetables)
    finalSortedList.addAll(listOfDairy)
    finalSortedList.addAll(listOfGeneralItems)
    return finalSortedList
}