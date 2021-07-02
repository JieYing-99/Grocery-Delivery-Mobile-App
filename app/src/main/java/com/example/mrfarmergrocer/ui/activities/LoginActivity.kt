package com.example.mrfarmergrocer.ui.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.TextView
import com.example.mrfarmergrocer.R
import com.example.mrfarmergrocer.firestore.FirestoreClass
import com.example.mrfarmergrocer.models.User
import com.google.firebase.auth.FirebaseAuth
import com.example.mrfarmergrocer.utils.Constants
import kotlinx.android.synthetic.main.activity_login.*

@Suppress("DEPRECATION")
class LoginActivity : BaseActivity(), View.OnClickListener {

    lateinit var sharedPreferences: SharedPreferences
    var isRemember = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // This is used to hide the status bar and make the login screen as a full screen activity.
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )

        sharedPreferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)

        isRemember = sharedPreferences.getBoolean("CHECKBOX", false)

        if(isRemember) {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        bt_remember.setOnClickListener{
            val email: String = et_email.text.toString()
            val password: String = et_password.text.toString()
            val checked: Boolean = bt_remember.isChecked

            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("Email", email)
            editor.putString("Password", password)
            editor.putBoolean("CHECKBOX", checked)
            editor.apply()

            //val intent = Intent(this@LoginActivity, MainActivity::class.java)
            //startActivity(intent)
            //finish()
        }

        // Click event assigned to Forgot Password text.
        tv_forgot_password.setOnClickListener(this)
        // Click event assigned to Login button.
        btn_login.setOnClickListener(this)
        // Click event assigned to Register text.
        tv_register.setOnClickListener(this)
    }

    fun userLoggedInSuccess(user: User) {

        // Hide the progress dialog.
        hideProgressDialog()

        // Print the user details in the log as of now.
        Log.i("First Name: ", user.firstName)
        Log.i("Last Name: ", user.lastName)
        Log.i("Email: ", user.email)

        // Redirect the user to Main Screen after log in.
        if (user.profileCompleted == 0) {
            // If the user profile is incomplete then launch the UserProfileActivity.
            val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
            startActivity(intent)
        } else {
            // Redirect the user to Main Screen after log in.
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }        
        finish()
    }

    // In Login screen the clickable components are Login Button, ForgotPassword text and Register Text.
    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {

                R.id.tv_forgot_password -> {
                    // Launch the forgot password screen when the user clicks on the forgot password text.
                    val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
                    startActivity(intent)
                }

                R.id.btn_login -> {

                    logInRegisteredUser()

                }

                R.id.tv_register -> {
                    // Launch the register screen when the user clicks on the text.
                    val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    /**
     * A function to validate the login entries of a user.
     */
    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(et_email.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(et_password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else -> {
                true
            }
        }
    }

    /**
     * A function to Log-In. The user will be able to log in using the registered email and password with Firebase Authentication.
     */
    private fun logInRegisteredUser() {

        if (validateLoginDetails()) {

            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))

            // Get the text from editText and trim the space
            val email = et_email.text.toString().trim { it <= ' ' }
            val password = et_password.text.toString().trim { it <= ' ' }

            // Log-In using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    // Hide the progress dialog
                    //hideProgressDialog()
                    if (task.isSuccessful) {

                        FirestoreClass().getUserDetails(this@LoginActivity)
                    } else {
                        // Hide the progress dialog
                        hideProgressDialog()
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }
    override fun onBackPressed() {
        doubleBackToExit()
    }
}