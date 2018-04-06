package com.example.pkshrestha.wallclock

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_product_list.*
import kotlinx.android.synthetic.main.activity_product_list.view.*
import kotlinx.android.synthetic.main.content_product_list.*


fun Context.productListIntent(userId: String): Intent {
    return Intent(this, ProductListActivity::class.java).apply {
        putExtra(INTENT_USER_ID, userId)
        putExtra(INTENT_PRODUCT_MAP, defaultProductMap)
    }
}

private const val INTENT_USER_ID = "user_id"
// map be replace it with class
private const val INTENT_PRODUCT_MAP= "product_map"

private val DEFAULT_PRODUCT_TEMPLATE: HashMap<String, Any> =
        hashMapOf( "name" to "Product 1",
                "description" to "Description of the product goes here ",
                "price" to "0.0",
                "currency" to "RS")
// to be generic for the time being we can use String to String map
// Any can be String or number or Map
// basically it's just a dataclass, not sure why i chose to use it instead of a data class
private val defaultProductMap: HashMap<String, Any> =
        hashMapOf( "name" to "Product 1",
                "description" to "Description of the product goes here ",
                "price" to "0.0",
                "currency" to "RS",
                "similar" to listOf(DEFAULT_PRODUCT_TEMPLATE) )

class ProductListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }


        val userId = intent.getStringExtra(INTENT_USER_ID)
        requireNotNull(userId) { "no user_id provided in Intent extras" }
        val productMap = defaultProductMap
        //  set title of the toolbar
        supportActionBar?.title= productMap["name"].toString()
        // if planning to use default title
         // toolbar.toolbar_title.text="title goes here"
        // it's subtitle
        toolbar_sub_title.text= productMap["name"]?.toString()
        txtProductDescription.text= productMap["description"]?.toString()
    }
}
