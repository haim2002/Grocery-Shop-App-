package grocery.shopping.data

import com.google.firebase.database.PropertyName


@Suppress("unused")
open class GroceryItems(
    open val type: String = GENERAL_TYPE,
    open var name: String = DEFAULT_PRODUCT_NAME,
    open var quantity: Int = DEFAULT_ITEM_QUANTITY,
    @get:PropertyName("isChecked") // Forces Firebase to use "isChecked"
    @set:PropertyName("isChecked") // Forces Firebase to use "isChecked"
    open var isChecked: Boolean = false
) {

    override fun toString(): String {
        return "GroceryItem(name='$name', qty=$quantity)"
    }

    constructor() : this("", "", 0, false)
}


class Vegetables(
    name: String,
    quantity: Int

) : GroceryItems(type = VEGETABLES_TYPE, name = name, quantity = quantity)

class Fruit(
    name: String,
    quantity: Int

) : GroceryItems(type = FRUIT_TYPE, name = name, quantity = quantity)

class Drinks(
    name: String,
    quantity: Int

) : GroceryItems(type = Drinks_TYPE, name = name, quantity = quantity)

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

class Pantry(
    name: String,
    quantity: Int

) : GroceryItems(type = Pantry_TYPE, name = name, quantity = quantity)

class Cleaning(
    name: String,
    quantity: Int

) : GroceryItems(type = Cleaning_TYPE, name = name, quantity = quantity)


