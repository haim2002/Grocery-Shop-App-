package grocery.shopping.data

import android.app.AlertDialog
import android.content.Context
import android.widget.EditText
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

//  // function that pops a dialog and returns value in string
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