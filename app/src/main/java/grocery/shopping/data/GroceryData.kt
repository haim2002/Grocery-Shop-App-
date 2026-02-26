package grocery.shopping.data

val typeDetermine = mapOf(

    "תפוח" to "Fruit"
)


open class GroceryItems(
    open val type: String = GENERAL_TYPE,
    open var name: String,
    open var quantity: Int
)
class Vegetables(
    name: String,
    quantity: Int

) : GroceryItems(type=VEGETABLES_TYPE, name=name, quantity=quantity)
class Fruit(
    name: String,
    quantity: Int

) : GroceryItems(type= FRUIT_TYPE, name=name, quantity=quantity)
class Dairy(
    name: String,
    quantity: Int

) : GroceryItems(type= DAIRY_TYPE, name=name, quantity=quantity)

fun sortGroceryInput(listOfProducts: MutableList<GroceryItems>) :MutableList<GroceryItems>{

    val listOfVegetables: MutableList<Vegetables> = mutableListOf()
    val listOfFruit: MutableList<Fruit> = mutableListOf()
    val listOfDairy: MutableList<Dairy> = mutableListOf()
    val listOfGeneralItems: MutableList<GroceryItems> = mutableListOf()
    val finalSortedList: MutableList<GroceryItems> = mutableListOf()

    for (product in listOfProducts) {
        val itemQuantity = product.quantity
        val productName = product.name
        val detectedType = typeDetermine[productName]

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