fun sortGroceryInput(listOfProducts: MutableList<GroceryItems>): MutableList<GroceryItems> {

    val listOfVegetables: MutableList<Vegetables> = mutableListOf()
    val listOfFruit: MutableList<Fruit> = mutableListOf()
    val listOfDrinks: MutableList<Drinks> = mutableListOf()
    val listOfDairy: MutableList<Dairy> = mutableListOf()
    val listOfBakery: MutableList<Bakery> = mutableListOf()
    val listOfMeat: MutableList<Meat> = mutableListOf()
    val listOfPantry: MutableList<Pantry> = mutableListOf()
    val listOfCleaning: MutableList<Cleaning> = mutableListOf()
    val listOfGeneralItems: MutableList<GroceryItems> = mutableListOf()
    val finalSortedList: MutableList<GroceryItems> = mutableListOf()

    for (product in listOfProducts) {
        val itemQuantity = product.quantity
        val productName = product.name.trim().replace("'", "")
        val detectedType = typeDetermine.entries.find { (key, _) ->
            productName.startsWith(key) || ignoreMisspells(
                productName,
                key
            ) && productName.startsWith(key)
        }?.value

        if (productName.isNotBlank() && productName != "תפוח אדמה") {
            when (detectedType) {

                VEGETABLES_TYPE -> {
                    listOfVegetables.add(Vegetables(name = productName, quantity = itemQuantity))
                }

                FRUIT_TYPE -> {
                    listOfFruit.add(Fruit(name = productName, quantity = itemQuantity))
                }

                Drinks_TYPE -> {
                    listOfDrinks.add(Drinks(name = productName, quantity = itemQuantity))
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

                Pantry_TYPE -> {
                    listOfPantry.add(Pantry(name = productName, quantity = itemQuantity))
                }

                Cleaning_TYPE -> {
                    listOfCleaning.add(Cleaning(name = productName, quantity = itemQuantity))
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
        if (productName == "תפוח אדמה") {

            listOfVegetables.add(Vegetables(name = productName, quantity = itemQuantity))
        }
    }

    finalSortedList.addAll(listOfVegetables.sortedBy { it.name })
    finalSortedList.addAll(listOfFruit.sortedBy { it.name })
    finalSortedList.addAll(listOfDrinks.sortedBy { it.name })
    finalSortedList.addAll(listOfDairy.sortedBy { it.name })
    finalSortedList.addAll(listOfBakery.sortedBy { it.name })
    finalSortedList.addAll(listOfMeat.sortedBy { it.name })
    finalSortedList.addAll(listOfPantry.sortedBy { it.name })
    finalSortedList.addAll(listOfCleaning.sortedBy { it.name })
    finalSortedList.addAll(listOfGeneralItems.sortedBy { it.name })
    return finalSortedList.distinctBy { it.name } as MutableList<GroceryItems>
}

fun ignoreMisspells(input: String, key: String): Boolean {
    if (key.length > input.length) return false

    val confusingPairs = mapOf(
        'ט' to 'ת', 'ת' to 'ט',
        'א' to 'ע', 'ע' to 'א',
        'כ' to 'ק', 'ק' to 'כ',
        // "תפוח אדמה" to ",תפוא"

    )

    return input.take(key.length).zip(key).all { (charInput, charKey) ->
        charInput == charKey || confusingPairs[charInput] == charKey
    }
}

val typeDetermine = mapOf(

    // --- Fruits & Vegetables (פירות וירקות) ---
    "אבטיח" to "Fruit",
    "אבוקדו" to "Vegetables",
    "אגס" to "Fruit",
    "אורגנו" to "Vegetables",
    "אפרסמון" to "Fruit",
    "אפרסק" to "Fruit",
    "בטטה" to "Vegetables",
    "ביבי תרד" to "Vegetables",
    "בננה" to "Fruit",
    "בצל" to "Vegetables",
    "בצל ירוק" to "Vegetables",
    "בצל סגול" to "Vegetables",
    "ברוקולי" to "Vegetables",
    "גינגר" to "Vegetables",
    "גזר" to "Vegetables",
    "דובדבן" to "Fruit",
    "דלורית" to "Vegetables",
    "דלעת" to "Vegetables",
    "חסה" to "Vegetables",
    "חציל" to "Vegetables",
    "כוסברה" to "Vegetables",
    "כרוב" to "Vegetables",
    "כרובית" to "Vegetables",
    "לימון" to "Fruit",
    "מלון" to "Fruit",
    "מלפפון" to "Vegetables",
    "מנגו" to "Fruit",
    "נענע" to "Vegetables",
    "נקטרינה" to "Fruit",
    "סלק" to "Vegetables",
    "עגבניה" to "Vegetables",
    "שרי" to "Vegetables",
    "עגבניות" to "Vegetables",
    "ענבים" to "Fruit",
    "פטרוזיליה" to "Vegetables",
    "פטריות" to "Vegetables",
    "פיטרוזיליה" to "Vegetables",
    "פלפל" to "Vegetables",
    "פלפל חריף" to "Vegetables",
    "פומלה" to "Fruit",
    "צנון" to "Vegetables",
    "צנונית" to "Vegetables",
    "קולורבי" to "Vegetables",
    "קישואים" to "Vegetables",
    "קלמנטינה" to "Fruit",
    "קיווי" to "Fruit",
    "שום" to "Vegetables",
    "שמיר" to "Vegetables",
    "שעועית ירוקה" to "Vegetables",
    "שזיף" to "Fruit",
    "תות" to "Fruit",
    "תירס" to "Vegetables",
    "תפוז" to "Fruit",
    "תפוח" to "Fruit",
    "תפוא" to "Vegetables",
    "תפוח אדמה" to "Vegetables",

    // --- Drinks (שתייה) ---
    "אייס קפה" to "Drinks",
    "אשכולית" to "Drinks",
    "בירה" to "Drinks",
    "בירה לבנה" to "Drinks",
    "בירה שחורה" to "Drinks",
    "גזוז" to "Drinks",
    "דיאט קולה" to "Drinks",
    "הייניקן" to "Drinks",
    "ויסקי" to "Drinks",
    "וודקה" to "Drinks",
    "יין" to "Drinks",
    "יין אדום" to "Drinks",
    "יין לבן" to "Drinks",
    "יין קידוש" to "Drinks",
    "מי טוניק" to "Drinks",
    "מיץ" to "Drinks",
    "מים" to "Drinks",
    "נסקפה" to "Drinks",
    "סודה" to "Drinks",
    "ספרייט" to "Drinks",
    "ספרייט זירו" to "Drinks",
    "ענבי טלי" to "Drinks",
    "פאנטה" to "Drinks",
    "פיוזטי" to "Drinks",
    "פיוז טי" to "Drinks",
    "פריגת" to "Drinks",
    "קוקה קולה" to "Drinks",
    "קולה" to "Drinks",
    "קולה זירו" to "Drinks",
    "קפה" to "Drinks",
    "שוופס" to "Drinks",
    "תה" to "Drinks",
    "תירוש" to "Drinks",

    // --- Dairy & Eggs (חלב, ביצים וגבינות) ---
    "ביצים" to "Dairy",
    "גבינה" to "Dairy",
    "גבינת" to "Dairy",
    "חלב" to "Dairy",
    "חמאה" to "Dairy",
    "יוגורט" to "Dairy",
    "ריוויון" to "Dairy",
    "לאבנה" to "Dairy",
    "מילקי" to "Dairy",
    "מעדן" to "Dairy",
    "מרגרינה" to "Dairy",
    "פרמזן" to "Dairy",
    "קוטג" to "Dairy",
    "שמנת" to "Dairy",


    // --- Meat & Fish (בשר ודגים) ---
    "אמנון" to "Meat",
    "אנטריקוט" to "Meat",
    "בשר" to "Meat",
    "דג" to "Meat",
    "חזה עוף" to "Meat",
    "כנפיים" to "Meat",
    "כרעיים" to "Meat",
    "נסיכת הנילוס" to "Meat",
    "נקניק" to "Meat",
    "נקניקיות" to "Meat",
    "סלמון" to "Meat",
    "סטייק" to "Meat",
    "עוף" to "Meat",
    "פסטרמה" to "Meat",
    "פרגיות" to "Meat",
    "צלי כתף" to "Meat",
    "צלעות" to "Meat",
    "קבב" to "Meat",
    "שוקיים" to "Meat",
    "שניצל" to "Meat",

    // --- Bakery (מאפים ולחם) ---
    "בורקס" to "Bakery",
    "חלה" to "Bakery",
    "לחם" to "Bakery",
    "לחמניה" to "Bakery",
    "מלאווח" to "Frozen",
    "עוגה" to "Bakery",
    "עוגיה" to "Bakery",
    "פיתה" to "Bakery",
    "קרואסון" to "Bakery",

    // --- Pantry & Cooking (מזווה ובישול) ---
    "אורז" to "Pantry",
    "אבקת אפייה" to "Pantry",
    "דבש" to "Pantry",
    "זיתים" to "Pantry",
    "חומוס" to "Pantry",
    "טחינה" to "Pantry",
    "טונה" to "Pantry",
    "יין" to "Pantry",
    "מיונז" to "Pantry",
    "מלח" to "Pantry",
    "מרק עוף" to "Pantry",
    "סוכר" to "Pantry",
    "סילאן" to "Pantry",
    "עדשים" to "Pantry",
    "עלי גפן" to "Pantry",
    "פירורי לחם" to "Pantry",
    "פסטה" to "Pantry",
    "פתיתים" to "Pantry",
    "צנוברים" to "Pantry",
    "קמח" to "Pantry",
    "קפה" to "Pantry",
    "קטשופ" to "Pantry",
    "קינואה" to "Pantry",
    "ריבה" to "Pantry",
    "שמן" to "Pantry",
    "שימורים" to "Pantry",
    "תה" to "Pantry",

    // --- Cleaning & Household (ניקיון ובית) ---
    "אבקת כביסה" to "Cleaning",
    "מרכך" to "Cleaning",
    "נייר טואלט" to "Cleaning",
    "נייר סופג" to "Cleaning",
    "ניילון נצמד" to "Cleaning",
    "סבון" to "Cleaning",
    "שמפו" to "Cleaning",
    "שקיות" to "Cleaning"
)