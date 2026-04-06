package grocery.shopping.metadata

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import grocery.shopping.R
import grocery.shopping.data.ListInfo
import grocery.shopping.data.ShoppingRepository
import grocery.shopping.data.choosingListNameInDialog
import grocery.shopping.displayer.ListDisplayer
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MetadataListAdapter(var lists: List<ListInfo>) :
    RecyclerView.Adapter<MetadataListAdapter.ItemViewHolder>() {


    var actionMode: ActionMode? = null
    var selectedPosition: Int = RecyclerView.NO_POSITION

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        //set up the variables for the adapter
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

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val context = holder.itemView.context
        val currentMetadata = lists[position]
        Log.d("MetadataListAdapter", "currentMetadata: $currentMetadata")

        // Set the text for the TextView
        holder.listName.text = currentMetadata.listName
        holder.createdBy.text = context.getString(R.string.created_by, currentMetadata.createdBy)
        holder.updatedBy.text = context.getString(R.string.updated_by, currentMetadata.updatedBy)
        holder.listDate.text =
            context.getString(R.string.list_date, setDateFormat(currentMetadata.timeCreated))

        Log.d("MetadataListAdapter", "onBindViewHolder: ${currentMetadata.listName}")
        Log.d("MetadataListAdapter", "onBindViewHolder: ${currentMetadata.createdBy}")
        Log.d("MetadataListAdapter", "onBindViewHolder: ${currentMetadata.timeCreated}")
        Log.d("MetadataListAdapter", "onBindViewHolder: ${currentMetadata.firebaseKey}")


        setSelectedPositionColor(position, holder)

        // Handle the "Button" click
        holder.itemView.setOnClickListener {

            Log.d("MetadataListAdapter", "listName: ${holder.listName.text}")
            Log.d("MetadataListAdapter", "listCreator: ${holder.createdBy.text}")
            Log.d("MetadataListAdapter", "listDate: ${holder.listDate.text}")

            // Check if an item is selected
            val currentPos = holder.bindingAdapterPosition
            if (currentPos == RecyclerView.NO_POSITION) return@setOnClickListener
            if (actionMode == null) {
                // If no item is selected, start a new activity for list content
                val intent = Intent(context, ListDisplayer::class.java)
                intent.putExtra("LIST_ID", currentMetadata.firebaseKey)
                intent.putExtra("LIST_NAME", currentMetadata.listName)
                context.startActivity(intent)

            }
            //
            changeSelectedItem(currentPos, holder)

        }
        //
        val actionModeCallback = object : ActionMode.Callback {


            override fun onCreateActionMode(
                mode: ActionMode,
                menu: Menu
            ): Boolean {
                // INFLATE your XML here - this puts the buttons on top
                mode.menuInflater.inflate(R.menu.list_delete_menu, menu)
                mode.title = "נבחרה רשימה"
                return true
            }

            override fun onActionItemClicked(
                mode: ActionMode,
                item: MenuItem,
            ): Boolean {
                return when (item.itemId) {
                    R.id.action_delete -> {
                        ShoppingRepository.deleteListFromFirebase(lists[selectedPosition].firebaseKey)
                        mode.finish() // Closes the top bar
                        true
                    }

                    R.id.action_edit -> {
                        // Standard launch for update
                        val context = holder.itemView.context as? AppCompatActivity
                        context?.lifecycleScope?.launch{
                          //  val listName = choosingListNameInDialog(context)
                            updateListName(lists[selectedPosition].firebaseKey, context)
                            mode.finish()

                        }
                        true
                    }

                    else -> false
                }
            }

            override fun onPrepareActionMode(mode: ActionMode, menu: Menu) =
                false

            override fun onDestroyActionMode(mode: ActionMode) {
                val previousSelection = selectedPosition
                selectedPosition = RecyclerView.NO_POSITION // Reset the selection
                actionMode = null

                // Refresh the item so the background turns transparent again
                notifyItemChanged(previousSelection)
            }
        }

// 3. Trigger it inside your Adapter's onLongClickListener
        holder.itemView.setOnLongClickListener { view ->

            val currentPos = holder.bindingAdapterPosition
            if (currentPos == RecyclerView.NO_POSITION) return@setOnLongClickListener false

            if (actionMode == null) {
                selectedPosition = currentPos // Save the clicked position
                val activity = view.context as AppCompatActivity
                actionMode = activity.startSupportActionMode(actionModeCallback)
                notifyItemChanged(currentPos) // Tell the list to redraw this item
            }
            true
        }


    }

    override fun getItemCount(): Int = lists.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<ListInfo>) {
        this.lists = newList
        notifyDataSetChanged()
    }

    fun setDateFormat(timeCreated: Long): String {

        return SimpleDateFormat(
            "dd/MM/yy HH:mm",
            Locale.getDefault()
        ).format(Date(timeCreated))
    }

    fun setSelectedPositionColor(
        currentPosition: Int,
        holder: ItemViewHolder
    ) {

        if (currentPosition == selectedPosition) {
            setSelectedColor(holder)
        } else {
            holder.itemView.setBackgroundColor(Color.LTGRAY)
            setDefaultColor(holder)
        }
    }

    fun changeSelectedItem(currentPos: Int, holder: ItemViewHolder) {

        if (actionMode != null) {
            if (currentPos == selectedPosition) {
                // User clicked the selected item again -> CANCEL
                setDefaultColor(holder)
                actionMode?.finish()
            } else {
                // User clicked a different item -> SWAP
                val previousPos = selectedPosition
                this.selectedPosition = currentPos

                notifyItemChanged(previousPos)
                notifyItemChanged(selectedPosition)

                // Only update the title if the menu is staying open
                actionMode?.title = "נבחרה רשימה"
            }
        }
    }

    fun setDefaultColor(holder: ItemViewHolder) {
        holder.itemView.setBackgroundColor(Color.LTGRAY)
    }

    fun setSelectedColor(holder: ItemViewHolder) {
        holder.itemView.setBackgroundColor(Color.DKGRAY)
    }

    suspend fun updateListName(listID: String, context: Context) {

        val listName = choosingListNameInDialog(context)
        ShoppingRepository.updateListNameInFirebase(listID, listName)
        notifyItemChanged(selectedPosition)
    }
}