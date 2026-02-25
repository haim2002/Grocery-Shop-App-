package grocery.shopping.data

import com.google.firebase.auth.FirebaseAuth

fun getGoogleUserName(): String? {

    val user = FirebaseAuth.getInstance().currentUser
    val displayName = user?.displayName

    return displayName
}
fun getGoogleUserID(): String? {

    val auth = FirebaseAuth.getInstance()
    val currentUid = auth.currentUser?.uid
    return currentUid
}