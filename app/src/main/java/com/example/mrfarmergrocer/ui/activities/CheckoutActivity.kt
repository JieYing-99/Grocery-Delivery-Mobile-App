package com.example.mrfarmergrocer.ui.activities


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mrfarmergrocer.R
import com.example.mrfarmergrocer.firestore.FirestoreClass
import com.example.mrfarmergrocer.models.Address
import com.example.mrfarmergrocer.models.Product
import com.example.mrfarmergrocer.models.CartItem
import com.example.mrfarmergrocer.models.Order
import com.example.mrfarmergrocer.ui.adapters.CartItemsListAdapter
import com.example.mrfarmergrocer.utils.Constants
import kotlinx.android.synthetic.main.activity_checkout.*


/**
 * A CheckOut activity screen.
 */
class CheckoutActivity : BaseActivity() {

    private var mAddressDetails: Address? = null
    private lateinit var mProductsList: ArrayList<Product>
    private lateinit var mCartItemsList: ArrayList<CartItem>
    // A global variable for the SubTotal Amount.
    private var mSubTotal: Double = 0.00

    // A global variable for the Total Amount.
    private var mTotalAmount: Double = 0.00
    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        // This is used to align the xml view to this class
        setContentView(R.layout.activity_checkout)
        setupActionBar()

        if (intent.hasExtra(Constants.EXTRA_SELECTED_ADDRESS)) {
            mAddressDetails =
                intent.getParcelableExtra<Address>(Constants.EXTRA_SELECTED_ADDRESS)!!
        }

        if (mAddressDetails != null) {
            tv_checkout_address_type.text = mAddressDetails?.type
            tv_checkout_full_name.text = mAddressDetails?.name
            tv_checkout_address.text = "${mAddressDetails!!.address}, ${mAddressDetails!!.zipCode}"
            tv_checkout_additional_note.text = mAddressDetails?.additionalNote

            if (mAddressDetails?.otherDetails!!.isNotEmpty()) {
                tv_checkout_other_details.text = mAddressDetails?.otherDetails
            }

            tv_checkout_mobile_number.text = mAddressDetails?.mobileNumber
        }

        btn_place_order.setOnClickListener {
            placeAnOrder()
        }
        getProductList()
    }

    /**
     * A function for actionBar Setup.
     */
    private fun setupActionBar() {

        setSupportActionBar(toolbar_checkout_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbar_checkout_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getProductList() {

        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getAllProductsList(this@CheckoutActivity)
    }

    /**
     * A function to get the success result of product list.
     *
     * @param productsList
     */
    fun successProductsListFromFireStore(productsList: ArrayList<Product>) {

        mProductsList = productsList
        getCartItemsList()

    }

    /**
     * A function to get the list of cart items in the activity.
     */
    private fun getCartItemsList() {

        FirestoreClass().getCartList(this@CheckoutActivity)
    }

    /**
     * A function to notify the success result of the cart items list from cloud firestore.
     *
     * @param cartList
     */
    fun successCartItemsList(cartList: ArrayList<CartItem>) {

        // Hide progress dialog.
        hideProgressDialog()

        for (product in mProductsList) {
            for (cart in cartList) {
                if (product.product_id == cart.product_id) {
                    cart.stock_amount = product.stock_amount
                }
            }
        }
        mCartItemsList = cartList

        rv_cart_list_items.layoutManager = LinearLayoutManager(this@CheckoutActivity)
        rv_cart_list_items.setHasFixedSize(true)

        val cartListAdapter = CartItemsListAdapter(this@CheckoutActivity, mCartItemsList, false)
        rv_cart_list_items.adapter = cartListAdapter

        for (item in mCartItemsList) {

            val availableQuantity = item.stock_amount.toInt()

            if (availableQuantity > 0) {
                val price = item.price.toDouble()
                val quantity = item.cart_quantity.toInt()

                mSubTotal += (price * quantity)
            }
        }

        tv_checkout_sub_total.text = "RM " + String.format("%.2f", mSubTotal)

        tv_checkout_shipping_charge.text = "RM 10.00"

        if (mSubTotal > 0) {
            ll_checkout_place_order.visibility = View.VISIBLE

            mTotalAmount = mSubTotal + 10.00
            tv_checkout_total_amount.text = "RM " + String.format("%.2f", mTotalAmount)
        } else {
            ll_checkout_place_order.visibility = View.GONE
        }

    }

    /**
     * A function to prepare the Order details to place an order.
     */
    private fun placeAnOrder() {

        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        val order = Order(
            FirestoreClass().getCurrentUserID(),
            mCartItemsList,
            mAddressDetails!!,
            "My order ${System.currentTimeMillis()}",
            mCartItemsList[0].image,
            String.format("%.2f", mSubTotal),
            "10.00", // The Shipping Charge is fixed as $10 for now in our case.
            String.format("%.2f", mTotalAmount),

                System.currentTimeMillis()
        )

        FirestoreClass().placeOrder(this@CheckoutActivity, order)
    }

    /**
     * A function to notify the success result of the order placed.
     */
    fun orderPlacedSuccess() {

        FirestoreClass().updateAllDetails(this@CheckoutActivity, mCartItemsList)
    }

    fun allDetailsUpdatedSuccessfully() {

        // Hide the progress dialog.
        hideProgressDialog()

        Toast.makeText(this@CheckoutActivity, "Your order placed successfully.", Toast.LENGTH_SHORT)
                .show()

        val intent = Intent(this@CheckoutActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()

    }
}