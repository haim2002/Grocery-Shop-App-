package grocery.shopping.ui.login
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.Firebase
import grocery.shopping.MainActivity
import grocery.shopping.R
import kotlinx.coroutines.launch

/**
 * Demonstrate Firebase Authentication using a Google ID Token.
 */
class GoogleSignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var credentialManager: CredentialManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_sign_in)

        auth = Firebase.auth
        credentialManager = CredentialManager.create(this)

        val googleBtn: com.google.android.gms.common.SignInButton = findViewById(R.id.googleSignInBtn)
        googleBtn.setOnClickListener {
            launchCredentialManager()
        }
    }

    private fun launchCredentialManager() {
        // FIX: Hardcode your type 3 Web Client ID to ensure it is never null
        val webClientId = "465154159838-rlmvfdvg0nrs1msefdcml7tucpoe7plj.apps.googleusercontent.com"

        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(webClientId)
            // FIX: This MUST be false for development/new users
            .setFilterByAuthorizedAccounts(false)
            .setAutoSelectEnabled(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                val result = credentialManager.getCredential(
                    context = this@GoogleSignInActivity,
                    request = request
                )
                handleSignIn(result.credential)
            } catch (e: GetCredentialException) {
                // Logcat filter: tag:GoogleActivity
                Log.e("GoogleActivity", "Credential Error: ${e.message}")
            }
        }
    }

    private fun handleSignIn(credential: Credential) {
        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("GoogleActivity", "Firebase Auth: SUCCESS")
                    updateUI(auth.currentUser)
                } else {
                    Log.e("GoogleActivity", "Firebase Auth: FAILED", task.exception)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            // Create an intent to go to your main screen
            val intent = Intent(this, MainActivity::class.java)
            // This clears the backstack so the user can't go back to login
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish() // Closes the LoginActivity
        }
    }



}
