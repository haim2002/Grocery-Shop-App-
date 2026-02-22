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
import grocery.shopping.data.GroceryItems
import grocery.shopping.data.sortGroceryInput


class ListCreatorAdapter : RecyclerView.Adapter<ListCreatorAdapter.ItemViewHolder>() {

    val listOfProducts: MutableList<GroceryItems> = mutableListOf()
    var finalSortedList: MutableList<GroceryItems> = mutableListOf()
/*
    init{

        listOfProducts.add(GroceryItems(DEFAULT_ID, GENERAL_TYPE, DEFAULT_PRODUCT_NAME, DEFAULT_ITEM_QUANTITY))
    }
 */
     class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

         //reference to the views in the layout
        val productName: EditText = itemView.findViewById(R.id.productName)
         val productAmount: Spinner = itemView.findViewById(R.id.productAmount)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder { //inflate the layout of each item
        val inflatedView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_creator_adapter, parent, false)
        return ItemViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

            // make reference of current item in the list
            val currentItem = listOfProducts[position]

            // Set up the EditText with the current name
            holder.productName.setText(currentItem.name)

            // Set up the spinner with options
            val options = (MIN_ITEM_QUANTITY..MAX_ITEM_QUANTITY).toList().map { it.toString() }
            val spinnerAdapter =
                ArrayAdapter(holder.itemView.context, android.R.layout.simple_spinner_item, options)
            holder.productAmount.adapter = spinnerAdapter

            // Set the selected item based on the current quantity
            holder.productAmount.setSelection(currentItem.quantity - 1)

            // Save name changes from the EditText
            holder.productName.doAfterTextChanged { text ->
                val input = text.toString()
                currentItem.name = input
                    // Update the item in the list
                    listOfProducts[position] = GroceryItems(name = input, quantity = currentItem.quantity)
            }

        // Set up the listener for quantity changes
        holder.productAmount.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                currentItem.quantity = options[pos].toInt()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    fun addNewItem() {

        // Add a new item to the list
        listOfProducts.add(GroceryItems(DEFAULT_ID, GENERAL_TYPE, DEFAULT_PRODUCT_NAME, DEFAULT_ITEM_QUANTITY))
        notifyItemInserted(listOfProducts.size - 1)
    }

    fun saveItems(){

        for (item in listOfProducts) {

            println("Item: ${item.name}, Quantity: ${item.quantity}")
        }

        finalSortedList= sortGroceryInput(listOfProducts)
            //notifyDataSetChanged()

        for (item in finalSortedList) {

            println("Item: ${item.name}, Quantity: ${item.quantity}")
        }
    }

    override fun getItemCount(): Int {
        // Return the number of items in the list
        return listOfProducts.size
    }

}