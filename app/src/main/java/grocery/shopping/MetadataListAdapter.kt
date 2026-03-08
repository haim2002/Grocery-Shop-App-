package grocery.shopping

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import grocery.shopping.data.ListInfo
import grocery.shopping.data.ShoppingRepository.deleteListFromFirebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MetadataListAdapter(var lists: List<ListInfo>) : RecyclerView.Adapter<MetadataListAdapter.ItemViewHolder>() {


    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val listName: TextView = view.findViewById(R.id.textName)
        var creatorName: TextView = view.findViewById(R.id.textCreator)
        val listDate: TextView = view.findViewById(R.id.textDate)


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.metadata_list_adapter, parent, false)
        return ItemViewHolder(view)


    }

    val selectedPositions = mutableSetOf<Int>()

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentMetadata = lists[position]
        Log.d("MY_TAG", "currentMetadata: $currentMetadata")

        holder.listName.text = currentMetadata.listName
        holder.creatorName.text = currentMetadata.creatorName
        holder.listDate.text = SimpleDateFormat("dd/MM/yy",Locale.getDefault()).format(Date(currentMetadata.timeCreated))
        Log.d("MY_TAG", "onBindViewHolder: ${currentMetadata.listName}")
        Log.d("MY_TAG", "onBindViewHolder: ${currentMetadata.creatorName}")
        Log.d("MY_TAG", "onBindViewHolder: ${currentMetadata.timeCreated}")
        Log.d("MY_TAG", "onBindViewHolder: ${currentMetadata.firebaseKey}")



        holder.itemView.setBackgroundColor(
            if (selectedPositions.contains(position)) Color.GRAY else Color.LTGRAY
        )





        // Handle the "Button" click
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            Log.d("MY_TAG", "listName: ${ holder.listName.text}")
            Log.d("MY_TAG", "listCreator: ${ holder.creatorName.text}")
            Log.d("MY_TAG", "listDate: ${ holder.listDate.text}")
            val intent = Intent(context, ListDisplayer::class.java)
            intent.putExtra("LIST_ID", currentMetadata.firebaseKey)
            context.startActivity(intent)

        }

        holder.itemView.setOnLongClickListener {
            if (selectedPositions.contains(position)) {
                selectedPositions.remove(position)
            } else {
                selectedPositions.add(position)
                deleteListFromFirebase(currentMetadata.firebaseKey)
            }
            notifyItemChanged(position) // Redraw just this row
            true
        }

    }

    override fun getItemCount(): Int = lists.size

    fun updateData(newList: List<ListInfo>) {
        this.lists = newList
        notifyDataSetChanged()
    }

}