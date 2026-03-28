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

class MetadataListAdapter(var lists: List<ListInfo>) :
    RecyclerView.Adapter<MetadataListAdapter.ItemViewHolder>() {


    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val listName: TextView = view.findViewById(R.id.listName)
        var createdBy: TextView = view.findViewById(R.id.creatorName)
        val listDate: TextView = view.findViewById(R.id.listDate)
        val updatedBy: TextView = view.findViewById(R.id.listUpdatedBy)


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.metadata_list_adapter, parent, false)
        return ItemViewHolder(view)


    }

    val selectedPositions = mutableSetOf<Int>()

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val context = holder.itemView.context
        val currentMetadata = lists[position]
        Log.d("MetadataListAdapter", "currentMetadata: $currentMetadata")

        holder.listName.text = currentMetadata.listName
        holder.createdBy.text = context.getString(R.string.created_by, currentMetadata.createdBy)
        holder.updatedBy.text = context.getString(R.string.updated_by, currentMetadata.updatedBy)
        holder.listDate.text = context.getString(R.string.list_date, setDateFormat(currentMetadata.timeCreated))

        Log.d("MetadataListAdapter", "onBindViewHolder: ${currentMetadata.listName}")
        Log.d("MetadataListAdapter", "onBindViewHolder: ${currentMetadata.createdBy}")
        Log.d("MetadataListAdapter", "onBindViewHolder: ${currentMetadata.timeCreated}")
        Log.d("MetadataListAdapter", "onBindViewHolder: ${currentMetadata.firebaseKey}")



        holder.itemView.setBackgroundColor(
            if (selectedPositions.contains(position)) Color.GRAY else Color.LTGRAY
        )


        // Handle the "Button" click
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            Log.d("MetadataListAdapter", "listName: ${holder.listName.text}")
            Log.d("MetadataListAdapter", "listCreator: ${holder.createdBy.text}")
            Log.d("MetadataListAdapter", "listDate: ${holder.listDate.text}")
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
    fun setDateFormat( timeCreated: Long) : String{

      return  SimpleDateFormat(
            "dd/MM/yy HH:mm", // הוספנו HH:mm לשעה ודקות
            Locale.getDefault()
        ).format(Date(timeCreated))
    }
}
