package com.example.mrfarmergrocer.ui.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.mrfarmergrocer.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dialog_progress.*

open class BaseActivity : AppCompatActivity() {

    var backPressedDuration: Long = 0
    /**
     * This is a progress dialog instance which we will initialize later on.
     */
    private lateinit var mProgressDialog: Dialog

    /**
     * A function to show the success and error messages in snack bar component.
     */
    fun showErrorSnackBar(message: String, errorMessage: Boolean) {
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        if (errorMessage) {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                    R.color.colorSnackBarError
                )
            )
        } else {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                    R.color.colorSnackBarSuccess
                )
            )
        }
        snackBar.show()
    }

    fun showProgressDialog(text: String) {
        mProgressDialog = Dialog(this)

        /*Set the screen content from a layout resource.
        The resource will be inflated, adding all top-level views to the screen.*/
        mProgressDialog.setContentView(R.layout.dialog_progress)

        mProgressDialog.tv_progress_text.text = text

        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        //Start the dialog and display it on screen.
        mProgressDialog.show()
    }

    /**
     * This function is used to dismiss the progress dialog if it is visible to user.
     */
    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }

    fun doubleBackToExit(){
        if (backPressedDuration + 1000 > System.currentTimeMillis()){
            finishAffinity() // Close all activites
            System.exit(0)  // Releasing resources
        }
        else{
            Toast.makeText(getBaseContext(),
                "Press back again to exit", Toast.LENGTH_SHORT)
                .show()
        }
        backPressedDuration = System.currentTimeMillis()

    }
}