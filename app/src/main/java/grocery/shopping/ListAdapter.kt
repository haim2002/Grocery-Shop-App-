package grocery.shopping

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import grocery.shopping.data.Dairy
import grocery.shopping.data.Fruit
import grocery.shopping.data.GroceryItems
import grocery.shopping.data.Vegetables
import grocery.shopping.data.typeDetermine

class ListAdapter() : RecyclerView.Adapter<ListAdapter.ItemViewHolder>() {

    val listOfProducts: MutableList<GroceryItems> = mutableListOf()
    val listOfVegetables: MutableList<Vegetables> = mutableListOf()
    val listOfFruit: MutableList<Fruit> = mutableListOf()
    val listOfDairy: MutableList<Dairy> = mutableListOf()
    val listOfGeneralItems: MutableList<GroceryItems> = mutableListOf()
    val finalSortedList: MutableList<GroceryItems> = mutableListOf()



/*
    init{

        listOfProducts.add(GroceryItems(DEFAULT_ID, GENERAL_TYPE, DEFAULT_PRODUCT_NAME, DEFAULT_ITEM_QUANTITY))
    }

 */
     class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val productName: EditText = itemView.findViewById(R.id.productName)
         val productAmount: Spinner = itemView.findViewById(R.id.productAmount)


    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder { //inflate the layout of each item
        val inflatedView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_adapter, parent, false)
        return ItemViewHolder(inflatedView)
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {





            // 1. Reference the data object for this specific row
            val currentItem = listOfProducts[position]
            // 2. Load data into the views (Standard Android behavior)
            holder.productName.setText(currentItem.name)

            // Setup the Spinner for quantity
            val options = (1..15).toList().map { it.toString() }
            val spinnerAdapter =
                ArrayAdapter(holder.itemView.context, android.R.layout.simple_spinner_item, options)
            holder.productAmount.adapter = spinnerAdapter

            // Set the spinner to the saved quantity (index is quantity - 1)
            holder.productAmount.setSelection(currentItem.quantity - 1)


            // 3. Save changes as the user types (The "Creation Phase" Logic)
            holder.productName.doAfterTextChanged { text ->
                val input = text.toString()
                currentItem.name = input
                    // Update the list at this position with the new specific object
                    listOfProducts[position] = GroceryItems(name = input, quantity = currentItem.quantity)
            }

        // 4. Save quantity changes from the Spinner
        holder.productAmount.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                currentItem.quantity = options[pos].toInt()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }



    }



    fun processGroceryInput( listOfProducts: MutableList<GroceryItems>){

        for (product in listOfProducts) {
            val itemQuantity = product.quantity
            val rowName = product.name
            val detectedType = typeDetermine[rowName] ?: "GENERAL"

            if (rowName.isNotBlank()) {
                when (detectedType) {
                    FRUIT_TYPE -> {
                        listOfFruit.add(Fruit(name = rowName, quantity = itemQuantity))
                    }

                    VEGETABLES_TYPE -> {
                        listOfVegetables.add(Vegetables(name = rowName, quantity = itemQuantity))
                    }

                    DAIRY_TYPE -> {
                        listOfDairy.add(Dairy(name = rowName, quantity = itemQuantity))
                    }

                    else -> {
                        listOfGeneralItems.add(GroceryItems(name = rowName, quantity = itemQuantity))
                    }
                }
            }
        }


    }
    fun addNewItem() {

        //Tell the RecyclerView to draw a new row at the end
        listOfProducts.add(GroceryItems(DEFAULT_ID, GENERAL_TYPE, DEFAULT_PRODUCT_NAME, DEFAULT_ITEM_QUANTITY))
        notifyItemInserted(listOfProducts.size - 1)
    }

    fun saveItems(){

        for (item in listOfProducts) {
            // Access the properties directly
            println("Item: ${item.name}, Quantity: ${item.quantity}")
        }


        processGroceryInput(listOfProducts)




        listOfProducts.clear()
        finalSortedList.addAll(listOfFruit)
        finalSortedList.addAll(listOfVegetables)
        finalSortedList.addAll(listOfDairy)
        finalSortedList.addAll(listOfGeneralItems)
        listOfFruit.clear()
        listOfVegetables.clear()
        listOfDairy.clear()
        listOfGeneralItems.clear()
       notifyDataSetChanged()

for (item in finalSortedList) {
            // Access the properties directly
            println("Item: ${item.name}, Quantity: ${item.quantity}")
        }


    }

    override fun getItemCount(): Int {
        return listOfProducts.size
    }

}