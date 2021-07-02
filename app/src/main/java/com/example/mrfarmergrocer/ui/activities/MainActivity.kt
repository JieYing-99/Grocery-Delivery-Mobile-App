package com.example.mrfarmergrocer.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mrfarmergrocer.R
import com.example.mrfarmergrocer.firestore.FirestoreClass
import com.example.mrfarmergrocer.models.Product
import com.example.mrfarmergrocer.ui.adapters.HomeItemsListAdapter
import com.example.mrfarmergrocer.utils.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_nav_view.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create an instance of Android SharedPreferences
        val sharedPreferences =
            getSharedPreferences(Constants.MRFARMERGROCER_PREFERENCES, Context.MODE_PRIVATE)

        val username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME, "")!!
        // Set the result to the tv_main.
        //tv_main.text= "The logged in user is $username."

        bottom_nav_view.selectedItemId = R.id.nav_home

        bottom_nav_view.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                }
                R.id.nav_products -> {
                    startActivity(Intent(this@MainActivity, ProductsActivity::class.java))
                    overridePendingTransition(0,0)
                }
                R.id.nav_orders -> {
                    startActivity(Intent(this@MainActivity, OrdersActivity::class.java))
                    overridePendingTransition(0,0)
                }
                R.id.nav_account -> {
                    startActivity(Intent(this@MainActivity, AccountActivity::class.java))
                    overridePendingTransition(0,0)
                }
            }
            false
        })


        val cart_view = findViewById(R.id.imageView) as ImageView

        cart_view.setOnClickListener{
            startActivity(Intent(this@MainActivity, CartListActivity::class.java))
            overridePendingTransition(0,0)
        }


    }

    fun successHomeItemsList(homeItemsList: ArrayList<Product>) {

        // Hide the progress dialog.
        hideProgressDialog()

        if (homeItemsList.size > 0) {

            rv_home_items.visibility = View.VISIBLE
            tv_no_home_items_found.visibility = View.GONE

            rv_home_items.layoutManager = GridLayoutManager(this, 2)
            rv_home_items.setHasFixedSize(true)

            val adapter = HomeItemsListAdapter(this, homeItemsList)
            rv_home_items.adapter = adapter

            adapter.setOnClickListener(object :
                HomeItemsListAdapter.OnClickListener {
                override fun onClick(position: Int, product: Product) {

                    val intent = Intent(this@MainActivity, ProductDetailsActivity::class.java)
                    intent.putExtra(Constants.EXTRA_PRODUCT_ID, product.product_id)
                    startActivity(intent)
                }
            })

        } else {
            rv_home_items.visibility = View.GONE
            tv_no_home_items_found.visibility = View.VISIBLE
        }
    }

    private fun getHomeItemsList() {
        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getHomeItemsList(this@MainActivity)
    }

    override fun onResume(){
        super.onResume()

        getHomeItemsList()
    }

    override fun onBackPressed() {
        doubleBackToExit()
    }
}


