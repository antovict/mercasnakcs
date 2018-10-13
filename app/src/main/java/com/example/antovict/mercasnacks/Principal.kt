package com.example.antovict.mercasnacks

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.icu.text.DecimalFormat
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.ListView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_principal.*
import kotlinx.android.synthetic.main.drawer_menu.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.math.RoundingMode
import java.util.concurrent.TimeUnit


 class Principal : AppCompatActivity() {
    internal lateinit var listView: ListView
    internal lateinit var listViewCart: ListView
    internal lateinit var productList : ArrayList<ProductGeneric>
    internal lateinit var productCartList : ArrayList<ProductGeneric>
    internal lateinit var adapter: ProductListAdapter
    internal lateinit var adapterCart: ProductCartListAdapter
    internal lateinit var drawerLayout: DrawerLayout
    internal lateinit var content: LinearLayout
    internal lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    internal lateinit var refresh: SwipeRefreshLayout

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayShowTitleEnabled(false);
        productList = ArrayList<ProductGeneric>()
        productCartList = ArrayList<ProductGeneric>()
        drawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout
        content = findViewById(R.id.content) as LinearLayout
        refresh = findViewById(R.id.refresh) as SwipeRefreshLayout
        // Set an on refresh listener for swipe refresh layout
        refresh.setOnRefreshListener {
            // Initialize a new Runnable
            run {
                taskGetProducts(this).execute()
            }
        }
        ProductsCache.instance
        ProductsCache.products
        drawerLayout.setScrimColor(Color.TRANSPARENT)
        actionBarDrawerToggle = object : ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            /*override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
                //val slideX = drawerView!!.width * slideOffset
                //content.translationX = slideX
            }*/
        }
        listView = findViewById(R.id.lvProducts) as ListView
        listViewCart = findViewById(R.id.lvItemCarrito) as ListView
        drawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout
        taskGetProducts(this).execute()
        tvOrderBy.setTag("0")
        tvOrderBy.setOnClickListener {view ->
            if (tvOrderBy.tag.equals("0")){
                tvOrderBy.setTag("1")
                productList.sortedWith(compareBy<ProductGeneric>({ it.price }).reversed())
            }else{
                tvOrderBy.setTag("0")
                productList.sortedWith(compareBy{ it.price })
            }
            adapterCart.notifyDataSetChanged()
        }
        //prepareMovieData()

        // Click event for single list row
        //(listView as ListView).onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l -> Toast.makeText(applicationContext, movieList?.get(i)?.title, Toast.LENGTH_SHORT).show() }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.principal, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_cart -> {
                if (!drawer_layout.isDrawerOpen(GravityCompat.END)) {
                    drawer_layout.openDrawer(GravityCompat.END)
                } else {
                    super.onBackPressed()
                }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    public fun addAlCarrito(producto : ProductGeneric){
        productCartList.add(producto)
        adapterCart = ProductCartListAdapter(this, productCartList)

        (listViewCart as ListView).adapter = adapterCart
        adapterCart?.notifyDataSetChanged()
    }

    private inner class taskGetProducts(val activity: Activity) : AsyncTask<Void, String, String>() {

        var products:ArrayList<ProductGeneric> = ArrayList<ProductGeneric>()
        override fun doInBackground(vararg params: Void?): String? {
            val request = Request.Builder()
                    .url("https://api.myjson.com/bins/1gbraw")
                    .build()
            return getOkHttpClient(request)
        }

        @Throws(IOException::class)
        fun getOkHttpClient(request: Request): String
        {
            val client = OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build()

            val response = client.newCall(request).execute()
            return response.body()!!.string()
        }

        override fun onPreExecute() {
            super.onPreExecute()
            refresh.isRefreshing = true
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            var objetos = Gson().fromJson(result, JSONObjectProducts::class.java)
            productList = objetos.retailer.products
            adapter = ProductListAdapter(activity, productList, object : ProductListAdapter.BtnClickListener {
                override fun onBtnClick(product1: ProductGeneric) {
                    addAlCarrito(product1)
                    var total : Double = 0.00
                    for (item: ProductGeneric in productCartList){
                        var price : Double = 0.00
                        if (item.special_price>0.00){
                            total += item.special_price
                        }else{
                            total += item.price
                        }
                        item.price
                    }
                    tvTotalCarrito.text = total.toString()
                }
            })
            (listView as ListView).adapter = adapter
            adapter?.notifyDataSetChanged()
            Log.i("Adapter", adapter?.count.toString())
            refresh.isRefreshing = false
        }
    }
}



