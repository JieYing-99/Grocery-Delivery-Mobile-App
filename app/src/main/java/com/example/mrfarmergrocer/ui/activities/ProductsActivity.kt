package com.example.mrfarmergrocer.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mrfarmergrocer.R
import com.example.mrfarmergrocer.ui.adapters.MyProductsListAdapter
import com.example.mrfarmergrocer.firestore.FirestoreClass
import com.example.mrfarmergrocer.models.Product
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import kotlinx.android.synthetic.main.activity_products.*
import kotlinx.android.synthetic.main.bottom_nav_view.*


class ProductsActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        bottom_nav_view.selectedItemId = R.id.nav_products

        bottom_nav_view.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this@ProductsActivity, MainActivity::class.java))
                    overridePendingTransition(0, 0)
                }
                R.id.nav_products -> {
                }
                R.id.nav_orders -> {
                    startActivity(Intent(this@ProductsActivity, OrdersActivity::class.java))
                    overridePendingTransition(0, 0)
                }
                R.id.nav_account -> {
                    startActivity(Intent(this@ProductsActivity, AccountActivity::class.java))
                    overridePendingTransition(0, 0)
                }
            }
            false
        })


        val cart_view = findViewById(R.id.imageView) as ImageView

        cart_view.setOnClickListener{
            startActivity(Intent(this@ProductsActivity, CartListActivity::class.java))
            overridePendingTransition(0, 0)
        }


        getProductListFromFireStore(0)

        category_tab_layout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                getProductListFromFireStore(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

    }

    fun successProductsListFromFireStore(productsList: ArrayList<Product>) {

        // Hide Progress dialog.
        hideProgressDialog()

        if (productsList.size > 0) {
            rv_my_product_items.visibility = View.VISIBLE
            tv_no_products_found.visibility = View.GONE

            rv_my_product_items.layoutManager = LinearLayoutManager(this)
            rv_my_product_items.setHasFixedSize(true)

            val adapterProducts =
                    MyProductsListAdapter(this, productsList, this@ProductsActivity)
            rv_my_product_items.adapter = adapterProducts

        } else {
            rv_my_product_items.visibility = View.GONE
            tv_no_products_found.visibility = View.VISIBLE
        }
    }

    private fun getProductListFromFireStore(tabPosition: Int){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getProductsList(this, tabPosition)
    }

}