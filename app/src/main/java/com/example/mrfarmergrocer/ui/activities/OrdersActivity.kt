package com.example.mrfarmergrocer.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mrfarmergrocer.R
import com.example.mrfarmergrocer.firestore.FirestoreClass
import com.example.mrfarmergrocer.models.Order
import com.example.mrfarmergrocer.ui.adapters.OrdersListAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_orders.*
import kotlinx.android.synthetic.main.bottom_nav_view.*

class OrdersActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)

        bottom_nav_view.selectedItemId = R.id.nav_orders

        bottom_nav_view.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@OrdersActivity, MainActivity::class.java))
                    overridePendingTransition(0,0)
                }
                R.id.nav_products -> {
                    startActivity(Intent(this@OrdersActivity, ProductsActivity::class.java))
                    overridePendingTransition(0,0)
                }
                R.id.nav_orders -> {
                }
                R.id.nav_account -> {
                    startActivity(Intent(this@OrdersActivity, AccountActivity::class.java))
                    overridePendingTransition(0,0)
                }
            }
            false
        })
    }

    override fun onResume() {
        super.onResume()

        getMyOrdersList()
    }


    /**
     * A function to get the list of my orders.
     */
    private fun getMyOrdersList() {
        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getMyOrdersList(this@OrdersActivity)
    }

    /**
     * A function to get the success result of the my order list from cloud firestore.
     *
     * @param ordersList List of my orders.
     */
    fun populateOrdersListInUI(ordersList: ArrayList<Order>) {

        // Hide the progress dialog.
        hideProgressDialog()

        // START
        if (ordersList.size > 0) {

            rv_order_items.visibility = View.VISIBLE
            tv_no_orders_found.visibility = View.GONE

            rv_order_items.layoutManager = LinearLayoutManager(this)
            rv_order_items.setHasFixedSize(true)

            val myOrdersAdapter = OrdersListAdapter(this, ordersList)
            rv_order_items.adapter = myOrdersAdapter
        } else {
            rv_order_items.visibility = View.GONE
            tv_no_orders_found.visibility = View.VISIBLE
        }
        // END
    }
}