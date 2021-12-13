package com.example.go4lunch

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import android.content.pm.PackageManager

import android.util.Base64
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import kotlinx.android.synthetic.main.activity_sign_in.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class SignInActivity : AppCompatActivity() {

    lateinit var callbackManager: CallbackManager

    companion object {
        private const val RC_SIGN_IN = 120
    }

    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        callbackManager = CallbackManager.Factory.create()
        val signInBtn = findViewById<Button>(R.id.signInBtn)
        //val signInBtnGoogle = findViewById<Button>(R.id.signInGoogleBtn)

        val viewAllBtn = findViewById<Button>(R.id.viewAllProfilesBtn)
        val mapsBtn = findViewById<Button>(R.id.mapsBtn)
        val listRestBtn = findViewById<Button>(R.id.listRestBtn)


        createAccountEmailBtn.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.create_account_layout, null)
            val emailET = dialogLayout.findViewById<EditText>(R.id.emailET)
            val passwordET = dialogLayout.findViewById<EditText>(R.id.passwordET)
            val passwordCheckerET = dialogLayout.findViewById<EditText>(R.id.passwordCheckerET)

            with(builder) {
                setTitle("Create Your Account")
                setPositiveButton("Submit") { _, _ ->
//                    if(editText.text.toString().length >3) {
//                        Days.currentDay[2] = editText.text.toString()
//                    }
                    println(emailET.text.toString())
                    println(passwordET.text.toString())
                    println(passwordCheckerET.text.toString())
                    createAccountEmail(emailET.text.toString(),passwordET.text.toString())
                }
                setNegativeButton("Cancel") { _, _ ->
                    Log.d("main", "Negative Button Clicked")
                }
                setView(dialogLayout)
                show()
            }
        }


        signInBtnEmail.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.sign_in_email_layout, null)
            val emailET = dialogLayout.findViewById<EditText>(R.id.emailSignInET)
            val passwordET = dialogLayout.findViewById<EditText>(R.id.passwordSignInET)

            with(builder) {
                setTitle("Create Your Account")
                setPositiveButton("Submit") { _, _ ->
//                    if(editText.text.toString().length >3) {
//                        Days.currentDay[2] = editText.text.toString()
//                    }
                    println(emailET.text.toString())
                    println(passwordET.text.toString())
                    signInEmail(emailET.text.toString(),passwordET.text.toString())


                }
                setNegativeButton("Cancel") { _, _ ->
                    Log.d("main", "Negative Button Clicked")
                }
                setView(dialogLayout)
                show()
            }
        }













        //Prints key hash
        try {
            val info = packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
               println("KeyHash: "+ Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }

///facebook sign in
        buttonFacebookLogin.setReadPermissions("email", "public_profile")
        buttonFacebookLogin.registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d(TAG, "facebook:onSuccess:$loginResult")
                println("IT WORKED HERE IS YOUR ID " + loginResult.accessToken)
                mAuth = FirebaseAuth.getInstance()

                handleFacebookAccessToken(loginResult.accessToken)





                //val credential = GoogleAuthProvider.getCredential(idToken, null)


            //    startWriteProfileActivity()
                //handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d(TAG, "facebook:onCancel")
            }

            override fun onError(error: FacebookException) {
                Log.d(TAG, "facebook:onError", error)
            }
        })
        // ...
        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)

            // Pass the activity result back to the Facebook SDK
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }


















        signInGoogleBtn.setOnClickListener {
            // Configure Google Sign In
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("456865060685-24o3ftvi205am9suk0p9jdahr19ho7vt.apps.googleusercontent.com")
                .requestEmail()
                .build()
            googleSignInClient = GoogleSignIn.getClient(this, gso)
            //Firebase Auth instance
            mAuth = FirebaseAuth.getInstance()

            signIn()
        }


        viewAllBtn.setOnClickListener {
            val intent = Intent(this, AllUsersActivity::class.java)
            startActivity(intent)

        }



        mapsBtn.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)

        }

        listRestBtn.setOnClickListener {
            val intent = Intent(this, ListofRestaurantsActivity::class.java)
            startActivity(intent)

        }
    }




    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("SignInActivity", "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("SignInActivity", "Google sign in failed", e)
                }
            } else {
                Log.w("SignInActivity", exception.toString())
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SignInActivity", "signInWithCredential:success")
                    val intent = Intent(this, WriteProfileActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.d("SignInActivity", "signInWithCredential:failure")
                }
            }




    }

fun startWriteProfileActivity(){
    val intent = Intent(this, WriteProfileActivity::class.java)
    startActivity(intent)
    finish()
}
    fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = mAuth.currentUser
                    println("Auth user is " + mAuth.currentUser)
                    startWriteProfileActivity()
                    // updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    //updateUI(null)
                }
            }
    }

    fun createAccountEmail(email:String,password:String){
        mAuth = FirebaseAuth.getInstance()
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = mAuth.currentUser
                    println("The user ${mAuth.currentUser} was created")
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun signInEmail(email:String,password:String){
        mAuth = FirebaseAuth.getInstance()
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = mAuth.currentUser
                    println("The user ${mAuth.currentUser} was signed in")

                    startWriteProfileActivity()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }

    }
}
