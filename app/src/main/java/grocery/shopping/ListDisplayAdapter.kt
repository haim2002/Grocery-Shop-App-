package grocery.shopping

import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import grocery.shopping.data.GroceryItems
import grocery.shopping.data.ShoppingRepository.updateCheckStatusInFirebase


class ListDisplayAdapter(var listOfProducts: MutableList<GroceryItems>,var listId: String) : RecyclerView.Adapter<ListDisplayAdapter.ItemViewHolder>() {


/*
        init {
             val onCheckChanged: (String, Boolean) -> Unit

        }

 */

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //reference to the views in the layout
        val productName: TextView = itemView.findViewById(R.id.product_display)
        val productQuantity: TextView = itemView.findViewById(R.id.quantity_display)
        val checkbox: CheckBox = itemView.findViewById(R.id.checkbox)



    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder { //inflate the layout of each item
        val inflatedView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_display_adapter, parent, false)
        return ItemViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        // make reference of current item in the list
        val currentItem = listOfProducts[position]
        holder.productName.text = currentItem.name
        holder.productQuantity.text = currentItem.quantity.toString()
        holder.checkbox.isChecked = currentItem.isChecked


        toggleCrossedLine(holder.productName, holder.productQuantity, currentItem.isChecked)

        holder.checkbox.setOnCheckedChangeListener {_, isChecked ->
            // Toggle the boolean
            currentItem.isChecked = isChecked
            updateCheckStatusInFirebase(listId, position.toString(), isChecked)

            // Update the UI immediately
            toggleCrossedLine(holder.productName,holder.productQuantity, currentItem.isChecked)
        }

    }



    override fun getItemCount(): Int {

        return listOfProducts.size
    }
    private fun toggleCrossedLine(productName: TextView, productQuantity: TextView, isChecked: Boolean) {
        if (isChecked) {
            // Adds the strike-through flag
            productName.paintFlags = productName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            productQuantity.paintFlags = productQuantity.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            productName.setTextColor(Color.GRAY)
            productQuantity.setTextColor(Color.GRAY)
        } else {
            // Removes the strike-through flag
            productName.paintFlags = productName.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            productQuantity.paintFlags = productQuantity.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            productName.setTextColor(Color.BLACK) // Return to normal
            productQuantity.setTextColor(Color.BLACK)
        }
    }

}