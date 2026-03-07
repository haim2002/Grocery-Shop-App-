package grocery.shopping

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import grocery.shopping.data.ListInfo

class MetadataListAdapter(private var lists: List<ListInfo>) :
RecyclerView.Adapter<MetadataListAdapter.ItemViewHolder>() {




    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val listName: TextView = view.findViewById(R.id.textName)
        val listCreator: TextView = view.findViewById(R.id.textCreator)
        val listDate: TextView = view.findViewById(R.id.textDate)


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.metadata_list_adapter, parent, false)
        return ItemViewHolder(view)
    }



    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentMetadata = lists[position]
        holder.listName.text = currentMetadata.listName
        holder.listCreator.text = currentMetadata.creatorUserName
        holder.listDate.text = currentMetadata.dateCreatedTime.toString()

        // Handle the "Button" click
        holder.itemView.setOnClickListener {
            // Logic to open the list using currentMetadata.firebaseKey
        }
    }

    override fun getItemCount(): Int = lists.size

    fun updateData(newList: List<ListInfo>) {
        this.lists = newList
        notifyDataSetChanged()
    }

}