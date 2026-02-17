package grocery.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import grocery.shopping.data.Dairy
import grocery.shopping.data.Fruit
import grocery.shopping.data.GroceryItems
import grocery.shopping.data.Vegetables
import grocery.shopping.data.typeDetermine

class ListAdapter() : RecyclerView.Adapter<ListAdapter.ItemViewHolder>() {

    val listOfProducts: MutableList<GroceryItems> = mutableListOf()


    init{

        listOfProducts.add(GroceryItems("", "", "", 1))
    }
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

            // Check if the word typed matches your 'typeDetermine' map
            val detectedType = typeDetermine[input]
            if (detectedType != null && detectedType != currentItem.type) {

                // Transform the item into the specific subclass (Fruit, Vegetable, etc.)
                val transformedItem = when (detectedType) {
                    "Fruit" -> Fruit(name = input, quantity = currentItem.quantity)
                    "Vegetable" -> Vegetables(name = input, quantity = currentItem.quantity)
                    "Dairy" -> Dairy(name = input, quantity = currentItem.quantity)
                    else -> GroceryItems(name = input, quantity = currentItem.quantity)
                }

                // Update the list at this position with the new specific object
                listOfProducts[position] = transformedItem
            }
        }

        // 4. Save quantity changes from the Spinner
        holder.productAmount.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                currentItem.quantity = options[pos].toInt()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    fun addNewItem() {
        listOfProducts.add(GroceryItems("", "", "", 1))
        //Tell the RecyclerView to draw a new row at the end
        notifyItemInserted(listOfProducts.size - 1)
    }

    override fun getItemCount(): Int {
        return listOfProducts.size
    }

}