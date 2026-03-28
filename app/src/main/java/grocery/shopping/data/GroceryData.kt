package grocery.shopping.data


open class GroceryItems(
    open val type: String = GENERAL_TYPE,
    open var name: String = DEFAULT_PRODUCT_NAME,
    open var quantity: Int = DEFAULT_ITEM_QUANTITY
)
{

    override fun toString(): String {
        return "GroceryItem(name='$name', qty=$quantity)"
    }
}


class Vegetables(
    name: String,
    quantity: Int

) : GroceryItems(type = VEGETABLES_TYPE, name = name, quantity = quantity)

class Fruit(
    name: String,
    quantity: Int

) : GroceryItems(type = FRUIT_TYPE, name = name, quantity = quantity)

class Dairy(
    name: String,
    quantity: Int

) : GroceryItems(type = DAIRY_TYPE, name = name, quantity = quantity)

class Bakery(
    name: String,
    quantity: Int

) : GroceryItems(type = BAKERY_TYPE, name = name, quantity = quantity)

class Meat(
    name: String,
    quantity: Int

) : GroceryItems(type = MEAT_TYPE, name = name, quantity = quantity)

fun sortGroceryInput(listOfProducts: MutableList<GroceryItems>): MutableList<GroceryItems> {

    val listOfVegetables: MutableList<Vegetables> = mutableListOf()
    val listOfFruit: MutableList<Fruit> = mutableListOf()
    val listOfDairy: MutableList<Dairy> = mutableListOf()
    val listOfBakery: MutableList<Bakery> = mutableListOf()
    val listOfMeat: MutableList<Meat> = mutableListOf()

    val listOfGeneralItems: MutableList<GroceryItems> = mutableListOf()
    val finalSortedList: MutableList<GroceryItems> = mutableListOf()

    for (product in listOfProducts) {
        val itemQuantity = product.quantity
        val productName = product.name.trim()
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

                BAKERY_TYPE -> {
                    listOfBakery.add(Bakery(name = productName, quantity = itemQuantity))
                }

                MEAT_TYPE -> {
                    listOfMeat.add(Meat(name = productName, quantity = itemQuantity))
                }

                else -> {
                    listOfGeneralItems.add(
                        GroceryItems(
                            name = productName,
                            quantity = itemQuantity
                        )
                    )
                }
            }
        }
    }

    finalSortedList.addAll(listOfFruit)
    finalSortedList.addAll(listOfVegetables)
    finalSortedList.addAll(listOfDairy)
    finalSortedList.addAll(listOfBakery)
    finalSortedList.addAll(listOfMeat)
    finalSortedList.addAll(listOfGeneralItems)
    return finalSortedList.distinctBy {it.name} as MutableList<GroceryItems>
}




val typeDetermine = mapOf(

    // --- Fruits (פירות) ---
    "תפוח" to "Fruit",
    "בננה" to "Fruit",
    "תפוז" to "Fruit",
    "ענבים" to "Fruit",
    "אפרסק" to "Fruit",
    "תות" to "Fruit",
    "קלמנטינה" to "Fruit",
    "מלון" to "Fruit",
    "אבטיח" to "Fruit",
    "אגס" to "Fruit",
    "אפרסמון" to "Fruit",
    "דובדבן" to "Fruit",
    "לימון" to "Fruit",
    "מנגו" to "Fruit",
    "נקטרינה" to "Fruit",
    "פומלה" to "Fruit",
    "קיווי" to "Fruit",
    "שזיף" to "Fruit",

    // --- Vegetables (ירקות) ---
    "עגבניה" to "Vegetables",
    "אבוקדו" to "Vegetables",
    "מלפפון" to "Vegetables",
    "פלפל" to "Vegetables",
    "בצל" to "Vegetables",
    "תפוח אדמה" to "Vegetables",
    "גזר" to "Vegetables",
    "חסה" to "Vegetables",
    "קישואים" to "Vegetables",
    "חציל" to "Vegetables",
    "שום" to "Vegetables",
    "ברוקולי" to "Vegetables",
    "כרובית" to "Vegetables",
    "כרוב" to "Vegetables",
    "תירס" to "Vegetables",
    "בטטה" to "Vegetables",
    "סלק" to "Vegetables",
    "צנון" to "Vegetables",
    "פטריות" to "Vegetables",
    "דלעת" to "Vegetables",
    "דלורית" to "Vegetables",
    "פטרוזיליה" to "Vegetables",
    "כוסברה" to "Vegetables",

    // --- Dairy (מוצרי חלב וגבינות) ---
    "חלב" to "Dairy",
    "חלב לקפה" to "Dairy",
    "גבינה" to "Dairy",
    "גבינה צהובה" to "Dairy",
    "גבינה לבנה" to "Dairy",
    "גבינה בולגרית" to "Dairy",
    "גבינה מלוחה" to "Dairy",
    "גבינה צפתית" to "Dairy",
    "גבינה שמנת" to "Dairy",
    "גבינה עזים" to "Dairy",
    "גבינה כחולה" to "Dairy",
    "קוטג'" to "Dairy",
    "יוגורט" to "Dairy",
    "חמאה" to "Dairy",
    "שמנת" to "Dairy",
    "שמנת חמוצה" to "Dairy",
    "שמנת מתוקה" to "Dairy",
    "לאבנה" to "Dairy",
    "יוגורט" to "Dairy",
    "פרמזן" to "Dairy",

    // --- Bakery (מאפים) ---
    "לחם" to "Bakery",
    "פיתה" to "Bakery",
    "לחמניה" to "Bakery",
    "חלה" to "Bakery",
    "בורקס" to "Bakery",
    "עוגה" to "Bakery",
    "עוגיה" to "Bakery",

    // --- Meat & Fish (בשר ודגים) ---
    "חזה עוף" to "Meat",
    "כרעיים" to "Meat",
    "שוקיים" to "Meat",
    "כנפיים" to "Meat",
    "פרגיות" to "Meat",
    "עוף שלם" to "Meat",
    "שניצל" to "Meat",
    "בשר טחון" to "Meat",
    "סטייק" to "Meat",
    "צלי כתף" to "Meat",
    "סינטה" to "Meat",
    "אנטריקוט" to "Meat",
    "צלעות" to "Meat",
    "קבב" to "Meat",
    "נקניקיות" to "Meat",
    "נקניק" to "Meat",
    "פסטרמה" to "Meat",
    "דג" to "Meat",
    "אמנון" to "Meat",
    "סלמון" to "Meat",
    "נסיכת הנילוס" to "Meat",
    "טונה" to "Meat"
)