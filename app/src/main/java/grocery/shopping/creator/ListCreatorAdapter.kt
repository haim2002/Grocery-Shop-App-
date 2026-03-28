package grocery.shopping.creator

import android.app.AlertDialog
import android.content.Context
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import grocery.shopping.R
import grocery.shopping.data.ListInfo
import grocery.shopping.data.DEFAULT_ITEM_QUANTITY
import grocery.shopping.data.DEFAULT_LIST_NAME
import grocery.shopping.data.DEFAULT_PRODUCT_NAME
import grocery.shopping.data.GroceryItems
import grocery.shopping.data.MAX_ITEM_QUANTITY
import grocery.shopping.data.MIN_ITEM_QUANTITY
import grocery.shopping.data.ShoppingRepository
import grocery.shopping.data.UNNAMED_LIST
import grocery.shopping.data.getGoogleUserName
import grocery.shopping.data.sortGroceryInput
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class ListCreatorAdapter(
    var listOfProducts: MutableList<GroceryItems>,
    var metadataList: ListInfo,
    var listID: String?
) : RecyclerView.Adapter<ListCreatorAdapter.ItemViewHolder>() {



    init {

        Log.d("ListCreatorAdapter", "listOfProducts: $listOfProducts")
    }


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
        val currentItem = listOfProducts[position]

        // 1. CLEANUP: Remove the old listener stored in the tag
        (holder.productName.tag as? TextWatcher)?.let {
            holder.productName.removeTextChangedListener(it)
        }

        // 2. SET TEXT: Safe to do now without triggering the old listener
        holder.productName.setText(currentItem.name)

        // 3. ATTACH NEW LISTENER: Update the data model as the user types
        val watcher = holder.productName.doAfterTextChanged { text ->
            currentItem.name = text.toString()
            // No need for listOfProducts[position].name = input because
            // currentItem is already a reference to that same object.
        }
        holder.productName.tag = watcher

        // 4. SPINNER SETUP
        val options = (MIN_ITEM_QUANTITY..MAX_ITEM_QUANTITY).toList().map { it.toString() }
        val spinnerAdapter = ArrayAdapter(
            holder.itemView.context,
            android.R.layout.simple_spinner_item,
            options
        )
        holder.productAmount.adapter = spinnerAdapter

        // 5. SPINNER SELECTION: Show the saved quantity
        val selectionIndex = (currentItem.quantity - MIN_ITEM_QUANTITY).coerceAtLeast(0)
        holder.productAmount.setSelection(
            selectionIndex,
            false
        ) // 'false' prevents triggering listener immediately

        // 6. SPINNER LISTENER: Update the data model
        holder.productAmount.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                currentItem.quantity = options[pos].toInt()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    // return the number of items in the list
    override fun getItemCount(): Int {

        return listOfProducts.size
    }

    // Add a new item to the list
    fun addNewItem(recyclerView: RecyclerView) {

        if (listOfProducts.isEmpty() || listOfProducts[listOfProducts.size - 1].name.isNotBlank()) {

            listOfProducts.add(
                GroceryItems(
                    name = DEFAULT_PRODUCT_NAME,
                    quantity = DEFAULT_ITEM_QUANTITY
                )
            )
            // Notify the adapter that an item has been added
            notifyItemInserted(listOfProducts.size - 1)

            useKeyboardForNextLIne(recyclerView)
        } else {
            Toast.makeText(recyclerView.context, "קודם יש לכתוב בשורה הזאת", Toast.LENGTH_SHORT)
                .show()
        }
    }

    // make keyboard show for the new line in recyclerview
    fun useKeyboardForNextLIne(recyclerView: RecyclerView) {
        // Scroll to the new item
        recyclerView.scrollToPosition(listOfProducts.size - 1)
        recyclerView.post {
            // refence to the last item in the list
            val viewHolder = recyclerView.findViewHolderForAdapterPosition(listOfProducts.size - 1)
            //reference to the edit text in the last item
            val editText = viewHolder?.itemView?.findViewById<EditText>(R.id.productName)
            editText?.let {
                // Set focus on the EditText
                it.requestFocus()
                // Show the keyboard
                val imm =
                    recyclerView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(it, InputMethodManager.SHOW_IMPLICIT)
            }
        }

    }

    fun removeItem(position: Int) {
        if (listOfProducts.isNotEmpty()) {
            listOfProducts.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    suspend fun saveItems(recyclerView: RecyclerView): SaveResult {


        //  check if data is valid
        if (!isDataValid(listOfProducts)) {
            return SaveResult.EmptyList
        }




        // check if user canceled or didn't name the list
        /*
        if (listName == UNNAMED_LIST) {
            return SaveResult.CanceledByUserInfo
        }
*/
        // prepare the data
        Log.d("list before sorting", "listOfProducts: $listOfProducts")
        val finalSortedList = sortGroceryInput(listOfProducts)
        Log.d("list after sorting", "listOfProducts: $finalSortedList")



        // 5. Attempt to save to Firebase

        if (listID == null) {

            val listName = choosingListNameInDialog(recyclerView.context)
            metadataList = ListInfo(
                listName = listName,
                createdBy = getGoogleUserName()
            )
            val result = ShoppingRepository.saveList(finalSortedList, metadataList)
            return if (result.isSuccess) {

                SaveResult.Success
            } else {
                SaveResult.NetworkError
            }
        } else {

            metadataList.timeCreated = System.currentTimeMillis()
            metadataList.updatedBy = getGoogleUserName()
            Log.d("FirebaseDebug", "Sending Update to ID: '$listID'")
            val result =
                ShoppingRepository.updateListInFirebase(listID!!, finalSortedList, metadataList)
            return if (result.isSuccess) {

                SaveResult.Success
            } else {
                SaveResult.NetworkError
            }
        }


        // 6. Return based on the Repository's Result

    }

    sealed class SaveResult {
        object Success : SaveResult()
        object EmptyList : SaveResult()
        object CanceledByUserInfo : SaveResult()
        object NetworkError : SaveResult()
    }

    // check if the list is valid
    fun isDataValid(listOfProducts: MutableList<GroceryItems>): Boolean {

        return listOfProducts.isNotEmpty() && listOfProducts.all { it.name.isNotBlank() }
    }


    suspend fun choosingListNameInDialog(context: Context): String =
        suspendCancellableCoroutine { continuation ->
            val inputField = EditText(context).apply {
                hint = "הרשימה שלי"
            }

            val dialog = AlertDialog.Builder(context)

                .setTitle("שם הרשימה")
                .setView(inputField)
                .setPositiveButton("שמור") { _, _ ->
                    val name = inputField.text.toString()
                    // Resume the coroutine with the name
                    continuation.resume(name.ifBlank { DEFAULT_LIST_NAME })
                }
                .setNegativeButton("ביטול") { dialog, _ ->
                    // Resume with null if canceled
                    continuation.resume(UNNAMED_LIST)
                    dialog.dismiss()
                }
                .setOnCancelListener {
                    // Important: handle if user clicks outside the dialog
                    continuation.resume(UNNAMED_LIST)
                }
                .create()

            // If the coroutine is canceled externally, dismiss the dialog
            continuation.invokeOnCancellation {
                dialog.dismiss()
            }

            dialog.show()
        }
}