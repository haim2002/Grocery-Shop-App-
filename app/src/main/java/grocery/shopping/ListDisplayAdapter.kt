package grocery.shopping

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import grocery.shopping.data.GENERAL_TYPE
import grocery.shopping.data.GroceryItems

class ListDisplayAdapter(var listOfProducts: MutableList<GroceryItems>) : RecyclerView.Adapter<ListDisplayAdapter.ItemViewHolder>() {

/*
    init {

        listOfProducts.add(GroceryItems(GENERAL_TYPE, "קקק", 8))
        listOfProducts.add(GroceryItems(GENERAL_TYPE, "בשדב", 5))

    }
*/
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //reference to the views in the layout
        val productName: TextView = itemView.findViewById(R.id.product_display)
        val productAmount: TextView = itemView.findViewById(R.id.quantity_display)
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
        holder.productAmount.text = currentItem.quantity.toString()
    }

    override fun getItemCount(): Int {

        return listOfProducts.size
    }


}