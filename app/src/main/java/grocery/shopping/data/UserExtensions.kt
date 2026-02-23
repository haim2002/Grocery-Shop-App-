package grocery.shopping.data

import com.google.firebase.auth.FirebaseAuth

fun getGoogleUser(): String? {

    val auth = FirebaseAuth.getInstance()
    val currentUid = auth.currentUser?.uid
    return currentUid
}