package com.example.antovict.mercasnacks

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import java.net.URL
import java.text.NumberFormat
import java.util.*


/**
 * Created by bett on 8/21/17.
 */
class ProductCartListAdapter(ctx: Context, private var items: ArrayList<ProductGeneric>): BaseAdapter() {
    private val mInflator: LayoutInflater

    init {
        this.mInflator = LayoutInflater.from(ctx)
    }
    private class ViewHolder(row: View?) {
        var ivPhoto: ImageView ? = null
        var tvName: TextView ? = null
        var tvPrice: TextView ? = null


        init {
            this.ivPhoto = row?.findViewById<ImageView>(R.id.item_product_cart_ivPhoto)
            this.tvName = row?.findViewById<TextView>(R.id.item_product_cart_tvName)
            this.tvPrice = row?.findViewById<TextView>(R.id.item_product_cart_tvPrice)
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View?
        val viewHolder: ViewHolder
        if (convertView == null) {
            view = this.mInflator.inflate(R.layout.item_product_cart, parent, false)
            viewHolder = ViewHolder(view)
            view?.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        ProductsCache.instance
        var product = items[position]
        viewHolder.tvName?.text = product.name
        if (product.special_price > 0){
            //precio especial
            viewHolder.tvPrice?.text = getCurrency(product.special_price)
        }else{
            //precio normal
            viewHolder.tvPrice?.text = getCurrency(product.price)
        }
        if (product.bitmap == null){
            DownLoadImageTaskCart(viewHolder.ivPhoto!!, position).execute(product.image_url)
        }else{
            viewHolder.ivPhoto?.setImageBitmap(product.bitmap)
        }

        return view as View
    }

    override fun getItem(i: Int): ProductGeneric {
        return items[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getCount(): Int {
        return items.size
    }

    fun getCurrency(value : Double) : String{
        var format = NumberFormat.getCurrencyInstance(Locale.CANADA)
        var currency = format.format(value)
        Log.i("Pesos",currency)
        return currency
    }
}
// Class to download an image from url and display it into an image view
private class DownLoadImageTaskCart(internal val imageView: ImageView, var pos : Int) : AsyncTask<String, Void, Bitmap?>() {
    override fun doInBackground(vararg urls: String): Bitmap? {
        val urlOfImage = urls[0]
        return try {
            val inputStream = URL(urlOfImage).openStream()
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) { // Catch the download exception
            e.printStackTrace()
            null
        }
    }
    override fun onPostExecute(result: Bitmap?) {

        var producto= ProductsCache.products.get(pos)
        producto.bitmap = result
        ProductsCache.products.set(pos, producto)
        if(result!=null){
            // Display the downloaded image into image view
            imageView.setImageBitmap(result)
        }else{
            imageView.setImageResource(R.mipmap.ic_launcher)
        }
    }
}